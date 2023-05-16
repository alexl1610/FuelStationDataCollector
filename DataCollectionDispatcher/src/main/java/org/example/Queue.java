package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Queue {
    private final static String SPRING_TO_DISPATCH = "SPRING_TO_DISPATCH";
    private final static String DISPATCH_TO_RECEIVE = "DISPATCH_TO_RECEIVE";
    private final static String DISPATCH_TO_STATION = "DISPATCH_TO_STATION";

    public static void handleQueue() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(SPRING_TO_DISPATCH, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String id = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [*] Received '" + id + "' from SpringBootApp");
            sendMessageToDataCollectionReceiver(channel, id);

            List<String> dbUrls = Database.getDBUrls();
            for (String dbUrl : dbUrls) {
                sendMessageToStationDataCollector(channel, dbUrl, id);
            }
        };

        channel.basicConsume(SPRING_TO_DISPATCH, true, deliverCallback, consumerTag -> {
        });
    }

    private static void sendMessageToDataCollectionReceiver(Channel channel, String id) throws IOException {
        String start = "New Task with CustomerID," + id;
        channel.queueDeclare(DISPATCH_TO_RECEIVE, false, false, false, null);
        channel.basicPublish("", DISPATCH_TO_RECEIVE, null, start.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + start + "' to DataCollectionReceiver");
    }

    public static void sendMessageToStationDataCollector(Channel channel, String db_url, String id) throws IOException {
        String message = db_url + "," + id;
        channel.queueDeclare(DISPATCH_TO_STATION, false, false, false, null);
        channel.basicPublish("", DISPATCH_TO_STATION, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "' to StationDataCollector");
    }
}
