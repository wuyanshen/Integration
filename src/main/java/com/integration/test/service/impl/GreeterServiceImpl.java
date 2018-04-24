package com.integration.test.service.impl;

import com.integration.test.service.GreeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class GreeterServiceImpl implements GreeterService {

//    注入一个消息通道
    @Autowired
    private MessageChannel helloWorldChannel;
    @Override
    public void greet(String name) {
        helloWorldChannel.send(
                                MessageBuilder //用MessageBuilder创建一个消息发送给MessageChannel
                                .withPayload( name )
                                .setHeader("xiaoMing","xiaoMing")
                                .build()
                            );
    }
}
