package com.elisoft.servicebus.moudles.esb.config;

import com.alibaba.fastjson.JSONObject;
import com.elisoft.servicebus.utils.Xml2JsonUtil;
import org.dom4j.DocumentException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


public class EsbTransformer {

   /* @Autowired
    private Transformer transformer;*/

    public Message<?> xml2json(Message<?> message) throws DocumentException {
        /*//转换json
        ObjectToJsonTransformer transformer = new ObjectToJsonTransformer();
        transformer.setContentType("text/xml");
        message = transformer.transform(message);*/

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




}
