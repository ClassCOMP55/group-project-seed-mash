import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import level.GameLevel;
import level.LevelStitcher;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Graphics pane for actually playing the levels. Handles the rendering of the levels.
 */
public class LevelGameplayPane extends GraphicsPane {
    public static final int ELEMENT_SCALING = 80; //how big (in pixels) obstacles are going to appear on screen
    private GameLevel currentLevel;
    private static LevelStitcher stitcher;
    private boolean paused = false;
    private long pauseTimestamp;
    private GImage levelImage;
    private GImage backgroundImage;
    private GImage backgroundImage2; //in order to have it scroll

    private final GRect progressBarBackground;
    private final GRect progressBar;
    private final GLabel progressBarPercentage;

    @Override
    public void showContent() {
        this.renderLevel(0);
    }


    @Override
    public void hideContent() {
        contents.clear();
        mainScreen.clear();
    }
    
    public GImage getBackgroundImage() {
    	return backgroundImage;
    }
    
    public GImage getBackgroundImage2() {
    	return backgroundImage2;
    }

    public LevelGameplayPane(MainApplication mainApplication) {
        stitcher = new LevelStitcher();
        progressBar = new GRect(200, 20, 1100, 30);
        progressBar.setFilled(true);
        progressBar.setFillColor(Color.GREEN);
        progressBar.setLineWidth(0);
        progressBarBackground = new GRect(200-5, 20-5, 1100 + 10, 30+10);
        progressBarBackground.setFilled(true);
        progressBarBackground.setFillColor(Color.WHITE);
        progressBarBackground.setLineWidth(0);
        progressBarPercentage = new GLabel("XX%", 1100 + 20 + 190, 45);
        progressBarPercentage.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        progressBarPercentage.setColor(Color.WHITE);
        this.mainScreen = mainApplication;
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(GameLevel currentLevel) {
        System.out.println("setting level to " + currentLevel.getLevelName());
        this.currentLevel = currentLevel;
        levelImage = new GImage("export/" + currentLevel.getLevelName() + "/level.png");
        backgroundImage = new GImage("export/" + currentLevel.getLevelName() + "/background.png");
        backgroundImage2 = new GImage("export/" + currentLevel.getLevelName() + "/background.png");
        stitcher.setLevel(currentLevel);
        this.mainScreen.setStartMillis(System.currentTimeMillis());

        contents.add(backgroundImage);
        mainScreen.add(backgroundImage);
        contents.add(backgroundImage2);
        mainScreen.add(backgroundImage2);
        contents.add(levelImage);
        mainScreen.add(levelImage);

        contents.add(progressBarBackground);
        mainScreen.add(progressBarBackground);
        contents.add(progressBar);
        mainScreen.add(progressBar);
        contents.add(progressBarPercentage);
        mainScreen.add(progressBarPercentage);
    }

    private void renderLevel(long delta) {
//        this.hideContent();
//        renderBackground(prog);
        if (paused) return;
        levelImage.setLocation(-(delta)/2d, -250);
        backgroundImage.setLocation((-(delta)/4d)%backgroundImage.getWidth(), 0);
        backgroundImage2.setLocation(backgroundImage.getX() + backgroundImage.getWidth(), 0);
        float completion = Math.min(1f, (delta / 1000f) / currentLevel.getRuntime());
        progressBar.setSize(1100 * completion, progressBar.getHeight());
        progressBarPercentage.setLabel((int)(completion * 100) + "%");
    }


    /**
     * Code that runs while the game is active
     * @param delta Time since game start in milliseconds
     */
    public void tick(long delta) {
//        System.out.println("tick " + delta);
        renderLevel(delta);
    }

    public void pauseUnpause() {
        if (!paused) { //pausing behavior
            paused = true;
            pauseTimestamp = mainScreen.getDelta();
        } else { //unpause behavior
            paused = false;
            mainScreen.setStartMillis(mainScreen.getStartMillis() + mainScreen.getDelta() - pauseTimestamp); //returns timestamp to where game was paused
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 27) {
            pauseUnpause();
        }
        super.keyPressed(e);
    }
}
