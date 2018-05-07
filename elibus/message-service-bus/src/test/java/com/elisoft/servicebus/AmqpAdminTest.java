package com.elisoft.servicebus;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class AmqpAdminTest {
    final static String queueName = "spring-boot";

    final static String HOST = "127.0.0.1";

    final static String USERNAME = "guest";

    final static String PASSWORD = "guest";

    final static int PORT = 5672;
    public static void main(String[] args){

        ConnectionFactory connectionFactory = new CachingConnectionFactory();

        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(new Queue("myqueue"));

        AmqpTemplate template = new RabbitTemplate(connectionFactory);
        template.convertAndSend("myqueue", "foo");

        String foo = (String) template.receiveAndConvert("myqueue");
    }
}
