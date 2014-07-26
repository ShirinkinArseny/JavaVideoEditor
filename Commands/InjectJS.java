package JVE.Commands;

import JVE.Parsers.MathParser;

import java.awt.image.BufferedImage;

public class InjectJS extends Command {

    private String code;

    public InjectJS(String[] s) {
        code = MathParser.prepareExpression(s[0]);
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        MathParser.injectCode(code);
        return canva;
    }
}
