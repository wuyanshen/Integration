package com.restful.api.jms_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jms")
public class JmsController {

    @RequestMapping("/hello")
    @ResponseBody
    public String jms(){
        return "hello jms";
    }
}
