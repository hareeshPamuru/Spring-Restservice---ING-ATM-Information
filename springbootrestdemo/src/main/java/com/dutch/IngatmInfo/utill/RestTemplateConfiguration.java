package com.dutch.IngatmInfo.utill;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate(){
        final RestTemplate restTemplate=new RestTemplate();
        return restTemplate;
    }
}
