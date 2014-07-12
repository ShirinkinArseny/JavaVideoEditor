package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.image.*;

public class ApplyAdd extends Command {

    String[] args;
    String color;

    public ApplyAdd(String[] s) {
        color = MathParser.prepareExpression(s[0]);
        args = new String[s.length - 1];
        System.arraycopy(s, 1, args, 0, s.length - 1);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {

        BufferedImageOp multiplyOp = new RescaleOp(1, parseTimedFloat(color, normalisedTime, absoluteTime), null);
        return multiplyOp.filter(canva, canva);
    }
}
