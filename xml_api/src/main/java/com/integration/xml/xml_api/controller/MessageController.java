package com.integration.xml.xml_api.controller;

import com.integration.xml.xml_api.service.MsgChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MessageController {


//    @Autowired
//    private MsgChannel msgChannel;

    @RequestMapping(value = "/getRep",method = RequestMethod.POST)
    public void getResponse(){
//        msgChannel.rep("aa");
        System.out.println("aaaa");
    }
}
