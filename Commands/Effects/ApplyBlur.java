package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ApplyBlur extends Command {

    private String size;
    private int lastSize=0;
    private ConvolveOp identity;

    public ApplyBlur(String[] s) {
        size = MathParser.prepareExpression(s[0]);
    }

    private float[] getKernel(int size) {
        //todo: chanels
        int size_2 = size * size;
        float[][] map = new float[size][size];
        float sizePer2 = size / 2f;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = Math.max(0, (float) (sizePer2 - Math.sqrt((i - sizePer2) * (i - sizePer2) + (j - sizePer2) * (j - sizePer2))));
            }
        }

        float sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sum += map[i][j];
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] /= sum;
            }
        }

        float[] out = new float[size_2];
        for (int i = 0; i < size_2; i++) {
            out[i] = map[i / size][i % size];
        }
        return out;
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        int size = parseTimedInt(this.size);
        if (size > 1) {
            if (size != lastSize) {
                identity = new ConvolveOp(new Kernel(size, size, getKernel(size)));
                lastSize = size;
            }
            return identity.filter(canva, null);
        }
        return canva;
    }
}
