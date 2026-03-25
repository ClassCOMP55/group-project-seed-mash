package level;

import java.awt.*;
import java.awt.image.RGBImageFilter;
import java.util.Arrays;

/**
 * Image color filter used to make levels have a certain color scheme (which is defined with the GameLevel object). - Sean Y.
 */
public class LevelColor extends RGBImageFilter {
    private GameLevel level;
    public LevelColor() {
        this.canFilterIndexColorModel = true; //indicates that color doesn't depend on pixel location
    }
    @Override
    public int filterRGB(int x, int y, int rgb) {
//        System.out.println(new Color(rgb));
        int alpha = (rgb & 0xff000000) >> 24;
        int red   = (rgb & 0x00ff0000) >> 16;
        int green = (rgb & 0x0000ff00) >> 8;
        int blue  = (rgb & 0x000000ff);

        Color c1 = level.getColorScheme().primary;
        Color c2 = level.getColorScheme().secondary;
        Color c3 = level.getColorScheme().tertiary;
        double sum = red + green + blue;
        int newRed = (int) average(c1.getRed(), c2.getRed(), c3.getRed(), red, green, blue, sum);

        int newGreen = (int) average(c1.getGreen(), c2.getGreen(), c3.getGreen(), red, green, blue, sum);

        int newBlue = (int) average(c1.getBlue(), c2.getBlue(), c3.getBlue(), red, green, blue, sum);
        return alpha << 24 | newRed << 16 | newGreen << 8 | newBlue;
    }

    public void setLevel(GameLevel level) {
        this.level = level;
    }

    public record Scheme(Color primary, Color secondary, Color tertiary) {}

    private double average(double num1, double num2, double num3, double weight1, double weight2, double weight3, double sumWeights) {
        return (num1*weight1 + num2*weight2 + num3*weight3) / sumWeights;
    }
}
