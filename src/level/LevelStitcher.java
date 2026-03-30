package level;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Level rendering approach: Stitch together obstacle images from level data in order to create one large panorama for rendering efficiency. - Sean Y.
 */
public class LevelStitcher {
    public static final int ELEMENT_SCALING = 80;
    public static final Component OBSERVER = new JPanel();
    private GameLevel level;
    private TestFilter colorFilter = new TestFilter();

    public void setLevel(GameLevel level) {
        this.colorFilter.setLevel(level);
        this.level = level;
    }
    public void createObstacles() {
        try {
            for (ObstacleType obstacle : ObstacleType.values()) {
                BufferedImage img = ImageIO.read(new File("Media/" + obstacle.getImageFileURL()));
                img = colorFilter.filter(img, img);
                //i don't know why but saving these is necessary for the level stitching, even though it doesn't save them properly
                //like this shouldn't affect the rendering which uses the img values from the map, but deleting it makes the whole thing black
                File output = new File("./Media/export/" + level.getLevelName() + "/" + obstacle.getImageFileURL());
                if (!output.exists()) output.mkdirs();
                if (ImageIO.write(img, "png", output)) {
                    System.out.println("level " + obstacle.getImageFileURL() + " successfully exported");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void createLevelImage() {
        try {
            BufferedImage levelImg = new BufferedImage(level.getGeometry()[0].length * ELEMENT_SCALING, 1080, BufferedImage.TYPE_INT_ARGB);
            HashMap<ObstacleType, BufferedImage> obstacleImageCache = new HashMap<>();
            for (ObstacleType type: ObstacleType.values()) {
                obstacleImageCache.put(type, ImageIO.read(new File("Media/export/" + level.getLevelName() + "/" + type.getImageFileURL())));
            }
            ObstacleType[][] geom = level.getGeometry();
            Graphics2D graphics = levelImg.createGraphics();
            for (int r = 0; r < geom.length; r++) {
                for (int c = 0; c < geom[r].length; c++) {
                    if (geom[r][c] != null) { //do not render anything for empty spaces
                        int x = ELEMENT_SCALING * c;
//                    System.out.println("Height: " + mainScreen.getWindow().getHeight());
                        int y = 1080 - (ELEMENT_SCALING * (r + 1)); //bottom of level will always be aligned with bottom of window, 37 accounts for top toolbar height i guess
//                    GImage toAdd = new GImage(obstacleImageCache.get(geom[r][c]), x, y);
//                    toAdd.setSize(ELEMENT_SCALING, ELEMENT_SCALING);
                        graphics.drawImage(obstacleImageCache.get(geom[r][c]), x, y, ELEMENT_SCALING, ELEMENT_SCALING, OBSERVER);
                    }
                }
            }
            if (ImageIO.write(levelImg, "png", new File("./Media/export/" + level.getLevelName() + "/level.png"))) {
                System.out.println("level " + level.getLevelName() + " successfully exported");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createBackgroundImage() {
        try {
            BufferedImage buf = ImageIO.read(new File("Media/background.png"));
            buf = colorFilter.filter(buf, buf);
            File output = new File("./Media/export/" + level.getLevelName() + "/background.png");

            if (!output.exists()) output.mkdirs();
            if (ImageIO.write(buf, "png", output)) {
                System.out.println("level " + level.getLevelName() + " background successfully exported");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        LevelStitcher stitch = new LevelStitcher();
        stitch.setLevel(GameLevel.TEST_LEVEL_2);

        stitch.createObstacles();
        stitch.createLevelImage();
        stitch.createBackgroundImage();

        stitch.setLevel(GameLevel.TEST_LEVEL);

        stitch.createObstacles();
        stitch.createLevelImage();
        stitch.createBackgroundImage();
    }
    public static class TestFilter implements BufferedImageOp {
        private GameLevel level;

        @Override
        public BufferedImage filter(BufferedImage src, BufferedImage dest) {
            for (int r = 0; r < src.getHeight(); r++) {
                for (int c = 0; c < src.getWidth(); c++) {
//                    System.out.println("(" + r + ", " + c + ")");
                    dest.setRGB(c, r, filterRGB(src.getRGB(c, r)));
                }
            }
            return dest;
        }

        @Override
        public Rectangle2D getBounds2D(BufferedImage src) {
            return src.getData().getBounds();
        }

        @Override
        public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
            return new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB, (IndexColorModel) destCM);
        }

        @Override
        public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
            return srcPt;
        }

        @Override
        public RenderingHints getRenderingHints() {
            return null;
        }

        public int filterRGB(int rgb) {
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