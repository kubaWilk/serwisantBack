package com.jakubwilk.serwisant.api;

import com.jakubwilk.serwisant.api.dao.UserDAO;
import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.entity.UserDetails;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserDAO userDao){
		return runner -> {
			User temp = new User();
			UserDetails userDetails = new UserDetails();

			temp.setUserDetails(userDetails);

			userDao.createUser(temp);
		};
	}

}
