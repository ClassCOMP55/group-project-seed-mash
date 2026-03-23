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
        int red = new Color(rgb).getRed();
        int green = new Color(rgb).getGreen();
        int blue = new Color(rgb).getBlue();

        float redPercent = red / 255f;
        float greenPercent = green / 255f;
        float bluePercent = blue / 255f;
        int alpha = rgb >> 24;
        int newRed = (int) average(level.getColorScheme().primary.getRed() * redPercent, level.getColorScheme().secondary.getRed() * greenPercent, level.getColorScheme().tertiary.getRed() * bluePercent, redPercent, greenPercent, bluePercent);

        int newGreen = (int) average(level.getColorScheme().primary.getGreen() * redPercent, level.getColorScheme().secondary.getGreen() * greenPercent, level.getColorScheme().tertiary.getGreen() * bluePercent, redPercent, greenPercent, bluePercent);

        int newBlue = (int) average(level.getColorScheme().primary.getBlue() * redPercent, level.getColorScheme().secondary.getBlue() * greenPercent, level.getColorScheme().tertiary.getBlue() * bluePercent, redPercent, greenPercent, bluePercent);
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
