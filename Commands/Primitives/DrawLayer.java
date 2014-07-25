package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.Video;
import JVE.Rendering.Scene;
import JVE.Rendering.SceneLayer;

import java.awt.image.BufferedImage;

public class DrawLayer extends Command {

    private SceneLayer layer;

    public DrawLayer(SceneLayer l) throws Exception {
        layer = l;
        //todo: configurable layer size (to not crop out-of-screen stuff)
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        BufferedImage f;
        if (Scene.getObeyProp())
        f = new BufferedImage((int) (Video.getW()*Scene.getProp()), (int) (Video.getH()*Scene.getProp()),
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        else
            f = new BufferedImage(Video.getW(), Video.getH(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        f = layer.render(f, normalisedTime, absoluteTime);
        canva.getGraphics().drawImage(f, 0, 0, null);
        return canva;
    }
}
