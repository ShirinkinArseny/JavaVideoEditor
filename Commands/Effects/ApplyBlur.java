package JVE.Commands.Effects;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.image.*;

public class ApplyBlur extends Command {

    private String size;

    public ApplyBlur(String[] s) {
        size = MathParser.prepareExpression(s[0]);
    }

    private float[] getKernel(int size) {
        int size_2 = size * size;
        float[][] map = new float[size][size];
        float sizePer2 = size / 2f;
        float maxLength = sizePer2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = Math.max(0, (float) (maxLength - Math.sqrt((i - sizePer2) * (i - sizePer2) + (j - sizePer2) * (j - sizePer2))));
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
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {

        int size = parseTimedInt(this.size, normalisedTime, absoluteTime);
        if (size > 1) {
            float[] identityKernel = getKernel(size);
            BufferedImageOp identity =
                    new ConvolveOp(new Kernel(size, size, identityKernel));
            return identity.filter(canva, null);
        }
        return canva;
    }
}
