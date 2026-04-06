package level;

import acm.graphics.GImage;
import acm.graphics.*;
import java.util.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Player character that auto-runs forward (Geometry Dash style) and
 * supports gravity-based arc jumping.
 */
public class Character {

    // Physics constants (units are grid cells and seconds)
    private static final double GRAVITY = 50.0;       // cells/s^2 downward
    private static final double JUMP_VELOCITY = 18.0;  // cells/s upward on jump
    private static final double RUN_SPEED = 8.0;       // cells/s horizontal

    private final GImage sprite;
    private GameLevel level;
    private ObstacleType[][] geometry;

    // Position in grid-cell units (fractional)
    private double xPos;
    private double yPos;

    // Velocity
    private double xVel;
    private double yVel;

    private boolean onGround;
    private boolean dead;
    private int elementScaling;

    public Character(int elementScaling) {
        this.elementScaling = elementScaling;
        this.sprite = new GImage("Media/Character Sprite (1).png");
        this.dead = false;
        this.onGround = false;
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

        // Find the starting Y: top of the highest block in column 0
        int startY = 0;
        for (int row = geometry.length - 1; row >= 0; row--) {
            if (geometry[row][0] != null) {
                startY = row + 1; // place character one cell above the block
                break;
            }
        }

        this.xPos = 0;
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
        yVel -= GRAVITY * deltaSeconds;

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
                    if (above == ObstacleType.DOWN_SPIKE) {
                        die();
                        return;
                    }
                    // Bonk head on ceiling
                    yPos = headCheckY - 1;
                    yVel = 0;
                } else {
                    yPos = newY;
                }
            } else {
                yPos = newY;
            }
            onGround = false;
        }

        // --- Check if character has gone past the level ---
        if (xPos >= geometry[0].length) {
            // Level complete — don't die, just stop
            xVel = 0;
        }
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
        double screenX = pixelX + levelOffsetX;
        double screenY = pixelY;

        sprite.setLocation(screenX, screenY);
    }

    /**
     * Convert grid Y to screen Y. Grid Y=0 is the bottom, screen Y=0 is the top.
     */
    private double getScreenYFromGrid(double gridY) {
        // The level image is 1080px tall, bottom of grid (row 0) aligns with bottom of screen
        return 1080 - ((gridY + 1) * elementScaling) - 250; // -250 matches the level image offset in LevelGameplayPane
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