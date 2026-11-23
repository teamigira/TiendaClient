package Classes.Utilities;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Central look-and-feel: one font family everywhere and a single colour accent,
 * applied globally so the whole app reads as one product — without moving the
 * left menu or any other component.
 *
 * <p>Two things make the typography actually consistent (the previous version
 * only set a default font, but ~360 components hard-code {@code "Segoe UI"},
 * which does not exist on Linux and fell back to different substitutes):
 * <ol>
 *   <li>{@link #apply()} picks the best font family that is <b>actually
 *       installed</b> on this machine and sets it as the UI default; and</li>
 *   <li>a window listener re-stamps that family onto every component as each
 *       window opens ({@link #normalizeFonts}), keeping each component's own
 *       size and bold/italic style but unifying the family.</li>
 * </ol>
 *
 * @author Nkanabo
 */
public final class Theme {

    /** Preferred UI fonts, best first; the first one installed here wins. */
    private static final String[] PREFERRED = {
        "Segoe UI", "Inter", "Roboto", "Open Sans", "Noto Sans",
        "Ubuntu", "Cantarell", "DejaVu Sans", "Liberation Sans"
    };

    /** Monospaced family for receipts/figures — kept separate on purpose. */
    public static final String MONO = "Monospaced";

    // Accent / palette (used via UIManager only — no component is relocated).
    public static final Color ACCENT = new Color(0x2E7D32);       // Tienda green
    public static final Color ACCENT_DK = new Color(0x1B5E20);
    private static String family = "SansSerif";
    private static boolean installed = false;

    private Theme() {
    }

    /** The resolved UI font family (valid after {@link #apply()}). */
    public static String family() {
        return family;
    }

    /** Apply global typography, colours and polish. Safe to call more than once. */
    public static void apply() {
        try {
            family = pickFamily();
            System.err.println("[Theme] UI font family resolved to: " + family);

            UIManager.put("defaultFont", new Font(family, Font.PLAIN, 13));

            // --- accent / selection colours -------------------------------
            UIManager.put("Component.accentColor", ACCENT);
            UIManager.put("Component.focusColor", ACCENT);
            UIManager.put("Button.default.background", ACCENT);
            UIManager.put("Button.default.foreground", Color.WHITE);
            UIManager.put("ProgressBar.foreground", ACCENT);
            UIManager.put("Table.selectionBackground", ACCENT);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("List.selectionBackground", ACCENT);
            UIManager.put("List.selectionForeground", Color.WHITE);
            UIManager.put("TabbedPane.underlineColor", ACCENT);
            UIManager.put("TextComponent.selectionBackground", new Color(0xA5D6A7));

            // --- rounding / spacing (cosmetic only) -----------------------
            UIManager.put("Button.arc", 14);
            UIManager.put("Component.arc", 14);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ProgressBar.arc", 12);
            UIManager.put("CheckBox.arc", 6);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("Table.rowHeight", 28);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
            UIManager.put("TableHeader.height", 32);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("TabbedPane.tabHeight", 36);
            UIManager.put("TitlePane.unifiedBackground", true);

            installWindowNormalizer();
        } catch (Exception ignore) {
            // Styling is best-effort; the app must still run.
        }
    }

    /** Choose the first preferred font family that is installed on this host. */
    private static String pickFamily() {
        try {
            Set<String> have = new HashSet<>(Arrays.asList(
                    GraphicsEnvironment.getLocalGraphicsEnvironment()
                            .getAvailableFontFamilyNames()));
            for (String f : PREFERRED) {
                if (have.contains(f)) {
                    return f;
                }
            }
        } catch (Exception ignore) {
        }
        return "SansSerif";
    }

    /** Add a listener that unifies fonts on every window as it opens. */
    private static void installWindowNormalizer() {
        if (installed) {
            return;
        }
        try {
            Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
                @Override
                public void eventDispatched(AWTEvent event) {
                    if (event instanceof WindowEvent
                            && event.getID() == WindowEvent.WINDOW_OPENED) {
                        Component w = ((WindowEvent) event).getWindow();
                        SwingUtilities.invokeLater(() -> normalizeFonts(w));
                    }
                }
            }, AWTEvent.WINDOW_EVENT_MASK);
            installed = true;
            System.err.println("[Theme] window font normalizer installed");
        } catch (Throwable t) {
            System.err.println("[Theme] could not install font normalizer: " + t);
        }
    }

    /** Normalize every window that is already on screen (call on the EDT). */
    public static void normalizeOpenWindows() {
        try {
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w.isDisplayable()) {
                    normalizeFonts(w);
                    w.repaint();
                }
            }
        } catch (Throwable ignore) {
        }
    }

    /**
     * Re-stamp the chosen family onto every component under {@code root},
     * preserving each one's size and style. Monospaced components (receipts,
     * barcodes) are left untouched.
     */
    public static void normalizeFonts(Component root) {
        if (root == null) {
            return;
        }
        Font f = root.getFont();
        if (f != null && !f.getFamily().equals(family)
                && !isMono(f)) {
            root.setFont(new Font(family, f.getStyle(), f.getSize()));
        }
        if (root instanceof Container) {
            for (Component c : ((Container) root).getComponents()) {
                normalizeFonts(c);
            }
        }
    }

    private static boolean isMono(Font f) {
        String n = f.getName().toLowerCase();
        return n.contains("mono") || n.contains("consol") || n.contains("courier");
    }
}
