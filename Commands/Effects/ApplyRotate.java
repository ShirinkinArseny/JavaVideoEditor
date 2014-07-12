package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ApplyRotate extends Command {

    private String[] params;

    public ApplyRotate(String[] s) {
        params = new String[s.length];
        for (int i = 0; i < s.length; i++)
            params[i] = MathParser.prepareExpression(s[i]);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        Graphics2D g2 = (Graphics2D) canva.getGraphics();
        g2.rotate(
                parseTimedFloat(params[0], normalisedTime, absoluteTime),
                parseTimedFloat(params[1], normalisedTime, absoluteTime),
                parseTimedFloat(params[2], normalisedTime, absoluteTime));
        return canva;
    }
}
