package com.jakubwilk.serwisant.api;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.service.RepairProtocolService;
import com.jakubwilk.serwisant.api.service.RepairService;
import com.jakubwilk.serwisant.api.service.RepairServiceDefault;
import com.jakubwilk.serwisant.api.utils.ApplicationProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}