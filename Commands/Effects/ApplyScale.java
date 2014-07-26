package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;
import JVE.Parsers.Video;

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
    public BufferedImage doAction(BufferedImage canva) throws Exception {

        int dx = parseTimedInt(args[0]);
        int dy = parseTimedInt(args[1]);

        BufferedImage f = new BufferedImage(Video.getW(), Video.getH(), BufferedImage.TYPE_INT_ARGB);
        f.getGraphics().drawImage(canva, 0, 0, dx, dy, null);
        return f;
    }
}
