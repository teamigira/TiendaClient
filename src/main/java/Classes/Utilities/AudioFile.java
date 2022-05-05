/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Utilities;

/**
 *
 * @author Nkanabo
 */

import static Classes.Utilities.OS.base_Url;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
 
/**
 * This is an example program that demonstrates how to play back an audio file
 * using the Clip in Java Sound API.
 * @author www.codejava.net
 *
 */
public class AudioFile implements LineListener {
     
    /**
     * this flag indicates whether the playback completes or not.
     */
    boolean playCompleted;
     
    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
     */
    void play(AudioInputStream audioFilePath) {
    //File audioFile = new File(audioFilePath);
 
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFilePath);
 
            AudioFormat format = audioStream.getFormat();
 
            DataLine.Info info = new DataLine.Info(Clip.class, format);
 
            Clip audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.addLineListener(this);
 
            audioClip.open(audioStream);
             
            audioClip.start();
             
            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
             
            audioClip.close();
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
         
    }
     
    /**
     * Listens to the START and STOP events of the audio line.
     */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback completed.");
        }
 
    }
 
    public void Playme(String call) throws UnsupportedAudioFileException, IOException {
        
        AudioFile player = new AudioFile();
        switch(call){
         case "info":
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("MP3/new-file.wav");
        AudioInputStream sound = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
        player.play((AudioInputStream) is);
         break;
         
         case "alert":
             
        ClassLoader classloaders = Thread.currentThread().getContextClassLoader();
        InputStream iss = classloaders.getResourceAsStream("MP3/duplicates.wav");
        AudioInputStream sounds = AudioSystem.getAudioInputStream(new BufferedInputStream(iss));
        player.play((AudioInputStream) iss);
         break;
         case "danger":
        ClassLoader classloader2 = Thread.currentThread().getContextClassLoader();
        InputStream is2 = classloader2.getResourceAsStream("MP3/duplicates.wav");
        AudioInputStream sound2 = AudioSystem.getAudioInputStream(new BufferedInputStream(is2));
        player.play((AudioInputStream) is2);
        
         }                
    }
 
}