package at.disys.springbootapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringBootController {

    /**
     * This endpoint returns
     * customer invoices
     */
    @GetMapping("/customer/{id}")
    public List<String> invoices (@PathVariable int id) {
        // placeholder
        return null;
    }
}
