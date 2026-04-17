package panes;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import level.Character;
import level.GameLevel;
import level.LevelStitcher;
import level.SaveData;
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
    private GImage levelImage2;
    private GImage backgroundImage;
    private GImage backgroundImage2; // for scrolling background

    private final GRect progressBarBackground;
    private final GRect progressBar;
    private final GLabel progressBarPercentage;
    
    long songPauseTime = 0;

    // Character
    private Character player;
    private long lastTickTime;

    // Death menu
    private DeathMenuPane deathMenu;
    private boolean showingDeathScreen = false;
    private CompletionPane completionMenu;
    private boolean showingCompletionScreen = false;
    boolean isProgressSaved = false;

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
        if (completionMenu != null && completionMenu.isVisible()) {
            completionMenu.hide();
        }
        showingCompletionScreen = false;
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
        this.completionMenu = new CompletionPane(mainApplication);
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(GameLevel currentLevel) {
        System.out.println("Setting level to " + currentLevel.getLevelName());
        this.currentLevel = currentLevel;
        showingDeathScreen = false;

        levelImage = new GImage("export/" + currentLevel.getLevelName() + "/segment/0.png");
        levelImage2 = new GImage("export/" + currentLevel.getLevelName() + "/segment/1.png");
        backgroundImage = new GImage("export/" + currentLevel.getLevelName() + "/background.png");
        backgroundImage2 = new GImage("export/" + currentLevel.getLevelName() + "/background.png");
        stitcher.setLevel(currentLevel);
        this.mainScreen.setStartMillis(System.currentTimeMillis());
        this.lastTickTime = System.currentTimeMillis();

        // Initialize the player character
        player = new Character(ELEMENT_SCALING, mainScreen);
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
        contents.add(levelImage2);
        mainScreen.add(levelImage2);

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
        if (paused || currentLevel == null) return;

        // Scroll the level based on the character's X position
        double characterPixelX = player.getXPos() * ELEMENT_SCALING;
        double levelOffsetX = -characterPixelX + 240; // keep character 200px from left edge
        levelImage.setLocation(levelOffsetX % 1920, -250);
        levelImage2.setLocation(levelImage.getX() + 1920, -250);
        if (Math.floor(player.getXPos()-3) % 24 == 0d) {
//          System.out.println("changing images");
            int i = (int) (Math.round(player.getXPos() - 3) / 24);
            int maxSegments = (int) Math.ceil(currentLevel.getGeometry()[0].length / 24f);
            levelImage.setImage("export/" + currentLevel.getLevelName() + "/segment/" + i + ".png");
            if (i + 1 > maxSegments - 1) {
                System.out.println("set invis");
                levelImage2.setVisible(false);
            } else {
                if (!levelImage2.isVisible()) {
                    System.out.println("set vis");
                    levelImage2.setVisible(true);
                }
                levelImage2.setImage("export/" + currentLevel.getLevelName() + "/segment/" + (i + 1) + ".png");
            }
        }
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

        // Save best completion to the level so panes.LevelSelectPane shows it
        float completionPercent = completion * 100;
        if (completionPercent > currentLevel.getCompletionPercent()) {
            currentLevel.setCompletionPercent(completionPercent);
        }
    }

    /**
     * Code that runs while the game is active.
     * @param delta Time since game start in milliseconds (from panes.MainApplication)
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
            if (player.isDead() && !showingCompletionScreen) {
                showDeathScreen();
                return;
            }
            if (this.player.getXPos() > this.currentLevel.getGeometry()[0].length && !showingDeathScreen) {
                showCompletion();
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

        if (!isProgressSaved) {
            SaveData.save(LevelSelectPane.levels);
            isProgressSaved = true;
        }
        deathMenu.show();
        mainScreen.stopLevelMusic(currentLevel.getSoundtrackURL());
    }

    private void showCompletion() {
    	if (showingCompletionScreen) return;
        showingCompletionScreen = true;
        mainScreen.playWinSound();

        if (!isProgressSaved) {
            SaveData.save(LevelSelectPane.levels);
            isProgressSaved = true;
        }
        completionMenu.show();
        mainScreen.stopLevelMusic(currentLevel.getSoundtrackURL());
    }


    /**
     * Restarts the current level.
     */
    private void restartLevel() {
        deathMenu.hide();
        showingDeathScreen = false;

        completionMenu.hide();
        showingCompletionScreen = false;
        isProgressSaved = false;
        paused = false;
        mainScreen.setMusicFrame(0);
        mainScreen.startLevelMusic(currentLevel.getSoundtrackURL(), 
        		mainScreen.getFullFrameLength(currentLevel.getSoundtrackURL()));

        // Remove old sprite
        mainScreen.remove(player.getSprite());
        contents.remove(player.getSprite());

        // Reinitialize character
        player = new Character(ELEMENT_SCALING, mainScreen);
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
        completionMenu.hide();
        showingCompletionScreen = false;
        paused = false;
        mainScreen.setMusicFrame(0);
        mainScreen.switchToLevelSelectScreen();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (showingDeathScreen || showingCompletionScreen) {
                goToLevelSelect(); // ESC on death menu goes back to level select
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (showingDeathScreen || showingCompletionScreen) {
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

        if (showingCompletionScreen && completionMenu.isVisible()) {
            String action = completionMenu.mouseClicked(e);
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