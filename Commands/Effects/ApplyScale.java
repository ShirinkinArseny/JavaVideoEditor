package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;
import JVE.Parsers.SyntaxParser;

import java.awt.image.BufferedImage;

public class ApplyScale extends Command {

    String[] args;

    public ApplyScale(String[] s) {
        args = new String[]{
                MathParser.prepareExpression(s[0]),
                MathParser.prepareExpression(s[1])
        };
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {

        int dx = parseTimedInt(args[0], normalisedTime, absoluteTime);
        int dy = parseTimedInt(args[1], normalisedTime, absoluteTime);

        BufferedImage f = new BufferedImage(SyntaxParser.getW(), SyntaxParser.getH(), BufferedImage.TYPE_INT_ARGB);
        f.getGraphics().drawImage(canva, 0, 0, dx, dy, null);
        return f;
    }
}
