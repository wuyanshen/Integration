package com.integration.xml.xml_api.test;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileOutputStream;
import java.util.List;

public class Main {
    public static void main(String... args) throws Exception {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("context.xml");
        // Simple Service
        /*TempConverter converter = ctx.getBean("simpleGateway", TempConverter.class);
        System.out.println(converter.getMsg(68.0f));*/
        // Web Service
        /*TempConverter converter1  = ctx.getBean("wsGateway", TempConverter.class);
        System.out.println(converter1.getMsg(68.0f));*/

        String requestXML =
                "<getOpResultForPackage xmlns=\"http://webservice.elisoft.com/\">\n" +
                "  <areanum xmlns: =\"\">2</areanum>\n" +
                "  <currentPage xmlns: =\"\">1</currentPage>\n" +
                "  <pageNum xmlns: =\"\">10</pageNum>\n" +
                "</getOpResultForPackage>";
        requestXML = requestXML.replaceAll("xmlns: ","xmlns");
//        requestXML = "<body><first>1</first><second><a></a></second><third><b><c></c></b></third></body>";

        Document document = DocumentHelper.parseText(requestXML);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.elements();
        /*Element ele = DocumentHelper.createElement("test");
        ele.addNamespace(" ","");
        list.add(0,ele);*/
        for(Element e:list){
            e.addElement("test");
        }
//        rootElement.addElement("test");
        //        Document document1 = getNodes(document);
        XMLWriter writer = new XMLWriter( new FileOutputStream("C:/myXML.xml"));
        writer.write( document );
        writer.close();
        System.out.println(requestXML);

    }


    private static Document getNodes(Document document){
        Element element = document.getRootElement();
        element.addAttribute("xmlns","111");
//        System.out.println(element.getName()+" ");
        //递归遍历当前节点所有的子节点
        List<Element> listElement=element.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
            e.addAttribute("xmlns","111");
//            getNodes(document);//递归
        }

        return document;
    }
}
