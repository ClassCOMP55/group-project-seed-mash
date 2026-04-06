import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import level.Character;
import level.GameLevel;
import level.LevelStitcher;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Graphics pane for actually playing the levels. Handles the rendering of the levels
 * and the player character.
 */
public class LevelGameplayPane extends GraphicsPane {
    public static final int ELEMENT_SCALING = 80; // how big (in pixels) obstacles appear on screen
    private GameLevel currentLevel;
    private static LevelStitcher stitcher;
    private boolean paused = false;
    private long pauseTimestamp;
    private GImage levelImage;
    private GImage backgroundImage;
    private GImage backgroundImage2; // for scrolling background

    private GRect progressBarBackground;
    private GRect progressBar;
    private GLabel progressBarPercentage;

    // Character
    private Character player;
    private long lastTickTime;

    // Death menu
    private DeathMenuPane deathMenu;
    private boolean showingDeathScreen = false;

    @Override
    public void showContent() {
        // Don't call renderLevel here — wait until a level is set via setCurrentLevel
    }

    @Override
    public void hideContent() {
        if (deathMenu != null && deathMenu.isVisible()) {
            deathMenu.hide();
        }
        showingDeathScreen = false;
        paused = false;
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
        progressBarBackground = new GRect(200 - 5, 20 - 5, 1100 + 10, 30 + 10);
        progressBarBackground.setFilled(true);
        progressBarBackground.setFillColor(Color.WHITE);
        progressBarBackground.setLineWidth(0);
        progressBarPercentage = new GLabel("0%", 1100 + 20 + 190, 45);
        progressBarPercentage.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        progressBarPercentage.setColor(Color.WHITE);
        this.mainScreen = mainApplication;
        this.deathMenu = new DeathMenuPane(mainApplication);
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(GameLevel currentLevel) {
        System.out.println("Setting level to " + currentLevel.getLevelName());
        this.currentLevel = currentLevel;
        showingDeathScreen = false;

        levelImage = new GImage("export/" + currentLevel.getLevelName() + "/level.png");
        backgroundImage = new GImage("export/" + currentLevel.getLevelName() + "/background.png");
        backgroundImage2 = new GImage("export/" + currentLevel.getLevelName() + "/background.png");
        stitcher.setLevel(currentLevel);
        this.mainScreen.setStartMillis(System.currentTimeMillis());
        this.lastTickTime = System.currentTimeMillis();

        // Initialize the player character
        player = new Character(ELEMENT_SCALING);
        player.initForLevel(currentLevel);
        GImage sprite = player.getSprite();
        sprite.setSize(ELEMENT_SCALING, ELEMENT_SCALING);

        // Add background
        contents.add(backgroundImage);
        mainScreen.add(backgroundImage);
        contents.add(backgroundImage2);
        mainScreen.add(backgroundImage2);

        // Add level
        contents.add(levelImage);
        mainScreen.add(levelImage);

        // Add character sprite
        contents.add(sprite);
        mainScreen.add(sprite);

        // Add progress bar
        contents.add(progressBarBackground);
        mainScreen.add(progressBarBackground);
        contents.add(progressBar);
        mainScreen.add(progressBar);
        contents.add(progressBarPercentage);
        mainScreen.add(progressBarPercentage);
    }

    private void renderLevel(long delta) {
        if (paused) return;
        if (currentLevel == null) return;

        // Scroll the level based on the character's X position
        double characterPixelX = player.getXPos() * ELEMENT_SCALING;
        double levelOffsetX = -characterPixelX + 200; // keep character 200px from left edge

        levelImage.setLocation(levelOffsetX, -250);

        // Parallax background scrolling (moves at half speed)
        double bgOffset = levelOffsetX / 2.0;
        backgroundImage.setLocation(bgOffset % backgroundImage.getWidth(), 0);
        backgroundImage2.setLocation(backgroundImage.getX() + backgroundImage.getWidth(), 0);

        // Update character sprite position on screen
        player.updateSpritePosition(levelOffsetX);

        // Progress bar
        float completion = Math.min(1f, (float) player.getXPos() / (float) currentLevel.getGeometry()[0].length);
        progressBar.setSize(1100 * completion, progressBar.getHeight());
        progressBarPercentage.setLabel((int) (completion * 100) + "%");

        // Save best completion to the level so LevelSelectPane shows it
        float completionPercent = completion * 100;
        if (completionPercent > currentLevel.getCompletionPercent()) {
            currentLevel.setCompletionPercent(completionPercent);
        }
    }

    /**
     * Code that runs while the game is active.
     * @param delta Time since game start in milliseconds (from MainApplication)
     */
    public void tick(long delta) {
        if (currentLevel == null) return;
        if (showingDeathScreen) return;

        // Calculate actual time elapsed since last tick for physics
        long now = System.currentTimeMillis();
        double dtSeconds = (now - lastTickTime) / 1000.0;
        lastTickTime = now;

        // Clamp delta to avoid physics explosion after pausing/lag
        if (dtSeconds > 0.1) dtSeconds = 0.1;

        if (!paused) {
            // Update character physics
            player.tick(dtSeconds);

            // Check for death
            if (player.isDead()) {
                showDeathScreen();
                return;
            }
        }

        renderLevel(delta);
    }

    /**
     * Shows the death menu overlay with Replay and Level Select buttons.
     */
    private void showDeathScreen() {
        showingDeathScreen = true;
        paused = true;
        deathMenu.show();
    }

    /**
     * Restarts the current level.
     */
    private void restartLevel() {
        deathMenu.hide();
        showingDeathScreen = false;
        paused = false;

        // Remove old sprite
        mainScreen.remove(player.getSprite());
        contents.remove(player.getSprite());

        // Reinitialize character
        player = new Character(ELEMENT_SCALING);
        player.initForLevel(currentLevel);
        GImage sprite = player.getSprite();
        sprite.setSize(ELEMENT_SCALING, ELEMENT_SCALING);
        contents.add(sprite);
        mainScreen.add(sprite);

        // Reset timing
        mainScreen.setStartMillis(System.currentTimeMillis());
        lastTickTime = System.currentTimeMillis();
    }

    /**
     * Returns to the level select screen.
     */
    private void goToLevelSelect() {
        deathMenu.hide();
        showingDeathScreen = false;
        paused = false;
        mainScreen.switchToLevelSelectScreen();
    }

    public void pauseUnpause() {
        if (showingDeathScreen) return; // Don't toggle pause while death menu is open
        if (!paused) {
            paused = true;
            pauseTimestamp = mainScreen.getDelta();
        } else {
            paused = false;
            mainScreen.setStartMillis(mainScreen.getStartMillis() + mainScreen.getDelta() - pauseTimestamp);
            lastTickTime = System.currentTimeMillis(); // prevent physics jump after unpause
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (showingDeathScreen) {
                goToLevelSelect(); // ESC on death menu goes back to level select
            } else {
                pauseUnpause();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (showingDeathScreen) {
                restartLevel(); // Space/Up on death menu replays
            } else if (!paused && player != null) {
                player.jump();
            }
        }
        super.keyPressed(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (showingDeathScreen && deathMenu.isVisible()) {
            String action = deathMenu.mouseClicked(e);
            if ("replay".equals(action)) {
                restartLevel();
            } else if ("levelselect".equals(action)) {
                goToLevelSelect();
            }
            return; // Consume click while death menu is open
        }

        if (!paused && player != null) {
            player.jump();
        }
    }
}