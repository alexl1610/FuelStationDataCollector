package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Queue {
    private final static String DISPATCH_TO_RECEIVE = "DISPATCH_TO_RECEIVE";
    private final static String STATION_TO_RECEIVE = "STATION_TO_RECEIVE";
    private final static String RECEIVE_TO_PDF = "RECEIVE_TO_PDF";

    public static void handleQueue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        com.rabbitmq.client.Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(DISPATCH_TO_RECEIVE, false, false, false, null);
        channel.queueDeclare(STATION_TO_RECEIVE, false, false, false, null);

        DeliverCallback dispatcherCallback = (consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            String[] parts = new String(body, StandardCharsets.UTF_8).split(",");
            String message = parts[0];
            int job_customer_id = Integer.parseInt(parts[1]);
            System.out.println(" [*] Received " + message + " '" + job_customer_id + "' from DataCollectionDispatcher");

            DataCollectionReceiver.processTaskStart(job_customer_id);
        };

        channel.basicConsume(DISPATCH_TO_RECEIVE, true, dispatcherCallback, consumerTag -> {});

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            String[] parts = new String(body, StandardCharsets.UTF_8).split(",");
            int total = Integer.parseInt(parts[0]);
            int customer_id = Integer.parseInt(parts[1]);
            System.out.println(" [*] Received '" + total + ", " + customer_id + "' from StationDataCollector");

            DataCollectionReceiver.processTotal(customer_id, total);
            int overallTotal = DataCollectionReceiver.calculateOverallTotal(customer_id);
            if (overallTotal != 0) {
                sendMessageToPDFGenerator(channel, overallTotal, customer_id);
            }
        };

        channel.basicConsume(STATION_TO_RECEIVE, true, deliverCallback, consumerTag -> {});
    }

    public static void sendMessageToPDFGenerator(Channel channel, int total, int id) throws IOException {
        String message = total + "," + id;
        channel.queueDeclare(RECEIVE_TO_PDF, false, false, false, null);
        channel.basicPublish("", RECEIVE_TO_PDF, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent overall total of '" + message + "' to PDF Generator");
    }
}