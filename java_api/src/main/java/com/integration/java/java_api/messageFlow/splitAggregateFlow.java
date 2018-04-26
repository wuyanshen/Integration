package com.integration.java.java_api.messageFlow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.PollableChannel;

import java.util.concurrent.Executors;

@Configuration
public class splitAggregateFlow {
    @Bean
    public IntegrationFlow spliAggregateFlow(){
        return IntegrationFlows
                .from("input")
                .split(splitter->splitter.delimiters(","))
                .channel(c->c.executor(Executors.newCachedThreadPool()))
                .resequence()
                .<String, String>transform(String::toUpperCase)
                .log(LoggingHandler.Level.WARN)
                .aggregate()
                .channel(output())
                .get();
    }

    @Bean
    public PollableChannel output(){
        return new QueueChannel();
    }
}
