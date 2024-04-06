/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Utilities;

/**
 *
 * @author Nkanabo
 */
import javax.sound.sampled.*;

public class SoundPlayer {

    public static void main(String[] args) {
        // Load the WAV file from the resources folder
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResourceAsStream("/resources/sounds/alert.wav"))) {
            // Get the audio format
            AudioFormat format = audioInputStream.getFormat();

            // Create a source data line for the audio format
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                // Open the line and start playing the sound
                line.open(format);
                line.start();

                // Read and play the audio data
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }

                // Block until all data is played
                line.drain();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
