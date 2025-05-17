package Interface.Receipts;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.*;

public class MyApplication extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    
    public MyApplication() {
        setTitle("My Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create JPanels for each page
        JPanel page1 = new JPanel();
        JPanel page2 = new JPanel();
        JPanel page3 = new JPanel();
        
        // Add components to JPanels
        page1.add(new JLabel("Page 1"));
        page2.add(new JLabel("Page 2"));
        page3.add(new JLabel("Page 3"));
        
        // Initialize CardLayout and JPanel to hold cards
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        
        // Add JPanels to the card panel with unique names
        cards.add(page1, "Page 1");
        cards.add(page2, "Page 2");
        cards.add(page3, "Page 3");
        
        // Add card panel to frame
        getContentPane().add(cards);
        
        // Initialize and add navigation buttons
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> cardLayout.next(cards));
        getContentPane().add(nextButton, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null); // Center the frame
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyApplication().setVisible(true);
        });
    }
}
