package com.xml.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@ImportResource("classpath:integration.xml")
public class TestXmlApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestXmlApiApplication.class, args);
	}
}
