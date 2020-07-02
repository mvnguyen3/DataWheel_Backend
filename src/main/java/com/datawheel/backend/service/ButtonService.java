package com.datawheel.backend.service;

import com.datawheel.backend.domain.Button;
import com.datawheel.backend.pojo.ClickRate;

import java.util.List;

public interface ButtonService {
    void saveCounterValue(long counter_value);

    long getRecentCounterValue();

    long countFromZero();

    long getAverageTimeClick();

    long getAverageClick(int click_rate);

    int getClickRate(AverageClickTimeOption averageClickTimeOption);

    List<Button> getAllClicks();

    List<ClickRate> buildClickRateList(List<Button> buttonList);

    List<Button> getAllClicksDESC();
}
