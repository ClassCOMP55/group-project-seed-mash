package panes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.*;
import level.GameLevel;

public class LevelSelectPane extends GraphicsPane {

    public static final GameLevel[] levels = {GameLevel.TEST_LEVEL, GameLevel.TEST_LEVEL_2};
    private GImage backButton;
    private GImage leftArrow;
    private GImage rightArrow;
    private GRect playButton;
    private GLabel playButtonText;

    // Promoted to fields so they can be removed on redraw
    private GImage levelDiffIcon;
    private GLabel runTimeLabel;
    private GRect progressLabel;
    private GRect progress;

    // Track all level-info elements for easy cleanup
    private ArrayList<GObject> levelInfoElements = new ArrayList<>();

    private int currentSelection = 0; // Start at 0 (first valid index)

    public LevelSelectPane(MainApplication mainScreen) {
        this.mainScreen = mainScreen;
    }


    @Override
    public void showContent() {
        addBackground();
        addTitle();
        drawLevelInfo();
        addBackButton();
        addLeftArrow();
        addRightArrow();
    }

    @Override
    public void hideContent() {
        for (GObject item : contents) {
            mainScreen.remove(item);
        }
        contents.clear();
        levelInfoElements.clear();
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
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        title.setColor(Color.WHITE);
        title.setLocation((mainScreen.getWidth() - title.getWidth()) / 2, 60);
        contents.add(title);
        mainScreen.add(title);
    }

    /**
     * Removes all previously drawn level-info elements from the screen and tracking lists.
     */
    private void clearLevelInfo() {
        for (GObject item : levelInfoElements) {
            mainScreen.remove(item);
            contents.remove(item);
        }
        levelInfoElements.clear();
    }

    /**
     * Helper to add an element to the screen and track it for cleanup.
     */
    private void addLevelInfoElement(GObject obj) {
        levelInfoElements.add(obj);
        contents.add(obj);
        mainScreen.add(obj);
    }

    private void drawLevelInfo() {
        // Clear old level info before drawing new info
        clearLevelInfo();

        GameLevel level = levels[currentSelection];

        // Play button
        playButton = new GRect(300, 150, mainScreen.getWidth() - 600, 300);
        playButton.setFilled(true);
        playButton.setColor(new Color(46, 204, 113));
        addLevelInfoElement(playButton);

        // Level name text
        playButtonText = new GLabel(level.getLevelName(), 0, 0);
        playButtonText.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        playButtonText.setColor(Color.WHITE);
        playButtonText.scale(2);
        playButtonText.setLocation(
                (playButton.getWidth() / 2) - (playButtonText.getWidth() / 2) + playButton.getX(),
                (playButton.getHeight() / 2) - (playButtonText.getHeight() / 2) + playButton.getY() + 50
        );
        addLevelInfoElement(playButtonText);

        // Difficulty icon
        int levelDifficulty = level.getDifficulty();
        String diffImage = "Difficulty1.png";
        if (levelDifficulty == 2) {
            diffImage = "Difficulty2.png";
        } else if (levelDifficulty == 3) {
            diffImage = "Difficulty3.png";
        } else if (levelDifficulty == 4) {
            diffImage = "Difficulty4.png";
        }
        levelDiffIcon = new GImage(diffImage);
        levelDiffIcon.scale(0.2);
        levelDiffIcon.setLocation(playButtonText.getX() - playButton.getX() / 2, playButtonText.getY() - 80);
        addLevelInfoElement(levelDiffIcon);

        // Runtime label
        int timeMin = level.getRuntime() / 60;
        int timeSec = level.getRuntime() % 60;
        String timeStr = timeMin + ":" + (timeSec < 10 ? "0" + timeSec : "" + timeSec);
        runTimeLabel = new GLabel(timeStr, 300, 150);
        runTimeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        runTimeLabel.setColor(Color.WHITE);
        runTimeLabel.setLocation((mainScreen.getWidth() - runTimeLabel.getWidth()) / 2, playButtonText.getY() + 70);
        addLevelInfoElement(runTimeLabel);

        // --- Progress Bar Section ---
        double barWidth = 1335;
        double barHeight = 50;
        double barX = (mainScreen.getWidth() - barWidth) / 2;
        double barY = runTimeLabel.getY() + 35;

        // "PROGRESS" label above the bar
        GLabel progressTitle = new GLabel("PROGRESS");
        progressTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        progressTitle.setColor(new Color(200, 230, 255));
        progressTitle.setLocation(barX, barY - 8);
        addLevelInfoElement(progressTitle);

        // Percentage label to the right of the title
        int pct = (int) (level.getCompletionPercent());
        GLabel pctLabel = new GLabel(pct + "%");
        pctLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        pctLabel.setColor(Color.WHITE);
        pctLabel.setLocation(barX + barWidth - pctLabel.getWidth(), barY - 8);
        addLevelInfoElement(pctLabel);

        // Bar background (dark rounded look)
        GRect barBg = new GRect(barX, barY, barWidth, barHeight);
        barBg.setFilled(true);
        barBg.setFillColor(new Color(0, 50, 120));
        barBg.setColor(new Color(255, 255, 255, 80));
        addLevelInfoElement(barBg);

        // Bar fill (bright green to match play button, scaled to completion)
        double fillWidth = Math.max(0, (level.getCompletionPercent() / 100.0) * barWidth);
        if (fillWidth > 0) {
            progress = new GRect(barX, barY, fillWidth, barHeight);
            progress.setFilled(true);
            progress.setFillColor(new Color(46, 204, 113));
            progress.setColor(new Color(46, 204, 113));
            addLevelInfoElement(progress);
        }

        // Bar border on top for crisp outline
        progressLabel = new GRect(barX, barY, barWidth, barHeight);
        progressLabel.setFilled(false);
        progressLabel.setColor(Color.WHITE);
        addLevelInfoElement(progressLabel);

        // Completed banner if 100%
        if (level.getCompletionPercent() >= 100) {
            GLabel completedLabel = new GLabel("\u2714 COMPLETED");
            completedLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
            completedLabel.setColor(new Color(46, 255, 140));
            completedLabel.setLocation((mainScreen.getWidth() - completedLabel.getWidth()) / 2, barY + barHeight + 25);
            addLevelInfoElement(completedLabel);
        }
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

    @Override
    public void mouseClicked(MouseEvent e) {
        GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());
        if (clicked == backButton) {
            System.out.println("Back button clicked!");
            mainScreen.switchToStartScreen();
        } else if (clicked == leftArrow) {
            incrementSelection(-1);
            drawLevelInfo();
        } else if (clicked == rightArrow) {
            incrementSelection(1);
            drawLevelInfo();
        } else if (clicked == playButton || clicked == playButtonText) {
            mainScreen.levelGameplayPane.setCurrentLevel(levels[currentSelection]);
            mainScreen.switchToGameplayScreen();
        }
    }

    private void incrementSelection(int amt) {
        currentSelection = Math.floorMod(currentSelection + amt, levels.length);
    }
}