package com.integration.xml.xml_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration  //启用 spring-integration
//@IntegrationComponentScan //spring-integration DSL 特性需启动
@ImportResource("classpath:new.xml")
public class XmlApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmlApiApplication.class, args);
	}


}

