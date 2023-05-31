package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Queue {
    private final static String DISPATCH_TO_STATION = "DISPATCH_TO_STATION";
    private final static String STATION_TO_RECEIVE = "STATION_TO_RECEIVE";
    private static int customer_id;
    private static String dbUrl;

    public static void handleQueue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        com.rabbitmq.client.Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(DISPATCH_TO_STATION, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            String[] parts = new String(body, StandardCharsets.UTF_8).split(",");
            dbUrl = parts[0];
            customer_id = Integer.parseInt(parts[1]);
            System.out.println(" [*] Received '" + dbUrl + ", " + customer_id + "' from DataCollectionDispatcher");

            sendMessageToDataCollectionReceiver(channel, getTotalKwhForCustomer(customer_id), customer_id);
        };

        channel.basicConsume(DISPATCH_TO_STATION, true, deliverCallback, consumerTag -> {
        });


    }

    private static int getTotalKwhForCustomer(int customer_id) {
        List<Charge> stationData = Database.getStationData(dbUrl);

        // converts the list into a stream, which allows for sequential processing of the elements
        return stationData.stream()
                // Filter the data based on the customer_id
                .filter(data -> data.getCustomer_id() == customer_id)
                // Map each filtered Charge object to its kWh value
                .mapToInt(Charge::getKwH)
                // Sum all the mapped kWh values and return the total
                .sum();
    }

    private static void sendMessageToDataCollectionReceiver(Channel channel, int total, int customer_id) throws IOException {
        String message = total + "," + customer_id;
        channel.queueDeclare(STATION_TO_RECEIVE, false, false, false, null);
        channel.basicPublish("", STATION_TO_RECEIVE, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "' to DataCollectionReceiver");
    }
}
