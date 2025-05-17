package Classes.Functions.Printers;


import ca.odell.glazedlists.impl.adt.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import java.awt.Color;
import java.awt.Graphics2D;
import Classes.Utilities.OS;
import static Classes.Utilities.OS.systemPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BarcodeGenerator {

    public static void generateBarcode(String barcodeText, int width, int height, String filePath) throws IOException, WriterException {
        // Create the barcode writer
        Code128Writer writer = new Code128Writer();
        // Encode the barcode
        BitMatrix bitMatrix = writer.encode(barcodeText, BarcodeFormat.CODE_128, width, height, null);
    
        // Convert the bit matrix to a buffered image
        BufferedImage image = toBufferedImage(bitMatrix);
    
        // Save the image directly to the file
        try {
            // Create the directory if it doesn't exist
            File directory = new File(filePath).getParentFile();
            directory.mkdirs(); // This will create the directory and any necessary parent directories
            // Save the image to the file
            ImageIO.write(image, "png", new File(filePath));
            System.out.println("Barcode image saved successfully at: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving barcode image: " + e.getMessage());
        }
    }
    

    private static BufferedImage toBufferedImage(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0 : 0xFFFFFFFF);
            }
        }
        return image;
    }

   
    public static void generateCode(String text) {
        String barcodeText = text;
        int width = 300;
        int height = 100;
        OS os = new OS();
        String systemPath = OS.getSystemPath();
        String fileName = text + ".png";
        String filePath = systemPath + "resources/usergenerated/barcodes/" + fileName;
        String CodeText = "" + barcodeText;
        // Display the system path based on the operating system
        // Create the folder if it doesn't exist
        //String filePath = BarcodeGenerator.class.getResource("/resources/barcode/" + fileName).getPath();

        try {
            generateBarcode(CodeText, width, height, filePath);
            System.out.println("Barcode image generated successfully.");
        } catch (IOException | WriterException e) {
            System.out.println("Error generating barcode image: " + e.getMessage());
        }
    }
}