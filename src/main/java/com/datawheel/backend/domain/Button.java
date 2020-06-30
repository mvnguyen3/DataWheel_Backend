package com.datawheel.backend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Button")
@Data
@NoArgsConstructor
public class Button{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int buttonCounter;
    private String buttonClickedTime;
    private long buttonClickTimeAverageInSecond;
}
