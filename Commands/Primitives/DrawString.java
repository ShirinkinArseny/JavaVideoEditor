package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DrawString extends Command {

    private String[] params;
    private Font f;

    public DrawString(String[] s) {
        params=new String[s.length];

        for (int i=0; i<s.length; i++)
            if (i!=0 && i!=3)
            params[i]= MathParser.prepareExpression(s[i]);
        else params[i]=s[i];

        if (params.length>3) {
        try {
            f = new Font(params[3], Font.PLAIN, MathParser.parseInt(params[4]));
        } catch (Exception e) {
            System.err.println("Failed to set up font "+ Arrays.toString(s));
        }      }
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        Graphics2D g= (Graphics2D) canva.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        switch (params.length) {
            case 3: {
                g.setFont(getDefaultFont());
                g.setColor(getDefaultColor());
                g.drawString(
                        params[0],
                        parseTimedInt(params[1], normalisedTime, absoluteTime),
                        parseTimedInt(params[2], normalisedTime, absoluteTime)
                );
                break;
            }
            case 5: {
                g.setFont(f);
                g.setColor(getDefaultColor());
                g.drawString(
                        params[0],
                        parseTimedInt(params[1], normalisedTime, absoluteTime),
                        parseTimedInt(params[2], normalisedTime, absoluteTime)
                );
                break;
            }
            case 8: {
                g.setFont(f);
                g.setColor(new Color(
                        parseTimedInt(params[5], normalisedTime, absoluteTime),
                        parseTimedInt(params[6], normalisedTime, absoluteTime),
                        parseTimedInt(params[7], normalisedTime, absoluteTime)
                ));
                g.drawString(
                        params[0],
                        parseTimedInt(params[1], normalisedTime, absoluteTime),
                        parseTimedInt(params[2], normalisedTime, absoluteTime)
                );
                break;
            }
            case 9: {
                g.setFont(f);
                g.setColor(new Color(
                        parseTimedInt(params[5], normalisedTime, absoluteTime),
                        parseTimedInt(params[6], normalisedTime, absoluteTime),
                        parseTimedInt(params[7], normalisedTime, absoluteTime),
                        parseTimedInt(params[8], normalisedTime, absoluteTime)
                ));
                g.drawString(
                        params[0],
                        parseTimedInt(params[1], normalisedTime, absoluteTime),
                        parseTimedInt(params[2], normalisedTime, absoluteTime)
                );
                break;
            }
            default:
                System.err.println("Unexpected number of params in draw string: "+params.length);
        }
        return canva;
    }
}
