package Classes.Functions.Printers;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.List;

public class PrinterSelector {

    public static List<String> getAvailablePrinters() {
        List<String> printerNames = new ArrayList<>();
        // Get an array of all print services
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        // Iterate through each print service
        for (PrintService printService : printServices) {
            printerNames.add(printService.getName());
        }
        return printerNames;
    }
}
