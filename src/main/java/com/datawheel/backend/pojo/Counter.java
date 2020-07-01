package com.datawheel.backend.pojo;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Counter {
    private static Counter INSTANCE;
    private AtomicLong counter_value;
    private Counter(){
        counter_value = new AtomicLong();
    }

    public static Counter getINSTANCE(){
        if(INSTANCE == null)
            synchronized (Counter.class){
                if(INSTANCE == null)
                    INSTANCE = new Counter();
            }
        return INSTANCE;
    }

}
