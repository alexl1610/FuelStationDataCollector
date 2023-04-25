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
    private Label invoiceText;

    @FXML
    protected void onGenerateButtonClick() {
        invoiceText.setText("You created an invoice!");
    }

    @FXML
    protected void getInvoice() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                //.uri(new URI("https://v2.jokeapi.dev/joke/Programming?format=txt"))
                .uri(new URI("http://localhost:8081/customer/" + customerID.getText()))
                //.uri(new URI("http://localhost:8081/books/post"))
                .GET()
                //.POST(HttpRequest.BodyPublishers.ofString(bookID.getText()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String invoice = response.body();
        invoiceText.setText(invoice);
    }
}