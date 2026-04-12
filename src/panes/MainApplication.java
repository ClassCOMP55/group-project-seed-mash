package panes;

import acm.graphics.GObject;
import acm.graphics.GWindow;
import acm.program.*;
import level.SaveData;
import sfx.AudioPlayer;
import sfx.Settings;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MainApplication extends GraphicsProgram {
	//sfx.Settings
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
	private boolean levelMusicPlaying = false;

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
			AudioPlayer.getInstance().playSound("Media/", "tian_mi_mi"); //Numbers are just for menu music ignore it
			menuMusicPlaying = true;
		}
	}
 

	public void stopMenuMusic() {
		if (menuMusicPlaying) {
			AudioPlayer.getInstance().stopSound();
			menuMusicPlaying = false;
		}
	}
	
	public void startLevelMusic(String songURL, long id) {
		if (!levelMusicPlaying) {
			AudioPlayer.getInstance().playSound("Media/", songURL);
			levelMusicPlaying = true;
		}
	}
	
	public void stopLevelMusic(String songURL) {
		if (levelMusicPlaying) {
			AudioPlayer.getInstance().stopSound();
			levelMusicPlaying = false;
		}
	}
	
	public long getMusicFrame(String songURL) {
		return AudioPlayer.getInstance().getFramePos("Media/", songURL);
	}
	
	public void setMusicFrame(long num) {
		AudioPlayer.getInstance().setFramePos(num);
	}
	
	public long getFullFrameLength(String songURL) {
		return AudioPlayer.getInstance().getFullFrameLength("Media/", songURL);
	}
	
	private static final String[] DEATH_SOUNDS = {
		    "deathSFX_ack",
		    "deathSFX_bong",
		    "deathSFX_fah",
		    "deathSFX_error",
		    "deathSFX_fart",
		    // add as many as you want
		};

	public void playDeathSound() {
	    int random = (int) (Math.random() * DEATH_SOUNDS.length);
	    AudioPlayer.getInstance().playSFX("Media/", DEATH_SOUNDS[random]);
	}
	
	private static final String[] WIN_SOUNDS = {
		    "winSFX_quack",
		    "winSFX_yipeee",
		};

	public void playWinSound() {
	    int random = (int) (Math.random() * WIN_SOUNDS.length);
	    AudioPlayer.getInstance().playSFX("Media/", WIN_SOUNDS[random]);
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
		
		SaveData.load(LevelSelectPane.levels);
		
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
		startLevelMusic(levelGameplayPane.getCurrentLevel().getSoundtrackURL(), 0);
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