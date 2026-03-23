import java.awt.*;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GObject;
import acm.graphics.*;


public class StartPane extends GraphicsPane{
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
		GImage moreButton = new GImage("more.jpeg", 200, 400);
		moreButton.scale(0.3, 0.3);
		moreButton.setLocation((mainScreen.getWidth() - moreButton.getWidth())/ 2, 400);
		
		contents.add(moreButton);
		mainScreen.add(moreButton);

	}
	
	
	private void addSettingButton() {
		GImage Setting = new GImage("2747966-200__1_-removebg-preview.png",200,400);
		Setting.scale(0.5, 0.5);
		Setting.setLocation((mainScreen.getWidth() - Setting.getWidth()) - 50, 50);
		
		contents.add(Setting);
		mainScreen.add(Setting);
	}

	

	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (mainScreen.getElementAtLocation(e.getX(), e.getY()) == contents.get(1)) {
			mainScreen.switchToLevelSelectScreen();
		}else{
			mainScreen.switchToSettings();
		}
	}

}
