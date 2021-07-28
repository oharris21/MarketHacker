package com.markethacker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.markethacker.service.CustomDataBuilder;

@SpringBootTest
public class CustomDataBuilderTests {
	
	@Autowired
	private CustomDataBuilder customDataBuilder; 
	
	@Test
	void contextLoads() {}

}
