package JVE.Commands;

import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Command {

    public abstract BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception;

    private static Color c;
    private static Font f;

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

    public String setTimes(String s, float nTime, float aTime) {
        return s.replaceAll("normalTime", String.valueOf(nTime)).replaceAll("absoluteTime", String.valueOf(aTime));
    }

    public int parseTimedInt(String s, float nTime, float aTime) throws Exception {
        return MathParser.parseInt(setTimes(s, nTime, aTime));
    }

    public float parseTimedFloat(String s, float nTime, float aTime) throws Exception {
        return MathParser.parseFloat(setTimes(s, nTime, aTime));
    }
}
