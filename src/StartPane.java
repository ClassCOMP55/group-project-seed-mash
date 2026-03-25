import java.awt.*;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GObject;
import acm.graphics.*;


public class StartPane extends GraphicsPane{
	
	private MainApplication mainScreen;
	private GImage settingButton;
	private GImage descriptionButton;
	
	public StartPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	

	@Override
	public void showContent() {
		addBackground();
		addText();
		addDescriptionButton();
		addSettingButton();
	}

	@Override
	public void hideContent() {
		for(GObject item : contents) {
			mainScreen.remove(item);
		}
		contents.clear();
	}
	
	private void addBackground() {
		GRect bg = new GRect(0,0, mainScreen.getWidth(), mainScreen.getHeight());
		bg.setFilled(true);
		bg.setColor(new Color(0, 102, 204));
		contents.add(bg);
		mainScreen.add(bg);	
	}
	
	private void addText() {
		GLabel Text = new GLabel("TRIGONOMETRY JUMP");
		Text.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
		Text.setColor(Color.BLACK);
		Text.setLocation((mainScreen.getWidth() - Text.getWidth()) / 2, 320);
		contents.add(Text);
		mainScreen.add(Text);
	}
	

	
	private void addDescriptionButton() {
		descriptionButton = new GImage("more.jpeg", 200, 400);
		descriptionButton.scale(0.3, 0.3);
		descriptionButton.setLocation((mainScreen.getWidth() - descriptionButton.getWidth())/ 2, 400);
		
		contents.add(descriptionButton);
		mainScreen.add(descriptionButton);

	}
	
	
	private void addSettingButton() {
		settingButton = new GImage("2747966-200__1_-removebg-preview.png",200,400);
		settingButton.scale(0.5, 0.5);
		settingButton.setLocation((mainScreen.getWidth() - settingButton.getWidth()) - 50, 50);
		
		contents.add(settingButton);
		mainScreen.add(settingButton);
	}

	

	
	@Override
	public void mouseClicked(MouseEvent e) {
		GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());
		
		if (clicked == descriptionButton) {
			mainScreen.switchToLevelSelectScreen();
		} else if (clicked == settingButton) {
			mainScreen.switchToSettings();
		}
	}
}
