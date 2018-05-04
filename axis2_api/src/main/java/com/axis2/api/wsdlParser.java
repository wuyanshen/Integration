package com.axis2.api;

import com.axis2.api.wsdlparse.bean.ParameterInfo;
import com.axis2.api.wsdlparse.util.DOMUtil;
import com.axis2.api.wsdlparse.util.WsdlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.NamespaceContext;
import java.util.ArrayList;
import java.util.List;

import static com.axis2.api.wsdlparse.util.WsdlUtil.getDefinitionDocument;
import static com.axis2.api.wsdlparse.util.WsdlUtil.getXpath;

/**
 * 通过用wsdl4j包 解析wsdl地址中的参数
 */
public class wsdlParser {
    public static void main(String[]args) throws Exception {
        String wsdlUrl = "http://180.76.165.24:8000/elisoft/eliservice?wsdl";
//        String wsdlUrl = "http://localhost:8092/services/CommonService?wsdl";


        //输出wsdl的内容
        /*Definition definition = WsdlUtil.getDefinition(wsdlUrl);
        WSDLFactory factory = WsdlUtil.getWsdlFactory();
        WSDLWriter writer = factory.newWSDLWriter();
        writer.writeWSDL(definition,System.out);*/

        System.out.println();
        System.out.println("--------------------------------------------------------------");
        System.out.println();

        //获取wsdl中的所有方法
        List<String> getOperationList = getOperationList(wsdlUrl);
        int i = 1;
        System.out.println("wsdl中的所有方法是：");
        for (String methodName:getOperationList){
            System.out.println("--"+i+"--："+methodName);

            //获取方法的参数
            Document document = WsdlUtil.getDefinitionDocument(wsdlUrl);
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

            i++;
        }

        System.out.println();
        System.out.println("--------------------------------------------------------------");
        System.out.println();

        //获取wsdl的命名空间
        /*org.w3c.dom.Document document = getDefinitionDocument(wsdlUrl);
        javax.xml.xpath.XPath xpath = getXpath(document);
        NamespaceContext namespaceContext =  xpath.getNamespaceContext();
        String uri = namespaceContext.getNamespaceURI("tns");
        System.out.println("wsdl的命名空间是："+uri);*/
    }

    /**
     * 得到wsdl中所有的方法
     *
     * @param wsdlUrl
     * @return
     * @throws Exception
     */
    public static List<String> getOperationList(String wsdlUrl) throws Exception {
        org.w3c.dom.Document document = getDefinitionDocument(wsdlUrl);
        javax.xml.xpath.XPath xpath = getXpath(document);

        NodeList operations = DOMUtil.findNodeList(document, "wsdl:definitions/wsdl:portType/wsdl:operation");

        // 返回的结果集list
        List<String> operationList = new ArrayList<String>();
        for (int i = 0; i < operations.getLength(); i++) {
            Node operation = operations.item(i);
            String operationName = DOMUtil.getNodeName(operation);
            if (operationName != null && !"".equals(operationName)) {
//                log.debug("解析" + wsdlUrl + "中的方法：" + operationName);
                operationList.add(operationName);
            }
        }
        return operationList;
    }
}
