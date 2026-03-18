import acm.graphics.*;
import java.util.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends GraphicsProgram{
	private int musicVol = 100;
	private int sfxVol = 100;
	ArrayList<GObject> settingsMenu = new ArrayList<GObject>();
	GImage closeButton;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	
	
	public int getMusicVol() {
		return musicVol;
	}
	public void setMusicVol(int musicVol) {
		this.musicVol = musicVol;
	}
	public int getSfxVol() {
		return sfxVol;
	}
	public void setSfxVol(int sfxVol) {
		this.sfxVol = sfxVol;
	}
	
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		requestFocus();
	}
	
	public void run() {
		requestFocus();
		openSettingsMenu();
		
		
	}
	
	
	public void openSettingsMenu() {
		
		GRect topBar = new GRect(150, 100, 500, 50);
		topBar.setFillColor(Color.green);
		topBar.setFilled(true);
		settingsMenu.add(topBar);
		
		GRect body = new GRect(200, 150, 400, 400);
		body.setFillColor(new Color(54, 212, 201));
		body.setFilled(true);
		settingsMenu.add(body);
		
		GLabel title = new GLabel("Settings", 330, 135);
		title.scale(3);
		settingsMenu.add(title);
		
		closeButton = new GImage("close.png", 605, 105);
		closeButton.scale(0.22);
		settingsMenu.add(closeButton);
		
		for (GObject x : settingsMenu) {
			add(x);
		}
	}
	
	public static void main(String[] args) {
		new StartScreen().start();
	}
}
