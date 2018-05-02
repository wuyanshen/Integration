package com.axis2.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class xmltojson {
        public static void main(String[] args) throws Exception {
            String xmlStr= readFile("C:/test.xml");
            Document doc= DocumentHelper.parseText(xmlStr);
            JSONObject json=new JSONObject();
            dom4j2Json(doc.getRootElement(),json);
            System.out.println("xml2Json:"+json.toJSONString());

            /*String xml = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "    <soapenv:Header/>\n" +
                    "    <soapenv:Body>\n" +
                    "        <ns:findAllCarsResponse xmlns:ns=\"http://service.api.axis2.com\" xmlns:ax21=\"http://entity.api.axis2.com/xsd\">\n" +
                    "            <ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ax21:Car\">\n" +
                    "                <ax21:decription>2018款 红标 运动版 1.5T 手动 两驱 精英型 前轮驱动 6挡 手动</ax21:decription>\n" +
                    "                <ax21:name>哈弗H6</ax21:name>\n" +
                    "                <ax21:price>指导价：8.88 - 14.68万</ax21:price>\n" +
                    "            </ns:return>\n" +
                    "            <ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ax21:Car\">\n" +
                    "                <ax21:decription>2017款 280TSI DSG尊雅版 前轮驱动 7挡 双离合</ax21:decription>\n" +
                    "                <ax21:name>帕萨特</ax21:name>\n" +
                    "                <ax21:price>指导价：15.29 - 30.39万</ax21:price>\n" +
                    "            </ns:return>\n" +
                    "            <ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ax21:Car\">\n" +
                    "                <ax21:decription>2018款 30周年 Sportback 35TFSI 进取型 前轮驱动 7挡 双离合</ax21:decription>\n" +
                    "                <ax21:name>奥迪A3</ax21:name>\n" +
                    "                <ax21:price>指导价：15.23 - 20.90万</ax21:price>\n" +
                    "            </ns:return>\n" +
                    "            <ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ax21:Car\">\n" +
                    "                <ax21:decription>2018款 18T 手动 两驱 都市进取型 前轮驱动 6挡 手动</ax21:decription>\n" +
                    "                <ax21:name>昂科拉ENCORE</ax21:name>\n" +
                    "                <ax21:price>指导价：10.99 - 16.30万</ax21:price>\n" +
                    "            </ns:return>\n" +
                    "            <ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ax21:Car\">\n" +
                    "                <ax21:decription>2018款 318i 后轮驱动 8挡 手自一体</ax21:decription>\n" +
                    "                <ax21:name>宝马3系</ax21:name>\n" +
                    "                <ax21:price>指导价：28.30 - 59.88万</ax21:price>\n" +
                    "            </ns:return>\n" +
                    "        </ns:findAllCarsResponse>\n" +
                    "    </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            JSONObject json = xml2Json(xml);
            System.out.println("xml2Json:"+json.toJSONString());*/

        }

        public static String readFile(String path) throws Exception {
            File file=new File(path);
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(new Long(file.length()).intValue());
            //fc向buffer中读入数据
            fc.read(bb);
            bb.flip();
            String str=new String(bb.array(),"UTF8");
            fc.close();
            fis.close();
            return str;

        }
        /**
         * xml转json
         * @param xmlStr
         * @return
         * @throws DocumentException
         */
        public static JSONObject xml2Json(String xmlStr) throws DocumentException{
            Document doc= DocumentHelper.parseText(xmlStr);
            JSONObject json=new JSONObject();
            dom4j2Json(doc.getRootElement(), json);
            return json;
        }

        /**
         * xml转json
         * @param element
         * @param json
         */
        public static void dom4j2Json(Element element,JSONObject json){
            //如果是属性
            for(Object o:element.attributes()){
                Attribute attr=(Attribute)o;
                if(!isEmpty(attr.getValue())){
                    json.put("@"+attr.getName(), attr.getValue());
                }
            }
            List<Element> chdEl=element.elements();
            if(chdEl.isEmpty()&&!isEmpty(element.getText())){//如果没有子元素,只有一个值
                json.put(element.getName(), element.getText());
            }

            for(Element e:chdEl){//有子元素
                if(!e.elements().isEmpty()){//子元素也有子元素
                    JSONObject chdjson=new JSONObject();
                    dom4j2Json(e,chdjson);
                    Object o=json.get(e.getName());
                    if(o!=null){
                        JSONArray jsona=null;
                        if(o instanceof JSONObject){//如果此元素已存在,则转为jsonArray
                            JSONObject jsono=(JSONObject)o;
                            json.remove(e.getName());
                            jsona=new JSONArray();
                            jsona.add(jsono);
                            jsona.add(chdjson);
                        }
                        if(o instanceof JSONArray){
                            jsona=(JSONArray)o;
                            jsona.add(chdjson);
                        }
                        json.put(e.getName(), jsona);
                    }else{
                        if(!chdjson.isEmpty()){
                            json.put(e.getName(), chdjson);
                        }
                    }


                }else{//子元素没有子元素
                    for(Object o:element.attributes()){
                        Attribute attr=(Attribute)o;
                        if(!isEmpty(attr.getValue())){
                            json.put("@"+attr.getName(), attr.getValue());
                        }
                    }
                    if(!e.getText().isEmpty()){
                        json.put(e.getName(), e.getText());
                    }
                }
            }
        }

        public static boolean isEmpty(String str) {

            if (str == null || str.trim().isEmpty() || "null".equals(str)) {
                return true;
            }
            return false;
        }
}
