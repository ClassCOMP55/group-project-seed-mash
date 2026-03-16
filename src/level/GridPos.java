package level;

/**
 * Class representing a position in the level aligned with the level grid.
 * Contains methods comparing positions to each other along with some utility methods for rendering to screen.
 * - Sean Y.
 * @param x X coordinate
 * @param y Y coordinate
 */
public record GridPos(int x, int y) {
/*
    Normally, positions on the screen are ordered to the bottom-right:
        _|0|1|2|3|
        0
        1
        2
        3
    This is less ideal for game logic, since vertical position is normally ordered top-down, meaning higher y-values
    indicate a higher position in the level/on the screen.
    Because of this, the coordinate system in our game (using the GridPos class) is ordered to the top-right:
        3
        2
        1
        0
        -|0|1|2|3|
 */

    /**
     *
     * @param xPos X coordinate
     * @param yPos Y coordinate
     * @return New GridPos object at (xPos, yPos)
     */
    public static GridPos at(int xPos, int yPos) {
        return new GridPos(xPos, yPos);
    }

    /**
     * @param gameTicks Current game ticks in level
     * @return The X coordinate of where the GridPos should be rendered on the screen.
     */
    public int getScreenX(int gameTicks) {
        return 0; //TODO: implement properly, relies on currently undecided screen display size, scaling, etc.
    }

    /**
     * @param gameTicks Current game ticks in level
     * @return The Y coordinate of where the GridPos should be rendered on the screen.
     */
    public int getScreenY(int gameTicks) {
        return 0; //TODO: implement properly
    }

    /**
     *
     * @param screenX X coordinate on the screen
     * @param screenY Y coordinate on the screen
     * @param gameTicks Current game ticks in level
     * @return GridPos of the coordinates from the screen.
     */
    public static GridPos atScreenPos(int screenX, int screenY, int gameTicks) {
        return GridPos.at(0,0); //TODO: implement properly
    }
}
