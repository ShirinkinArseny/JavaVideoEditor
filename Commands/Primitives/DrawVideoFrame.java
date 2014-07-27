package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;
import JVE.Rendering.FFMpegUsage;
import JVE.Rendering.Scene;

import java.awt.image.BufferedImage;

public class DrawVideoFrame extends Command {

    private int startTime;
    private String[] params;

    private static String[] paramsLast = new String[]{"Filename.mp4", "0", "1", "0", "0", "200", "200"};

    public DrawVideoFrame(String[] s) throws Exception {
        params = new String[paramsLast.length];
        System.arraycopy(paramsLast, 0, params, 0, paramsLast.length);
        for (int i = 0; i < s.length; i++) {
            if (i != 0)
                params[i] = MathParser.prepareExpression(s[i]);
            else
                params[i] = s[i];
            paramsLast[i]=params[i];
        }
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {

            BufferedImage img = FFMpegUsage.makeFrame(params[0], parseTimedFloat(params[1]));

            if (Scene.getObeyProp())
                canva.getGraphics().drawImage(img,
                        (int) (parseTimedInt(params[2]) * Scene.getProp()),
                        (int) (parseTimedInt(params[3]) * Scene.getProp()),
                        (int) (parseTimedInt(params[4]) * Scene.getProp()),
                        (int) (parseTimedInt(params[5]) * Scene.getProp()),
                        null, null
                );
            else
                canva.getGraphics().drawImage(img,
                        parseTimedInt(params[2]),
                        parseTimedInt(params[3]),
                        parseTimedInt(params[4]),
                        parseTimedInt(params[5]),
                        null, null
                );
        return canva;
    }
}
