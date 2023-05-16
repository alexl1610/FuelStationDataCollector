package at.disys.springbootapp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Queue {
    private final static String SPRING_TO_DISPATCH = "SPRING_TO_DISPATCH";

    public static void sendMessage(String id) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.queueDeclare(SPRING_TO_DISPATCH, false, false, false, null);

            System.out.println(" [*] Received customer_id '" + id + "' from JavaFX");

            channel.basicPublish("", SPRING_TO_DISPATCH, null, String.valueOf(id).getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent customer_id '" + id + "' to DataCollectionDispatcher");
        }
    }
}
