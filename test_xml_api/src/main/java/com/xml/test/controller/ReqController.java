package com.xml.test.controller;

import com.xml.test.api.HelloMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
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
    private PollableChannel outputChannel;

    @RequestMapping(path = "/welcome/{name}",method = RequestMethod.GET,produces = "application/json",consumes = "application/json")
    public void sendMsg(@PathVariable String name){
        requestChannel.send(MessageBuilder.withPayload(name).build());
    }

    @RequestMapping(path = "/getMsg",method = RequestMethod.GET)
    public void getMsg(){
        HelloMsg msg = (HelloMsg)outputChannel.receive(0).getPayload();
        System.out.println(msg.getMsg());
    }
}
