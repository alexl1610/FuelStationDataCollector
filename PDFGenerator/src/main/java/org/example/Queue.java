package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Queue {

    private final static String RECEIVE_TO_PDF = "RECEIVE_TO_PDF";

    public static void handleQueue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        com.rabbitmq.client.Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(RECEIVE_TO_PDF, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            String[] parts = new String(body, StandardCharsets.UTF_8).split(",");
            int overallTotal = Integer.parseInt(parts[0]);
            String customer_id = parts[1];
            System.out.println(" [*] Received '" + overallTotal + ", " + customer_id + "' from DataCollectionReceiver");

            PDFGenerator.createInvoice(customer_id, overallTotal);

        };

        channel.basicConsume(RECEIVE_TO_PDF, true, deliverCallback, consumerTag -> {});
    }
}
