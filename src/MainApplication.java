import acm.graphics.GObject;
import acm.program.*;


import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MainApplication extends GraphicsProgram {
	//Settings
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	
	//List of all the full screen panes
	private StartPane startPane;
	private LevelSelectPane levelSelectPane;
    public LevelGameplayPane levelGameplayPane;
	private GraphicsPane currentScreen;
	
	//Sound Values
	int sfxVol = 100;
	int musicVol = 100;
		

	public MainApplication() {
		super();
	}
	
	public int getSfxVol() {
		return sfxVol;
	}

	public void setSfxVol(int sfxVol) {
		this.sfxVol = sfxVol;
	}

	public int getMusicVol() {
		return musicVol;
	}

	public void setMusicVol(int musicVol) {
		this.musicVol = musicVol;
	}
	
	public void quitGame() {
		clear();
	}

	protected void setupInteractions() {
		requestFocus();
		addKeyListeners();
		addMouseListeners();
	}
	
	public void init() {
        this.gw.setTitle("Trigonometry Jump");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	public void run() {

		System.out.println("Lets' Begin!");
		setupInteractions();
		
		//Initialize all Panes
		startPane = new StartPane(this);
		levelSelectPane = new LevelSelectPane(this);
        levelGameplayPane = new LevelGameplayPane(this);
		//TheDefaultPane
		switchToScreen(startPane);
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
		if(currentScreen != null) {
			currentScreen.mousePressed(e);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(currentScreen != null) {
			currentScreen.mouseReleased(e);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(currentScreen != null) {
			currentScreen.mouseClicked(e);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(currentScreen != null) {
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
