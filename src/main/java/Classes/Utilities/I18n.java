package Classes.Utilities;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * Kiswahili interface. The screens are NetBeans forms with English text, so
 * rather than editing every form, each window is translated as it opens:
 * labels, buttons, menus, titles, borders, tabs, table headers and tooltips
 * whose English text is in {@code resources/i18n/sw.tsv} ("English TAB
 * Kiswahili", one per line).
 *
 * <p>Text that is not in the list stays in English, so a missing translation
 * never breaks a screen. Changing the language takes effect after a restart.
 *
 * @author Nkanabo
 */
public final class I18n {

    public static final String ENGLISH = "en";
    public static final String KISWAHILI = "sw";
    private static final Preferences PREFS = Preferences.userRoot().node("TiendaOptions");
    private static final String DONE = "tienda.i18n.done";
    /** Exact text (spaces collapsed) -> translation. */
    private static Map<String, String> dict;
    /** Same, keyed in lower case, for text whose capitals differ. */
    private static Map<String, String> dictLower;

    private I18n() {
    }

    public static String language() {
        return PREFS.get("language", ENGLISH);
    }

    public static void setLanguage(String lang) {
        PREFS.put("language", KISWAHILI.equals(lang) ? KISWAHILI : ENGLISH);
    }

    public static boolean isKiswahili() {
        return KISWAHILI.equals(language());
    }

    /** Translate a piece of English text (returned unchanged if unknown or in English mode). */
    public static String t(String english) {
        if (english == null || !isKiswahili()) {
            return english;
        }
        String trimmed = english.trim();
        String hit = lookup(trimmed);
        if (hit != null) {
            return keepPadding(english, hit);
        }
        // "Price:" / "Total: " -> translate the word, keep the punctuation.
        if (trimmed.endsWith(":")) {
            hit = lookup(trimmed.substring(0, trimmed.length() - 1));
            if (hit != null) {
                return keepPadding(english, hit + ":");
            }
        }
        // Simple HTML: translate the visible text if the whole of it is known.
        if (trimmed.regionMatches(true, 0, "<html>", 0, 6)) {
            String inner = trimmed.replaceAll("<[^>]+>", " ").trim();
            hit = lookup(inner);
            if (hit != null) {
                return "<html>" + hit + "</html>";
            }
        }
        return english;
    }

    /** Translate every window from now on (call once at startup). */
    public static void install() {
        if (!isKiswahili()) {
            return;
        }
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
            if (e.getID() == WindowEvent.WINDOW_OPENED || e.getID() == ComponentEvent.COMPONENT_SHOWN) {
                Object src = e.getSource();
                if (src instanceof Component) {
                    SwingUtilities.invokeLater(() -> translateTree((Component) src));
                }
            }
        }, AWTEvent.WINDOW_EVENT_MASK | AWTEvent.COMPONENT_EVENT_MASK);
    }

    /** Translate a component and everything inside it. */
    public static void translateTree(Component c) {
        if (c instanceof Frame) {
            Frame f = (Frame) c;
            f.setTitle(t(f.getTitle()));
        } else if (c instanceof Dialog) {
            Dialog d = (Dialog) c;
            d.setTitle(t(d.getTitle()));
        }
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            if (!Boolean.TRUE.equals(jc.getClientProperty(DONE))) {
                translateOne(jc);
                jc.putClientProperty(DONE, Boolean.TRUE);
            }
        }
        if (c instanceof JMenu) {
            for (Component item : ((JMenu) c).getMenuComponents()) {
                translateTree(item);
            }
        }
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                translateTree(child);
            }
        }
    }

    private static void translateOne(JComponent jc) {
        if (jc instanceof JLabel) {
            JLabel l = (JLabel) jc;
            l.setText(t(l.getText()));
        } else if (jc instanceof AbstractButton) {
            AbstractButton b = (AbstractButton) jc;
            b.setText(t(b.getText()));
        } else if (jc instanceof JTextField) {
            // Placeholder-style hints written into search fields.
            JTextField f = (JTextField) jc;
            String tr = t(f.getText());
            if (!tr.equals(f.getText()) && f.getText().length() > 0) {
                f.setText(tr);
            }
        } else if (jc instanceof JTabbedPane) {
            JTabbedPane tp = (JTabbedPane) jc;
            for (int i = 0; i < tp.getTabCount(); i++) {
                tp.setTitleAt(i, t(tp.getTitleAt(i)));
            }
        } else if (jc instanceof JTable) {
            JTableHeader h = ((JTable) jc).getTableHeader();
            if (h != null) {
                TableColumnModel cols = h.getColumnModel();
                for (int i = 0; i < cols.getColumnCount(); i++) {
                    Object v = cols.getColumn(i).getHeaderValue();
                    if (v instanceof String) {
                        cols.getColumn(i).setHeaderValue(t((String) v));
                    }
                }
                h.repaint();
            }
        }
        String tip = jc.getToolTipText();
        if (tip != null) {
            jc.setToolTipText(t(tip));
        }
        Border border = jc.getBorder();
        if (border instanceof TitledBorder) {
            TitledBorder tb = (TitledBorder) border;
            tb.setTitle(t(tb.getTitle()));
        }
    }

    private static String lookup(String text) {
        dictionary();
        String key = text.trim().replaceAll("\\s+", " ");
        String hit = dict.get(key);
        return hit != null ? hit : dictLower.get(key.toLowerCase());
    }

    private static synchronized Map<String, String> dictionary() {
        if (dict == null) {
            dict = new HashMap<>();
            dictLower = new HashMap<>();
            try (InputStream in = I18n.class.getClassLoader().getResourceAsStream("resources/i18n/sw.tsv")) {
                if (in != null) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                    String line;
                    while ((line = r.readLine()) != null) {
                        if (line.startsWith("#") || !line.contains("\t")) {
                            continue;
                        }
                        String[] kv = line.split("\t", 2);
                        String key = kv[0].trim().replaceAll("\\s+", " ");
                        dict.put(key, kv[1].trim());
                        dictLower.putIfAbsent(key.toLowerCase(), kv[1].trim());
                    }
                }
            } catch (Exception e) {
                Log.error(e);
            }
        }
        return dict;
    }

    private static String keepPadding(String original, String translated) {
        String lead = original.substring(0, original.indexOf(original.trim()));
        String tail = original.substring(lead.length() + original.trim().length());
        return lead + translated + tail;
    }
}
