package com.datawheel.backend.service;

public interface ButtonService {
    void saveCounterValue(long counter_value);

    long getRecentCounterValue();

    long countFromZero();

    long getAverageTimeClick();
}
