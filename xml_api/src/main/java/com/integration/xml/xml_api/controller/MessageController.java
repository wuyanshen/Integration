package com.integration.xml.xml_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/esb")
public class MessageController {

    @Autowired
    private MessageChannel input;

    @Autowired
    private PollableChannel output;

    @RequestMapping(value = "/call",method = RequestMethod.POST)
    public Object sendMessage(@RequestHeader String soapUrl,@RequestHeader String soapAction,@RequestHeader String nameSpaceUrl,@RequestBody String payload){
        Message message = MessageBuilder
                .withPayload(payload)
                .setHeader("soapUrl",soapUrl)
                .setHeader("ws_soapAction",soapAction)
                .setHeader("nameSpaceUrl",nameSpaceUrl)
                .build();
        input.send(message);
        message = output.receive(0);
        System.out.println(message.getPayload().toString());
        return message.getPayload().toString();
    }
}
