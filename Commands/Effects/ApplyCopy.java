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
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        Graphics2D g2 = (Graphics2D) canva.getGraphics();
        g2.copyArea(
                parseTimedInt(params[0]),
                parseTimedInt(params[1]),
                parseTimedInt(params[2]),
                parseTimedInt(params[3]),
                parseTimedInt(params[4]),
                parseTimedInt(params[5]));
        return canva;
    }
}
