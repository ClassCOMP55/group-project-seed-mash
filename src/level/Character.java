package level;

import acm.graphics.GImage;
import acm.graphics.*;
import java.util.*;

import javax.imageio.ImageIO;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;



/**
 * Player character that auto-runs forward and
 * supports gravity-based arc jumping.
 */
public class Character {

    // Physics constants (units are grid cells and seconds)
    private static final double GRAVITY = 50.0;       // cells/s^2 downward
    private static final double JUMP_VELOCITY = 18.0;  // cells/s upward on jump
    private static final double RUN_SPEED = 8.0;       // cells/s horizontal
    
    private static final double ROTATION_SPEED = Math.PI * 2.0;

    // Jump input forgiveness (Geometry Dash style)
    private static final double JUMP_BUFFER_TIME = 0.15; // seconds

    // --- Spike hitbox dimensions (as a fraction of a 1x1 cell) ---
    // Only the SHARP TIP of each spike is lethal. The flat base and the sides
    // of the triangle are safe to brush against. UP and DOWN spikes have their
    // own heights because we want the V-spike to be even more forgiving — only
    // the very point should kill, not the upper portion of the triangle.
    private static final double SPIKE_HITBOX_WIDTH         = 0.25; // narrow horizontally (just the tip width)
    private static final double UP_SPIKE_HITBOX_HEIGHT     = 0.35; // '^' — tip at top of cell
    private static final double DOWN_SPIKE_HITBOX_HEIGHT   = 0.20; // 'V' — tip at bottom of cell (smaller: just the point)

    private final GImage sprite;
    private ObstacleType[][] geometry;
    
    private final panes.MainApplication mainApp;

    // Position in grid-cell units (fractional)
    private double xPos;
    private double yPos;

    // Velocity
    private double xVel;
    private double yVel;
    
    private double rotationAngle = 0;
    private boolean wasOnGround = true;
    
    private BufferedImage originalImage;
    private final int spriteSize;

    private boolean onGround;
    private boolean dead;
    private final int elementScaling;

    // Jump input state
    private boolean jumpHeld = false;
    private double jumpBufferTimer = 0;

    public Character(int elementScaling, panes.MainApplication mainApp) {
        this.mainApp = mainApp;
        this.elementScaling = elementScaling;
        this.sprite = new GImage("Media/Character Sprite (1).png");
        this.dead = false;
        this.onGround = false;
        this.spriteSize = elementScaling;
        
        try {
            originalImage = ImageIO.read(new File("Media/Character Sprite (1).png"));
        } catch (IOException e) {
            System.out.println("Could not load character sprite for rotation:" + e.getMessage());
            originalImage = null;
        }
    }

    /**
     * Initialize the character for a given level. Places the character
     * on top of the first column's topmost block.
     */
    public void initForLevel(GameLevel level) {
        this.geometry = level.getGeometry();
        this.dead = false;
        this.onGround = false;
        this.xVel = RUN_SPEED;
        this.yVel = 0;
        this.rotationAngle = 0;
        this.wasOnGround = true;
        this.jumpHeld = false;
        this.jumpBufferTimer = 0;

        // Find the starting Y: top of the highest block in column 0
        int startY = 0;
        for (int row = geometry.length - 1; row >= 0; row--) {
            if (geometry[row][0] != null) {
                startY = row + 1; // place character one cell above the block
                break;
            }
        }

        this.xPos = 3;
        this.yPos = startY;
    }

    public GImage getSprite() {
        return sprite;
    }

    public boolean isDead() {
        return dead;
    }

    /**
     * Attempt to jump. If on the ground, jump immediately.
     * If airborne, buffer the input so if we land within JUMP_BUFFER_TIME,
     * we'll auto-jump on landing 
     */
    public void jump() {
        if (dead) return;
        if (onGround) {
            yVel = JUMP_VELOCITY;
            onGround = false;
        } else {
            jumpBufferTimer = JUMP_BUFFER_TIME;
        }
    }

    /**
     * Set whether the jump button is being held. When held and the character
     * is on the ground, it will auto-jump 
     */
    public void setJumpHeld(boolean held) {
        this.jumpHeld = held;
    }

    /**
     * Surfaces the character can stand on / walk along.
     *   - BLOCK and PLATFORM: full floor surfaces.
     *   - DOWN_SPIKE: the flat BASE at the top of a 'V' spike cell acts as a
     *     platform. The lethal zone is only the point at the bottom of the
     *     cell (see hitsSpike), so landing on top from above is safe.
     *   - UP_SPIKE: NOT landable — the tip is at the top, so landing on a
     *     '^' from above impales the character, which is handled by the spike
     *     collision check at the end of tick().
     */
    private static boolean isLandableSurface(ObstacleType t) {
        return t == ObstacleType.BLOCK
            || t == ObstacleType.PLATFORM
            || t == ObstacleType.DOWN_SPIKE;
    }

