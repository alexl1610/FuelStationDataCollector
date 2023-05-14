package at.disys.springbootapp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;


@RestController
public class SpringBootController {
    private final static String QUEUE_NAME = "JOB_STARTED";


    /**
     * This endpoint returns
     * customer invoices
     */
    @GetMapping("/customer/{id}")
    public String invoices(@PathVariable int id) throws IOException, TimeoutException {
        System.out.println(id);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            System.out.println(" [*] Received customer_id '" + id + "'");

            channel.basicPublish("", QUEUE_NAME, null, String.valueOf(id).getBytes(StandardCharsets.UTF_8));
        }

        return "'" + id + "'";
    }
}
