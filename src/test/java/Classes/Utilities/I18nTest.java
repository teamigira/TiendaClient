package Classes.Utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class I18nTest {

    @AfterEach
    void backToEnglish() {
        I18n.setLanguage(I18n.ENGLISH);
    }

    @Test
    void englishModeChangesNothing() {
        I18n.setLanguage(I18n.ENGLISH);
        assertEquals("Save", I18n.t("Save"));
    }

    @Test
    void translatesKnownTextKeepingPunctuation() {
        I18n.setLanguage(I18n.KISWAHILI);
        assertEquals("Hifadhi", I18n.t("Save"));
        assertEquals("Bei:", I18n.t("Price:"));
        assertEquals(" Jumla ", I18n.t(" Total "));
        assertEquals("Something new", I18n.t("Something new"));
    }

    @Test
    void translatesAWholeWindowOnce() {
        I18n.setLanguage(I18n.KISWAHILI);
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder("Customers"));
        JLabel l = new JLabel("Quantity");
        JButton b = new JButton("Cancel");
        p.add(l);
        p.add(b);
        I18n.translateTree(p);
        I18n.translateTree(p);
        assertEquals("Idadi", l.getText());
        assertEquals("Ghairi", b.getText());
        assertEquals("Wateja", ((TitledBorder) p.getBorder()).getTitle());
    }
}
