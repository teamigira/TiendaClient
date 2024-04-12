package Interface;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleFrame extends JFrame {

    private JTextArea textArea;

    public ConsoleFrame() {
        setTitle("Console Output");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane);

        // Redirect standard output and error streams
        redirectSystemStreams();

        setVisible(true);
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text));
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
        System.out.println("This is a test message.");
        System.err.println("This is an error message.");
    }
}
