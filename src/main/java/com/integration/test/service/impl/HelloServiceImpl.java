package com.integration.test.service.impl;

import com.integration.test.service.HelloService;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService{
    @Override
    public void hello(String name) {
        System.out.println("Hello ,"+name);
    }
}
