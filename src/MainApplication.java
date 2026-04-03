import acm.graphics.GObject;
import acm.graphics.GWindow;
import acm.program.*;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MainApplication extends GraphicsProgram {
	//Settings
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;

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
    private long prevMillis = 0;

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

	public void quitGame() {
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
//		this.gw.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void run() {
		System.out.println("Lets' Begin!");
		setupInteractions();

		//Initialize all Panes
		startPane = new StartPane(this);
		levelSelectPane = new LevelSelectPane(this);
		levelGameplayPane = new LevelGameplayPane(this);
		settings = new Settings(this);
		
		//TheDefaultPane
		switchToScreen(startPane);

        startMillis = System.currentTimeMillis();
        do {
            System.out.print(" \b"); //<-- this line makes the moving level work for some reason
            if (getDelta() != prevMillis && getDelta() % 8 == 0) { //limit frames to 125 per second
                if (currentScreen.equals(levelGameplayPane)) {
                    levelGameplayPane.tick(getDelta());
                }
                prevMillis = getDelta();
            }
        } while (!endGame);
	}

	public static void main(String[] args) {
		new MainApplication().start();
	}

	public void switchToLevelSelectScreen() {
		switchToScreen(levelSelectPane);
	}

	public void switchToStartScreen() {
		switchToScreen(startPane);
	}

	public void switchToGameplayScreen() {
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