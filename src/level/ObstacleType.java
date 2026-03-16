package level;

/**
 * All the types of static objects that can appear in a level. - Sean Y.
 * <ul>
 * <li> <b>Block</b>: Basic square, foundation of the level. Can be traversed upon.</li>
 * <li> <b>Platform</b>: Same as a Block, but visually occupies less space.</li>
 * <li> <b>Up-Spike</b>: A spike facing upwards. Game over if the player touches the spike.</li>
 * <li> <b>Down-Spike</b>: A spike facing downwards. Game over if the player touches the spike.</li>
 * </ul>
 */
public enum ObstacleType {
    BLOCK("obstacles/block.png", 'B'),
    UP_SPIKE("obstacles/spike_up.png", '^'),
    DOWN_SPIKE("obstacles/spike_down.png", 'V'),
    PLATFORM("obstacles/platform.png", '-');

    private final String imageFile;
    private final char charRepresentation;

    public String getImageFileURL() {
        return imageFile;
    }

    public char getCharRepresentation() {
        return charRepresentation;
    }
    ObstacleType(String imageURL, char charRep) {
        imageFile = imageURL;
        charRepresentation = charRep;
    }

    /**
     * Used in the level creation process to turn arranged strings into level geometry.
     * @param c Character (potentially) representing an ObstacleType
     * @return ObstacleType corresponding to the character c
     */
    public static ObstacleType fromCharacter(char c) {
        return switch (c) {
            case 'B' -> BLOCK;
            case '^' -> UP_SPIKE;
            case 'v' -> DOWN_SPIKE;
            case '-' -> PLATFORM;
            case ' ' -> null; //Empty, occupied by "air"
            default -> {
                System.out.println("Warning: attempted to get obstacle with invalid character '" + c + "', returning null.");
                yield null;
            }
        };

    }
}
