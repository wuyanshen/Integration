package com.integration.xml.xml_api.service;

import com.integration.xml.xml_api.entity.MyParam;
import org.springframework.messaging.Message;

public interface MsgChannel {
    boolean rep(String requestXML);
//    Message<MyParam> transforParam(Message<?> inMessage);
}
