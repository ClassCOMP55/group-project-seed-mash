import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import acm.graphics.*;

public class LevelSelectPane extends GraphicsPane {

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
	private GImage backButton;
	private GImage leftArrow;
	private GImage rightArrow;
	private GRect playButton;
	private GLabel playButtonText;

	public LevelSelectPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}

	@Override
	public void showContent() {
		addBackground();
		addTitle();
		addPlayButton();
		addBackButton();
		addLeftArrow();
		addRightArrow();
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
		bg.setColor(new Color(0, 102, 204));
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

	private void addPlayButton() {
		playButton = new GRect(200, 100);
		playButton.setFilled(true);
		playButton.setColor(new Color(46, 204, 113));
		playButton.setLocation((mainScreen.getWidth() - 200) / 2, 250);
		
		contents.add(playButton);
		mainScreen.add(playButton);
		
		playButtonText = new GLabel("PLAY");
		playButtonText.setFont(new Font("Arial", Font.BOLD, 32));
		playButtonText.setColor(Color.WHITE);
		playButtonText.setLocation((mainScreen.getWidth() - playButtonText.getWidth()) / 2, 315);
		
		contents.add(playButtonText);
		mainScreen.add(playButtonText);
	}

	private void addBackButton() {
		backButton = new GImage("back.jpg");
		backButton.scale(0.3, 0.3);
		backButton.setLocation((mainScreen.getWidth() - backButton.getWidth())/ 2, 500);
		contents.add(backButton);
		mainScreen.add(backButton);
	}

	private void addLeftArrow() {
		leftArrow = new GImage("Left_arrow.png");
		leftArrow.scale(0.3, 0.3);
		leftArrow.setLocation(50, 500);
		contents.add(leftArrow);
		mainScreen.add(leftArrow);
	}

	private void addRightArrow() {
		rightArrow = new GImage("Right_arrow.png");
		rightArrow.scale(0.3, 0.3);
		rightArrow.setLocation(mainScreen.getWidth() - rightArrow.getWidth() - 50, 500);
		contents.add(rightArrow);
		mainScreen.add(rightArrow);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());

		if (clicked == backButton) {
			System.out.println("Back button clicked!");
			mainScreen.switchToStartScreen();
		} else if (clicked == leftArrow) {
			System.out.println("Left arrow clicked!");
		} else if (clicked == rightArrow) {
			System.out.println("Right arrow clicked!");
		} else if (clicked == playButton || clicked == playButtonText) {
			System.out.println("Play button clicked!");
			mainScreen.switchToGameplayScreen();
		}
	}
}