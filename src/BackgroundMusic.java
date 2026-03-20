import javax.sound.sampled.*;
import java.io.File;

public class BackgroundMusic {
    public static void main(String[] args) {
        try {
            // Load the audio file
            File musicPath = new File("Media/sunflower-seed-wav.wav");
            
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                
                // Open and set looping
                clip.open(audioInput);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
                
                // Keep the program running to hear the music
                javax.swing.JOptionPane.showMessageDialog(null, "Music is playing! Press OK to stop.");
            } else {
                System.out.println("Could not find the music file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
