package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ApplyShear extends Command {

    private String[] params;

    public ApplyShear(String[] s) {
        //todo: проверить к-во параметров у эффектов
        params = new String[s.length];
        for (int i = 0; i < s.length; i++)
            params[i] = MathParser.prepareExpression(s[i]);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        Graphics2D g2 = (Graphics2D) canva.getGraphics();
        g2.shear(
                parseTimedFloat(params[0]),
                parseTimedFloat(params[1]));
        return canva;
    }
}
