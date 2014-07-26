package JVE.Rendering;

import JVE.Commands.Command;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SceneLayer {

    private ArrayList<Command> commands=new ArrayList<>();

    public BufferedImage render(BufferedImage toDraw) throws Exception {
        for (Command c: commands) {
                toDraw= c.doAction(toDraw);
            }
        return toDraw;
    }

    public void addCommand(Command c) {
        commands.add(c);
    }


}
