package com.datawheel.backend.controller;

import com.datawheel.backend.service.ButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ControllerV1 {

    @Autowired
    private ButtonService buttonService;

//    @PostMapping(value = "/increment", produces = "application/json")
//    public ResponseEntity<?> increment(@RequestParam("counter") int counter_value) {
//        Map<String, Object> response = new HashMap<>();
//        buttonService.saveCounterValue(counter_value);
//        response.put("counter_value", counter_value);
//        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
//    }

    @MessageMapping("/increment")
    @SendTo("/topic/incremented")
    public ResponseEntity<?> increment(@RequestParam("counter") long counter_value) {
        Map<String, Object> response = new HashMap<>();
        buttonService.saveCounterValue(counter_value);
        response.put("counter_value", counter_value);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/getCounterValue", produces = "application/json")
    public ResponseEntity<?> getCounterValue() {
        Map<String, Object> response = new HashMap<>();
        long counter_value = buttonService.getRecentCounterValue();
        response.put("counter_value", counter_value);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @MessageMapping("/countFromZero")
    @SendTo("/topic/countFromZero")
    public ResponseEntity<?> countFromZero() {
        Map<String, Object> response = new HashMap<>();
        long counter_value = buttonService.countFromZero();
        response.put("counter_value", counter_value);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

//    @DeleteMapping(value = "/countFromZero", produces = "application/json")
//    public ResponseEntity<?> countFromZero() {
//        Map<String, Object> response = new HashMap<>();
//        long counter_value = buttonService.countFromZero();
//        response.put("counter_value", counter_value);
//        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
//    }

    @GetMapping(value = "/getAverageTime", produces = "application/json")
    public ResponseEntity<?> getAverageTime() {
        Map<String, Object> response = new HashMap<>();
        long average_time_click = buttonService.getAverageTimeClick();
        response.put("average_time_click", average_time_click);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }


    private LocalDateTime previousTime = null;

    @GetMapping("/testTime")
    public ResponseEntity<?> testTime() {
        Map<String, Object> response = new HashMap<>();
        LocalDateTime timeNow = LocalDateTime.now();
        System.out.println(timeNow.toString());
        System.out.println(timeNow.getSecond());
        if (previousTime != null) {
            long seconds = previousTime.until(timeNow, ChronoUnit.SECONDS);
            System.out.println("Different in seconds: " + seconds);
        }

        previousTime = timeNow;

        String timeNowString = timeNow.toString();
        LocalDateTime dateTime = LocalDateTime.parse(timeNowString);
        System.out.println("formatted: " + dateTime);

        response.put("status", "Success");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
