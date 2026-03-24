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
    public int filterRGB(int x, int y, int rgb) { //TODO: primary, secondary, and tertiary colors using color masks
//        System.out.println(new Color(rgb));
        int alpha = (rgb & 0xff000000) >> 24;
        int red   = (rgb & 0x00ff0000) >> 16;
        int green = (rgb & 0x0000ff00) >> 8;
        int blue  = (rgb & 0x000000ff);

        int newRed = (int) average(level.getColorScheme().primary.getRed(), level.getColorScheme().secondary.getRed(), level.getColorScheme().tertiary.getRed(), red, green, blue);

        int newGreen = (int) average(level.getColorScheme().primary.getGreen(), level.getColorScheme().secondary.getGreen(), level.getColorScheme().tertiary.getGreen(), red, green, blue);

        int newBlue = (int) average(level.getColorScheme().primary.getBlue(), level.getColorScheme().secondary.getBlue(), level.getColorScheme().tertiary.getBlue(), red, green, blue);
        return alpha << 24 | newRed << 16 | newGreen << 8 | newBlue;
    }

    public void setLevel(GameLevel level) {
        this.level = level;
    }

    public record Scheme(Color primary, Color secondary, Color tertiary) {}

    private double average(double num1, double num2, double num3, double weight1, double weight2, double weight3) {
        return (num1*weight1 + num2*weight2 + num3*weight3) / (weight1 + weight2 + weight3);
    }
}
