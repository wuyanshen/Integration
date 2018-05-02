package com.integration.xml.xml_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class XmlConfig {
    @Bean
    public WebServiceTemplate webServiceTemplate(){
        return new WebServiceTemplate();
    }
}
