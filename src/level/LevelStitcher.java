package level;

import acm.graphics.GImage;
import acm.graphics.GImageTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Level rendering approach: Stitch together obstacle images from level data in order to create one large panorama for rendering efficiency. - Sean Y.
 */
public class LevelStitcher {
    public static final int ELEMENT_SCALING = 80;
    private GameLevel level;
    private ColorFilter colorFilter = new ColorFilter();

    public ColorFilter getColorFilter() {
        return colorFilter;
    }
    public void setLevel(GameLevel level) {
        this.colorFilter.setLevel(level);
        this.level = level;
    }

    public void createImage() {
        BufferedImage image = new BufferedImage(level.getGeometry()[0].length * ELEMENT_SCALING, 1080, BufferedImage.TYPE_INT_ARGB);

        HashMap<ObstacleType, Image> obstacleImageCache = new HashMap<>();
        int i = 0;
        for (ObstacleType obstacle: ObstacleType.values()) {
            Image img = GImageTools.loadImage(obstacle.getImageFileURL());
            img = GImageTools.getImageObserver().createImage(new FilteredImageSource(img.getSource(), colorFilter));
            obstacleImageCache.put(obstacle, img);
            //i don't know why but saving these is necessary for the level stitching, even though it doesn't save them properly
            //like this shouldn't affect the rendering which uses the img values from the map, but deleting it makes the whole thing black
            BufferedImage buf = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            buf.createGraphics().drawImage(img, 0, 0, GImageTools.getImageObserver());
            try {
                if (ImageIO.write(buf, "png", new File("./Media/export/obstacle" + i + ".png"))) {
                    System.out.println("level " + obstacle.getImageFileURL() + " successfully exported");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }

        ObstacleType[][] geom = level.getGeometry();
        Graphics2D graphics = image.createGraphics();
        for (int r = 0; r < geom.length; r++) {
            for (int c = 0; c < geom[r].length; c++) {
                if (geom[r][c] != null) { //do not render anything for empty spaces
                    int x = ELEMENT_SCALING*c;
//                    System.out.println("Height: " + mainScreen.getWindow().getHeight());
                    int y = 1080 - (ELEMENT_SCALING*(r+1)); //bottom of level will always be aligned with bottom of window, 37 accounts for top toolbar height i guess
//                    GImage toAdd = new GImage(obstacleImageCache.get(geom[r][c]), x, y);
//                    toAdd.setSize(ELEMENT_SCALING, ELEMENT_SCALING);
                    graphics.drawImage(obstacleImageCache.get(geom[r][c]), x, y, ELEMENT_SCALING, ELEMENT_SCALING,null);
                }
            }
        }
        try {
            if (ImageIO.write(image, "png", new File("./Media/export/" + level.getLevelName() + ".png"))) {
                System.out.println("level " + level.getLevelName() + " successfully exported");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        LevelStitcher stitch = new LevelStitcher();
        stitch.setLevel(GameLevel.TEST_LEVEL);
        stitch.createImage();
    }

    /**
     * Image color filter used to make levels have a certain color scheme (which is defined with the GameLevel object). - Sean Y.
     */
    public static class ColorFilter extends RGBImageFilter {
        private GameLevel level;

        public ColorFilter() {
            this.canFilterIndexColorModel = true; //indicates that color doesn't depend on pixel location
        }

        @Override
        public int filterRGB(int x, int y, int rgb) {
//        System.out.println(new Color(rgb));
            int alpha = (rgb & 0xff000000) >> 24;
            int red = (rgb & 0x00ff0000) >> 16;
            int green = (rgb & 0x0000ff00) >> 8;
            int blue = (rgb & 0x000000ff);

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

        private double average(double num1, double num2, double num3, double weight1, double weight2, double weight3, double sumWeights) {
            return (num1 * weight1 + num2 * weight2 + num3 * weight3) / sumWeights;
        }
    }

    public record ColorScheme(Color primary, Color secondary, Color tertiary) {}
}
