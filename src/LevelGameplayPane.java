import acm.graphics.GImage;
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
    private long startMillis = 0;
    private GImage levelImage;
    private GImage backgroundImage;
    private GImage backgroundImage2; //in order to have it scroll



    public void startGame() {
        System.out.println("game start");
        startMillis = System.currentTimeMillis();
        renderLevel(System.currentTimeMillis() - startMillis);
        while (System.currentTimeMillis() - startMillis < 180000) {
//            System.out.println("mis " + (System.currentTimeMillis()-startMillis));
            renderLevel(System.currentTimeMillis() - startMillis);
        }

    }

    @Override
    public void showContent() {
        this.renderLevel(0);
    }


    @Override
    public void hideContent() {
        contents.clear();
        mainScreen.clear();
    }

    public LevelGameplayPane(MainApplication mainApplication) {
        stitcher = new LevelStitcher();
        this.mainScreen = mainApplication;
        this.setCurrentLevel(GameLevel.TEST_LEVEL);
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(GameLevel currentLevel) {
        System.out.println("setting level to " + currentLevel.getLevelName());
        if (this.currentLevel != null && this.currentLevel.equals(currentLevel)) return;

        this.currentLevel = currentLevel;
        levelImage = new GImage("export/" + currentLevel.getLevelName() + "/level.png");
        backgroundImage = new GImage("export/" + currentLevel.getLevelName() + "/background.png");
        backgroundImage2 = new GImage("export/" + currentLevel.getLevelName() + "/background.png");
        stitcher.setLevel(currentLevel);

        contents.add(backgroundImage);
        mainScreen.add(backgroundImage);
        contents.add(backgroundImage2);
        mainScreen.add(backgroundImage2);
        contents.add(levelImage);
        mainScreen.add(levelImage);
    }

    /**
     * Test for level rendering
     */
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.start();
        app.levelGameplayPane.setCurrentLevel(GameLevel.TEST_LEVEL_2);
        app.switchToScreen(app.levelGameplayPane);
        app.levelGameplayPane.startGame();
    }

    private void renderLevel(long delta) {
//        this.hideContent();
//        renderBackground(prog);
        levelImage.setLocation(-(delta)/2d, -250);
        backgroundImage.setLocation((-(delta)/4d)%backgroundImage.getWidth(), 0);
        backgroundImage2.setLocation(backgroundImage.getX() + backgroundImage.getWidth(), 0);

    }
    public void progressBar() {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 27) { //27 = escape key
            mainScreen.switchToLevelSelectScreen();
        }
        super.keyPressed(e);
    }
}
