package com.integration.test.service.impl;

import com.integration.test.service.GreeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class GreeterServiceImpl implements GreeterService {
    @Autowired
    private MessageChannel helloWorldChannel;
    @Override
    public void greet(String name) {
        helloWorldChannel.send( MessageBuilder.withPayload( name ).build() );
    }
}
