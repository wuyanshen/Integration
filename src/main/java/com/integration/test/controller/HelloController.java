package com.integration.test.controller;

import com.integration.test.service.GreeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private GreeterService greeterService;

    @RequestMapping(path = "/static",method = RequestMethod.GET)
    public String sayHelloStatic(HttpServletRequest request){
        request.setAttribute("msg","hello Integration");
        return "redirect:/index.html";
    }

    @RequestMapping(path = "/template",method = RequestMethod.GET)
    public String sayHelloTemplate(HttpServletRequest request){
        request.setAttribute("msg","hello Integration");
        return "/index";
    }

    @RequestMapping(path = "/greet",method = RequestMethod.GET)
    public String greet(HttpServletRequest request){
        greeterService.greet("spring Integration!");
        return "/index";
    }
}
