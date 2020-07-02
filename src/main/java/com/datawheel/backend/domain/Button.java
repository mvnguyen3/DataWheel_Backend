package com.datawheel.backend.domain;

import lombok.Data;

import javax.persistence.*;


// Make this class singleton and having an atomic long as a button counter;
@Entity
@Table(name = "Button")
@Data
public class Button {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long buttonCounter;
    private String buttonClickedTime;
    private long buttonClickTimeAverageInSecond;
    private long buttonClickTimeDifferentInSecond;

    public Button() {
    }

}
