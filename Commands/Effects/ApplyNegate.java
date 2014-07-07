package JVE.Commands.Effects;

import JVE.Commands.Command;

import java.awt.image.*;

public class ApplyNegate extends Command {

    BufferedImageOp negateOp;

    public ApplyNegate(String[] s) {
            short[] invert = new short[256];
            short[] straight = new short[256];
            for (int i = 0; i < 256; i++) {
                invert[i] = (short) (255 - i);
                straight[i] = (short) i;
            }

            short[][] invertMap = divideChanels(straight, invert, s);
            negateOp = new LookupOp(new ShortLookupTable(0, invertMap), null);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        return negateOp.filter(canva, canva);
    }
}
