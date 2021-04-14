package com.markethacker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MarketHackerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MarketHackerApplication.class, args);
		System.out.println("Hello World!");
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate(); 
	}

}
