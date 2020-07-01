package com.datawheel.backend.service;

import com.datawheel.backend.domain.Button;
import com.datawheel.backend.pojo.Counter;
import com.datawheel.backend.repository.ButtonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
public class ButtonServiceImpl implements ButtonService {
    @Autowired
    private ButtonRepository buttonRepository;

    @Autowired
    private Counter counter;

    @Async
    @Override
    public void saveCounterValue(long counter_value) {
        counter.getCounter_value().set(counter_value);

        Button button = new Button();
        button.setButtonCounter(counter.getCounter_value().get());
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
}
