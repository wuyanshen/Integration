package com.xml.test.controller;

import com.xml.test.api.HelloMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.ws.WebServiceHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReqController {

    /*@Autowired
    private MessageChannel requestChannel;

    @Autowired
    private PollableChannel outputChannel;*/

    @Autowired
    @Qualifier(value = "wsinputChannel")
    private MessageChannel wsinputChannel;

  /*  @Autowired
    @Qualifier(value = "output")
    private PollableChannel output;*/

    /*@RequestMapping(path = "/welcome/{name}",method = RequestMethod.GET,produces = "application/json",consumes = "application/json")
    public void sendMsg(@PathVariable String name){
        requestChannel.send(MessageBuilder.withPayload(name).build());
    }

    @RequestMapping(path = "/getMsg",method = RequestMethod.GET)
    public void getMsg(){
        HelloMsg msg = (HelloMsg)outputChannel.receive(0).getPayload();
        System.out.println(msg.getMsg());
    }*/

    @RequestMapping(value = "/call2", method = RequestMethod.POST)
    public String call2(@RequestHeader String soapAction, @RequestHeader String soapUrl, @RequestBody String reqXml) throws Exception {
        MessageBuilder<String> stringMessageBuilder = MessageBuilder.withPayload(reqXml);
        stringMessageBuilder.setHeader(WebServiceHeaders.SOAP_ACTION, soapAction);
        stringMessageBuilder.setHeader("soapUrl", soapUrl);

        wsinputChannel.send(stringMessageBuilder.build());
       /* Message<?> receive = output.receive(180000);
        String resXml = receive.getPayload().toString();
        return resXml;*/
       return "ok";
    }
}
