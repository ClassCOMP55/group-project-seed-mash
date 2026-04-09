package panes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.*;
import level.GameLevel;

public class LevelSelectPane extends GraphicsPane {
	
    private static final Color THEME_BLUE         = new Color(0, 102, 204);
    private static final Color THEME_BLUE_DARK    = new Color(0, 70, 150);
    private static final Color THEME_BLUE_DARKER  = new Color(0, 50, 110);
    private static final Color THEME_BLUE_DARKEST = new Color(0, 30, 80);
    private static final Color THEME_BLUE_LIGHT   = new Color(40, 140, 230);
 
    private static final Color THEME_GREEN         = new Color(46, 204, 113);
    private static final Color THEME_GREEN_LIGHT   = new Color(80, 230, 140);
    private static final Color THEME_GREEN_DARK    = new Color(30, 150, 80);
    private static final Color THEME_GREEN_DARKEST = new Color(20, 110, 60);
 
    private static final Color THEME_TEAL       = new Color(54, 212, 201);
    private static final Color THEME_TEAL_LIGHT = new Color(90, 235, 225);
    private static final Color THEME_TEAL_DARK  = new Color(35, 160, 150);
 
    private static final Color TEXT_WHITE  = new Color(240, 240, 240);
    private static final Color TEXT_SUBTLE = new Color(180, 210, 240);
 
    private static final Color GROUND_TOP    = new Color(0, 85, 170);
    private static final Color GROUND_MID    = new Color(0, 65, 135);
    private static final Color GROUND_BOTTOM = new Color(0, 45, 100);
    private static final Color GROUND_DEEP   = new Color(0, 30, 70);
 
    private static final int PX = 8;
    private static final double CARD_WIDTH  = 900;
    private static final double CARD_HEIGHT = 420;

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
    
    private GRect playBtnBorder, playBtnBg, playBtnHighlight;
    private GLabel playBtnLabel;
    
    

    // Track all level-info elements for easy cleanup
    private ArrayList<GObject> levelInfoElements = new ArrayList<>();

    private int currentSelection = 0; // Start at 0 (first valid index)

    public LevelSelectPane(MainApplication mainScreen) {
        this.mainScreen = mainScreen;
    }


    @Override
    public void showContent() {
    	drawBackground();
    	drawGround();
    	drawTitle();
    	drawTitleLine();
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

    private void drawBackground() {
        int bands = 8;
        double bandH = mainScreen.getHeight() / (double) bands;
        for (int i = 0; i < bands; i++) {
            int r = lerp(0, THEME_BLUE.getRed(), i, bands);
            int g = lerp(30, THEME_BLUE.getGreen(), i, bands);
            int b = lerp(80, THEME_BLUE.getBlue(), i, bands);
            GRect band = new GRect(0, i * bandH, mainScreen.getWidth(), bandH + 1);
            band.setFilled(true);
            Color c = new Color(r, g, b);
            band.setFillColor(c);
            band.setColor(c);
            contents.add(band);
            mainScreen.add(band);
        }
    }
    
    private void drawGround() {
        double groundY = mainScreen.getHeight() - 160;
        double w = mainScreen.getWidth();
        int blockSize = PX * 5;
 
        // Surface row
        for (int x = 0; x < w; x += blockSize) {
            GRect surface = new GRect(x, groundY, blockSize, blockSize);
            surface.setFilled(true);
            surface.setFillColor((x / blockSize) % 2 == 0 ? THEME_BLUE : THEME_BLUE_DARK);
            surface.setColor(THEME_BLUE_DARK);
            contents.add(surface);
            mainScreen.add(surface);
 
            GRect highlight = new GRect(x, groundY, blockSize, PX);
            highlight.setFilled(true);
            highlight.setFillColor(THEME_TEAL);
            highlight.setColor(THEME_TEAL);
            contents.add(highlight);
            mainScreen.add(highlight);
        }
 
        // Deeper rows
        Color[] depthColors    = {GROUND_TOP, GROUND_MID, GROUND_BOTTOM};
        Color[] depthAltColors = {THEME_BLUE_DARKER, GROUND_TOP, GROUND_MID};
        for (int row = 1; row <= 3; row++) {
            for (int x = 0; x < w; x += blockSize) {
                GRect block = new GRect(x, groundY + row * blockSize, blockSize, blockSize);
                block.setFilled(true);
                Color c = ((x / blockSize) + row) % 2 == 0 ? depthColors[row - 1] : depthAltColors[row - 1];
                block.setFillColor(c);
                block.setColor(GROUND_DEEP);
                contents.add(block);
                mainScreen.add(block);
            }
        }
    }
    private void drawTitle() {
        GLabel shadow = new GLabel("S E L E C T   L E V E L");
        shadow.setFont(new Font("Courier New", Font.BOLD, 64));
        shadow.setColor(THEME_BLUE_DARKEST);
        shadow.setLocation((mainScreen.getWidth() - shadow.getWidth()) / 2 + 4, 104);
        contents.add(shadow);
        mainScreen.add(shadow);
 
        GLabel title = new GLabel("S E L E C T   L E V E L");
        title.setFont(new Font("Courier New", Font.BOLD, 64));
        title.setColor(TEXT_WHITE);
        title.setLocation((mainScreen.getWidth() - title.getWidth()) / 2, 100);
        contents.add(title);
        mainScreen.add(title);
    }
 
    private void drawTitleLine() {
        double cx = mainScreen.getWidth() / 2.0;
        double lineW = 860;
        GRect bar = new GRect(cx - lineW / 2, 120, lineW, 1);
        bar.setFilled(true);
        bar.setFillColor(THEME_TEAL);
        bar.setColor(THEME_TEAL);
        contents.add(bar);
        mainScreen.add(bar);
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
        double cx = mainScreen.getWidth() / 2.0;
        double cardX = cx - CARD_WIDTH / 2;
        double cardY = 180;
        
        /* ---------- card shadow ---------- */
        GRect cardShadow = new GRect(cardX + 5, cardY + 5, CARD_WIDTH, CARD_HEIGHT);
        cardShadow.setFilled(true);
        cardShadow.setFillColor(new Color(0, 0, 0, 70));
        cardShadow.setColor(new Color(0, 0, 0, 70));
        addLevelInfoElement(cardShadow);
        
        /* ---------- card body ---------- */
        GRect cardBody = new GRect(cardX, cardY, CARD_WIDTH, CARD_HEIGHT);
        cardBody.setFilled(true);
        cardBody.setFillColor(THEME_BLUE_DARK);
        cardBody.setColor(THEME_BLUE_DARK);
        addLevelInfoElement(cardBody);
        
        /* ---------- teal accent strip ---------- */
        GRect accent = new GRect(cardX, cardY, CARD_WIDTH, PX * 2);
        accent.setFilled(true);
        accent.setFillColor(THEME_TEAL);
        accent.setColor(THEME_TEAL);
        addLevelInfoElement(accent);
        
        

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
    
    private int lerp(int a, int b, int step, int total) {
        if (total == 0) return a;
        return a + (b - a) * step / total;
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