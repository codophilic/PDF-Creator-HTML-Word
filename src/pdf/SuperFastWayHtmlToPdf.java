package pdf;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SuperFastWayHtmlToPdf {
    public static void main(String[] args) {
        try {
        	long start=System.currentTimeMillis();
            // Load and parse HTML
            Document doc = loadHTMLDocument("C:/Users/pandy/OneDrive/Desktop/Real PDF Creator/PDF-Creator-HTML/resources/Sample.html");  // Update path

            // Create PDF if Document is loaded successfully
            if (doc != null) {
                try (FileOutputStream os = new FileOutputStream("C:\\Users\\pandy\\OneDrive\\Desktop\\Real PDF Creator\\PDF-Creator-HTML\\resources\\FastestSample.pdf")) {
                    PdfRendererBuilder builder = new PdfRendererBuilder();
                    builder.useFastMode();
                    builder.withW3cDocument(doc, new File("C:/Users/pandy/OneDrive/Desktop/Real PDF Creator/PDF-Creator-HTML/resources/Sample.html").toURI().toString());
                    builder.toStream(os);
                    builder.run();
                	long end=System.currentTimeMillis();
                	System.out.println((end-start));
                    System.out.println("Done");
                }
            } else {
                System.out.println("Failed to load HTML document. 'doc' is null.");
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document loadHTMLDocument(String filePath) {
        try {
            File htmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(htmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Error loading HTML document: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

