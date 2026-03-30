import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;

public class AudioPlayer {

    private final ArrayList<Clip> players = new ArrayList<>();
    private static AudioPlayer somePlayer;

    private AudioPlayer() {}

    public static AudioPlayer getInstance() {
        if (somePlayer == null) {
            somePlayer = new AudioPlayer();
        }
        return somePlayer;
    }

    private Clip createMediaPlayer(String folder, String name) {
        try {
            File file = new File(folder + "/" + name + ".wav");
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            players.add(clip);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void playSound(String folder, String name) {
        Clip clip = createMediaPlayer(folder, name);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void stopSound(String folder, String name) {
        for (Clip clip : players) {
            clip.stop();
        }
    }

    public void setVolume(float volume) {
        for (Clip clip : players) {
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(Math.max(volume, 0.0001)) * 20);
            fc.setValue(dB);
        }
    }
}