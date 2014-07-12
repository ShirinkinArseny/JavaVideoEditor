package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DrawString extends Command {

    private String[] params;
    private static String[] paramsLast = new String[]{"Sample text", "0", "0", "DejaVu Sans", "24", "0", "0", "0", "255"};
    private Font f;

    public DrawString(String[] s) {
        params = new String[paramsLast.length];
        System.arraycopy(paramsLast, 0, params, 0, paramsLast.length);

        for (int i = 0; i < s.length; i++)  {
            if (i != 0 && i != 3)
                params[i] = MathParser.prepareExpression(s[i]);
                else params[i] = s[i];
            paramsLast[i]=params[i];
            }

        if (params.length > 3) {
            try {
                f = new Font(params[3], Font.PLAIN, MathParser.parseInt(params[4]));
            } catch (Exception e) {
                System.err.println("Failed to set up font " + Arrays.toString(s));
            }
        }
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        Graphics2D g = (Graphics2D) canva.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

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
        return canva;
    }
}
