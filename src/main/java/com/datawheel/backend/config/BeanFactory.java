package com.datawheel.backend.config;

import com.datawheel.backend.pojo.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanFactory {

    @Bean
    public Counter counter() {
        return Counter.getINSTANCE();
    }

}
