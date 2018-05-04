package com.elisoft.servicebus.moudles.esb.config;

import com.alibaba.fastjson.JSONObject;
import com.elisoft.servicebus.utils.Xml2JsonUtil;
import net.javacrumbs.json2xml.JsonXmlReader;
import org.dom4j.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import java.util.List;

import static com.elisoft.servicebus.utils.WsdlUtil.getDefinitionDocument;
import static com.elisoft.servicebus.utils.WsdlUtil.getXpath;

/**
 * 报文协议转换器
 * @author wuyanshen
 */

public class EsbTransformer {


    public Message<?> json2xml(Message<?> message) throws Exception{

        //如果请求报文不是json格式就不进行转换，直接返回
        if(!message.getHeaders().get("Content-Type").toString().equals("application/json")){
            return message;
        }

        String wsdlUrl = message.getHeaders().get("soapUrl").toString()+"?wsdl";
        //获取wsdl的命名空间
        org.w3c.dom.Document document = getDefinitionDocument(wsdlUrl);
        javax.xml.xpath.XPath xpath = getXpath(document);
        NamespaceContext namespaceContext =  xpath.getNamespaceContext();
        String namespaceURI = namespaceContext.getNamespaceURI("tns");
        System.out.println("wsdl的命名空间是："+namespaceURI);

        //如果请求报文是json就继续转换
        String result = message.getPayload().toString();
        String requestXML = Xml2JsonUtil.json2xml(result,new JsonXmlReader(namespaceURI));


        /*Document document1 = DocumentHelper.parseText(requestXML);
        Element element = document1.getRootElement();
        element = this.getNodes(element);
        requestXML = element.getDocument().asXML();*/
         /*requestXML =
                "<getOpResultForPackage xmlns=\"http://webservice.elisoft.com/\">\n" +
                "  <areanum xmlns=\"\">2</areanum>\n" +
                "  <currentPage xmlns=\"\">1</currentPage>\n" +
                "  <pageNum xmlns=\"\">10</pageNum>\n" +
                "</getOpResultForPackage>";*/

        message = MessageBuilder
                .withPayload(requestXML)
                .copyHeadersIfAbsent(message.getHeaders())
                .build();
        return message;
    }


    public Message<?> xml2json(Message<?> message) throws DocumentException {

        String result = message.getPayload().toString();
        if(result.indexOf("&lt;")!=-1){
            String firtRepalce = result.replaceAll("&lt;", '<' +"");
            result = firtRepalce.replaceAll("&gt;",'>'+"");
        }

        JSONObject jsonObject = Xml2JsonUtil.xml2Json(result);
        message = MessageBuilder
                .withPayload(jsonObject)
                .copyHeadersIfAbsent(message.getHeaders())
                .build();
        return message;
    }



    private Element getNodes(Element element){
        element.addAttribute("xmlns","");
        //递归遍历当前节点所有的子节点
        List<Element> listElement=element.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
            this.getNodes(e);//递归
        }
        return element;
    }


}
