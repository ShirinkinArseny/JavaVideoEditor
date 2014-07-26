package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;
import JVE.Parsers.Video;

import java.awt.image.BufferedImage;

public class ApplyMove extends Command {

    String[] args;

    public ApplyMove(String[] s) {
        args = new String[]{
                MathParser.prepareExpression(s[0]),
                MathParser.prepareExpression(s[1])
        };
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {

        int dx = parseTimedInt(args[0]);
        int dy = parseTimedInt(args[1]);

        BufferedImage f = new BufferedImage(Video.getW(), Video.getH(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        f.getGraphics().drawImage(canva, dx, dy, null);
        return f;
    }
}
