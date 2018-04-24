package com.integration.test.service.impl;

import com.integration.test.service.HelloService;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService{
    @Override
    public void hello(String name) {
        System.out.println("Hello ,"+name);
    }

    @Override
    public String getHelloMessage(String name) {
        System.out.println("我收到的了Greetr发来的消息："+name);
        return "我是hello的返回值！";
    }
}
