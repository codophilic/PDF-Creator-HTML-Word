package pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dummy {
	
	public static void main(String args[]) {
//		for(int i=0;i<=1000;i++) {
//			System.out.println("<p>{{date"+i+"}}</p>"
//					+"<p>{{total"+i+"}}</p>");
//		}
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
        Map<String, String> placeholders = new HashMap<String, String>();
        placeholders.put("{{header}}", "Invoice");
        placeholders.put("{{name}}", name);
        placeholders.put("{{date}}", date);
        placeholders.put("{{tableHeader}}", tableHeader);
        placeholders.put("{{total}}", total);
        for(int i=0;i<=10;i++) {
        	placeholders.put("{{date"+i+"}}",date);
        	placeholders.put("{{total"+i+"}}",total);
//			System.out.println("\"{{date"+i+"}}\",date,\n"+"\"{{total"+i+"}}\",total,");
		}
		System.out.println(placeholders);
	}
}
