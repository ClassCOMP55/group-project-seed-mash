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
 * Player character that auto-runs forward (Geometry Dash style) and
 * supports gravity-based arc jumping.
 */
public class Character {

    // Physics constants (units are grid cells and seconds)
    private static final double GRAVITY = 50.0;       // cells/s^2 downward
    private static final double JUMP_VELOCITY = 18.0;  // cells/s upward on jump
    private static final double RUN_SPEED = 8.0;       // cells/s horizontal
    
    private static final double ROTATION_SPEED = Math.PI * 2.0;

    private final GImage sprite;
    private GameLevel level;
    private ObstacleType[][] geometry;
    
    private panes.MainApplication mainApp;

    // Position in grid-cell units (fractional)
    private double xPos;
    private double yPos;

    // Velocity
    private double xVel;
    private double yVel;
    
    private double rotationAngle = 0;
    private double targetRotationAngle = 0;
    private boolean wasOnGround = true;
    
    private BufferedImage originalImage;
    private int spriteSize;

    private boolean onGround;
    private boolean dead;
    private int elementScaling;

    public Character(int elementScaling) {
        this.elementScaling = elementScaling;
        this.sprite = new GImage("Media/Character Sprite (1).png");
        this.dead = false;
        this.onGround = false;
        this.spriteSize = elementScaling;
        
        try {
        	originalImage = ImageIO.read(new File("Media/Character Sprite (1).png"));
        }catch (IOException e) {
        	System.out.println("Could not load character sprite for rotationn:" + e.getMessage());
        	originalImage = null;
        }
    }

    /**
     * Initialize the character for a given level. Places the character
     * on top of the first column's topmost block.
     */
    public void initForLevel(GameLevel level) {
        this.level = level;
        this.geometry = level.getGeometry();
        this.dead = false;
        this.onGround = false;
        this.xVel = RUN_SPEED;
        this.yVel = 0;
        this.rotationAngle = 0;
        this.targetRotationAngle = 0;
        this.wasOnGround = true;

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
     * Attempt to jump. Only works if the character is on the ground.
     */
    public void jump() {
        if (onGround && !dead) {
            yVel = JUMP_VELOCITY;
            onGround = false;
        }
    }

    /**
     * Main update method, called each frame.
     * @param deltaSeconds time elapsed since last tick, in seconds
     */
    public void tick(double deltaSeconds) {
        if (dead) return;

        // --- Apply gravity ---
        yVel -= GRAVITY * deltaSeconds * (xPos >= geometry[0].length ? -0.17 : 1);

        // --- Calculate new position ---
        double newX = xPos + xVel * deltaSeconds;
        double newY = yPos + yVel * deltaSeconds;

        // --- Horizontal collision ---
        int cellX = (int) Math.floor(newX + 0.9); // leading edge (right side of character)
        int cellYBottom = (int) Math.floor(yPos);
        // Check the cell the character's right edge is moving into at current height
        if (cellX >= 0 && cellX < geometry[0].length && cellYBottom >= 0 && cellYBottom < geometry.length) {
            ObstacleType obstacle = geometry[cellYBottom][cellX];
            if (obstacle != null) {
                if (obstacle == ObstacleType.UP_SPIKE || obstacle == ObstacleType.DOWN_SPIKE) {
                    die();
                    return;
                } else if (obstacle == ObstacleType.BLOCK) {
                    // Hit a wall — die (Geometry Dash style)
                    die();
                    return;
                }
            }
        }
        xPos = newX;

        // --- Vertical collision ---
        if (yVel <= 0) {
            // Falling or on ground — check below
            int groundCheckY = (int) Math.floor(newY);
            int charCol = (int) Math.floor(xPos);

            if (groundCheckY < 0) {
                // Fell off the bottom of the level
                die();
                return;
            }

            if (charCol >= 0 && charCol < geometry[0].length && groundCheckY >= 0 && groundCheckY < geometry.length) {
                ObstacleType below = geometry[groundCheckY][charCol];
                if (below != null) {
                    if (below == ObstacleType.UP_SPIKE) {
                        die();
                        return;
                    }
                    // Land on top of the block
                    yPos = groundCheckY + 1;
                    yVel = 0;
                    onGround = true;
                } else {
                    yPos = newY;
                    onGround = false;
                }
            } else {
                yPos = newY;
                onGround = false;
            }
        } else {
            // Moving upward — check above
            int headCheckY = (int) Math.floor(newY + 0.9);
            int charCol = (int) Math.floor(xPos);

            if (charCol >= 0 && charCol < geometry[0].length && headCheckY >= 0 && headCheckY < geometry.length) {
                ObstacleType above = geometry[headCheckY][charCol];
                if (above != null) {
                    die();
                    return;
                } else {
                    yPos = newY;
                }
            } else {
                yPos = newY;
            }
            onGround = false;
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
                targetRotationAngle = rotationAngle;
                wasOnGround = true;
            }
        }
 
        // Update the sprite image with rotation
        updateSpriteRotation();
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
        System.out.println("Character died at (" + xPos + ", " + yPos + ")");
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }
}