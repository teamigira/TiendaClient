import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Draws the Tienda POS app icon (a shopping bag with a "T") and writes
 * tienda.png (512 px, Linux/macOS) and tienda.ico (16-256 px, Windows).
 *
 *   java packaging/IconMaker.java packaging
 */
public class IconMaker {

    static BufferedImage draw(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        float s = size / 256f;

        // Rounded tile, teal gradient.
        g.setPaint(new GradientPaint(0, 0, new Color(0x14B8A6), 0, size, new Color(0x0F766E)));
        g.fill(new RoundRectangle2D.Float(8 * s, 8 * s, 240 * s, 240 * s, 56 * s, 56 * s));

        // Bag handle.
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(14 * s, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D handle = new Path2D.Float();
        handle.moveTo(96 * s, 100 * s);
        handle.lineTo(96 * s, 80 * s);
        handle.curveTo(96 * s, 46 * s, 160 * s, 46 * s, 160 * s, 80 * s);
        handle.lineTo(160 * s, 100 * s);
        g.draw(handle);

        // Bag body.
        Path2D bag = new Path2D.Float();
        bag.moveTo(60 * s, 96 * s);
        bag.lineTo(196 * s, 96 * s);
        bag.lineTo(206 * s, 210 * s);
        bag.lineTo(50 * s, 210 * s);
        bag.closePath();
        g.fill(bag);

        // "T" on the bag.
        g.setColor(new Color(0x0F766E));
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, Math.round(96 * s));
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        String t = "T";
        g.drawString(t, (size - fm.stringWidth(t)) / 2f, 156 * s + (fm.getAscent() - fm.getDescent()) / 2f);
        g.dispose();
        return img;
    }

    static byte[] png(BufferedImage img) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "png", out);
        return out.toByteArray();
    }

    /** ICO file with PNG-compressed entries (supported since Windows Vista). */
    static void writeIco(File file, int[] sizes) throws Exception {
        List<byte[]> images = new ArrayList<>();
        for (int sz : sizes) {
            images.add(png(draw(sz)));
        }
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            ByteBuffer h = ByteBuffer.allocate(6 + 16 * sizes.length).order(ByteOrder.LITTLE_ENDIAN);
            h.putShort((short) 0).putShort((short) 1).putShort((short) sizes.length);
            int offset = 6 + 16 * sizes.length;
            for (int i = 0; i < sizes.length; i++) {
                int sz = sizes[i];
                h.put((byte) (sz >= 256 ? 0 : sz)).put((byte) (sz >= 256 ? 0 : sz));
                h.put((byte) 0).put((byte) 0).putShort((short) 1).putShort((short) 32);
                h.putInt(images.get(i).length).putInt(offset);
                offset += images.get(i).length;
            }
            out.write(h.array());
            for (byte[] b : images) {
                out.write(b);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        File dir = new File(args.length > 0 ? args[0] : ".");
        ImageIO.write(draw(512), "png", new File(dir, "tienda.png"));
        writeIco(new File(dir, "tienda.ico"), new int[]{16, 24, 32, 48, 64, 128, 256});
        System.out.println("Wrote tienda.png and tienda.ico to " + dir.getAbsolutePath());
    }
}
