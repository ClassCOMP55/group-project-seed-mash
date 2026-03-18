package level;

import java.awt.image.RGBImageFilter;

/**
 * Image color filter used to make levels have a certain color scheme (which is defined with the GameLevel object). - Sean Y.
 */
public class LevelColorFilter extends RGBImageFilter {
    private GameLevel level;
    public LevelColorFilter() {
        this.canFilterIndexColorModel = true;
    }
    @Override
    public int filterRGB(int x, int y, int rgb) {
        return level.getColorScheme().getRGB() & rgb;
    }

    public GameLevel getLevel() {
        return level;
    }

    public void setLevel(GameLevel level) {
        this.level = level;
    }
}
