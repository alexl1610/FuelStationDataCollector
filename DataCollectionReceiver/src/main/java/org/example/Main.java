package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class Main {

    private final static String QUEUE_NAME_START = "JOB_STARTED";
    private final static String QUEUE_NAME_STATION = "STATION";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME_START, false, false, false, null);
        channel.queueDeclare(QUEUE_NAME_STATION, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println(message);

        };

        channel.basicConsume(QUEUE_NAME_START, true, deliverCallback, consumerTag -> {
        });
    }
}