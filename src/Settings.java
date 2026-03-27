import acm.graphics.*;
import java.util.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;

public class Settings extends GraphicsProgram{
	MainApplication mainApp;
	ArrayList<GObject> settingsMenu = new ArrayList<GObject>();
	GImage closeButton;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int ScaleWidth = 300;
	GOval musicSet;
	GOval sfxSet;
	GRect musicScale;
	GRect sfxScale;
	private GObject toDrag;
	private int lastX;
	
	public Settings() {
		mainApp = new MainApplication();
	}
	
	public Settings(MainApplication mainApp) {
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
		
	}
	
	public void run() {
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
		
		musicScale = new GRect(250, 250, ScaleWidth, 25);
		musicScale.setFillColor(new Color(19, 117, 203));
		musicScale.setFilled(true);
		settingsMenu.add(musicScale);
		
		GLabel musicLabel = new GLabel("Music Volume", 325, 225);
		musicLabel.scale(2);
		settingsMenu.add(musicLabel);
		
		sfxScale = new GRect(250, 350, ScaleWidth, 25);
		sfxScale.setFillColor(new Color(19, 117, 203));
		sfxScale.setFilled(true);
		settingsMenu.add(sfxScale);
		
		GLabel sfxLabel = new GLabel("SFX Volume", 330, 325);
		sfxLabel.scale(2);
		settingsMenu.add(sfxLabel);
		
		musicSet = new GOval(musicScale.getX() + (ScaleWidth/100)*getMusicVol() - 17.5, 245, 35, 35);
		musicSet.setFillColor(new Color(50, 159, 255));
		musicSet.setFilled(true);
		settingsMenu.add(musicSet);
		
		sfxSet = new GOval((sfxScale.getX() + (ScaleWidth/100)*getSfxVol() - 17.5), 345, 35, 35);
		sfxSet.setFillColor(new Color(50, 159, 255));
		sfxSet.setFilled(true);
		settingsMenu.add(sfxSet);
		
		for (GObject x : settingsMenu) {
			x.setLocation(x.getX()+1920/5, x.getY()+1080/10);
			mainApp.add(x);
		}
		
//		mainApp.addMouseListeners(this);
	}
	
	public void closeSettingsMenu() { //Use this to close settings
		for (GObject x : settingsMenu) {
			mainApp.remove(x);
		}
		settingsMenu.clear();
		mainApp.setSettingsOpen(false);
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) { //For close button
		GObject x = mainApp.getElementAtLocation(e.getX(), e.getY());
		if(x != null) {
			if (mainApp.getElementAtLocation(e.getX(), e.getY()) == closeButton) {
				closeSettingsMenu();
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		GObject temp = mainApp.getElementAtLocation(e.getX(), e.getY());
		if(temp != null) {
			if (temp == musicSet || temp == sfxSet) {
				int deltaX = e.getX() - lastX;
				double max = (musicScale.getX() + ScaleWidth - 17.5);
				double min = musicScale.getX() - 17.5;
				if (toDrag != null) {
					if (deltaX >= 0) {
						if (temp.getX() >= max) {
							toDrag.move(0, 0);
						} else {
							toDrag.move(deltaX, 0);
						}
					} else {
						if (temp.getX() <= min) {
							toDrag.move(0, 0);
						} else {
							toDrag.move(deltaX, 0);
						}
					}
					
				}
				lastX = e.getX();
				
			}
			
		} 
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		double setValue = (musicSet.getX() + 17.5 - 250) / 3;
		if (setValue > 100) { setValue = 100; }
		else if (setValue < 0) {setValue = 0; }
		mainApp.setMusicVol(setValue);
		
		setValue = (sfxSet.getX() + 17.5 - 250) / 3;
		if (setValue > 100) { setValue = 100; }
		else if (setValue < 0) {setValue = 0; }
		mainApp.setSfxVol(setValue);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		GObject x = mainApp.getElementAtLocation(e.getX(), e.getY());
		if (x != null) {
			if (x == musicSet || x == sfxSet) {
				toDrag = x;
			}
		}
		
		lastX = e.getX();
	}
}
