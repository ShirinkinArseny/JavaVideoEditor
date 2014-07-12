package JVE.Commands;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import java.awt.image.BufferedImage;

public class InjectJS extends Command {

    private String code;

    public InjectJS(String[] s) {
        code = MathParser.prepareExpression(s[0]);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        MathParser.injectCode(setTimes(code, normalisedTime, absoluteTime));
        return canva;
    }
}
