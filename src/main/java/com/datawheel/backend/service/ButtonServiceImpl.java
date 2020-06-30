package com.datawheel.backend.service;

import com.datawheel.backend.domain.Button;
import com.datawheel.backend.repository.ButtonRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
public class ButtonServiceImpl implements ButtonService {
    @Autowired
    private ButtonRepository buttonRepository;

    @Async
    @Override
    public void saveCounterValue(int counter_value) {
        Button button = new Button();
        button.setButtonCounter(counter_value);

        String clickedTime = LocalDateTime.now().toString();
        button.setButtonClickedTime(clickedTime);
        Optional<Long> previousClickTimeAverage = buttonRepository.getPreviousClickTimeAverage();

        long clickTimeAverage = 0;
        if (previousClickTimeAverage.isPresent()) {
            clickTimeAverage = calculateClickTimeAverage(previousClickTimeAverage.get());
        } else {
            clickTimeAverage = 0;
        }

        button.setButtonClickTimeAverageInSecond(clickTimeAverage);

        buttonRepository.save(button);
    }

    private long calculateClickTimeAverage(long previousClickTimeAverage) {

        LocalDateTime timeNow = LocalDateTime.now();
        Optional<String> lastClickTimeOptional = buttonRepository.getPreviousClickTime();
        LocalDateTime lastClickTime = null;
        if(lastClickTimeOptional.isPresent()){
            lastClickTime = LocalDateTime.parse(lastClickTimeOptional.get());
        }else{
            return 0;
        }
        long clickTimeDifferent = lastClickTime.until(timeNow, ChronoUnit.SECONDS);


        return  (previousClickTimeAverage + clickTimeDifferent)/2;
    }

    @Override
    public int getRecentCounterValue() {
        Optional<Integer> counterValue = buttonRepository.getRecentCounterValue();
        if (counterValue.isPresent())
            return counterValue.get();
        return 0;
    }

    @Override
    public int countFromZero() {
        buttonRepository.countFromZero();
        return 0;
    }

    @Override
    public long getAverageTimeClick() {
        Optional<Long> average_time_click_optional = buttonRepository.getPreviousClickTimeAverage();
        if(average_time_click_optional.isPresent())
            return average_time_click_optional.get();
        return 0;
    }
}
