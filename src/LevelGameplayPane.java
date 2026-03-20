import acm.graphics.GImage;
import acm.graphics.GImageTools;
import acm.graphics.GLabel;
import level.GameLevel;
import level.LevelColorFilter;
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
    private static LevelColorFilter colorFilter;

    /**
     * When a GameLevel is loaded into LevelGameplayPane via {@link #setCurrentLevel(GameLevel)}, the images of the obstacles
     * are automatically colored (according to the GameLevel's color scheme) and stored
     * here to be referenced statically when the level is being rendered.
     */
    private static final HashMap<ObstacleType, Image> obstacleImageCache = new HashMap<>();

    @Override
    public void showContent() {
        this.addText();
        this.renderLevel();
    }

    @Override
    public void hideContent() {
        contents.clear();
        mainScreen.clear();
    }

    public LevelGameplayPane(MainApplication mainApplication) {
        colorFilter = new LevelColorFilter();
        this.mainScreen = mainApplication;
        this.setCurrentLevel(GameLevel.TEST_LEVEL);
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(GameLevel currentLevel) {
        this.currentLevel = currentLevel;
        colorFilter.setLevel(currentLevel);
        for (ObstacleType obstacle: ObstacleType.values()) {
            Image img = GImageTools.loadImage(obstacle.getImageFileURL());
            img = GImageTools.getImageObserver().createImage(new FilteredImageSource(img.getSource(), colorFilter));
            obstacleImageCache.put(obstacle, img);
        }
    }

    /**
     * Test for level rendering
     */
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.start();
        app.levelGameplayPane.setCurrentLevel(GameLevel.TEST_LEVEL);
        app.switchToScreen(app.levelGameplayPane);
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


    private void renderLevel() {
        //TODO: stitch together one big image from level geometry? have to see if rendering all of the obstacles each run() call
        this.hideContent();
        ObstacleType[][] geom = currentLevel.getGeometry();
        for (int r = 0; r < geom.length; r++) {
            for (int c = 0; c < geom[r].length; c++) {
                if (geom[r][c] != null) { //do not render anything for empty spaces
                    int x = ELEMENT_SCALING*c;
//                    System.out.println("Height: " + mainScreen.getWindow().getHeight());
                    int y = mainScreen.getWindow().getHeight()- (ELEMENT_SCALING*(r+1)) - 37; //bottom of level will always be aligned with bottom of window, 37 accounts for top toolbar height i guess
                    GImage toAdd = new GImage(obstacleImageCache.get(geom[r][c]), x, y);
                    toAdd.setSize(ELEMENT_SCALING, ELEMENT_SCALING);
                    contents.add(toAdd);
                    mainScreen.add(toAdd);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 27) { //27 = escape key
            mainScreen.switchToLevelSelectScreen();
        }
        super.keyPressed(e);
    }
}
