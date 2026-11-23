package Classes.Functions.Printers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
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

/**
 * Builds, previews and prints a sales receipt in one of five designs.
 *
 * <p>Each design is drawn with Java2D (not monospaced text), once for the
 * on-screen preview and once for the printer, so what the cashier sees is what
 * the printer produces. Everything is black on white so it prints cleanly on
 * thermal paper.
 *
 * <ul>
 *   <li><b>Template 101, Classic</b>: till-roll look, typewriter font, dashed rules.</li>
 *   <li><b>Template 102, Modern</b>: black header band with a monogram, info
 *       block, item list, total in a box.</li>
 *   <li><b>Template 103, Invoice</b>: company and receipt number side by side,
 *       Item/Qty/Price/Amount columns, subtotal, tax (when set), barcode.</li>
 *   <li><b>Template 104, Minimal</b>: lots of white space, hairlines, spaced caps.</li>
 *   <li><b>Template 105, Signature</b> (Premium licence): double frame, monogram
 *       seal, striped items, inverted total bar, barcode, signed thank-you.</li>
 * </ul>
 * Without a Premium licence, Template 105 prints as Template 101.
 *
 * <pre>
 *   ReceiptPrinter r = new ReceiptPrinter(company, address, footer, cashier, receiptNo, currency);
 *   r.addItem("Coca-Cola 500ml", 2, 1500);
 *   r.withSettings();              // printer, template and licence from settings
 *   r.showPreview(parentWindow);   // cashier clicks Print
 * </pre>
 *
 * @author Nkanabo
 */
public class ReceiptPrinter implements Printable {

    /** Printable width in points of 80 mm rolls (72 mm print area). */
    public static final float WIDTH_80MM = 204f;
    /** Printable width in points of 58 mm rolls (48 mm print area). */
    public static final float WIDTH_58MM = 136f;
    /** Narrower paper is drawn at this width and scaled down, so layouts never cramp. */
    private static final float MIN_LOGICAL_W = 190f;

    /** Printable width in points; from the paper size in settings. */
    public float paperWidth = paperWidthFromSettings();
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.##");
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm");

    private static final Color INK = Color.BLACK;
    private static final Color SOFT = new Color(0x555555);
    private static final Color TINT = new Color(0xEEEEEE);

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
    /** Optional customer name; hidden when blank. */
    public String customer = "";
    /** Tax rate in percent already included in prices (e.g. 18). 0 hides the tax line. */
    public double taxRate = 0;
    /** When the sale happened; defaults to now. */
    public LocalDateTime when = LocalDateTime.now();

