package com.markethacker.webservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestWebService {
	
	@GetMapping(value = "/api")
	public String helloSpring() {
		System.out.println("Hello Spring Boot Controller");
		return "Welcome to Owen's project!"; 
	}
	
}
