package com.integration.xml.xml_api.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String... args) throws Exception {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("context.xml");
        // Simple Service
        TempConverter converter = ctx.getBean("simpleGateway", TempConverter.class);
        System.out.println(converter.getMsg(68.0f));
        // Web Service
        /*TempConverter converter1  = ctx.getBean("wsGateway", TempConverter.class);
        System.out.println(converter1.getMsg(68.0f));*/
    }
}
