package com.integration.xml.xml_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;


@Controller
public class WebServiceController {


    @Autowired
    private WebServiceTemplate webServiceTemplate;

    /**
     * 跳转请求报文页面
     * @return
     */
    @RequestMapping(value = "/request",method = RequestMethod.GET)
    public String toRequest(){
        return "/requestPage";
    }

    /**
     * 通过消息通道请求webService
     * @param url
     * @param requestXML
     * @param request
     * @return
     */
    @RequestMapping(value = "/getRep",method = RequestMethod.POST)
    public String getResponse(String url,String requestXML,HttpServletRequest request){
        StringResult result = new StringResult();
        /*Source payload = new StringSource(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.api.cxf.com\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <getCountry xmlns=\"http://service.api.cxf.com\">\n" +
                        "       <name xmlns=\"\">China</name>\n" +
                        "      </getCountry>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>"
        );*/

        /**
         * 注意：webServiceTemplate不需要传soap头信息，只需要传body中的内容就可以
         *      还需要加入命名空间 xmlns="http://service.api.cxf.com"
         */
        /*Source payload = new StringSource(
                "      <getCountry xmlns=\"http://service.api.cxf.com\">\n" +
                        "       <name xmlns=\"\">China</name>\n" +
                        "      </getCountry>\n"
        );*/
        Source payload = new StringSource(requestXML);
//        webServiceTemplate.sendSourceAndReceiveToResult("http://localhost:8092/services/CommonService",payload,result);
        webServiceTemplate.sendSourceAndReceiveToResult(url,payload,result);

        Message message = MessageBuilder.withPayload(payload).build();


        System.out.println(result.toString());
        request.setAttribute("result",result.toString());
        //回写页面原来输入的值
        request.setAttribute("url",url);
        request.setAttribute("requestXML",requestXML);
        return "/requestPage";
    }
}
