package level;

import java.awt.*;

/**
 * Class representing a single level to be played.
 * Each GameLevel object contains its own geometry (arrangement of obstacles in the world), the music that should play,
 * and all additional effects unique to that level alone (color, play speed, etc.) - Sean Y.
 */
public class GameLevel {
    private final String levelName;
    private final ObstacleType[][] geometry;
    private final String soundtrackURL;
    private float completionPercent = 0;
    private final Color colorScheme;
    private final int runtime;

    /**
     *
     * @param levelName Name of the level in plain text
     * @param soundtrackURL File path to the .mp3 soundtrack of the level
     * @param color Color of the level (changes how the obstacles and background looks)
     * @param runtime Runtime of the level in seconds
     * @param geom Level geometry
     */
    public GameLevel(String levelName, String soundtrackURL, Color color, int runtime, ObstacleType[][] geom) {
        this.levelName = levelName;
        this.soundtrackURL = soundtrackURL;
        this.colorScheme = color;
        this.runtime = runtime;
        this.geometry = geom;
    }

    public String getLevelName() {
        return levelName;
    }

    public ObstacleType[][] getGeometry() {
        return geometry;
    }

    public String getSoundtrackURL() {
        return soundtrackURL;
    }

    public float getCompletionPercent() {
        return completionPercent;
    }

    public void setCompletionPercent(float completionPercent) {
        this.completionPercent = completionPercent;
    }

    public Color getColorScheme() {
        return colorScheme;
    }

    public int getRuntime() {
        return runtime;
    }
     //
     //
     //
     //
     //                 ^
     //       ^     BBBBBBBB
     //BBBBBBBBBBBBBBBBBBBBB

    /**
     * Creates a 2D array of {@link ObstacleType}s from an array of Strings for use as level geometry
     * @param strings String array to translate into ObstacleTypes
     * @return Level geometry in the form of a 2D array of ObstacleTypes
     */
    public static ObstacleType[][] geomFromString(String[] strings) {
        ObstacleType[][] arr = new ObstacleType[strings.length][strings[0].length()];
        for (int r = 0; r < strings.length; r++) {
            for (int c = 0; c < strings[r].length(); c++) {
                int arrRow = strings.length - 1 - r; //if level is 5 units tall, then starting at row 0 for the string[] should point to row 4 for the obstacles
                arr[arrRow][c] = ObstacleType.fromCharacter(strings[r].charAt(c));
            }
        }
        return arr;
    }
    public static final String[] TEST_GEOM = {
            "BBB   BBB                                     ",
            "B       B                                     ",
            "BBB    BB                                     ",
            "B B     B                                     ",
            "BBB     B                                     ",
            "                                              ",
    };

    public static final GameLevel TEST_LEVEL = new GameLevel("Test Level", "", Color.MAGENTA, 60, geomFromString(TEST_GEOM));
}
