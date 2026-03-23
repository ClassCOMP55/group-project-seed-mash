package level;

import java.awt.image.RGBImageFilter;

/**
 * Image color filter used to make levels have a certain color scheme (which is defined with the GameLevel object). - Sean Y.
 */
public class LevelColorFilter extends RGBImageFilter {
    private GameLevel level;
    public LevelColorFilter() {
        this.canFilterIndexColorModel = true; //indicates that color doesn't depend on pixel location
    }
    @Override
    public int filterRGB(int x, int y, int rgb) { //TODO: primary, secondary, and tertiary colors using color masks
        return level.getColorScheme().getRGB() & rgb;
    }

    public void setLevel(GameLevel level) {
        this.level = level;
    }
}
