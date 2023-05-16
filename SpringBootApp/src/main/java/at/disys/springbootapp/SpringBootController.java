package at.disys.springbootapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@RestController
public class SpringBootController {

    /**
     * This endpoint returns customer invoices
     */
    @GetMapping("/customer/{id}")
    public String invoices(@PathVariable int id) throws IOException, TimeoutException {
        Queue.sendMessage(String.valueOf(id));
        return "'" + id + "'";
    }
}
