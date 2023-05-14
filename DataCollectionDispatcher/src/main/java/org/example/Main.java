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

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String query = "SELECT db_url FROM station";
            String db_url = "";
            String id = new String(delivery.getBody(), StandardCharsets.UTF_8);

            String start = "A job has been started for customer with the ID: " + id;
            channel.basicPublish("", QUEUE_NAME_START, null, start.getBytes(StandardCharsets.UTF_8));

            try (
                    java.sql.Connection conn = Database.getConnection();
                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    db_url = rs.getString("db_url");
                    String message = db_url + "," + id;
                    System.out.println(" [x] Received '" + message + "'");
                    channel.basicPublish("", QUEUE_NAME_STATION, null, message.getBytes(StandardCharsets.UTF_8));
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };

        channel.basicConsume(QUEUE_NAME_START, true, deliverCallback, consumerTag -> {
        });
    }
}