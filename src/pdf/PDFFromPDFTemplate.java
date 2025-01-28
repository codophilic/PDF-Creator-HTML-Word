package pdf;

import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;

public class PDFFromPDFTemplate {
    public static void main(String[] args) {
        String src = "C:\\Users\\pandy\\OneDrive\\Desktop\\New folder\\PDF-Creator-HTML-Word\\resources\\FontStyleDiff.pdf";  // Source PDF with placeholders
        String dest = "C:\\\\Users\\\\pandy\\\\OneDrive\\\\Desktop\\\\New folder\\\\PDF-Creator-HTML-Word\\\\resources\\\\FontStyleDiffUpdated.pdf"; // Output PDF

        try {
            // Read the existing PDF
            PdfReader reader = new PdfReader(src);
            // Create a stamper to modify the PDF
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));

            // Access the AcroFields in the PDF
            AcroFields fields = stamper.getAcroFields();

            // Replace field values
            fields.setField("nameplaceholdername", "Harsh");
            fields.setField("ageplaceholdername", "25");
            fields.setField("paragraphplaceholdername", "Address1,Address1,Address1,Address1,Address1,Address1,Address1,Address1,Address1,Address1,Address1,Address1");

            // Flatten the form to prevent further editing (optional)
            stamper.setFormFlattening(true);

            // Close the stamper and reader
            stamper.close();
            reader.close();

            System.out.println("Fields replaced and PDF saved at: " + dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
