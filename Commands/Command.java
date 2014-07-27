package JVE.Commands;

import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.IOException;

public abstract class Command {

    public abstract BufferedImage doAction(BufferedImage canva) throws Exception;

    public static BufferedImage applyRGBFilter(BufferedImage canva, RGBImageFilter f) throws IOException {

        Image image = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(
                canva.getSource(), f));


        BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(image, 0, 0, null);

        return bimage;
    }

    public short[][] divideChanels(short[] straight, short[] effected, String[] args) {
        if (args.length == 0)
            args = new String[]{""};
        short[][] chanelMap = new short[][]{straight, straight, straight, straight};
        for (String color : args) {
            switch (color.toLowerCase()) {
                case "red":
                    chanelMap[0] = effected;
                    break;
                case "green":
                    chanelMap[1] = effected;
                    break;
                case "blue":
                    chanelMap[2] = effected;
                    break;
                case "alpha":
                    chanelMap[3] = effected;
                    break;
                case "":
                    chanelMap[0] = effected;
                    chanelMap[1] = effected;
                    chanelMap[2] = effected;
                    break;
                default:
                    System.err.println("Unknown chanel parameter: " + color);
            }
        }
        return chanelMap;
    }

    public int parseTimedInt(String s) throws Exception {
        return MathParser.parseInt(s);
    }

    public float parseTimedFloat(String s) throws Exception {
        return MathParser.parseFloat(s);
    }
}
