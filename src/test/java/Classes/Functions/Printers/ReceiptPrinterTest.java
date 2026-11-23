package Classes.Functions.Printers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

class ReceiptPrinterTest {

    @Test
    void everyTemplateRenders() {
        for (int n = 101; n <= 105; n++) {
            BufferedImage img = ReceiptPrinter.sample("Template " + n + ":", true, null, null, null).renderImage(1);
            assertTrue(img.getHeight() > 200, "template " + n);
            assertEquals(226, img.getWidth());
        }
    }

    @Test
    void premiumTemplateNeedsPremium() {
        assertTrue(ReceiptPrinter.isPremiumTemplate("Template 105:"));
        assertFalse(ReceiptPrinter.isPremiumTemplate("Template 101:"));
        int classic = ReceiptPrinter.sample("Template 101:", false, null, null, null).renderImage(1).getHeight();
        int lockedSignature = ReceiptPrinter.sample("Template 105:", false, null, null, null).renderImage(1).getHeight();
        int signature = ReceiptPrinter.sample("Template 105:", true, null, null, null).renderImage(1).getHeight();
        assertEquals(classic, lockedSignature, "without Premium, 105 prints as 101");
        assertNotEquals(classic, signature);
    }

    @Test
    void longNamesWrapInsteadOfOverflowing() {
        ReceiptPrinter r = new ReceiptPrinter("Shop", "", "", "", "1", "Tsh");
        r.addItem("A very long product name that will never fit on one receipt line at all", 1, 1000);
        int one = r.renderImage(1).getHeight();
        ReceiptPrinter s = new ReceiptPrinter("Shop", "", "", "", "1", "Tsh");
        s.addItem("Short", 1, 1000);
        assertTrue(one > s.renderImage(1).getHeight());
    }

    @Test
    void emptyReceiptStillRenders() {
        assertTrue(new ReceiptPrinter(null, null, null, null, null, null).renderImage(1).getHeight() > 50);
    }
}
