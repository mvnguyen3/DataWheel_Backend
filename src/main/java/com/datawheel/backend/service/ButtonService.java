package com.datawheel.backend.service;

public interface ButtonService {
    void saveCounterValue(int counter_value);

    int getRecentCounterValue();

    int countFromZero();

    long getAverageTimeClick();
}
