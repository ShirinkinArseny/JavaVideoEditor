package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ApplyCopy extends Command {

    private String[] params;

    public ApplyCopy(String[] s) {
        params = new String[s.length];
        for (int i = 0; i < s.length; i++)
            params[i] = MathParser.prepareExpression(s[i]);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        Graphics2D g2 = (Graphics2D) canva.getGraphics();
        g2.copyArea(
                parseTimedInt(params[0], normalisedTime, absoluteTime),
                parseTimedInt(params[1], normalisedTime, absoluteTime),
                parseTimedInt(params[2], normalisedTime, absoluteTime),
                parseTimedInt(params[3], normalisedTime, absoluteTime),
                parseTimedInt(params[4], normalisedTime, absoluteTime),
                parseTimedInt(params[5], normalisedTime, absoluteTime));
        return canva;
    }
}