    /**
     * Does the character's 1x1 AABB overlap the shrunken hitbox of the spike
     * at the given grid cell?
     *
     * Geometry (tip-aligned, so only the sharp point is lethal):
     *   Character AABB  : (xPos, yPos) .. (xPos+1, yPos+1)
     *   UP_SPIKE   hitbox: centered horizontally, anchored to the TOP of the
     *                      cell (the sharp tip points up)
     *   DOWN_SPIKE hitbox: centered horizontally, anchored to the BOTTOM of
     *                      the cell (the sharp tip points down)
     */
    private boolean hitsSpike(ObstacleType spike, int spikeCol, int spikeRow) {
        double charLeft   = xPos;
        double charRight  = xPos + 1;
        double charBottom = yPos;
        double charTop    = yPos + 1;

        double spikeLeft  = spikeCol + (1.0 - SPIKE_HITBOX_WIDTH) / 2.0;
        double spikeRight = spikeLeft + SPIKE_HITBOX_WIDTH;
        double spikeBottom;
        double spikeTop;

        if (spike == ObstacleType.UP_SPIKE) {
            // tip sits at the cell ceiling, hitbox extends downward from the tip
            spikeTop    = spikeRow + 1.0;
            spikeBottom = spikeTop - UP_SPIKE_HITBOX_HEIGHT;
        } else { // DOWN_SPIKE
            // tip sits at the cell floor, hitbox extends upward from the tip.
            // The top of the V (flat base and upper sides) stays safe.
            spikeBottom = spikeRow;
            spikeTop    = spikeBottom + DOWN_SPIKE_HITBOX_HEIGHT;
        }

        return charRight  > spikeLeft
            && charLeft   < spikeRight
            && charTop    > spikeBottom
            && charBottom < spikeTop;
    }

