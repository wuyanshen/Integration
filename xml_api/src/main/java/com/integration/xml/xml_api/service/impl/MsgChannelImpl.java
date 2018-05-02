package com.integration.xml.xml_api.service.impl;

import com.integration.xml.xml_api.entity.MyParam;
import com.integration.xml.xml_api.service.MsgChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.LinkedList;

@Service
public class MsgChannelImpl implements MsgChannel{

    @Autowired
    private MessageChannel input;

    @Autowired
    private PollableChannel output;

    @Override
    public String rep() {
        String payload = output.receive(21).getPayload().toString();
        return payload;
    }

    @Override
    public void req(String requestXML) {
        input.send(MessageBuilder
                .withPayload(requestXML)
                .setHeader("name","123")
                .build());
    }

    /*@Transformer
    @Override
    public Message<MyParam> transforParam(Message<?> inMessage) {
        MultiValueMap map = (MultiValueMap)inMessage.getPayload();

        LinkedList list = (LinkedList)map.get("employeeId");

        String id = (String)list.get(0);

        String name = (String)map.get("name");

        MyParam param = new MyParam();

        param.setName(name);

        param.setId(id);

        Message<MyParam>message = new GenericMessage<MyParam>(param,inMessage.getHeaders());

        return message;
    }*/


}
