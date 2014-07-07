package JVE.Commands.Simplificators;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.image.BufferedImage;

public class SetDefaultColor extends Command {

    private String r, g, b, a;

    public SetDefaultColor(String[] s) {
        r=MathParser.prepareExpression(s[0]);
        g=MathParser.prepareExpression(s[1]);
        b=MathParser.prepareExpression(s[2]);
        a=MathParser.prepareExpression(s[3]);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        Command.setDefaultColor(
                parseTimedInt(r, normalisedTime, absoluteTime),
                parseTimedInt(g, normalisedTime, absoluteTime),
                parseTimedInt(b, normalisedTime, absoluteTime),
                parseTimedInt(a, normalisedTime, absoluteTime)
        );
        return canva;
    }
}
