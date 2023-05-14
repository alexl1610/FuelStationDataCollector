package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class Main {
    private final static String QUEUE_NAME = "STATION";
    private static int customer_id;
    private static String db_url;
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        com.rabbitmq.client.Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            String[] parts = new String(body, StandardCharsets.UTF_8).split(",");
            db_url = parts[0];
            customer_id = Integer.parseInt(parts[1]);
            System.out.println(" [x] Received '" + db_url + " " + customer_id + "'");

            String query = "SELECT * FROM charge WHERE customer_id = ?";

            try (Connection conn = Database.getConnection(db_url);
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, customer_id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int kwH = rs.getInt("kwh");
                    int res_customer_id = rs.getInt("customer_id");

                    Charge charge = new Charge(id, kwH, res_customer_id);

                    System.out.println(charge);
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });




    }
}