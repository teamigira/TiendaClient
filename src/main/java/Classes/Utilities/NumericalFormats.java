/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Utilities;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 *
 * @author Nkanabo
 */
public class NumericalFormats {
    
    public static String accountsFormat(String amount){
        Double cash = Double.parseDouble(amount);
        Locale tzs = new Locale("en", "US");
        Currency tsh = Currency.getInstance(tzs);
        // Create a formatter given the Locale
        NumberFormat tshFormat = NumberFormat.getCurrencyInstance(tzs);
        // Format the Number into a Currency String
        System.out.println(tsh.getDisplayName() + ": " + tshFormat.format(cash));
        
        String formatedAmount = tsh.getDisplayName() + ": " + tshFormat.format(cash);
        return formatedAmount;
    }
}
