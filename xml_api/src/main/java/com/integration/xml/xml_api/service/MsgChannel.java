package com.integration.xml.xml_api.service;

import com.integration.xml.xml_api.entity.MyParam;
import org.springframework.messaging.Message;

public interface MsgChannel {
    String rep();
//    Message<MyParam> transforParam(Message<?> inMessage);
    void req(String requestXML);

}
