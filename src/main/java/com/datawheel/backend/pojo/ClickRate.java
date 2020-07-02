package com.datawheel.backend.pojo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class ClickRate {
    private LocalDateTime clickTimeStart;
    private LocalDateTime clickTimeEnd;
    private long clickRateCount; // sum of the click

    public ClickRate() {
        this.clickRateCount = 0;
        this.clickTimeStart = null;
        this.clickTimeEnd = null;
    }
}

