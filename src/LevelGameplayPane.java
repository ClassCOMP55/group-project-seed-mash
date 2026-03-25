import acm.graphics.GImage;
import acm.graphics.GImageTools;
import acm.graphics.GLabel;
import level.GameLevel;
import level.LevelStitcher;
import level.ObstacleType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.FilteredImageSource;
import java.util.HashMap;

/**
 * Graphics pane for actually playing the levels. Handles the rendering of the levels.
 */
public class LevelGameplayPane extends GraphicsPane {
    public static final int ELEMENT_SCALING = 80; //how big (in pixels) obstacles are going to appear on screen
    private GameLevel currentLevel;
    private static LevelStitcher stitcher;
    private int progress = 0;



    public void startGame() {
        progress = 0;
        while (progress < this.currentLevel.getRuntime()*1000) {
            System.out.println("progress: " + progress);
            renderLevel(progress);
            progress++;
        }
    }

    @Override
    public void showContent() {
        this.addText();
        this.renderLevel(progress);
    }

    private void renderBackground(int prog) {
        Image img = GImageTools.loadImage("background.png");
        img = GImageTools.getImageObserver().createImage(new FilteredImageSource(img.getSource(), stitcher.getColorFilter()));
        GImage toAdd = new GImage(img, -prog/2f, 0);
        toAdd.setSize(mainScreen.getWidth(), mainScreen.getHeight());
        contents.add(toAdd);
        mainScreen.add(toAdd);
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
        stitcher.setLevel(currentLevel);
    }

    /**
     * Test for level rendering
     */
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.start();
        app.levelGameplayPane.setCurrentLevel(GameLevel.TEST_LEVEL);
        app.switchToScreen(app.levelGameplayPane);
        app.levelGameplayPane.startGame();
    }

    private void addText() {
        GLabel text = new GLabel("Level gameplay", 100, 70);
        text.setColor(Color.BLUE);
        text.setFont("DialogInput-PLAIN-24");
        text.setLocation((mainScreen.getWidth() - text.getWidth()) / 2, 70);
//        GImage image = new GImage("obstacles/block.png", 100, 100);
//        image.setSize(70, 70);
//        contents.add(image);
//        mainScreen.add(image);

        contents.add(text);
        mainScreen.add(text);
    }


    private void renderLevel(int prog) {
        //TODO: stitch together one big image from level geometry? have to see if rendering all of the obstacles each run() call
        this.hideContent();
        renderBackground(prog);
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
