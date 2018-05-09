package com.integration.xml.xml_api.gateway;

import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

/**
 * 网关类
 */

public interface EsbGateWay {
    Object wsRequest(@Payload String requestXM, @Headers Map<String,String> map);
    Object httpRequest(@Payload String requestXM, @Headers Map<String,String> map);

}
