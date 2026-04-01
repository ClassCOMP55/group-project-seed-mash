package level;

import acm.graphics.GImage;


public class Obstacle {
    private ObstacleType type;
    private GridPos position;
    private GImage image;
    
    public Obstacle(ObstacleType type, GridPos position) {
        this.type = type;
        this.position = position;
        this.image = new GImage(type.getImageFileURL());
    }
    
    public ObstacleType getType() {
        return type;
    }
    
    public GridPos getPosition() {
        return position;
    }
    
    public GImage getImage() {
        return image;
    }
    
    /**
     * Updates the visual position of the obstacle based on game ticks
     */
    public void updatePosition(int gameTicks) {
        image.setLocation(
            position.getScreenX(gameTicks), 
            position.getScreenY(gameTicks)
        );
    }
    
    /**
     * Check if this obstacle is deadly (a spike)
     */
    public boolean isDeadly() {
        return type == ObstacleType.UP_SPIKE || type == ObstacleType.DOWN_SPIKE;
    }
}