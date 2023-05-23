package at.disys.springbootapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
public class SpringBootController {

    /**
     * This endpoint returns customer invoices
     */
    @GetMapping("/customer/{id}")
    public String getInvoice(@PathVariable int id) {
        String fileName = "invoice" + id + ".pdf";
        Path folderPath = Paths.get("..", "Invoices"); // Replace with the actual path to the invoices folder
        Path filePath = folderPath.resolve(fileName);

        File file = filePath.toFile();
        if (file.exists() && file.isFile()) {
            return "Invoice is available at " + filePath;
        } else {
            return "Invoice not found";
        }
    }

    @PostMapping("/customer/{id}")
    public boolean createInvoice(@PathVariable("id") int id) throws IOException, TimeoutException {
        Queue.sendMessage(String.valueOf(id));
        return true;
    }
}
