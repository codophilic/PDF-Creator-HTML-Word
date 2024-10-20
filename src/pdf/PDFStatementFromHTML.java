package pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.apache.commons.lang3.StringUtils;


public class PDFStatementFromHTML {
	
    public static String parallelReplace(String input, Map<String, String> placeholders) {
        // Use StringBuffer to accumulate the result safely in a multi-threaded environment
        StringBuffer resultBuffer = new StringBuffer(input);

        // Use parallelStream to replace placeholders concurrently
        //The Map of placeholders is processed in parallel. Each placeholder replacement is executed in a concurrent fashion.
        placeholders.entrySet().parallelStream().forEach(entry -> {
        	
        	/**
        	 * When you’re working with parallel processing, multiple threads are changing the string at the same time. 
        	 * To make sure they don’t interfere with each other, we use the synchronized block. 
        	 * This ensures that only one thread at a time can modify the resultBuffer.
        	 */
            synchronized (resultBuffer) {
                // Replace the placeholder with the corresponding value using StringUtils
            	/**
            	 * Apache Commons Lang's StringUtils is often considered the fastest way to replace values 
            	 * in a string compared to the standard Java String methods, particularly when dealing with 
            	 * multiple replacements or large strings, as it can offer significant performance improvements
            	 */
                String result = StringUtils.replace(resultBuffer.toString(), entry.getKey(), entry.getValue());
                
                /**
                 * Suppose resultBuffer currently contains: 
                 * 			- "This is a {{placeholder1}} and another {{placeholder2}}".
                 * After calling setLength(0), the resultBuffer becomes an empty string: "".
                 * `resultBuffer.append(result)`:
                 * 			- This appends the newly updated string (stored in result) to the now-empty StringBuffer.
                 * 			- The new result contains the string after one round of placeholder replacement
                 * 
                 * Let's say result is now "This is a test1 and another {{placeholder2}}" (after replacing {{placeholder1}} with test1).
                 * After calling append(result), resultBuffer will now contain the new string: "This is a test1 and another {{placeholder2}}"
                 * Why to do Resetting?
                 * 	- When replacing placeholders in a string, we want to overwrite the existing string with the updated one after each replacement.
                 * 	- Instead of creating a new StringBuffer each time (which would use more memory), we reuse the same resultBuffer object and just reset it by clearing its current contents using setLength(0).
                 * 	- This way, after every placeholder replacement, we store the updated string in the same buffer without creating new objects, which is more efficient.
                 */
                resultBuffer.setLength(0); // Reset the buffer instead of creating a new StringBuffer making less usage of memory
                resultBuffer.append(result); // Append the updated result
            }
        });

        return resultBuffer.toString();
    }
	
    public static void main(String[] args) throws IOException, com.lowagie.text.DocumentException {
    	
    	//Check your Java Version
        System.out.println("Java Version: " + System.getProperty("java.version"));

        // Path to the HTML file
        String filePath = "C:/Users/pandy/OneDrive/Desktop/Real PDF Creator/PDF-Creator-HTML/resources/Sample.html";
        String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Replace placeholders with actual values
        String name = "Harsh Pandya";
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

        // Map Placeholder and its value
        Map<String, String> placeholders = Map.of(
                "{{header}}", "Invoice",
                "{{name}}", name,
                "{{date}}", date,
                "{{tableHeader}}", tableHeader,
                "{{table}}",tableData,
                "{{total}}", total
        );
//        String filledHtml = htmlContent
//                .replace("{{header}}", "Invoice")        // Replacing header dynamically
//                .replace("{{name}}", name)
//                .replace("{{date}}", date)
//                .replace("{{tableHeader}}", tableHeader)  // Dynamic table header
//                .replace(, tableData)      // Dynamic table data
//                .replace();
        
        //Replace placeholder values inside the HTML using Parallel Stream and StringUtils.
        String filledHtml=parallelReplace(htmlContent, placeholders);
        // Parse the modified HTML
        Document doc = Jsoup.parse(filledHtml, "UTF-8");
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml); // Ensure it's XHTML compatible

        // Path to output PDF
        String outputPdf = "C:/Users/pandy/OneDrive/Desktop/Real PDF Creator/PDF-Creator-HTML/resources/HTMLPDF.pdf";

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

            System.out.println("PDF generated successfully with header image.");
        }
    }
}
