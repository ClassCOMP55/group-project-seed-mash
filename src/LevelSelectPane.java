import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import acm.graphics.*;

public class LevelSelectPane extends GraphicsPane{

	
	private static class LevelData {
		String name;
		int difficulty; 
		int stars;
		boolean completed;
		Color color;
		
		LevelData(String name, int difficulty, int stars, Color color) {
			this.name = name;
			this.difficulty = difficulty;
			this.stars = stars;
			this.completed = false;
			this.color = color;
		}
	}
	
	private LevelData[] levels;
	
	public LevelSelectPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	@Override
	public void showContent() {
		addBackground();
		addTitle();
	}

	@Override
	public void hideContent() {
		for(GObject item : contents) {
			mainScreen.remove(item);
		}
		contents.clear();
	}
	
	private void addBackground() {
		GRect bg = new GRect(0, 0, mainScreen.getWidth(), mainScreen.getHeight());
		bg.setFilled(true);
		bg.setColor(new Color(0, 102, 204)); // Geometry Dash blue
		contents.add(bg);
		mainScreen.add(bg);
	}
	
	private void addTitle() {
		GLabel title = new GLabel("SELECT LEVEL");
		title.setFont(new Font("Arial", Font.BOLD, 36));
		title.setColor(Color.WHITE);
		title.setLocation((mainScreen.getWidth() - title.getWidth()) / 2, 60);
		
		contents.add(title);
		mainScreen.add(title);
	}
	

}