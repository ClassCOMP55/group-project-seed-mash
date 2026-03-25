import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import acm.graphics.*;
import level.GameLevel;

public class LevelSelectPane extends GraphicsPane {

    private final GameLevel[] levels = {GameLevel.TEST_LEVEL, GameLevel.TEST_LEVEL_2};
	private GImage backButton;
	private GImage leftArrow;
	private GImage rightArrow;
	private GRect playButton;
	private GLabel playButtonText;

    private int currentSelection = 0;

	public LevelSelectPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}

	@Override
	public void showContent() {
		addBackground();
		addTitle();
		addBackButton();
		addLeftArrow();
		addRightArrow();
		addProgressBar();

        addPlayButton();
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
    private void drawLevelInfo() {
        GameLevel level = levels[currentSelection];
        GRect levelInfoBox = new GRect(300, 150, mainScreen.getWidth() - 600, 500);
        levelInfoBox.setFilled(true);
        levelInfoBox.setColor(new Color(46, 204, 113));
        contents.add(levelInfoBox);
        mainScreen.add(levelInfoBox);

        GLabel levelInfoName = new GLabel(level.getLevelName(), 300, 150);
        levelInfoName.setFont(new Font("Arial", Font.BOLD, 36));
        levelInfoName.setColor(Color.WHITE);
        levelInfoName.setLocation((mainScreen.getWidth() - levelInfoName.getWidth()) / 2, 210);
        contents.add(levelInfoName);
        mainScreen.add(levelInfoName);
        
        int levelDifficulty = level.getDifficulty();
        GImage levelDiffIcon = new GImage("Difficulty1.png", levelInfoName.getX() - 60, levelInfoName.getY() - 40);
        if (levelDifficulty == 2) {
        	levelDiffIcon.setImage("Difficulty2.png");
        } else if (levelDifficulty == 3) {
        	levelDiffIcon.setImage("Difficulty3.png");
        } else if (levelDifficulty == 4) {
        	levelDiffIcon.setImage("Difficulty4.png");
        }
        levelDiffIcon.scale(0.1);
        contents.add(levelDiffIcon);
        mainScreen.add(levelDiffIcon);
    }

	private void addBackButton() {
		backButton = new GImage("close.png");
		backButton.scale(0.5, 0.5);
		backButton.setLocation(50, 50);
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
	
	
	private void addProgressBar() {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());

		if (clicked == backButton) {
			System.out.println("Back button clicked!");
			mainScreen.switchToStartScreen();
		} else if (clicked == leftArrow) {
            incrementSelection(-1);
            drawLevelInfo();
//            System.out.println("current selection " + currentSelection);
		} else if (clicked == rightArrow) {
            incrementSelection(1);
            drawLevelInfo();
//            System.out.println("current selection " + currentSelection);
		} else if (clicked == playButton || clicked == playButtonText) {
			System.out.println("Play button clicked!");
            startLevel(levels[currentSelection]);
		}
	}
    private void incrementSelection(int amt) {
        currentSelection = Math.floorMod(currentSelection + amt, levels.length);
    }
    private void startLevel(GameLevel level) {
        mainScreen.levelGameplayPane.setCurrentLevel(level);
        mainScreen.levelGameplayPane.startGame();
        mainScreen.switchToGameplayScreen();
    }
}