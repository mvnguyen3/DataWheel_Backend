package com.datawheel.backend.service;

import com.datawheel.backend.domain.Button;
import com.datawheel.backend.pojo.ClickRate;
import com.datawheel.backend.pojo.Counter;
import com.datawheel.backend.repository.ButtonRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ButtonServiceImpl implements ButtonService {
    @Autowired
    private ButtonRepository buttonRepository;

    @Autowired
    private Counter counter;

    @Override
    public void saveCounterValue(long counter_value) {
        counter.getCounter_value().set(counter_value);

        Button button = new Button();
        button.setButtonCounter(counter.getCounter_value().get());
        String clickedTime = LocalDateTime.now().toString();
        button.setButtonClickedTime(clickedTime);
        Optional<Long> previousClickTimeAverage = buttonRepository.getPreviousClickTimeAverage();

        long clickTimeAverage = 0;
        long clickTimeDiferrent = 0;
        if (previousClickTimeAverage.isPresent()) {
            clickTimeAverage = calculateClickTimeAverage(previousClickTimeAverage.get());
            clickTimeDiferrent = calculateClickTimeDifferent();
        } else {
            clickTimeAverage = 0;
        }

        button.setButtonClickTimeAverageInSecond(clickTimeAverage);
        button.setButtonClickTimeDifferentInSecond(clickTimeDiferrent);


        buttonRepository.save(button);
    }

    private long calculateClickTimeDifferent() {
        LocalDateTime timeNow = LocalDateTime.now();
        Optional<String> lastClickTimeOptional = buttonRepository.getPreviousClickTime();
        LocalDateTime lastClickTime = null;
        if (lastClickTimeOptional.isPresent()) {
            lastClickTime = LocalDateTime.parse(lastClickTimeOptional.get());
        } else {
            return 0;
        }
        return lastClickTime.until(timeNow, ChronoUnit.SECONDS);
    }

    private long calculateClickTimeAverage(long previousClickTimeAverage) {

        LocalDateTime timeNow = LocalDateTime.now();
        Optional<String> lastClickTimeOptional = buttonRepository.getPreviousClickTime();
        LocalDateTime lastClickTime = null;
        if (lastClickTimeOptional.isPresent()) {
            lastClickTime = LocalDateTime.parse(lastClickTimeOptional.get());
        } else {
            return 0;
        }
        long clickTimeDifferent = lastClickTime.until(timeNow, ChronoUnit.SECONDS);
        return (previousClickTimeAverage + clickTimeDifferent) / 2;
    }

    @Override
    public long getRecentCounterValue() {
        Optional<Long> counterValue = buttonRepository.getRecentCounterValue();
        if (counterValue.isPresent())
            return counterValue.get();
        return 0;
    }

    @Override
    public long countFromZero() {
        buttonRepository.countFromZero();
        return 0;
    }

    @Override
    public long getAverageTimeClick() {
        Optional<Long> average_time_click_optional = buttonRepository.getPreviousClickTimeAverage();
        if (average_time_click_optional.isPresent())
            return average_time_click_optional.get();
        return 0;
    }

    @Override
    public long getAverageClick(int click_rate) {

        List<Button> buttonList = buttonRepository.findAll();
        List<Integer> averageClickList = new ArrayList<>();
        for (Button button : buttonList) {
            calculateAverageClick(averageClickList, button, click_rate);
        }
        int total = 0;
        for (Integer clickRate : averageClickList) {
            total += clickRate;
        }

        int listSize = averageClickList.size();
        if (listSize == 0)
            return 0;

        return total / averageClickList.size();
    }

    @Override
    public int getClickRate(AverageClickTimeOption averageClickTimeOption) {
        int clickTimeRate = 0;
        switch (averageClickTimeOption) {
            case IN_30_SECOND:
                clickTimeRate = 30;
                break;
            case IN_60_SECOND:
                clickTimeRate = 60;
                break;
        }
        return clickTimeRate;
    }

    @Override
    public List<Button> getAllClicks() {
        return buttonRepository.findAll();
    }

    @Override
    public List<ClickRate> buildClickRateList(List<Button> buttonList) {
        List<ClickRate> clickRateList = new ArrayList<>();
        ClickRate clickRate = new ClickRate();
        for (Button button : buttonList) {
            LocalDateTime btnClickTime = LocalDateTime.parse(button.getButtonClickedTime());
            if (clickRate.getClickTimeEnd() == null && clickRate.getClickTimeStart() == null) {
                // Range will be in one minute
                clickRate.setClickTimeStart(btnClickTime.minusSeconds(30));
                clickRate.setClickTimeEnd(btnClickTime.plusSeconds(30));

            }
            if (buttonClickInRange(clickRate, btnClickTime)) {
                long clickCount = clickRate.getClickRateCount() + 1;
                clickRate.setClickRateCount(clickCount);
                continue;
            }


            if (!buttonClickInRange(clickRate, btnClickTime)) {
                clickRateList.add(clickRate);
                clickRate = new ClickRate();
            }

        }
        return clickRateList;

    }

    @Override
    public List<Button> getAllClicksDESC() {
        return buttonRepository.findAllDESC();
    }

    private boolean buttonClickInRange(ClickRate clickRate, LocalDateTime btnClickTime) {
        return btnClickTime.isAfter(clickRate.getClickTimeStart()) && btnClickTime.isBefore(clickRate.getClickTimeEnd());
    }

    private void calculateAverageClick(List<Integer> averageClickList, Button button, int click_rate) {
        long clickTimeDifferent = button.getButtonClickTimeDifferentInSecond();
        if (clickTimeDifferent == 0)
            averageClickList.add(click_rate);
        else
            averageClickList.add((int) (click_rate / clickTimeDifferent));
    }

}
