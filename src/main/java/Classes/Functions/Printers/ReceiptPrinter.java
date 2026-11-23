package Classes.Functions.Printers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * Builds and prints a real sales receipt — the one feature the old
 * {@code PrintReceipt} stub never delivered (it printed a hard-coded
 * "CITY AUTO" header and nothing else).
 *
 * <p>A receipt is rendered once into a list of monospaced lines, which are then
 * used both for the on-screen <b>preview page</b> ({@link #showPreview}) and for
 * the physical thermal print ({@link #print}). This keeps what the cashier sees
 * identical to what the printer produces.
 *
 * <p>Typical use from the sales screen:
 * <pre>
 *   ReceiptPrinter r = new ReceiptPrinter(company, address, footer, cashier, receiptNo, currency);
 *   r.addItem("Coca-Cola 500ml", 2, 1500);
 *   r.total = 3000;
 *   r.showPreview(parentWindow);   // shows the page; cashier clicks Print
 * </pre>
 *
 * @author Nkanabo
 */
public class ReceiptPrinter implements Printable {

    /** Characters across a standard 80 mm thermal line. Use 32 for 58 mm paper. */
    private static final int WIDTH = 42;
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.##");
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /** One line of the sale. */
    public static final class Item {
        public final String name;
        public final double qty;
        public final double unitPrice;
        public Item(String name, double qty, double unitPrice) {
            this.name = name;
            this.qty = qty;
            this.unitPrice = unitPrice;
        }
    }

    private final String company;
    private final String address;
    private final String footer;
    private final String cashier;
    private final String receiptNo;
    private final String currency;
    private final List<Item> items = new ArrayList<>();

    /** Grand total; if left at 0 it is computed from the items. */
    public double total = 0;

    /** Name of the printer chosen in settings; blank uses the system default. */
    public String printerName = "";
    /** Receipt template name chosen in settings (e.g. "Template 101:"). */
    public String templateName = "";
    /** Premium licences get the richer template (border, barcode, branding). */
    public boolean premium = false;

    public ReceiptPrinter(String company, String address, String footer,
                          String cashier, String receiptNo, String currency) {
        this.company = blankTo(company, "RECEIPT");
        this.address = blankTo(address, "");
        this.footer = blankTo(footer, "Thank you for your business!");
        this.cashier = blankTo(cashier, "");
        this.receiptNo = blankTo(receiptNo, "");
        this.currency = blankTo(currency, "Tsh");
    }

    public ReceiptPrinter addItem(String name, double qty, double unitPrice) {
        items.add(new Item(name, qty, unitPrice));
        return this;
    }

    /**
     * Pull the printer name + template from the saved receipt settings and the
     * premium flag from the current licence, so the printed paper and layout
     * match what was chosen in settings.
     */
    public ReceiptPrinter withSettings() {
        try {
            Classes.AbstractClasses.ReceiptSettings s = Receipts.getLastReceiptSettings();
            if (s != null) {
                if (s.getPrinterName() != null) {
                    this.printerName = s.getPrinterName();
                }
                if (s.getTemplate() != null) {
                    this.templateName = s.getTemplate();
                }
            }
        } catch (Exception ignore) {
        }
        try {
            this.premium = Authentication.LicenseManager.isPremium();
        } catch (Exception ignore) {
        }
        return this;
    }

    // ---- content -----------------------------------------------------

    /** Render the whole receipt to monospaced text lines. */
    private List<String> lines() {
        double grand = total > 0 ? total : computeTotal();
        int style = premium ? templateStyle() : -1;
        List<String> out = new ArrayList<>();
        if (style == 0) {
            // Classic premium: double frame + stars.
            out.add(doubleRule());
            out.add(center("★ PREMIUM RECEIPT ★"));
            out.add(doubleRule());
        } else if (style == 1) {
            // Elegant premium: thin banner.
            out.add(center("~ ~ ~  P R E M I U M  ~ ~ ~"));
            out.add(rule());
        } else if (style == 2) {
            // Bold premium: block banner.
            out.add(center("██ PREMIUM ██"));
            out.add(doubleRule());
        }
        out.add(center(company.toUpperCase()));
        if (!address.isEmpty()) {
            for (String part : address.split("\\r?\\n|,")) {
                if (!part.trim().isEmpty()) {
                    out.add(center(part.trim()));
                }
            }
        }
        out.add(rule());
        out.add(leftRight("Date:", STAMP.format(LocalDateTime.now())));
        if (!receiptNo.isEmpty()) {
            out.add(leftRight("Receipt #:", receiptNo));
        }
        if (!cashier.isEmpty()) {
            out.add(leftRight("Served by:", cashier));
        }
        out.add(rule());
        out.add("Item");
        out.add(leftRight("  Qty x Price", "Amount"));
        out.add(rule());
        for (Item it : items) {
            out.add(clip(it.name, WIDTH));
            String qp = "  " + trimNum(it.qty) + " x " + MONEY.format(it.unitPrice);
            out.add(leftRight(qp, MONEY.format(it.qty * it.unitPrice)));
        }
        out.add(rule());
        out.add(leftRight("TOTAL", currency + " " + MONEY.format(grand)));
        // Multi-currency (Premium): show the converted total when configured.
        try {
            if (Classes.Utilities.AppOptions.hasSecondaryCurrency()) {
                String sec = Classes.Utilities.AppOptions.getSecondaryCurrency();
                double rate = Classes.Utilities.AppOptions.getSecondaryRate();
                out.add(leftRight("  ≈ " + sec, MONEY.format(grand * rate)));
            }
        } catch (Exception ignore) {
        }
        out.add(rule());
        out.add("");
        for (String part : footer.split("\\r?\\n")) {
            out.add(center(part.trim()));
        }
        if (style >= 0) {
            // A scannable-looking barcode strip + branding for the premium tier.
            out.add("");
            out.add(center(barcodeStrip()));
            if (!templateName.isEmpty()) {
                out.add(center(templateName.replace(":", "").trim()));
            }
            out.add(center("Powered by Tienda POS • Premium"));
            out.add(style == 1 ? rule() : doubleRule());
        }
        out.add("");
        return out;
    }

    /** Map the selected template name to one of the premium layouts (0..2). */
    private int templateStyle() {
        String t = templateName == null ? "" : templateName.toLowerCase();
        if (t.contains("102")) {
            return 1;
        }
        if (t.contains("103")) {
            return 2;
        }
        return 0; // default / Template 101
    }

    private String doubleRule() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WIDTH; i++) {
            sb.append('=');
        }
        return sb.toString();
    }

    private String barcodeStrip() {
        // Deterministic pattern from the receipt number — a visual flourish.
        String seed = receiptNo.isEmpty() ? String.valueOf(items.size() + 7) : receiptNo;
        StringBuilder sb = new StringBuilder();
        int n = Math.min(WIDTH - 4, 34);
        for (int i = 0; i < n; i++) {
            int v = (seed.charAt(i % seed.length()) + i) % 3;
            sb.append(v == 0 ? "█" : v == 1 ? "│" : "║");
        }
        return sb.toString();
    }

    private double computeTotal() {
        double t = 0;
        for (Item it : items) {
            t += it.qty * it.unitPrice;
        }
        return t;
    }

    // ---- preview page ------------------------------------------------

    /** Show the receipt as a page with Print / Close buttons. */
    public void showPreview(Window parent) {
        JDialog dialog = new JDialog(parent, "Receipt preview", JDialog.ModalityType.APPLICATION_MODAL);

        BufferedImage img = renderImage(2); // 2x for a crisp preview
        JLabel paper = new JLabel(new ImageIcon(img));
        paper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16),
                BorderFactory.createLineBorder(new Color(0xCCCCCC))));
        JPanel holder = new JPanel();
        holder.setBackground(new Color(0xEDEDED));
        holder.add(paper);

        JScrollPane scroll = new JScrollPane(holder);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(360, 540));

        JButton print = new JButton("Print");
        print.addActionListener(e -> {
            if (print()) {
                dialog.dispose();
            }
        });
        JButton close = new JButton("Close");
        close.addActionListener(e -> dialog.dispose());

        JPanel buttons = new JPanel();
        buttons.add(print);
        buttons.add(close);

        dialog.getContentPane().add(scroll, BorderLayout.CENTER);
        dialog.getContentPane().add(buttons, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private BufferedImage renderImage(int scale) {
        Font font = new Font("Monospaced", Font.PLAIN, 11);
        List<String> lines = lines();
        BufferedImage probe = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D pg = probe.createGraphics();
        pg.setFont(font);
        int lineH = pg.getFontMetrics().getHeight();
        int charW = pg.getFontMetrics().charWidth('0');
        pg.dispose();

        int w = (charW * WIDTH + 24) * scale;
        int h = (lineH * lines.size() + 24) * scale;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.scale(scale, scale);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawLines(g, font, lines, 12, 12);
        g.dispose();
        return img;
    }

    // ---- printing ----------------------------------------------------

    /** Send the receipt to the printer. Returns false if cancelled/failed. */
    public boolean print() {
        PrinterJob job = PrinterJob.getPrinterJob();

        // Route to the printer chosen in settings, if it is available.
        boolean chosen = false;
        try {
            if (printerName != null && !printerName.trim().isEmpty()) {
                for (javax.print.PrintService ps : PrinterJob.lookupPrintServices()) {
                    if (ps.getName() != null
                            && ps.getName().equalsIgnoreCase(printerName.trim())) {
                        job.setPrintService(ps);
                        chosen = true;
                        break;
                    }
                }
            }
        } catch (Exception ignore) {
        }

        PageFormat pf;
        if (chosen) {
            // Honour the selected printer's own paper so the receipt matches
            // what was configured in settings.
            pf = job.defaultPage();
        } else {
            // No specific printer: fall back to a ~80 mm thermal page.
            pf = job.defaultPage();
            Paper paper = new Paper();
            paper.setImageableArea(2, 2, 227, 2000);
            pf.setPaper(paper);
            pf.setOrientation(PageFormat.PORTRAIT);
        }
        job.setPrintable(this, pf);
        try {
            job.print();
            return true;
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Printing failed: " + ex.getMessage(),
                    "Printer", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.setColor(Color.BLACK);
        drawLines(g2d, new Font("Monospaced", Font.PLAIN, 9), lines(), 0, 0);
        return PAGE_EXISTS;
    }

    private void drawLines(Graphics2D g, Font font, List<String> lines, int x, int y) {
        g.setColor(Color.BLACK);
        int lineH = g.getFontMetrics(font).getHeight();
        int cy = y + lineH;
        for (String line : lines) {
            // Headline (company) and TOTAL in bold for emphasis.
            if (line.equals(center(company.toUpperCase())) || line.startsWith("TOTAL")) {
                g.setFont(font.deriveFont(Font.BOLD));
            } else {
                g.setFont(font);
            }
            g.drawString(line, x, cy);
            cy += lineH;
        }
    }

    // ---- text helpers ------------------------------------------------

    private static String rule() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WIDTH; i++) {
            sb.append('-');
        }
        return sb.toString();
    }

    private static String center(String s) {
        if (s.length() >= WIDTH) {
            return s.substring(0, WIDTH);
        }
        int pad = (WIDTH - s.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pad; i++) {
            sb.append(' ');
        }
        return sb.append(s).toString();
    }

    private static String leftRight(String left, String right) {
        int space = WIDTH - left.length() - right.length();
        if (space < 1) {
            left = clip(left, Math.max(1, WIDTH - right.length() - 1));
            space = Math.max(1, WIDTH - left.length() - right.length());
        }
        StringBuilder sb = new StringBuilder(left);
        for (int i = 0; i < space; i++) {
            sb.append(' ');
        }
        return sb.append(right).toString();
    }

    private static String clip(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max);
    }

    private static String trimNum(double d) {
        return d == Math.floor(d) ? String.valueOf((long) d) : String.valueOf(d);
    }

    private static String blankTo(String s, String fallback) {
        return (s == null || s.trim().isEmpty()) ? fallback : s.trim();
    }
}
