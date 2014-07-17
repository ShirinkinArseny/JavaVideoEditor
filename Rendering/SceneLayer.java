package JVE.Rendering;

import JVE.Commands.Command;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SceneLayer {

    private ArrayList<Command> commands=new ArrayList<>();

    public BufferedImage render(BufferedImage toDraw, float timeNormalised, float absoluteTime) throws Exception {

        for (Command c: commands) {
                toDraw= c.doAction(toDraw, timeNormalised, absoluteTime);
            }
        return toDraw;
    }

    public void addCommand(Command c) {
        commands.add(c);
    }

    public SceneLayer() {
    }

}
