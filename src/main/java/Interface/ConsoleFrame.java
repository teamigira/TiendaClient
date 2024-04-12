package Interface;


import javax.swing.*;

import Classes.Utilities.Resources;

import java.awt.*;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class ConsoleFrame extends JFrame {

    private JTextArea textArea;

    public ConsoleFrame() {
        setTitle("System Setup");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Set the app icon
        try {
            String url = "resources/images/icons8.jpg";
            Resources rs = new Resources();
            File is = rs.getFileFromResource(url);
            String filepath = Paths.get(is.toURI()).toFile().getAbsolutePath();
            ImageIcon icon = new ImageIcon(filepath);
            setIconImage(icon.getImage());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane);

        // Redirect standard output and error streams
        redirectSystemStreams();

        setVisible(true);
    }

    private void updateTextArea(final String text) {
        for (int i = 0; i < text.length(); i++) {
            final int index = i;
            try {
                Thread.sleep(5); // Adjust the delay time as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> textArea.append(String.valueOf(text.charAt(index))));
        }
    }

    private void redirectSystemStreams() {
        OutputStream outStream = new OutputStream() {
            @Override
            public void write(int b) {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) {
                write(b, 0, b.length);
            }
        };

        PrintStream outPrintStream = new PrintStream(outStream, true);

        System.setOut(outPrintStream);
        System.setErr(outPrintStream);
    }

    public static void main(String[] args) {
        new ConsoleFrame();
        // Example console messages
        // Simulate some console output
    System.out.println("System setup started...");
    System.out.println("Loading configuration...");
    System.out.println("Connecting to database...");
    System.out.println("Database connection successful.");
    System.out.println("Initializing components...");
    }
}
