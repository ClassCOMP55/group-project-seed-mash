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
    private long startMillis = System.currentTimeMillis();
    private GImage levelImage;



    public void startGame() {
        while (true)
        renderLevel();
    }

    @Override
    public void showContent() {
        this.renderLevel();
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
        if (this.currentLevel != null && this.currentLevel.equals(currentLevel)) return;
        this.currentLevel = currentLevel;
        levelImage = new GImage("export/" + currentLevel.getLevelName() + "/level.png");
        stitcher.setLevel(currentLevel);
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



    private void renderLevel() {
//        this.hideContent();
//        renderBackground(prog);
        System.out.println(System.currentTimeMillis() - startMillis);
        levelImage.setLocation(-(System.currentTimeMillis() - startMillis)/2d, -250);
        contents.add(levelImage);
        mainScreen.add(levelImage);
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
