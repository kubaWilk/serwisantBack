package com.jakubwilk.serwisant.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    SimpleAsyncTaskExecutor simpleAsyncTaskExecutor(){
        return new SimpleAsyncTaskExecutor();
    }
}
