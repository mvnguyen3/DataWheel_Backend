package com.datawheel.backend.controller;

import com.datawheel.backend.domain.Button;
import com.datawheel.backend.pojo.ClickRate;
import com.datawheel.backend.service.AverageClickTimeOption;
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
import java.util.List;
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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value="/getAverageClick", produces = "application/json")
    public ResponseEntity<?> getAverageClick(){
        Map<String, Object> response = new HashMap<>();
        int click_rate = buttonService.getClickRate(AverageClickTimeOption.IN_30_SECOND);
        long average_click = buttonService.getAverageClick(click_rate);
        response.put("average_click", average_click);
        response.put("click_rate", click_rate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllClick", produces = "application/json")
    public ResponseEntity<?> getAllClick(){
        Map<String, Object> response = new HashMap<>();
        List<Button> buttonList = buttonService.getAllClicksDESC();
        List<ClickRate> clickRateList = buttonService.buildClickRateList(buttonList);

        response.put("click_rate_list", clickRateList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
