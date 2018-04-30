package com.xml.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReqController {

    @Autowired
    private MessageChannel requestChannel;

    @Autowired
    private MessageChannel outputChannel;

    @RequestMapping(path = "/welcome/{name}",method = RequestMethod.GET,produces = "application/json",consumes = "application/json")
    public void sendMsg(@PathVariable String name){
        requestChannel.send(MessageBuilder.withPayload(name).build());
    }


}
