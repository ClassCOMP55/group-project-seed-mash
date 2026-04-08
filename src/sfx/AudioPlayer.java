package sfx;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;

public class AudioPlayer {

    private final ArrayList<Clip> players = new ArrayList<>();
    private final ArrayList<Long> songLengths = new ArrayList<>();
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
            songLengths.add(clip.getMicrosecondLength());
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void playSound(String folder, String name, long id) {
        for (int i = 0; i < songLengths.size(); i++) {
        	if (songLengths.get(i) == id) {
        		players.get(i).loop(Clip.LOOP_CONTINUOUSLY);
        		players.get(i).start();
        		return;
        	}
        }
        
        //In case it's not there
        Clip clip = createMediaPlayer(folder, name);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
    }

    public void stopSound(String folder, String name) {
        for (Clip player : players) {
            player.stop();
        }
    }

    public void setVolume(float volume) {
        for (Clip clip : players) {
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(Math.max(volume, 0.0001)) * 20);
            fc.setValue(dB);
        }
    }
    
    public long getFramePos(String folder, String name) {
    	for (Clip clip : players) {
    		if (clip.getMicrosecondPosition() != 0) {
    			//System.out.println(clip.getFramePosition());
    			return clip.getMicrosecondPosition();
    		}
    	}
    	return 0;
    }
    
    // Helper method to find frame length
    public long getFullFrameLength(String folder, String name) {
    	Clip clip = createMediaPlayer(folder, name);
    	return clip.getMicrosecondLength();
    }
    
    public void setFramePos(long num) {
    	for (Clip clip : players) {
    		clip.setMicrosecondPosition(num);
    	}
    }
}