package com.integration.test.service.impl;

import com.integration.test.service.GreeterService;
import com.integration.test.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class GreeterServiceImpl implements GreeterService {

//    注入一个消息通道
    @Autowired
    private MessageChannel helloWorldChannel;

    @Autowired
    private HelloService helloWorldGateway;

    @Override
    public void greet(String name) {
        helloWorldChannel.send(
                                MessageBuilder //用MessageBuilder创建一个消息发送给MessageChannel
                                .withPayload( name )
                                .setHeader("xiaoMing","xiaoMing")
                                .build()
                            );
    }

    @Override
    public void greet2(String name) {
//        获取hello的返回消息
        String msg = helloWorldGateway.getHelloMessage(name);
        System.out.println(msg);
    }
}
