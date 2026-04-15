package level;

import java.awt.*;

/**
 * Class representing a single level to be played.
 * Each GameLevel object contains its own geometry (arrangement of obstacles in the world), the music that should play,
 * and all additional effects unique to that level alone (color, play speed, etc.) - Sean Y.
 */
public class GameLevel {
    private final String levelName;
    //TODO: use GridPos -> ObstacleType map?
    private final ObstacleType[][] geometry;
    private final String soundtrackURL;
    private float completionPercent = 0;
    private final LevelStitcher.ColorScheme colorScheme;
    private final int runtime;
    private final int difficulty;
    private final int stars;
    private boolean completed = false;

    /**
     *
     * @param levelName     Name of the level in plain text
     * @param soundtrackURL File path to the .mp3 soundtrack of the level
     * @param color         Color of the level (changes how the obstacles and background looks)
     * @param runtime       Runtime of the level in seconds
     * @param geom          Level geometry
     * @param difficulty    Difficulty of the level
     * @param stars         Number of stars for the level
     */
    public GameLevel(String levelName, String soundtrackURL, LevelStitcher.ColorScheme color, int runtime, ObstacleType[][] geom, int difficulty, int stars) {
        this.levelName = levelName;
        this.soundtrackURL = soundtrackURL;
        this.colorScheme = color;
        this.runtime = runtime;
        this.geometry = geom;
        this.difficulty = difficulty;
        this.stars = stars;
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

    public LevelStitcher.ColorScheme getColorScheme() {
        return colorScheme;
    }

    public int getRuntime() {
        return runtime;
    }

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
            "                                                                                             ",
            "                                                                                             ",
            "                                                                                             ",
            "                                                                                             ",
            "                                                                                             ",
            "  B                                                                    B B                   ",
            "  B     B    B    B                                                    B B                   ",
            "  B                 B                                                  BBB                   ",
            "  B             B                                                      B B                   ",
            "  B         ^                                                          B B                   ",
            "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB      BBBBBBBBBBBBBBBBB",
    };


    public static final String[] TEST_GEOM_2 = {
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                       B                                        ",
            "                                                  BBBBB                                         ",
            "                                                                                                ",
            "                                                                                                ",
            "            ^                                                                                   ",
            "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
    };


    
    public static final GameLevel TEST_LEVEL = new GameLevel(
            "Test Level 1",
            "Pursuing_My_True_Self",
            new LevelStitcher.ColorScheme(
                    Color.BLUE,
                    Color.WHITE,
                    Color.BLACK
            ),
            60,
            geomFromString(TEST_GEOM),
            1,
            5
    );

    public static final GameLevel TEST_LEVEL_2 = new GameLevel(
            "Test Level 2",
            "",
            new LevelStitcher.ColorScheme(
                    new Color(255, 100, 10),
                    Color.WHITE,
                    new Color(50, 0, 0)
            ),
            60,
            geomFromString(TEST_GEOM_2),
            3,
            1
    );


    public int getDifficulty() {
        return difficulty;
    }

    public int getStars() {
        return stars;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted() {
        this.completed = true;
    }
    
    public void printGeom() {
    	for(int i = geometry.length-1; i >= 0; i--) {
			for(int j = 0; j < geometry[0].length; j++) {
				if (geometry[i][j] == null) {
					System.out.print(" ");
				} else {
					System.out.print(geometry[i][j]);
				}
			}
			System.out.println();
		}
    }

    public static final String[] RED_SUN_GEOM = {
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                ^BBBBBB                                                         ",
            "                           ^BBBBBB    B  -                                                      ",
            "                       ^BBBBBB        B     -                                                   ",
            "              ^BBBBBBBBBBBBB          B        -                                                ",
            "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
    };
    public static final GameLevel RED_SUN = new GameLevel(
            "Red Sun In The Sky",
            "Red_Sun",
            new LevelStitcher.ColorScheme(
                    new Color(255,50, 50),
                    new Color(255, 255, 0),
                    new Color(238/3, 28/3, 37/3)),
            112,
            geomFromString(RED_SUN_GEOM),
            1,
            0
    );
    
    public static final String[] TUMBLING_DICE_GEOM = {
            "                                                                                                                                                                                 BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "                                                                                                                                                                                 BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "                                                                                                                                                                                 BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "                                                                  BB                            B ^^^                    ^                                                       BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "                                                                  vv                            v BBB                    -                                                       BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "                  ^                         ^                                              ^                       ^^                     ^                      ^               BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "                  v                         v                                          B-------B            B-----------B               ---                      v               BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "              ^              ^          ^             ^                 ^              B       B        ^B--B           B       ^                         ----           B----B  BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "              B          ^   v    B-------B     ^     B--B  ^           v   ^     B--B B       B  ----  BB  B           B  B----B     ^                B        --   ^   B    B  BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
            "          B---B^        ^B      ^^B       B     B-----B  B  B     ^^BB      B-----B  B B       B        BB  B           B  B    B   ---              ^^B          ----   B    B  B                                            ",
            "BBBBBBBBBBB   BBBBBBBBBBBBBBBBBBBBB       BBBBBBB     B  B  BBBBBBBBBBBBBBBBB     B  BBB       B        BB  B           BBBB    B              BBBBBBBBB                 B    B  BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB    ",
    };
    public static final GameLevel TUMBLING_DICE = new GameLevel(
            "Tumbling Dice",
            "Tumbling_Dice",
            new LevelStitcher.ColorScheme(
                    new Color(255,26, 26),
                    Color.WHITE,
                    new Color(0, 0, 0)),
            80,
            geomFromString(TUMBLING_DICE_GEOM),
            3,
            0
    );
    
    public  static final String[] FINAL_DESTI = {
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                                                                ",
            "                                                       B                                        ",
            "                                                  BBBBB                                         ",
            "                                                                                                ",
            "                                                                                                ",
            "            ^                                                                                   ",
            "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
    };
    
    public static final GameLevel FINAL_DESTINATION = new GameLevel(
    		"Final Destination",
    		"final_destination",
    		 new LevelStitcher.ColorScheme(
                     new Color(255, 100, 10),
                     Color.WHITE,
                     new Color(50, 0, 0)
             ),
             60,
             geomFromString(FINAL_DESTI),
             3,
             1
    		
    		);
    
    public static final String[] GREAT_FAIRY_FOUNTAIN_GEOM = {
    	    "                                                                                                                         ",
    	    "                                                                                                                         ",
    	    "                                                                                                                         ",
    	    "                                                                                                                         ",
    	    "                                                                                                                         ",
    	    "                         B             B        B                   B              B                                     ",
    	    "                         B        B    B   B    B             B     B        B     B                                     ",
    	    "                    B    B   B    B    B   B    B       B     B     B   B    B     B                                     ",
    	    "                    B ^  B   B ^  B ^  B ^ B ^  B ^ ^   B ^   B ^   B   B ^  B ^   B                                     ",
    	    "               B    B    B        B        B            B         B        B                                             ",
    	    "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
    	};
    
    public static final GameLevel GREAT_FAIRY_FOUNTAIN = new GameLevel(
    	    "Great Fairy Fountain",
    	    "Great_Fairy_Fountain",
    	    new LevelStitcher.ColorScheme(
	    		new Color(30, 30, 60),   // deep dark navy — primary
	    	    new Color(60, 50, 80),   // dark muted purple — secondary
	    	    new Color(80, 70, 50) 
    	    ),
    	    180,
    	    geomFromString(GREAT_FAIRY_FOUNTAIN_GEOM),
    	    2,
    	    3
    	);
}
