package com.elisoft.servicebus.modules.esb.web;

import com.elisoft.servicebus.core.EsbWsHeader;
import com.elisoft.servicebus.core.gateway.EsbGateway;
import com.elisoft.servicebus.core.gateway.EsbSyncGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ws.WebServiceHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Component
@RestController
//@Scope("prototype")
@RequestMapping(value = "/esb")
public class EsbWebController {

//    @Autowired
//    @Qualifier("wsinputChannel")
//    private MessageChannel wsinputChannel;
//
//    @Autowired
//    @Qualifier(value = "output")
//    private PollableChannel output;

//    @Autowired
//    @Qualifier(value = "wsRespOutput")
//    private PollableChannel wsRespOutput;

    @Autowired
    private EsbGateway esbGateway;

    @Autowired
    private EsbSyncGateway syncGateway;

//    @Autowired
//    WsService wsService;
//    @Autowired
//    IntegrationFlow flow1;

//    @RequestMapping(value = "/call2", method = RequestMethod.POST)
//    public Object call2(@RequestHeader String soapAction, @RequestHeader String soapUrl, @RequestBody String reqXml) throws Exception {
//        MessageBuilder<String> stringMessageBuilder = MessageBuilder.withPayload(reqXml);
//        stringMessageBuilder.setHeader(WebServiceHeaders.SOAP_ACTION, soapAction);
//        stringMessageBuilder.setHeader("soapUrl", soapUrl);
//
//
//        wsinputChannel.send(stringMessageBuilder.build());
//        Message<?> receive = output.receive(500);
//        String resXml = receive.getPayload().toString();
//        return resXml;
//    }

    @RequestMapping(value = "/call3", method = RequestMethod.POST)
    public Object call3(@RequestHeader String soapAction, @RequestHeader String soapUrl, @RequestBody String reqXml) throws Exception {
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put(EsbWsHeader.SOAP_URL,soapUrl);
        headerMap.put(WebServiceHeaders.SOAP_ACTION, soapAction);

        Object wsResp = esbGateway.toSend(reqXml, headerMap);
        return wsResp;
    }

    @RequestMapping(value = "/call4", method = RequestMethod.POST)
    public Object call4(@RequestHeader(name = "Content-Type") String contentType,@RequestHeader String soapAction, @RequestHeader String soapUrl, @RequestBody String reqXml) throws Exception {
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put(EsbWsHeader.SOAP_URL,soapUrl);
        headerMap.put("Content-Type", contentType);
        headerMap.put(WebServiceHeaders.SOAP_ACTION, soapAction);
        Object wsResp = syncGateway.wsProcess(reqXml, headerMap);
        return wsResp;
    }
}
