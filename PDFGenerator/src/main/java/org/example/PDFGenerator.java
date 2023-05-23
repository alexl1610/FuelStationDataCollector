package org.example;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFGenerator {

    public PDFGenerator() throws IOException, TimeoutException {
        Queue.handleQueue();
    }

    public static void createInvoice(String id, int total) {
        String name = Database.getCustomerData(id);
        String consumedKwh = Integer.toString(total);

        // Create a new PDF document
        PDDocument document = new PDDocument();

        // Create a new page
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Set font and font size
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Set starting position for writing text
            float y = page.getMediaBox().getHeight() - 50;

            // Write customer name
            contentStream.beginText();
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Customer Name: " + name);
            contentStream.endText();

            // Update Y position for the next line
            y -= 20;

            // Write consumed kWh
            contentStream.beginText();
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Consumed kWh: " + consumedKwh);
            contentStream.endText();

            // Save the content stream
            contentStream.close();

            // Define the output file path
            Path outputPath = Paths.get("..", "Invoices", "invoice" + id + ".pdf");

            // Save the PDF document to the output file
            try (OutputStream outputStream = Files.newOutputStream(outputPath, StandardOpenOption.CREATE)) {
                document.save(outputStream);
            }

            System.out.println("Invoice created successfully!");
        } catch (IOException e) {
            System.out.println("Error creating invoice: " + e.getMessage());
        } finally {
            // Close the PDF document
            try {
                document.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
