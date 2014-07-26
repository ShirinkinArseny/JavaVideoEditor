package JVE.Commands.Effects;

import JVE.Commands.Command;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;

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
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        return negateOp.filter(canva, canva);
    }
}
