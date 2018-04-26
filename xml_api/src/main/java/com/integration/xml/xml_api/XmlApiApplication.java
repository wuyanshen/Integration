package com.integration.xml.xml_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
//@EnableIntegration
public class XmlApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmlApiApplication.class, args);
	}


}

