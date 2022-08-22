/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 * to edit this template
 */
package Interface;

/**
 *
 * @author Nkanabo
 */
import java.awt.*;
import javax.swing.*;

public class SplashJava extends JWindow {
   Image splashScreen;
   ImageIcon imageIcon;
   public SplashJava() {
      splashScreen = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/images/R.jpg"));
      // Create ImageIcon from Image
      imageIcon = new ImageIcon(splashScreen);
      // Set JWindow size from image size
      setSize(imageIcon.getIconWidth(),imageIcon.getIconHeight());
      // Get current screen size
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      // Get x coordinate on screen for make JWindow locate at center
      int x = (screenSize.width-getSize().width)/2;
      // Get y coordinate on screen for make JWindow locate at center
      int y = (screenSize.height-getSize().height)/2;
      // Set new location for JWindow
      setLocation(x,y);
      // Make JWindow visible
      setVisible(true);
   }
   // Paint image onto JWindow
   @Override
   public void paint(Graphics g) {
      super.paint(g);
      g.drawImage(splashScreen, 0, 0, this);
   }
   public static void main(String[]args) {
      SplashJava splash = new SplashJava();
      try {
         // Make JWindow appear for 10 seconds before disappear
         Thread.sleep(10000);
         splash.dispose();
      } catch(InterruptedException e) {
      }
   }
}