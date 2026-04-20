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

    // Card layout
    private static final double CARD_WIDTH  = 900;
    private static final double CARD_HEIGHT = 420;

    public static final GameLevel[] levels = {GameLevel.RED_SUN, GameLevel.FINAL_DESTINATION, GameLevel.TUMBLING_DICE, GameLevel.GREAT_FAIRY_FOUNTAIN};
    private GImage backButton;
    private GImage leftArrow;
    private GImage rightArrow;

    // Play button pieces for click detection
    private GRect playBtnBorder, playBtnBg, playBtnHighlight;
    private GLabel playBtnLabel;

    // Track all level-info elements for easy cleanup
    private ArrayList<GObject> levelInfoElements = new ArrayList<>();

    private int currentSelection = 0;

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

    /* ------------------------------------------------------------------ */
    /*  Level info card                                                     */
    /* ------------------------------------------------------------------ */

    private void clearLevelInfo() {
        for (GObject item : levelInfoElements) {
            mainScreen.remove(item);
            contents.remove(item);
        }
        levelInfoElements.clear();
    }

    private void addLevelInfoElement(GObject obj) {
        levelInfoElements.add(obj);
        contents.add(obj);
        mainScreen.add(obj);
    }

    private void drawLevelInfo() {
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

        /* ---------- card border ---------- */
        GRect cardBorder = new GRect(cardX - PX, cardY - PX, CARD_WIDTH + PX * 2, CARD_HEIGHT + PX * 2);
        cardBorder.setFilled(true);
        cardBorder.setFillColor(THEME_BLUE_DARKEST);
        cardBorder.setColor(THEME_BLUE_DARKEST);
        addLevelInfoElement(cardBorder);

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

        /* ---------- level number ---------- */
        String numStr = "L E V E L  " + (currentSelection + 1) + " / " + levels.length;
        GLabel numLabel = new GLabel(numStr);
        numLabel.setFont(new Font("Courier New", Font.BOLD, 18));
        numLabel.setColor(THEME_TEAL_LIGHT);
        numLabel.setLocation(cardX + 30, cardY + 50);
        addLevelInfoElement(numLabel);

        /* ---------- level name (with shadow) ---------- */
        GLabel nameShadow = new GLabel(level.getLevelName().toUpperCase());
        nameShadow.setFont(new Font("Courier New", Font.BOLD, 52));
        nameShadow.setColor(THEME_BLUE_DARKEST);
        nameShadow.setLocation(cardX + (CARD_WIDTH - nameShadow.getWidth()) / 2 + 3, cardY + 113);
        addLevelInfoElement(nameShadow);

        GLabel nameLabel = new GLabel(level.getLevelName().toUpperCase());
        nameLabel.setFont(new Font("Courier New", Font.BOLD, 52));
        nameLabel.setColor(TEXT_WHITE);
        nameLabel.setLocation(cardX + (CARD_WIDTH - nameLabel.getWidth()) / 2, cardY + 110);
        addLevelInfoElement(nameLabel);

        /* ---------- divider ---------- */
        double divY = cardY + 130;
        GRect divider = new GRect(cardX + 40, divY, CARD_WIDTH - 80, 1);
        divider.setFilled(true);
        divider.setFillColor(THEME_TEAL_DARK);
        divider.setColor(THEME_TEAL_DARK);
        addLevelInfoElement(divider);

        /* ---------- info row: difficulty + runtime ---------- */
        double infoY = divY + 35;
        double colWidth = (CARD_WIDTH - 80) / 2.0;

        // Difficulty
        double diffX = cardX + 40;
        GLabel diffTitle = new GLabel("DIFFICULTY");
        diffTitle.setFont(new Font("Courier New", Font.BOLD, 14));
        diffTitle.setColor(TEXT_SUBTLE);
        diffTitle.setLocation(diffX + (colWidth - diffTitle.getWidth()) / 2, infoY);
        addLevelInfoElement(diffTitle);

        int levelDifficulty = level.getDifficulty();
        String diffImage = "Difficulty1.png";
        if (levelDifficulty == 2) {
            diffImage = "Difficulty2.png";
        } else if (levelDifficulty == 3) {
            diffImage = "Difficulty3.png";
        } else if (levelDifficulty == 4) {
            diffImage = "Difficulty4.png";
        }
        GImage levelDiffIcon = new GImage(diffImage);
        levelDiffIcon.scale(0.2);
        levelDiffIcon.setLocation(diffX + (colWidth - levelDiffIcon.getWidth()) / 2, infoY + 10);
        addLevelInfoElement(levelDiffIcon);

        // Runtime
        double timeX = cardX + 40 + colWidth;
        GLabel timeTitle = new GLabel("RUNTIME");
        timeTitle.setFont(new Font("Courier New", Font.BOLD, 14));
        timeTitle.setColor(TEXT_SUBTLE);
        timeTitle.setLocation(timeX + (colWidth - timeTitle.getWidth()) / 2, infoY);
        addLevelInfoElement(timeTitle);

        int mins = level.getRuntime() / 60;
        int secs = level.getRuntime() % 60;
        String timeStr = mins + ":" + (secs < 10 ? "0" : "") + secs;
        GLabel timeValue = new GLabel(timeStr);
        timeValue.setFont(new Font("Courier New", Font.BOLD, 28));
        timeValue.setColor(TEXT_WHITE);
        timeValue.setLocation(timeX + (colWidth - timeValue.getWidth()) / 2, infoY + 30);
        addLevelInfoElement(timeValue);
        
        

        /* ---------- progress bar ---------- */
        double barMargin = 50;
        double barX = cardX + barMargin;
        double barFullW = CARD_WIDTH - barMargin * 2;
        double barH = 30;
        double barY = cardY + CARD_HEIGHT - 120;

        GLabel progTitle = new GLabel("PROGRESS");
        progTitle.setFont(new Font("Courier New", Font.BOLD, 14));
        progTitle.setColor(TEXT_SUBTLE);
        progTitle.setLocation(barX, barY - 10);
        addLevelInfoElement(progTitle);

        int pct = (int) level.getCompletionPercent();
        GLabel pctLabel = new GLabel(pct + "%");
        pctLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        pctLabel.setColor(TEXT_WHITE);
        pctLabel.setLocation(barX + barFullW - pctLabel.getWidth(), barY - 10);
        addLevelInfoElement(pctLabel);

        // Bar border
        GRect barBorder = new GRect(barX - PX / 2, barY - PX / 2, barFullW + PX, barH + PX);
        barBorder.setFilled(true);
        barBorder.setFillColor(THEME_BLUE_DARKEST);
        barBorder.setColor(THEME_BLUE_DARKEST);
        addLevelInfoElement(barBorder);

        // Bar track
        GRect barTrack = new GRect(barX, barY, barFullW, barH);
        barTrack.setFilled(true);
        barTrack.setFillColor(THEME_BLUE_DARKER);
        barTrack.setColor(THEME_BLUE_DARKER);
        addLevelInfoElement(barTrack);

        // Bar fill
        double fillW = Math.max(0, (level.getCompletionPercent() / 100.0) * barFullW);
        if (fillW > 0) {
            GRect barFill = new GRect(barX, barY, fillW, barH);
            barFill.setFilled(true);
            barFill.setFillColor(THEME_GREEN);
            barFill.setColor(THEME_GREEN);
            addLevelInfoElement(barFill);

            GRect fillHl = new GRect(barX, barY, fillW, PX);
            fillHl.setFilled(true);
            fillHl.setFillColor(THEME_GREEN_LIGHT);
            fillHl.setColor(THEME_GREEN_LIGHT);
            addLevelInfoElement(fillHl);
        }

        // Completed banner
        if (pct >= 100) {
            GLabel doneLabel = new GLabel("\u2714  C O M P L E T E D");
            doneLabel.setFont(new Font("Courier New", Font.BOLD, 18));
            doneLabel.setColor(THEME_GREEN_LIGHT);
            doneLabel.setLocation(cardX + (CARD_WIDTH - doneLabel.getWidth()) / 2, barY + barH + 30);
            addLevelInfoElement(doneLabel);
        }

     // Name Credit
        GLabel nameCredit = new GLabel(level.getCredit());
        nameCredit.setFont(new Font("Courier New", Font.BOLD, 28));
        nameCredit.setColor(TEXT_WHITE);
        nameCredit.setLocation(barTrack.getX(), barTrack.getY() + barTrack.getHeight()*2);
        addLevelInfoElement(nameCredit);
    }

    /* ------------------------------------------------------------------ */
    /*  Back / Arrow buttons (unchanged — your existing image buttons)      */
    /* ------------------------------------------------------------------ */
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

    /* ------------------------------------------------------------------ */
    /*  Input                                                               */
    /* ------------------------------------------------------------------ */
    @Override
    public void mouseClicked(MouseEvent e) {
        GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());
        if (clicked == backButton) {
            System.out.println("Back button clicked!");
            mainScreen.switchToStartScreen();
            mainScreen.playClickSound();
        } else if (clicked == leftArrow) {
            incrementSelection(-1);
            drawLevelInfo();
            mainScreen.playClickSound();
        } else if (clicked == rightArrow) {
            incrementSelection(1);
            drawLevelInfo();
            mainScreen.playClickSound();
        } else if (levelInfoElements.contains(clicked)) {
            mainScreen.levelGameplayPane.setCurrentLevel(levels[currentSelection]);
            mainScreen.switchToGameplayScreen();
            mainScreen.playClickSound();
        }
    }

    private void incrementSelection(int amt) {
        currentSelection = Math.floorMod(currentSelection + amt, levels.length);
    }
}