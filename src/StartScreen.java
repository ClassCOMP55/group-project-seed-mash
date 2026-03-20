import acm.graphics.*;
import java.util.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends GraphicsProgram{
	MainApplication mainApp;
	ArrayList<GObject> settingsMenu = new ArrayList<GObject>();
	GImage closeButton;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int ScaleWidth = 300;
	
	public StartScreen() {
		mainApp = new MainApplication();
	}
	
	public StartScreen(MainApplication mainApp) {
		this.mainApp = mainApp;
	}
	
	public double getMusicVol() {
		return mainApp.getMusicVol();
	}
	public void setMusicVol(int musicVol) {
		mainApp.setMusicVol(musicVol);
	}
	public double getSfxVol() {
		return mainApp.getSfxVol();
	}
	public void setSfxVol(int sfxVol) {
		mainApp.setSfxVol(sfxVol);
	}
	
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		requestFocus();
	}
	
	public void run() {
		requestFocus();
		addMouseListeners();
		openSettingsMenu();
	}
	
	
	public void openSettingsMenu() { //Use this to open settings
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
		
		GRect musicScale = new GRect(250, 250, ScaleWidth, 25);
		musicScale.setFillColor(new Color(19, 117, 203));
		musicScale.setFilled(true);
		settingsMenu.add(musicScale);
		
		GLabel musicLabel = new GLabel("Music Volume", 325, 225);
		musicLabel.scale(2);
		settingsMenu.add(musicLabel);
		
		GRect sfxScale = new GRect(250, 350, ScaleWidth, 25);
		sfxScale.setFillColor(new Color(19, 117, 203));
		sfxScale.setFilled(true);
		settingsMenu.add(sfxScale);
		
		GLabel sfxLabel = new GLabel("SFX Volume", 330, 325);
		sfxLabel.scale(2);
		settingsMenu.add(sfxLabel);
		
		GOval musicSet = new GOval((ScaleWidth/100)*getMusicVol(), 345, 35, 35);
		settingsMenu.add(musicSet);
		
		for (GObject x : settingsMenu) {
			add(x);
		}
	}
	
	public void closeSettingsMenu() { //Use this to close settings
		for (GObject x : settingsMenu) {
			remove(x);
		}
		settingsMenu.clear();
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) { //For close button
		if(e != null) {
			if (getElementAt(e.getX(), e.getY()) == closeButton) {
				closeSettingsMenu();
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		new StartScreen().start();
	}
}
