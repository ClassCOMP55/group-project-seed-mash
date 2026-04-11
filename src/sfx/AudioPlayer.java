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

	private AudioPlayer() {
		final CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(() -> latch.countDown());
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
	
	public void playSound(String folder, String name) {
		Platform.runLater(() -> {  // JavaFX must run on its own thread
            stopSound();
            String path = new File(folder + name + ".mp3").toURI().toString();
            Media media = new Media(path);
            currentPlayer = new MediaPlayer(media);
            currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            currentPlayer.play();
        });
	}
	
	public void playSFX(String folder, String name) {
	    Platform.runLater(() -> {
	        String path = new File(folder + name + ".mp3").toURI().toString();
	        Media media = new Media(path);
	        MediaPlayer sfxPlayer = new MediaPlayer(media);
	        sfxPlayer.setCycleCount(1); // play once
	        sfxPlayer.play();
	        // auto cleanup when done
	        sfxPlayer.setOnEndOfMedia(() -> sfxPlayer.dispose());
	    });
	}
	
	public void stopSound() {
       if (currentPlayer != null) {
           currentPlayer.stop();
       }
	}
  
	public void pauseSound() {
       if (currentPlayer != null) {
           currentPlayer.pause();
       }
	}
	
	public void setVolume(float volume) {
   	if (currentPlayer != null) {
           currentPlayer.setVolume(volume); // 0.0 to 1.0
       }
	}
  
	public long getFramePos(String folder, String name) {
   	if (currentPlayer != null) {
           return (long) currentPlayer.getCurrentTime().toMillis();
       }
       return 0;
   }
  
   // Helper method to find frame length
   public long getFullFrameLength(String folder, String name) {
   	if (currentPlayer != null) {
           return (long) currentPlayer.getTotalDuration().toMillis();
       }
       return 0;
   }
  
   public void setFramePos(long num) {
   	if (currentPlayer != null) {
           currentPlayer.seek(Duration.millis(num / 1000));
       }
   }
}