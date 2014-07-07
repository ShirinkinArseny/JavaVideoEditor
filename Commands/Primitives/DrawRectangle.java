package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawRectangle extends Command {

    private String[] params;

    public DrawRectangle(String[] s) {
        params=new String[s.length];
        for (int i=0; i<s.length; i++)
            params[i]= MathParser.prepareExpression(s[i]);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        Graphics g=canva.getGraphics();

        switch (params.length) {
            case 4: {
                g.setColor(getDefaultColor());
                g.fillRect(
                        parseTimedInt(params[0], normalisedTime, absoluteTime),
                        parseTimedInt(params[1], normalisedTime, absoluteTime),
                        parseTimedInt(params[2], normalisedTime, absoluteTime),
                        parseTimedInt(params[3], normalisedTime, absoluteTime)
                );
                break;
            }
            case 7: {
                g.setColor(new Color(
                        parseTimedInt(params[4], normalisedTime, absoluteTime),
                        parseTimedInt(params[5], normalisedTime, absoluteTime),
                        parseTimedInt(params[6], normalisedTime, absoluteTime)
                ));
                g.fillRect(
                        parseTimedInt(params[0], normalisedTime, absoluteTime),
                        parseTimedInt(params[1], normalisedTime, absoluteTime),
                        parseTimedInt(params[2], normalisedTime, absoluteTime),
                        parseTimedInt(params[3], normalisedTime, absoluteTime)
                );
                break;
            }
            case 8: {
                g.setColor(new Color(
                        parseTimedInt(params[4], normalisedTime, absoluteTime),
                        parseTimedInt(params[5], normalisedTime, absoluteTime),
                        parseTimedInt(params[6], normalisedTime, absoluteTime),
                        parseTimedInt(params[7], normalisedTime, absoluteTime)
                ));
                g.fillRect(
                        parseTimedInt(params[0], normalisedTime, absoluteTime),
                        parseTimedInt(params[1], normalisedTime, absoluteTime),
                        parseTimedInt(params[2], normalisedTime, absoluteTime),
                        parseTimedInt(params[3], normalisedTime, absoluteTime)
                );
                break;
            }
            default:
                System.err.println("Unexpected number of params in draw rectangle: "+params.length);
        }
        return canva;
    }
}