    /**
     * Check every grid cell the character's AABB might overlap for a deadly
     * spike intersection. Returns true at the first hit.
     */
    private boolean isHittingAnySpike() {
        if (geometry == null) return false;

        int minCol = (int) Math.floor(xPos);
        int maxCol = (int) Math.floor(xPos + 1 - 1e-9);
        int minRow = (int) Math.floor(yPos);
        int maxRow = (int) Math.floor(yPos + 1 - 1e-9);

        for (int col = minCol; col <= maxCol; col++) {
            if (col < 0 || col >= geometry[0].length) continue;
            for (int row = minRow; row <= maxRow; row++) {
                if (row < 0 || row >= geometry.length) continue;

                ObstacleType obs = geometry[row][col];
                if (obs == ObstacleType.UP_SPIKE || obs == ObstacleType.DOWN_SPIKE) {
                    if (hitsSpike(obs, col, row)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Main update method, called each frame.
     * @param deltaSeconds time elapsed since last tick, in seconds
     */
    public void tick(double deltaSeconds) {
        if (dead) return;

        // --- Decay jump buffer ---
        if (jumpBufferTimer > 0) {
            jumpBufferTimer -= deltaSeconds;
        }
        // --- Auto-jump on ground if button is held OR a jump was recently buffered ---
        if (onGround && (jumpHeld || jumpBufferTimer > 0)) {
            yVel = JUMP_VELOCITY;
            onGround = false;
            jumpBufferTimer = 0;
        }

        // --- Apply gravity ---
        yVel -= GRAVITY * deltaSeconds * (xPos >= geometry[0].length ? -0.17 : 1);

        // --- Calculate new position ---
        double newX = xPos + xVel * deltaSeconds;
        double newY = yPos + yVel * deltaSeconds;

        // --- Horizontal collision (walls only; spikes handled at end of tick) ---
        int cellX = (int) Math.floor(newX + 0.999999999f); // leading edge (right side of character)
        int cellYBottom = (int) Math.floor(yPos);
        if (cellX >= 0 && cellX < geometry[0].length && cellYBottom >= 0 && cellYBottom < geometry.length) {
            ObstacleType obstacle = geometry[cellYBottom][cellX];
            if (obstacle == ObstacleType.BLOCK) {
                // Hit a wall — die (Geometry Dash style)
                die();
                return;
            }
        }
        xPos = newX;

        // --- Vertical collision (blocks/platforms only; spikes handled at end of tick) ---
        if (yVel <= 0) {
            // Falling or on ground — check below
            int groundCheckY = (int) Math.floor(newY);
            int charCol = (int) Math.floor(xPos);

            if (groundCheckY < 0) {
                // Fell off the bottom of the level
                die();
                return;
            }

            // Is there a landable surface under the character's footprint?
            // (check both the column the character's left edge is in and the one to the right,
            // since the character's 1-unit-wide AABB usually straddles two columns)
            boolean landed = false;
            if (groundCheckY < geometry.length) {
                if (charCol >= 0 && charCol < geometry[0].length
                        && isLandableSurface(geometry[groundCheckY][charCol])) {
                    landed = true;
                }
                if (!landed && (charCol + 1) >= 0 && (charCol + 1) < geometry[0].length
                        && isLandableSurface(geometry[groundCheckY][charCol + 1])) {
                    landed = true;
                }
            }

            if (landed) {
                yPos = groundCheckY + 1;
                yVel = 0;
                onGround = true;
            } else {
                yPos = newY;
                onGround = false;
            }
        } else {
            // Moving upward — ceiling check against solid blocks
            int headCheckY = (int) Math.floor(newY + 0.9);
            int charCol = (int) Math.floor(xPos);

            if (charCol >= 0 && charCol < geometry[0].length
                    && headCheckY >= 0 && headCheckY < geometry.length) {
                ObstacleType above = geometry[headCheckY][charCol];
                if (above == ObstacleType.BLOCK) {
                    // Smack head on a ceiling — die (Geometry Dash style)
                    die();
                    return;
                }
            }
            yPos = newY;
            onGround = false;
        }

        // --- Spike collision (centralized, uses narrow base-aligned hitbox) ---
        if (isHittingAnySpike()) {
            die();
            return;
        }

        // --- Rotation ---
        updateRotation(deltaSeconds);

        // --- Check if character has gone past the level ---
        if (xPos >= geometry[0].length) {
            // Level complete — don't die, just stop
            xVel = 0;
        }
    }
    
    private void updateRotation(double deltaSeconds) {
        if (!onGround) {
            // Rotate while in the air
            rotationAngle -= ROTATION_SPEED * deltaSeconds;
            wasOnGround = false;
        } else {
            if (!wasOnGround) {
                // Just landed — snap to nearest 90°
                double halfPI = Math.PI / 2.0;
                rotationAngle = Math.round(rotationAngle / halfPI) * halfPI;
                wasOnGround = true;
            }
        }

 
        // Update the sprite image with rotation
        updateSpriteRotation();
        mainApp.getGW().setIconImage(this.sprite.getImage());
    }
    /**
     * Renders the sprite rotated by the current angle.
     */
    private int rotationPadding = 0;
 
    private void updateSpriteRotation() {
        if (originalImage == null) return;
 
        int size = spriteSize;
        int diagSize = (int) Math.ceil(size * Math.sqrt(2));
        rotationPadding = (diagSize - size) / 2;
 
        BufferedImage rotated = new BufferedImage(diagSize, diagSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
 
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        // Draw the character at original size, centered in the larger canvas, then rotate
        AffineTransform transform = new AffineTransform();
        transform.translate(diagSize / 2.0, diagSize / 2.0);
        transform.rotate(-rotationAngle);
        transform.scale((double) size / originalImage.getWidth(), (double) size / originalImage.getHeight());
        transform.translate(-originalImage.getWidth() / 2.0, -originalImage.getHeight() / 2.0);
 
        g2d.drawImage(originalImage, transform, null);
        g2d.dispose();
 
        int[][] pixels = new int[diagSize][diagSize];
        for (int row = 0; row < diagSize; row++) {
            for (int col = 0; col < diagSize; col++) {
                pixels[row][col] = rotated.getRGB(col, row);
            }
        }
        sprite.setImage(new GImage(pixels).getImage());
        sprite.setSize(diagSize, diagSize);
    }

    /**
     * Update the sprite's screen position based on current grid position.
     * @param levelOffsetX the pixel offset the level image is rendered at (negative, scrolling left)
     */
    public void updateSpritePosition(double levelOffsetX) {
        // Character's pixel position relative to the level
        double pixelX = xPos * elementScaling;
        double pixelY = getScreenYFromGrid(yPos);

        // On screen, add the level's scroll offset
        double screenX = pixelX + levelOffsetX - rotationPadding;
        double screenY = pixelY - rotationPadding;

        sprite.setLocation(screenX, screenY);
    }

    /**
     * Convert grid Y to screen Y. Grid Y=0 is the bottom, screen Y=0 is the top.
     */
    private double getScreenYFromGrid(double gridY) {
        // The level image is 1080px tall, bottom of grid (row 0) aligns with bottom of screen
        return 1080 - ((gridY + 1) * elementScaling) - 250; // -250 matches the level image offset in panes.LevelGameplayPane
    }

    private void die() {
        dead = true;
        xVel = 0;
        yVel = 0;
        mainApp.playDeathSound();
        System.out.println("Character died at (" + xPos + ", " + yPos + ")");
    }

    public double getXPos() {
        return xPos;
    }

    @SuppressWarnings("unused")
    public double getYPos() {
        return yPos;
    }
}