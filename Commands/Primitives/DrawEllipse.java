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
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        Graphics2D g = (Graphics2D) canva.getGraphics();

        g.setColor(new Color(
                parseTimedInt(params[4], normalisedTime, absoluteTime),
                parseTimedInt(params[5], normalisedTime, absoluteTime),
                parseTimedInt(params[6], normalisedTime, absoluteTime),
                parseTimedInt(params[7], normalisedTime, absoluteTime)
        ));
        if (Scene.getObeyProp())
        g.fillOval(
                (int) (parseTimedInt(params[0], normalisedTime, absoluteTime)*Scene.getProp()),
                (int) (parseTimedInt(params[1], normalisedTime, absoluteTime)*Scene.getProp()),
                (int) (parseTimedInt(params[2], normalisedTime, absoluteTime)*Scene.getProp()),
                (int) (parseTimedInt(params[3], normalisedTime, absoluteTime)*Scene.getProp())
        );
        else
            g.fillOval(
                    parseTimedInt(params[0], normalisedTime, absoluteTime),
                    parseTimedInt(params[1], normalisedTime, absoluteTime),
                    parseTimedInt(params[2], normalisedTime, absoluteTime),
                    parseTimedInt(params[3], normalisedTime, absoluteTime)
            );
        return canva;
    }
}
