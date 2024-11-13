package pdf;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import javax.xml.namespace.QName;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordDocGeneratorFromWordDoc {

    public static void main(String[] args) throws IOException {
        String inputFilePath = "C:/Users/pandy/OneDrive/Desktop/Real PDF Creator/PDF-Creator-HTML/resources/Invoice.docx";
        String outputFilePath = "C:/Users/pandy/OneDrive/Desktop/Real PDF Creator/PDF-Creator-HTML/resources/InvoiceUpdate.docx";
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("(nameplc)", "John Doe");
        placeholders.put("(dateplaceholder)", "2024-10-20");
        placeholders.put("(totalplaceholder)", "$1000");

        // Define dynamic headers and rows for the table (order details)
        List<String> headers = List.of("Item", "Description", "Quantity", "Price", "Total");
        List<List<String>> rows = List.of(
                List.of("1", "Laptop", "1", "$700", "$1400"),
                List.of("2", "Mouse", "1", "$50", "$50"),
                List.of("3", "Keyboard", "1", "$100", "$100")
        );

        // Font size and style for the table
        int tableFontSize = 20;  // Example font size
        String tableFontStyle = "Algerian";  // Example font style

        replacePlaceholdersAndAddTable(inputFilePath, outputFilePath, placeholders, headers, rows, tableFontSize, tableFontStyle);
    }

    private static void replacePlaceholdersAndAddTable(String inputFilePath, String outputFilePath,
                                                       Map<String, String> placeholders, List<String> headers, List<List<String>> rows,
                                                       int tableFontSize, String tableFontStyle) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             FileOutputStream fos = new FileOutputStream(outputFilePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            // Replace placeholders in paragraphs
            document.getParagraphs().forEach(paragraph -> replacePlaceholdersInParagraph(paragraph, placeholders));

            // Find the "Order Details" paragraph to insert the table
            XWPFParagraph orderDetailsParagraph = findOrderDetailsParagraph(document);
            if (orderDetailsParagraph != null) {
                XmlCursor cursor = orderDetailsParagraph.getCTP().newCursor();

                // Create the table with headers and rows right after the "Order Details" paragraph            	
                // Create a new table at the cursor position
                XWPFTable table = document.insertNewTbl(cursor);
                addTableHeaders(table, headers, tableFontSize, tableFontStyle);
                addTableRows(table, rows, tableFontSize, tableFontStyle);
                
                //Removing the placeholders
                document.removeBodyElement(document.getPosOfParagraph(orderDetailsParagraph));

            }

            // Replace placeholders in tables
            document.getTables().forEach(table -> table.getRows().forEach(row ->
                    row.getTableCells().forEach(cell ->
                            cell.getParagraphs().forEach(paragraph -> replacePlaceholdersInParagraph(paragraph, placeholders)))));

            // Save the updated document
            document.write(fos);
            // Convert XWPFDocument to ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.write(byteArrayOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            // Convert ByteArrayInputStream to WordprocessingMLPackage
            
            WordprocessingMLPackage wordMLPackage;
			try {
				wordMLPackage = WordprocessingMLPackage.load(byteArrayInputStream);
				// Convert to PDF using docx4j

	            OutputStream os = new FileOutputStream(new File("C:/Users/pandy/OneDrive/Desktop/Real PDF Creator/PDF-Creator-HTML/resources/InvoiceUpdate.pdf"));
	            
				Docx4J.toPDF(wordMLPackage, os);
				os.close();
			} catch (Docx4JException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            

        }
    }

    // Method to find the "Order Details" paragraph
    private static XWPFParagraph findOrderDetailsParagraph(XWPFDocument document) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            if (paragraph.getText().contains("(tableplaceholder)")) {
                return paragraph;
            }
        }
        return null; // Return null if not found
    }

    // Method to replace placeholders in paragraphs
    private static void replacePlaceholdersInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        paragraph.getRuns().forEach(run -> {
            String text = run.getText(0);
            if (text != null) {
                // Replace placeholders based on map entries
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    text = text.replace(entry.getKey(), entry.getValue());
                }
                run.setText(text, 0);  // Set the updated text in the run
            }
        });
    }

    // Method to add table headers
    private static void addTableHeaders(XWPFTable table, List<String> headers, int fontSize, String fontStyle) {
        XWPFTableRow headerRow = table.getRow(0);  // Get the first row (already created)
        for (int i = 0; i < headers.size(); i++) {
            if (i == 0) {
                headerRow.getCell(0).setText(headers.get(i));  // Set the first cell in the row
            } else {
                headerRow.addNewTableCell().setText(headers.get(i));  // Add new cells for other headers
            }
            // Set font size and style
            setCellFont(headerRow.getCell(i), fontSize, fontStyle);
        }
    }

    // Method to add data rows to the table
    private static void addTableRows(XWPFTable table, List<List<String>> rows, int fontSize, String fontStyle) {
        rows.forEach(rowData -> {
            XWPFTableRow row = table.createRow();  // Create a new row for each set of row data
            for (int i = 0; i < rowData.size(); i++) {
                row.getCell(i).setText(rowData.get(i));  // Set text in each cell
                // Set font size and style
                setCellFont(row.getCell(i), fontSize, fontStyle);
            }
        });
    }

    // Method to set font size and style for a cell
    private static void setCellFont(XWPFTableCell cell, int fontSize, String fontStyle) {
        XWPFParagraph paragraph = cell.addParagraph();
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontSize(fontSize);
        run.setFontFamily(fontStyle);
        run.setText(cell.getText()); // Set the text from the cell
    }
}
