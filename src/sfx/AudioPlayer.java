package sfx;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.concurrent.CountDownLatch;

public class AudioPlayer {
	
	private static AudioPlayer somePlayer;
	private MediaPlayer currentPlayer;
	private final java.util.HashMap<String, Media> mediaCache = new java.util.HashMap<>();
	private float currentVolume = 1.0f;
	private float sfxVolume = 1.0f;

	private AudioPlayer() {
		final CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await();
        } catch (Exception e) {
            // Already initialized, ignore
        }
	}
		
	public static AudioPlayer getInstance() {
		if (somePlayer == null) {
			somePlayer = new AudioPlayer();
		}
		return somePlayer;
	}
	
	public void preload(String folder, String name) {
	    Platform.runLater(() -> {
	        String path = new File(folder + name + ".mp3").toURI().toString();
	        mediaCache.put(folder + name, new Media(path));
	    });
	}
	
	public void playSound(String folder, String name) {
		Platform.runLater(() -> {  // JavaFX must run on its own thread
            stopSound();
            String path = new File(folder + name + ".mp3").toURI().toString();
            Media media = new Media(path);
            currentPlayer = new MediaPlayer(media);
            currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            currentPlayer.setVolume(currentVolume);
            currentPlayer.play();
        });
	}
	
	public void playSFX(String folder, String name) {
		float vol = sfxVolume;
	    Platform.runLater(() -> {
	        Media media = mediaCache.getOrDefault(folder + name, 
	            new Media(new File(folder + name + ".mp3").toURI().toString()));
	        MediaPlayer sfxPlayer = new MediaPlayer(media);
	        sfxPlayer.setVolume(vol);
	        sfxPlayer.setCycleCount(1);
	        sfxPlayer.play();
	        sfxPlayer.setOnEndOfMedia(sfxPlayer::dispose);
	    });
	}
	
	public void stopSound() {
       if (currentPlayer != null) {
           currentPlayer.stop();
       }
	}
    @SuppressWarnings("unused")
	public void pauseSound() {
       if (currentPlayer != null) {
           currentPlayer.pause();
       }
	}
	
	public void setVolume(float volume) {
		this.currentVolume = volume;
	    Platform.runLater(() -> {
	        if (currentPlayer != null) {
	            currentPlayer.setVolume(volume);
	        }
	    });
	}
	
	public void setSFXVolume(float volume) {
	    this.sfxVolume = volume;
	}
    @SuppressWarnings("unused")
	public long getFramePos(String folder, String name) {
   	if (currentPlayer != null) {
           return (long) currentPlayer.getCurrentTime().toMillis();
       }
       return 0;
   }
  
   // Helper method to find frame length
   @SuppressWarnings("unused")
   public long getFullFrameLength(String folder, String name) {
   	if (currentPlayer != null) {
           return (long) currentPlayer.getTotalDuration().toMillis();
       }
       return 0;
   }
  
   public void setFramePos(long num) {
   	if (currentPlayer != null) {
           currentPlayer.seek(Duration.millis(num / 1000d));
       }
   }
}