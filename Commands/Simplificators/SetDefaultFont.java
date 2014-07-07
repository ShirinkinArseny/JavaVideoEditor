package JVE.Commands.Simplificators;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SetDefaultFont extends Command {

    private String name, size;

    public SetDefaultFont(String[] s) {
        name=s[0];
        size=MathParser.prepareExpression(s[1]);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        Command.setDefaultFont(
                new Font(name, Font.PLAIN, parseTimedInt(size, normalisedTime, absoluteTime))
        );
        return canva;
    }
}
