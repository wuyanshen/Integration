package com.axis2.api;

import com.axis2.api.wsdlparse.bean.ParameterInfo;
import com.axis2.api.wsdlparse.util.DOMUtil;
import com.axis2.api.wsdlparse.util.WsdlUtil;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.NamespaceContext;
import java.util.ArrayList;
import java.util.List;

import static com.axis2.api.wsdlparse.util.WsdlUtil.getDefinitionDocument;
import static com.axis2.api.wsdlparse.util.WsdlUtil.getOperationList;
import static com.axis2.api.wsdlparse.util.WsdlUtil.getXpath;

/**
 * 通过用wsdl4j包 解析wsdl地址中的参数
 */
public class wsdlParser {
    public static void main(String[]args) throws Exception {
        String wsdlUrl = "http://180.76.165.24:8000/elisoft/eliservice?wsdl";
//        String wsdlUrl = "http://localhost:8092/services/CommonService?wsdl";


        //输出wsdl的内容
        getWSDL(wsdlUrl);

        System.out.println();
        System.out.println("--------------------------------------------------------------");
        System.out.println();

        //获取wsdl中的所有方法
        getWSDLMethods(wsdlUrl);

        System.out.println();
        System.out.println("--------------------------------------------------------------");
        System.out.println();

        //获取wsdl的命名空间
        getWSDLNamespace(wsdlUrl);


        System.out.println();
        System.out.println("--------------------------------------------------------------");
        System.out.println();

        //给子节点添加命名空间
        parseXML();

    }


    /**
     * 输出wsdl的内容
     * @param wsdlUrl
     */
    public static void getWSDL(String wsdlUrl) throws Exception {
        Definition definition = null;
            definition = WsdlUtil.getDefinition(wsdlUrl);
            WSDLFactory factory = WsdlUtil.getWsdlFactory();
            WSDLWriter writer = factory.newWSDLWriter();
            writer.writeWSDL(definition,System.out);
    }

    /**
     * 获取wsdl中的所有方法
     * @param wsdlUrl
     * @throws Exception
     */
    public static void getWSDLMethods(String wsdlUrl) throws Exception {
        List<String> getOperationList = getOperationList(wsdlUrl);
        int i = 1;
        System.out.println("wsdl中的所有方法是：");
        for (String methodName:getOperationList){
            System.out.println("--"+i+"--："+methodName);

            //获取方法参数
            getParamsByMethodName(methodName,wsdlUrl);

            i++;
        }
    }


    /**
     * 根据方法名获取方法参数
     * @param methodName
     * @param wsdlUrl
     * @throws Exception
     */
    public static void getParamsByMethodName(String methodName,String wsdlUrl) throws Exception {
        //获取方法的参数
        org.w3c.dom.Document document = WsdlUtil.getDefinitionDocument(wsdlUrl);
        // 返回结果
        List<ParameterInfo> inputParamList = new ArrayList<>();
        // 解析参数
        StringBuilder xpathBuilder = new StringBuilder();
        WsdlUtil.getInputParam(inputParamList, document, methodName, xpathBuilder, null, false);

        if(inputParamList.size()==0){
            System.out.println("没有参数");
        }else{
            System.out.println("参数：");
            int j= 1;
            for(ParameterInfo param :inputParamList){
                System.out.println("    ["+j+"]--："+"名称："+param.getName()+" 类型："+param.getType());
                j++;
            }
        }
    }

    /**
     * 获取wsdl的命名空间
     * @param wsdlUrl
     * @throws Exception
     */
    public static void getWSDLNamespace(String wsdlUrl) throws Exception {
        org.w3c.dom.Document document = getDefinitionDocument(wsdlUrl);
        javax.xml.xpath.XPath xpath = getXpath(document);
        NamespaceContext namespaceContext =  xpath.getNamespaceContext();
        String uri = namespaceContext.getNamespaceURI("tns");
        System.out.println("wsdl的命名空间是："+uri);
    }


    public static void parseXML() throws Exception {
        String requestXML =
                "<getOpResultForPackage>\n" +
                "  <areanum>2</areanum>\n" +
                "  <currentPage>1</currentPage>\n" +
                "  <pageNum><test><first>1</first><second>2</second></test></pageNum>\n" +
                "</getOpResultForPackage>";

        org.dom4j.Document document = DocumentHelper.parseText(requestXML);
        Element root = document.getRootElement();
        getNodes(root);
        String newXML = document.asXML().replaceAll("xmlns:temp","xmlns");

        System.out.println(newXML);
    }

    public static void getNodes(Element element){
        if(element.elements().size()==0){
            System.out.println(element.getName()+"节点下没有子节点了！");
            //如果是根节点就不添加
            if(!element.isRootElement()){
                element.addAttribute("\nxmlns","");
            }
        }else{
            //递归遍历当前节点所有的子节点
            List<Element> listElement=element.elements();//所有一级子节点的list
            for(Element e:listElement){//遍历所有一级子节点
                e.addAttribute("\nxmlns","");
                getNodes(e);//递归
            }
        }
    }

}
