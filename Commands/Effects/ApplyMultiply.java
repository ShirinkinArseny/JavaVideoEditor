package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;

public class ApplyMultiply extends Command {

    String[] args;
    String color;

    public ApplyMultiply(String[] s) {
        color = MathParser.prepareExpression(s[0]);
        args = new String[s.length - 1];
        System.arraycopy(s, 1, args, 0, s.length - 1);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {

        short[] modified = new short[256];
        short[] straight = new short[256];
        for (int i = 0; i < 256; i++) {
            straight[i] = (short) i;
            modified[i] = (short) Math.min(255, (i*parseTimedFloat(color)));
        }

        BufferedImageOp multiplyOp;
        short[][] invertMap = divideChanels(straight, modified, args);
        multiplyOp = new LookupOp(new ShortLookupTable(0, invertMap), null);

        return multiplyOp.filter(canva, canva);
    }
}
