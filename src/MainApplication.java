import acm.graphics.GObject;
import acm.graphics.GWindow;
import acm.program.*;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MainApplication extends GraphicsProgram {
	//Settings
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	private static final int TICK_INTERVAL_MS = 8; // ~125 FPS

	//List of all the full screen panes
	private StartPane startPane;
	private LevelSelectPane levelSelectPane;
	public LevelGameplayPane levelGameplayPane;
	private GraphicsPane currentScreen;
	private Settings settings;
	private boolean settingsOpen = false;

	//Sound Values
	double sfxVol = 100;
	double musicVol = 100;
	private boolean endGame = false;
	private long startMillis = 0;
	private Timer gameTimer;
	private boolean menuMusicPlaying = false;


	public void setStartMillis(long startMillis) {
		this.startMillis = startMillis;
	}
	public long getStartMillis() {
		return startMillis;
	}
	public long getDelta() {
		return System.currentTimeMillis() - startMillis;
	}

	public MainApplication() {
		super();
	}

	public double getSfxVol() {
		return sfxVol;
	}

	public void setSfxVol(double sfxVol) {
		this.sfxVol = sfxVol;
	}

	public double getMusicVol() {
		return musicVol;
	}

	public void setMusicVol(double musicVol) {
		this.musicVol = musicVol;
		AudioPlayer.getInstance().setVolume((float) musicVol / 100.0f);
	}
	

	public void startMenuMusic() {
		if (!menuMusicPlaying) {
			AudioPlayer.getInstance().playSound("Media/", "sunflower-seed-wav");
			menuMusicPlaying = true;
		}
	}
 

	public void stopMenuMusic() {
		if (menuMusicPlaying) {
			AudioPlayer.getInstance().stopSound("Media/", "sunflower-seed-wav");
			menuMusicPlaying = false;
		}
	}

	public void endGame() {
		endGame = true;
		if (gameTimer != null) {
			gameTimer.stop();
		}
	}

	public void quitGame() {
		endGame();
		clear();
	}

	public GWindow getWindow() {
		return gw;
	}
	
	public void setSettingsOpen(boolean open) {
		this.settingsOpen = open;
	}
	
	public void switchToSettings() {
		settingsOpen = true;
		settings.openSettingsMenu();
	}
	
	protected void setupInteractions() {
		requestFocus();
		addKeyListeners();
		addMouseListeners();
	}

	public void init() {
		this.gw.setTitle("Trigonometry Jump");
		try {
			this.gw.setIconImage(ImageIO.read(new File("Media/Character Sprite (1).png")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void run() {
		System.out.println("Let's Begin!");
		setupInteractions();

		//Initialize all Panes
		startPane = new StartPane(this);
		levelSelectPane = new LevelSelectPane(this);
		levelGameplayPane = new LevelGameplayPane(this);
		settings = new Settings(this);
		
		//TheDefaultPane
		
		SaveData.load(new level.GameLevel[]{
    	level.GameLevel.TEST_LEVEL,
    	level.GameLevel.TEST_LEVEL_2
});	
		
		switchToStartScreen();

		startMillis = System.currentTimeMillis();

		// Use a Swing Timer instead of a busy-wait loop
		gameTimer = new Timer(TICK_INTERVAL_MS, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (endGame) {
					gameTimer.stop();
					return;
				}
				if (currentScreen != null && currentScreen.equals(levelGameplayPane)) {
					levelGameplayPane.tick(getDelta());
				}
			}
		});
		gameTimer.start();
	}

	public static void main(String[] args) {
		new MainApplication().start();
		
	}
	

	public void switchToLevelSelectScreen() {
		switchToScreen(levelSelectPane);
		startMenuMusic();

	}
	
	public void switchToStartScreen() {
		switchToScreen(startPane);
		startMenuMusic();
	}



	public void switchToGameplayScreen() {
		stopMenuMusic();
		switchToScreen(levelGameplayPane);
	}

	protected void switchToScreen(GraphicsPane newScreen) {
		if(currentScreen != null) {
			currentScreen.hideContent();
		}
		newScreen.showContent();
		currentScreen = newScreen;
	}

	public GObject getElementAtLocation(double x, double y) {
		return getElementAt(x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (settingsOpen) {
			settings.mousePressed(e);
		} 
		else if (currentScreen != null) {
			currentScreen.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(settingsOpen) {
			settings.mouseReleased(e);
		}
		else if(currentScreen != null) {
			currentScreen.mouseReleased(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (settingsOpen) {
			settings.mouseClicked(e); 
		}
		else if (currentScreen != null) {
			currentScreen.mouseClicked(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (settingsOpen) { 
			settings.mouseDragged(e); 
		}
		else if(currentScreen != null) {
			currentScreen.mouseDragged(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(currentScreen != null) {
			currentScreen.mouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(currentScreen != null) {
			currentScreen.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(currentScreen != null) {
			currentScreen.keyReleased(e);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(currentScreen != null) {
			currentScreen.keyTyped(e);
		}
	}

}