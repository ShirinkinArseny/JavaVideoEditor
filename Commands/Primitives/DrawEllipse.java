package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;
import JVE.Rendering.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawEllipse extends Command {

    private String[] params;
    private static String[] paramsLast = new String[]{"0", "0", "200", "200", "0", "0", "0", "255"};

    public DrawEllipse(String[] s) {
        params = new String[paramsLast.length];
        System.arraycopy(paramsLast, 0, params, 0, paramsLast.length);
        for (int i = 0; i < s.length; i++) {
            params[i] = MathParser.prepareExpression(s[i]);
            paramsLast[i]=params[i];
        }
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        Graphics2D g = (Graphics2D) canva.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(
                parseTimedInt(params[4]),
                parseTimedInt(params[5]),
                parseTimedInt(params[6]),
                parseTimedInt(params[7])
        ));
        if (Scene.getObeyProp())
        g.fillOval(
                (int) (parseTimedInt(params[0])*Scene.getProp()),
                (int) (parseTimedInt(params[1])*Scene.getProp()),
                (int) (parseTimedInt(params[2])*Scene.getProp()),
                (int) (parseTimedInt(params[3])*Scene.getProp())
        );
        else
            g.fillOval(
                    parseTimedInt(params[0]),
                    parseTimedInt(params[1]),
                    parseTimedInt(params[2]),
                    parseTimedInt(params[3])
            );
        return canva;
    }
}
