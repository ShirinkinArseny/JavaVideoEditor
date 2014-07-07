package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.SyntaxParser;

import java.awt.image.BufferedImage;
import java.util.Random;

import static JVE.Parsers.MathParser.prepareExpression;

public class DrawNoise extends Command {

    private String count;
    private Random rnd;

    public DrawNoise(String[] args) throws Exception {
        count=prepareExpression(args[0]);
        rnd=new Random();
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        int limit=parseTimedInt(count, normalisedTime, absoluteTime);
        for (int i=0; i<limit; i++) {
            int x=rnd.nextInt(SyntaxParser.getW());
            int y=rnd.nextInt(SyntaxParser.getH());
            canva.setRGB(x, y, rnd.nextInt(Integer.MAX_VALUE));
        }
        return canva;
    }
}
