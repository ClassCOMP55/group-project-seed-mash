import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import acm.graphics.*;
import level.GameLevel;
import acm.util.MediaTools;


public class LevelSelectPane extends GraphicsPane {

    private final GameLevel[] levels = {GameLevel.TEST_LEVEL, GameLevel.TEST_LEVEL_2};
	private GImage backButton;
	private GImage leftArrow;
	private GImage rightArrow;
	private GRect playButton;
	private GLabel playButtonText;
    private AudioPlayer backgroundMusic;

	

    private int currentSelection = 0;

	public LevelSelectPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	private void playMusic() {
        backgroundMusic = AudioPlayer.getInstance();
        backgroundMusic.playSound("Media/", "sunflower-seed-wav");
    }

    private void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stopSound("Media/", "sunflower-seed-wav");
        }
    }

	@Override
	public void showContent() {
		addBackground();
		addTitle();
		drawLevelInfo();
		addBackButton();
		addLeftArrow();
		addRightArrow();
		addProgressBar();
		playMusic();
	}

	@Override
	public void hideContent() {
		stopMusic();
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

    private void drawLevelInfo() {
        GameLevel level = levels[currentSelection];
        playButton = new GRect(300, 150, mainScreen.getWidth() - 600, 300); //954 wide
        playButton.setFilled(true);
        playButton.setColor(new Color(46, 204, 113));
        contents.add(playButton);
        mainScreen.add(playButton);

        playButtonText = new GLabel(level.getLevelName(), 0, 0);
        playButtonText.setFont(new Font("Arial", Font.BOLD, 36));
        playButtonText.setColor(Color.WHITE);
        playButtonText.scale(2);
        playButtonText.setLocation((playButton.getWidth()/2) - (playButtonText.getWidth() / 2) + playButton.getX(),
        		(playButton.getHeight()/2) - (playButtonText.getHeight()/2) + playButton.getY() + 50);
        contents.add(playButtonText);
        mainScreen.add(playButtonText);
        
        int levelDifficulty = level.getDifficulty();
        GImage levelDiffIcon = new GImage("Difficulty1.png", playButtonText.getX() - playButton.getX(), playButtonText.getY() - 80);
        if (levelDifficulty == 2) {
        	levelDiffIcon.setImage("Difficulty2.png");
        } else if (levelDifficulty == 3) {
        	levelDiffIcon.setImage("Difficulty3.png");
        } else if (levelDifficulty == 4) {
        	levelDiffIcon.setImage("Difficulty4.png");
        }
        levelDiffIcon.scale(0.2);
        levelDiffIcon.setLocation(playButtonText.getX() - playButton.getX() / 2, playButtonText.getY() - 80);
        contents.add(levelDiffIcon);
        mainScreen.add(levelDiffIcon);
        
        int timeMin = level.getRuntime() / 60;
        int timeSec = level.getRuntime() % 60;
        GLabel runTimeLabel;
        if (timeSec < 10) { //to create time format if below 10 seconds
        	runTimeLabel = new GLabel(timeMin + ":0" + timeSec, 300, 150);
        } else {
        	runTimeLabel = new GLabel(timeMin + ":" + timeSec, 300, 150);
        }
        runTimeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        runTimeLabel.setColor(Color.WHITE);
        runTimeLabel.setLocation((mainScreen.getWidth() - playButtonText.getHeight()) / 2, playButtonText.getY() + 75);
        contents.add(runTimeLabel);
        mainScreen.add(runTimeLabel);
        
        GRect progressLabel = new GRect(playButton.getX() + 50, runTimeLabel.getY() + 10, 500, 50);
        contents.add(progressLabel);
        mainScreen.add(progressLabel);
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
		leftArrow.setLocation(50, 400);
		contents.add(leftArrow);
		mainScreen.add(leftArrow);
	}

	private void addRightArrow() {
		rightArrow = new GImage("Right_arrow.png");
		rightArrow.scale(0.3, 0.3);
		rightArrow.setLocation(mainScreen.getWidth() - rightArrow.getWidth() - 50, 400);
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
            System.out.println("current selection " + currentSelection);
            incrementSelection(-1);
            drawLevelInfo();
		} else if (clicked == rightArrow) {
            System.out.println("current selection " + currentSelection);
            incrementSelection(1);
            drawLevelInfo();
		} else if (clicked == playButton || clicked == playButtonText) {
			System.out.println("Play button clicked!");
            mainScreen.levelGameplayPane.setCurrentLevel(levels[currentSelection]);
			mainScreen.switchToGameplayScreen();
		}
	}

    private void incrementSelection(int amt) {
        currentSelection = Math.floorMod(currentSelection + amt, levels.length);
    }
}