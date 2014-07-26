package JVE.Commands.Effects;

import JVE.Commands.Command;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ApplyContur extends Command {

    private static float[] identityKernel = {
            0.0f, -1.0f, 0.0f,
            -1.0f, 4.0f, -1.0f,
            0.0f, -1.0f, 0.0f
    };
    private static BufferedImageOp identity =
            new ConvolveOp(new Kernel(3, 3, identityKernel));

    public ApplyContur() {
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        return identity.filter(canva, null);
    }
}
