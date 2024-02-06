package com.goldencashbunny.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GoldencashbunnyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldencashbunnyApplication.class, args);
	}

}