    /** Name of the printer chosen in settings; blank uses the system default. */
    public String printerName = "";
    /** Receipt template name chosen in settings (e.g. "Template 101:"). */
    public String templateName = "";
    /** A Premium licence unlocks Template 105. */
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
     * Pull the printer name and template from the saved receipt settings and
     * the premium flag from the current licence.
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
        this.premium = licencePremium();
        return this;
    }

    /** True when the active licence is Premium (false if it cannot be read). */
    public static boolean licencePremium() {
        try {
            return Authentication.LicenseManager.isPremium();
        } catch (Throwable ignore) {
            return false;
        }
    }

    /** True when this template needs a Premium licence. */
    public static boolean isPremiumTemplate(String templateName) {
        return templateNumber(templateName) == 105;
    }

    /**
     * A receipt filled with example items, for the template previews in
     * settings. Blank values fall back to example ones.
     */
    public static ReceiptPrinter sample(String templateName, boolean premium,
                                        String company, String address, String footer) {
        String cashier = "Cashier";
        try {
            cashier = blankTo(Authentication.Sessions.LoggedUser, cashier);
        } catch (Throwable ignore) {
        }
        ReceiptPrinter r = new ReceiptPrinter(
                blankTo(company, "Tienda Mini Market"),
                blankTo(address, "Plot 12, Samora Avenue, Dar es Salaam"),
                blankTo(footer, "Asante kwa kununua! Karibu tena."),
                cashier, "100245", "Tsh");
        r.customer = "Walk-in customer";
        r.templateName = templateName == null ? "" : templateName;
        r.premium = premium;
        r.addItem("Coca-Cola 500ml", 2, 1500)
         .addItem("Azam Bread 600g", 1, 2800)
         .addItem("Kilimanjaro Water 1.5L", 3, 1000)
         .addItem("Blue Band Margarine 250g", 1, 4500);
        return r;
    }

    // ---- template choice ----------------------------------------------

    private static int templateNumber(String name) {
        String t = name == null ? "" : name;
        for (int n = 101; n <= 105; n++) {
            if (t.contains(String.valueOf(n))) {
                return n;
            }
        }
        return 101;
    }

    /** The design actually drawn: 105 falls back to 101 without Premium. */
    private int design() {
        int n = templateNumber(templateName);
        return (n == 105 && !premium) ? 101 : n;
    }

    // ---- drawing --------------------------------------------------------

    /**
     * Draws the receipt at (0,0) across {@code w} points and returns the height
     * used. Called once on a scratch image to measure, then for real.
     */
    private float draw(Graphics2D g, float w) {
        float logical = Math.max(w, MIN_LOGICAL_W);
        float k = w / logical;
        if (k < 1f) {
            g.scale(k, k);
        }
        return drawLogical(g, logical) * Math.min(1f, k);
    }

    private float drawLogical(Graphics2D g, float w) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Pen p = new Pen(g, w);
        switch (design()) {
            case 102: modern(p); break;
            case 103: invoice(p); break;
            case 104: minimal(p); break;
            case 105: signature(p); break;
            default: classic(p); break;
        }
        return p.y;
    }

    /** Template 101: the familiar till roll. */
    private void classic(Pen p) {
        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 8);
        Font monoB = mono.deriveFont(Font.BOLD);
        p.pad = 8;
        p.gap(10);
        p.center(company.toUpperCase(), new Font(Font.MONOSPACED, Font.BOLD, 13));
        p.gap(1);
        for (String line : addressLines()) {
            p.center(line, mono);
        }
        p.gap(4);
        p.dashed();
        p.row("Date", STAMP.format(when), mono);
        if (!receiptNo.isEmpty()) {
            p.row("Receipt #", receiptNo, mono);
        }
        if (!cashier.isEmpty()) {
            p.row("Cashier", cashier, mono);
        }
        if (!customer.isEmpty()) {
            p.row("Customer", customer, mono);
        }
        p.dashed();
        for (Item it : items) {
            p.wrap(it.name, monoB, Align.LEFT);
            p.row("  " + qty(it.qty) + " x " + MONEY.format(it.unitPrice),
                    MONEY.format(it.qty * it.unitPrice), mono);
            p.gap(2);
        }
        p.dashed();
        p.row("Items", qty(itemCount()), mono);
        p.gap(2);
        p.row("TOTAL " + currency, MONEY.format(grand()), new Font(Font.MONOSPACED, Font.BOLD, 12));
        secondaryCurrency(p, mono);
        p.dashed();
        p.gap(4);
        for (String line : footerLines()) {
            p.wrap(line, mono, Align.CENTER);
        }
        p.gap(4);
        p.center("*** " + (receiptNo.isEmpty() ? "THANK YOU" : "No. " + receiptNo) + " ***", mono);
        p.gap(12);
    }

    /** Template 102: black band header, info block, item list, boxed total. */
    private void modern(Pen p) {
        Font body = new Font(Font.SANS_SERIF, Font.PLAIN, 8);
        Font bodyB = body.deriveFont(Font.BOLD);
        Font label = new Font(Font.SANS_SERIF, Font.BOLD, 6);
        Font head = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        p.pad = 10;

        // Header band with a monogram badge.
        float bandH = 46;
        p.g.setColor(INK);
        p.g.fill(new Rectangle2D.Float(0, p.y, p.w, bandH));
        float r = 13, cx = p.pad + r, cy = p.y + bandH / 2;
        p.g.setColor(Color.WHITE);
        p.g.fill(new Ellipse2D.Float(cx - r, cy - r, 2 * r, 2 * r));
        p.g.setColor(INK);
        p.text(initials(), new Font(Font.SANS_SERIF, Font.BOLD, 11), cx, cy, Align.CENTER, true);
        p.g.setColor(Color.WHITE);
        float tx = cx + r + 8;
        p.textAt(clipTo(company, head, p.w - tx - p.pad), head, tx, cy - 1);
        p.textAt("S A L E S   R E C E I P T", label, tx, cy + 10);
        p.y += bandH + 8;
        p.g.setColor(INK);

        for (String line : addressLines()) {
            p.center(line, new Font(Font.SANS_SERIF, Font.PLAIN, 7), SOFT);
        }
        p.gap(8);

        // Two-column info block.
        float half = (p.w - 2 * p.pad) / 2;
        float top = p.y;
        p.labelValue("RECEIPT NO.", receiptNo.isEmpty() ? "-" : receiptNo, label, bodyB, p.pad);
        p.labelValue("CASHIER", cashier.isEmpty() ? "-" : cashier, label, bodyB, p.pad);
        float leftEnd = p.y;
        p.y = top;
        p.labelValue("DATE", STAMP.format(when), label, bodyB, p.pad + half);
        p.labelValue("CUSTOMER", customer.isEmpty() ? "Walk-in" : customer, label, bodyB, p.pad + half);
        p.y = Math.max(leftEnd, p.y) + 2;

        // Item list.
        p.solid(1.2f);
        p.row("ITEM", "AMOUNT", label);
        p.solid(0.4f);
        for (Item it : items) {
            p.gap(2);
            p.row(it.name, MONEY.format(it.qty * it.unitPrice), bodyB);
            p.left(qty(it.qty) + " × " + MONEY.format(it.unitPrice),
                    new Font(Font.SANS_SERIF, Font.PLAIN, 7), SOFT);
            p.gap(2);
        }
        p.solid(0.4f);
        p.gap(4);

        // Boxed total.
        float boxH = 28;
        p.g.setStroke(new BasicStroke(1.5f));
        p.g.draw(new Rectangle2D.Float(p.pad, p.y, p.w - 2 * p.pad, boxH));
        p.text("TOTAL", label, p.pad + 8, p.y + boxH / 2, Align.LEFT, true);
        p.text(currency + " " + MONEY.format(grand()), new Font(Font.SANS_SERIF, Font.BOLD, 13),
                p.w - p.pad - 8, p.y + boxH / 2, Align.RIGHT, true);
        p.y += boxH + 4;
        secondaryCurrency(p, body);
        p.gap(10);
        for (String line : footerLines()) {
            p.wrap(line, new Font(Font.SANS_SERIF, Font.ITALIC, 8), Align.CENTER);
        }
        p.gap(14);
    }

    /** Template 103: invoice-style columns, subtotal and tax, barcode. */
    private void invoice(Pen p) {
        Font body = new Font(Font.SANS_SERIF, Font.PLAIN, 7);
        Font bodyB = body.deriveFont(Font.BOLD);
        Font label = new Font(Font.SANS_SERIF, Font.BOLD, 6);
        Font head = new Font(Font.SANS_SERIF, Font.BOLD, 10);
        p.pad = 8;
        p.gap(12);

        // Company (left) and receipt number (right).
        float top = p.y;
        p.g.setColor(INK);
        p.g.fill(new Rectangle2D.Float(p.pad, p.y, 24, 24));
        p.g.setColor(Color.WHITE);
        p.text(initials(), new Font(Font.SANS_SERIF, Font.BOLD, 10), p.pad + 12, p.y + 12, Align.CENTER, true);
        p.g.setColor(INK);
        float tx = p.pad + 30;
        float room = p.w - tx - p.pad - 52;
        p.textAt(clipTo(company, head, room), head, tx, p.y + 10);
        p.g.setColor(SOFT);
        p.textAt(clipTo(String.join(", ", addressLines()), body, room), body, tx, p.y + 20);
        p.g.setColor(INK);
        p.text("RECEIPT", label, p.w - p.pad, p.y + 7, Align.RIGHT, true);
        p.text("#" + (receiptNo.isEmpty() ? "-" : receiptNo), new Font(Font.SANS_SERIF, Font.BOLD, 9),
                p.w - p.pad, p.y + 18, Align.RIGHT, true);
        p.y = top + 32;
        p.solid(1f);

        p.row("Date: " + STAMP.format(when), cashier.isEmpty() ? "" : "Cashier: " + cashier, body);
        if (!customer.isEmpty()) {
            p.left("Bill to: " + customer, bodyB, INK);
        }
        p.gap(5);

        // Column table.
        float c1 = p.pad, c4 = p.w - p.pad, c3 = c4 - 44, c2 = c3 - 40;
        p.g.setColor(TINT);
        p.g.fill(new Rectangle2D.Float(p.pad, p.y, p.w - 2 * p.pad, 12));
        p.g.setColor(INK);
        float hy = p.y + 6;
        p.text("ITEM", label, c1 + 3, hy, Align.LEFT, true);
        p.text("QTY", label, c2, hy, Align.RIGHT, true);
        p.text("PRICE", label, c3, hy, Align.RIGHT, true);
        p.text("AMOUNT", label, c4 - 3, hy, Align.RIGHT, true);
        p.y += 14;
        for (Item it : items) {
            List<String> name = p.split(it.name, body, c2 - c1 - 24);
            float lineH = p.lineH(body);
            float rowTop = p.y;
            for (int i = 0; i < name.size(); i++) {
                p.textAt(name.get(i), body, c1 + 3, rowTop + lineH * (i + 1) - 2);
            }
            float base = rowTop + lineH - 2;
            p.text(qty(it.qty), body, c2, base, Align.RIGHT, false);
            p.text(MONEY.format(it.unitPrice), body, c3, base, Align.RIGHT, false);
            p.text(MONEY.format(it.qty * it.unitPrice), bodyB, c4 - 3, base, Align.RIGHT, false);
            p.y = rowTop + lineH * name.size() + 2;
            p.dotted();
            p.gap(1);
        }
        p.gap(3);

        // Totals, right half.
        double gross = grand();
        p.rowRight("Subtotal", MONEY.format(gross), body);
        if (taxRate > 0) {
            double tax = gross * taxRate / (100 + taxRate);
            p.rowRight("VAT incl. (" + qty(taxRate) + "%)", MONEY.format(tax), body);
        }
        p.rowRight("Items", qty(itemCount()), body);
        p.gap(2);
        p.g.setStroke(new BasicStroke(1f));
        p.g.draw(new Line2D.Float(p.w / 2, p.y, p.w - p.pad, p.y));
        p.gap(3);
        p.rowRight("TOTAL " + currency, MONEY.format(gross), new Font(Font.SANS_SERIF, Font.BOLD, 10));
        secondaryCurrency(p, body);
        p.gap(10);
        barcode(p, 30);
        p.gap(6);
        for (String line : footerLines()) {
            p.wrap(line, body, Align.CENTER);
        }
        p.gap(14);
    }

    /** Template 104: quiet and airy. */
    private void minimal(Pen p) {
        Font body = new Font(Font.SANS_SERIF, Font.PLAIN, 8);
        Font small = new Font(Font.SANS_SERIF, Font.PLAIN, 6);
        p.pad = 16;
        p.gap(20);
        p.center(spaced(company.toUpperCase()), new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        p.gap(3);
        for (String line : addressLines()) {
            p.center(line, small, SOFT);
        }
        p.gap(14);
        p.center(STAMP.format(when) + (receiptNo.isEmpty() ? "" : "   ·   No. " + receiptNo), small, SOFT);
        p.gap(10);
        p.hairline();
        p.gap(8);
        for (Item it : items) {
            String name = it.qty == 1 ? it.name : qty(it.qty) + " × " + it.name;
            p.row(name, MONEY.format(it.qty * it.unitPrice), body);
            p.gap(4);
        }
        p.gap(2);
        p.hairline();
        p.gap(10);
        p.row(spaced("TOTAL"), currency + " " + MONEY.format(grand()), new Font(Font.SANS_SERIF, Font.BOLD, 10));
        secondaryCurrency(p, small);
        p.gap(18);
        for (String line : footerLines()) {
            p.wrap(line, small, Align.CENTER);
        }
        if (!cashier.isEmpty()) {
            p.gap(2);
            p.center("served by " + cashier, small, SOFT);
        }
        p.gap(22);
    }

    /** Template 105: the Premium signature design. */
    private void signature(Pen p) {
        Font body = new Font(Font.SANS_SERIF, Font.PLAIN, 7);
        Font bodyB = body.deriveFont(Font.BOLD);
        Font label = new Font(Font.SANS_SERIF, Font.BOLD, 6);
        float frame = 4;
        p.pad = 14;
        float start = p.y;
        p.gap(18);

        // Monogram seal: two rings around the initials.
        float r = 17, cx = p.w / 2, cy = p.y + r;
        p.g.setStroke(new BasicStroke(1.4f));
        p.g.draw(new Ellipse2D.Float(cx - r, cy - r, 2 * r, 2 * r));
        p.g.setStroke(new BasicStroke(0.5f));
        p.g.draw(new Ellipse2D.Float(cx - r + 3, cy - r + 3, 2 * r - 6, 2 * r - 6));
        p.text(initials(), new Font(Font.SERIF, Font.BOLD, 13), cx, cy, Align.CENTER, true);
        p.y += 2 * r + 8;

        p.wrap(company, new Font(Font.SERIF, Font.BOLD, 14), Align.CENTER);
        p.gap(1);
        for (String line : addressLines()) {
            p.center(line, body, SOFT);
        }
        p.gap(6);
        p.ornament();
        p.gap(6);

        p.row("RECEIPT", receiptNo.isEmpty() ? "-" : "No. " + receiptNo, label);
        p.row("DATE", STAMP.format(when), label);
        if (!cashier.isEmpty()) {
            p.row("SERVED BY", cashier, label);
        }
        if (!customer.isEmpty()) {
            p.row("GUEST", customer, label);
        }
        p.gap(6);

        // Striped item rows.
        boolean shade = true;
        for (Item it : items) {
            float rowTop = p.y;
            float h = p.lineH(bodyB) + p.lineH(body) + 4;
            if (shade) {
                p.g.setColor(TINT);
                p.g.fill(new Rectangle2D.Float(p.pad - 4, rowTop, p.w - 2 * p.pad + 8, h));
            }
            p.g.setColor(INK);
            p.gap(2);
            p.row(it.name, MONEY.format(it.qty * it.unitPrice), bodyB);
            p.left(qty(it.qty) + " × " + MONEY.format(it.unitPrice), body, SOFT);
            p.y = Math.max(p.y, rowTop + h);
            shade = !shade;
        }
        p.gap(6);

        // Inverted total bar.
        float barH = 24;
        p.g.setColor(INK);
        p.g.fill(new Rectangle2D.Float(p.pad - 4, p.y, p.w - 2 * p.pad + 8, barH));
        p.g.setColor(Color.WHITE);
        p.text("TOTAL", label, p.pad + 2, p.y + barH / 2, Align.LEFT, true);
        p.text(currency + " " + MONEY.format(grand()), new Font(Font.SANS_SERIF, Font.BOLD, 12),
                p.w - p.pad - 2, p.y + barH / 2, Align.RIGHT, true);
        p.g.setColor(INK);
        p.y += barH + 3;
        p.row(qty(itemCount()) + " items", "", body, SOFT);
        secondaryCurrency(p, body);
        p.gap(10);
        barcode(p, 28);
        p.gap(8);
        p.wrap("Thank you", new Font(Font.SERIF, Font.ITALIC, 15), Align.CENTER);
        for (String line : footerLines()) {
            p.wrap(line, new Font(Font.SERIF, Font.ITALIC, 8), Align.CENTER);
        }
        p.gap(6);
        p.ornament();
        p.gap(4);
        p.center("TIENDA POS · PREMIUM", new Font(Font.SANS_SERIF, Font.PLAIN, 5), SOFT);
        p.gap(12 + frame);

        // Double frame around the whole receipt.
        p.g.setColor(INK);
        p.g.setStroke(new BasicStroke(1.2f));
        p.g.draw(new Rectangle2D.Float(frame, start + frame, p.w - 2 * frame, p.y - start - 2 * frame));
        p.g.setStroke(new BasicStroke(0.4f));
        p.g.draw(new Rectangle2D.Float(frame + 3, start + frame + 3,
                p.w - 2 * frame - 6, p.y - start - 2 * frame - 6));
    }

    // ---- shared pieces --------------------------------------------------

    private void secondaryCurrency(Pen p, Font f) {
        try {
            if (Classes.Utilities.AppOptions.hasSecondaryCurrency()) {
                String sec = Classes.Utilities.AppOptions.getSecondaryCurrency();
                double rate = Classes.Utilities.AppOptions.getSecondaryRate();
                p.row("≈ " + sec, MONEY.format(grand() * rate), f, SOFT);
            }
        } catch (Throwable ignore) {
        }
    }

    /** Code 128 barcode of the receipt number, with the number under it. */
    private void barcode(Pen p, float height) {
        String code = receiptNo.isEmpty() ? "0000" : receiptNo;
        try {
            BitMatrix m = new Code128Writer().encode(code, BarcodeFormat.CODE_128, 0, 0);
            float module = Math.min(1.4f, (p.w - 2 * p.pad) / m.getWidth());
            float x0 = (p.w - module * m.getWidth()) / 2;
            p.g.setColor(INK);
            for (int x = 0; x < m.getWidth(); x++) {
                if (m.get(x, 0)) {
                    p.g.fill(new Rectangle2D.Float(x0 + x * module, p.y, module, height));
                }
            }
            p.y += height + 2;
            p.center(code, new Font(Font.MONOSPACED, Font.PLAIN, 7));
        } catch (Throwable ignore) {
            p.center("No. " + code, new Font(Font.MONOSPACED, Font.PLAIN, 8));
        }
    }

    private List<String> addressLines() {
        List<String> out = new ArrayList<>();
        for (String part : address.split("\\r?\\n|,")) {
            if (!part.trim().isEmpty()) {
                out.add(part.trim());
            }
        }
        return out;
    }

    private List<String> footerLines() {
        List<String> out = new ArrayList<>();
        for (String part : footer.split("\\r?\\n")) {
            if (!part.trim().isEmpty()) {
                out.add(part.trim());
            }
        }
        return out;
    }

    private String initials() {
        StringBuilder sb = new StringBuilder();
        for (String w : company.trim().split("\\s+")) {
            if (!w.isEmpty() && Character.isLetterOrDigit(w.charAt(0))) {
                sb.append(Character.toUpperCase(w.charAt(0)));
            }
            if (sb.length() == 2) {
                break;
            }
        }
        return sb.length() == 0 ? "T" : sb.toString();
    }

    private double grand() {
        if (total > 0) {
            return total;
        }
        double t = 0;
        for (Item it : items) {
            t += it.qty * it.unitPrice;
        }
        return t;
    }

    private double itemCount() {
        double n = 0;
        for (Item it : items) {
            n += it.qty;
        }
        return n;
    }

    private static String spaced(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    private static final Graphics2D METRICS =
            new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();

    private static String clipTo(String s, Font f, float max) {
        FontMetrics fm = METRICS.getFontMetrics(f);
        if (fm.stringWidth(s) <= max) {
            return s;
        }
        String t = s;
        while (t.length() > 1 && fm.stringWidth(t + "…") > max) {
            t = t.substring(0, t.length() - 1);
        }
        return t + "…";
    }

    private enum Align { LEFT, CENTER, RIGHT }

    /** A cursor that moves down the paper as things are drawn. */
    private static final class Pen {
        final Graphics2D g;
        final float w;
        float y = 0;
        float pad = 8;

        Pen(Graphics2D g, float w) {
            this.g = g;
            this.w = w;
            g.setColor(INK);
        }

        float lineH(Font f) {
            FontMetrics fm = g.getFontMetrics(f);
            return fm.getAscent() + fm.getDescent() + 1;
        }

        void gap(float h) {
            y += h;
        }

        /** Draw at a baseline. */
        void textAt(String s, Font f, float x, float baseline) {
            g.setFont(f);
            g.drawString(s, x, baseline);
        }

        /** Draw aligned on x; centred vertically on yy when {@code middle}, else yy is the baseline. */
        void text(String s, Font f, float x, float yy, Align a, boolean middle) {
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            float sw = fm.stringWidth(s);
            float tx = a == Align.CENTER ? x - sw / 2 : a == Align.RIGHT ? x - sw : x;
            float base = middle ? yy + (fm.getAscent() - fm.getDescent()) / 2f : yy;
            g.drawString(s, tx, base);
        }

        void line(String s, Font f, Align a, Color c) {
            Color old = g.getColor();
            g.setColor(c);
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            float x = a == Align.CENTER ? (w - fm.stringWidth(s)) / 2
                    : a == Align.RIGHT ? w - pad - fm.stringWidth(s) : pad;
            g.drawString(s, x, y + fm.getAscent());
            y += lineH(f);
            g.setColor(old);
        }

        void center(String s, Font f) {
            wrap(s, f, Align.CENTER);
        }

        void center(String s, Font f, Color c) {
            for (String l : split(s, f, w - 2 * pad)) {
                line(l, f, Align.CENTER, c);
            }
        }

        void left(String s, Font f, Color c) {
            for (String l : split(s, f, w - 2 * pad)) {
                line(l, f, Align.LEFT, c);
            }
        }

        void wrap(String s, Font f, Align a) {
            for (String l : split(s, f, w - 2 * pad)) {
                line(l, f, a, g.getColor());
            }
        }

        /** Left text (wrapped if needed) with a right-aligned value on its first line. */
        void row(String left, String right, Font f) {
            row(left, right, f, g.getColor());
        }

        void row(String left, String right, Font f, Color c) {
            Color old = g.getColor();
            g.setColor(c);
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            float rw = fm.stringWidth(right);
            g.drawString(right, w - pad - rw, y + fm.getAscent());
            for (String l : split(left, f, w - 2 * pad - rw - 6)) {
                g.drawString(l, pad, y + fm.getAscent());
                y += lineH(f);
            }
            g.setColor(old);
        }

        /** Label and value in the right half (for totals blocks). */
        void rowRight(String label, String value, Font f) {
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            float base = y + fm.getAscent();
            float vw = fm.stringWidth(value);
            // Start in the right half, but move left rather than overlap the value.
            float lx = Math.max(pad, Math.min(w / 2, w - pad - vw - 8 - fm.stringWidth(label)));
            g.drawString(label, lx, base);
            g.drawString(value, w - pad - vw, base);
            y += lineH(f);
        }

        void labelValue(String label, String value, Font lf, Font vf, float x) {
            Color old = g.getColor();
            g.setColor(SOFT);
            textAt(label, lf, x, y + g.getFontMetrics(lf).getAscent());
            y += lineH(lf);
            g.setColor(INK);
            textAt(clipTo(value, vf, (w - 2 * pad) / 2 - 4), vf, x, y + g.getFontMetrics(vf).getAscent());
            y += lineH(vf) + 4;
            g.setColor(old);
        }

        void solid(float stroke) {
            g.setStroke(new BasicStroke(stroke));
            g.draw(new Line2D.Float(pad, y + 2, w - pad, y + 2));
            y += 5;
        }

        void hairline() {
            g.setStroke(new BasicStroke(0.3f));
            g.draw(new Line2D.Float(pad, y, w - pad, y));
            y += 1;
        }

        void dashed() {
            g.setStroke(new BasicStroke(0.7f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f,
                    new float[]{3f, 2f}, 0f));
            g.draw(new Line2D.Float(pad, y + 3, w - pad, y + 3));
            g.setStroke(new BasicStroke(1f));
            y += 7;
        }

        void dotted() {
            g.setStroke(new BasicStroke(0.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f,
                    new float[]{0.5f, 2f}, 0f));
            g.draw(new Line2D.Float(pad, y, w - pad, y));
            g.setStroke(new BasicStroke(1f));
            y += 2;
        }

        /** A short rule with a diamond in the middle. */
        void ornament() {
            float cx = w / 2, cy = y + 3, half = 40;
            g.setStroke(new BasicStroke(0.5f));
            g.draw(new Line2D.Float(cx - half, cy, cx - 6, cy));
            g.draw(new Line2D.Float(cx + 6, cy, cx + half, cy));
            Path2D d = new Path2D.Float();
            d.moveTo(cx, cy - 3);
            d.lineTo(cx + 3, cy);
            d.lineTo(cx, cy + 3);
            d.lineTo(cx - 3, cy);
            d.closePath();
            g.fill(d);
            y += 6;
        }

        /** Word-wrap to a width; very long words are cut. */
        List<String> split(String s, Font f, float max) {
            FontMetrics fm = g.getFontMetrics(f);
            List<String> out = new ArrayList<>();
            StringBuilder cur = new StringBuilder();
            for (String word : (s == null ? "" : s).split(" ")) {
                String trial = cur.length() == 0 ? word : cur + " " + word;
                if (fm.stringWidth(trial) <= max) {
                    cur.setLength(0);
                    cur.append(trial);
                    continue;
                }
                if (cur.length() > 0) {
                    out.add(cur.toString());
                }
                String wd = word;
                while (fm.stringWidth(wd) > max && wd.length() > 1) {
                    int n = wd.length();
                    while (n > 1 && fm.stringWidth(wd.substring(0, n)) > max) {
                        n--;
                    }
                    out.add(wd.substring(0, n));
                    wd = wd.substring(n);
                }
                cur.setLength(0);
                cur.append(wd);
            }
            if (cur.length() > 0 || out.isEmpty()) {
                out.add(cur.toString());
            }
            return out;
        }
    }

    // ---- images and preview -----------------------------------------

    private float measure() {
        Graphics2D pg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
        float h = draw(pg, paperWidth);
        pg.dispose();
        return h;
    }

    /** Render the receipt to an image; {@code scale} 2 gives a crisp preview. */
    public BufferedImage renderImage(double scale) {
        float h = measure();
        int iw = (int) Math.ceil(paperWidth * scale);
        int ih = (int) Math.ceil(h * scale);
        BufferedImage img = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, iw, ih);
        g.scale(scale, scale);
        draw(g, paperWidth);
        g.dispose();
        return img;
    }

    /** The receipt as a paper-like label, for embedding in a settings page. */
    public JLabel previewLabel(double scale) {
        JLabel paper = new JLabel(new ImageIcon(renderImage(scale)));
        paper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0xD5D5D5)),
                BorderFactory.createLineBorder(new Color(0xCCCCCC))));
        return paper;
    }

    /** Show the receipt as a page with Print / Close buttons. */
    public void showPreview(Window parent) {
        JDialog dialog = new JDialog(parent, "Receipt preview", JDialog.ModalityType.APPLICATION_MODAL);

        JPanel holder = new JPanel();
        holder.setBackground(new Color(0xEDEDED));
        holder.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        holder.add(previewLabel(2));

        JScrollPane scroll = new JScrollPane(holder);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(520, 640));

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

        PageFormat pf = job.defaultPage();
        if (!chosen) {
            // No specific printer: an 80 mm roll page as long as the receipt.
            float h = measure();
            Paper paper = new Paper();
            paper.setSize(paperWidth + 4, h + 4);
            paper.setImageableArea(2, 2, paperWidth, h);
            pf.setPaper(paper);
            pf.setOrientation(PageFormat.PORTRAIT);
        }
        job.setPrintable(this, pf);
        try {
            job.print();
            if (Classes.Utilities.AppOptions.isCashDrawerEnabled()) {
                openCashDrawer(job.getPrintService());
            }
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
        draw(g2d, (float) Math.min(paperWidth, pageFormat.getImageableWidth()));
        return PAGE_EXISTS;
    }

    // ---- cash drawer -------------------------------------------------

    /**
     * Pulse the cash drawer plugged into the receipt printer (ESC p 0 25 250,
     * the standard ESC/POS drawer kick).
     *
     * @return true if the command was sent
     */
    public static boolean openCashDrawer(javax.print.PrintService service) {
        if (service == null) {
            return false;
        }
        byte[] kick = {0x1B, 0x70, 0x00, 0x19, (byte) 0xFA};
        try {
            javax.print.DocFlavor flavor = javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE;
            if (!service.isDocFlavorSupported(flavor)) {
                return false;
            }
            service.createPrintJob().print(new javax.print.SimpleDoc(kick, flavor, null), null);
            return true;
        } catch (Exception e) {
            Classes.Utilities.Log.error(e);
            return false;
        }
    }

    private static float paperWidthFromSettings() {
        try {
            return Classes.Utilities.AppOptions.getPaperWidthMm() == 58 ? WIDTH_58MM : WIDTH_80MM;
        } catch (Throwable e) {
            return WIDTH_80MM;
        }
    }

    // ---- helpers -------------------------------------------------------

    private static String qty(double d) {
        return d == Math.floor(d) ? String.valueOf((long) d) : MONEY.format(d);
    }

    private static String blankTo(String s, String fallback) {
        return (s == null || s.trim().isEmpty()) ? fallback : s.trim();
    }
}
