package com.example.javafxapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FuelDataController {
    @FXML
    private TextField customerID;
    @FXML
    private Label getInvoiceText;
    @FXML
    private Label createInvoiceText;

    @FXML
    protected void getInvoice() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/customer/" + customerID.getText()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String invoice = response.body();
        getInvoiceText.setText(invoice);
    }

    @FXML
    protected void createInvoice() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String customerId = customerID.getText();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/customer/" + customerId)) // Include the customer ID in the URL
                .POST(HttpRequest.BodyPublishers.ofString(customerId))
                .header("Content-Type", "text/plain")
                .build();

        client.send(request, HttpResponse.BodyHandlers.discarding());

        System.out.println("Invoice creation request sent successfully!");
        createInvoiceText.setText("Invoice creation request sent successfully!");
    }

}