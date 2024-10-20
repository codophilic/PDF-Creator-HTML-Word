
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class App {
    public static void main(String[] args) throws IOException, com.lowagie.text.DocumentException {
        // Path to the HTML file
        String filePath = "C:/Users/pandy/OneDrive/Desktop/Test/html2pdf/src/main/java/com/demo/html2pdf/Sample.html";
        String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Replace placeholders with actual values
        String name = "John Doe";
        String date = "2024-10-16";
        String total = "$100";

        // Define table headers dynamically
        List<String> tableHeaders = new ArrayList<>();
        tableHeaders.add("Item");
        tableHeaders.add("Quantity");
        tableHeaders.add("Price");

        // Dynamically build the table header row
        StringBuilder tableHeaderBuilder = new StringBuilder();
        for (String headerTitle : tableHeaders) {
            tableHeaderBuilder.append("<th>").append(headerTitle).append("</th>");
        }
        String tableHeader = tableHeaderBuilder.toString();

        // Sample table data (Object arrays) to dynamically generate table rows
        Object[][] tableDataArray = {
                {"Product A", "2", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product B", "1", "$50"},
                {"Product C", "5", "$100"}
        };

        // Build the table rows dynamically
        StringBuilder tableDataBuilder = new StringBuilder();
        for (Object[] row : tableDataArray) {
            tableDataBuilder.append("<tr>");
            for (Object cell : row) {
                tableDataBuilder.append("<td>").append(cell.toString()).append("</td>");
            }
            tableDataBuilder.append("</tr>");
        }
        String tableData = tableDataBuilder.toString();

        // Replace placeholders in the HTML content
        String filledHtml = htmlContent
                .replace("{{header}}", "Invoie")        // Replacing header dynamically
                .replace("{{name}}", name)
                .replace("{{date}}", date)
                .replace("{{tableHeader}}", tableHeader)  // Dynamic table header
                .replace("{{table}}", tableData)      // Dynamic table data
                .replace("{{total}}", total);

        // Parse the modified HTML
        Document doc = Jsoup.parse(filledHtml, "UTF-8");
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml); // Ensure it's XHTML compatible

        // Path to output PDF
        String outputPdf = System.getProperty("user.home") + "/Downloads/HTMLPDF.pdf";

        // Set up the PDF rendering
        try (OutputStream os = new FileOutputStream(outputPdf)) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext context = renderer.getSharedContext();
            context.setPrint(true);
            context.setInteractive(false);

            // Set base URL to find the images
            String baseUrl = "file:///C:/Users/pandy/OneDrive/Desktop/Test/html2pdf/src/main/java/com/demo/html2pdf/";

            // Pass the HTML and base URL to the renderer
            renderer.setDocumentFromString(doc.html(), baseUrl);
            renderer.layout();
            renderer.createPDF(os);

            System.out.println("PDF generated successfully with watermark and header image.");
        }
    }
}
