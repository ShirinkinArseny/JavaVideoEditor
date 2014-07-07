package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;
import JVE.Parsers.SyntaxParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FillImage extends Command {

    private java.awt.image.BufferedImage image;
    private String[] params;

    public FillImage(String[] s) throws Exception {
        params=new String[s.length];
        for (int i=0; i<s.length; i++)
            if (i!=0)
            params[i]= MathParser.prepareExpression(s[i]);
        try {
            image= ImageIO.read(new File(s[0]));
        } catch (Exception e) {
            System.err.println("Failed to load image: "+s[0]);
            throw e;
        }
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {
        int dx=parseTimedInt(params[1], normalisedTime, absoluteTime);
        int dy=parseTimedInt(params[2], normalisedTime, absoluteTime);
        int w=parseTimedInt(params[3], normalisedTime, absoluteTime);
        int h=parseTimedInt(params[4], normalisedTime, absoluteTime);

        Graphics g=canva.getGraphics();

        for (int x=-dx; x< SyntaxParser.getW(); x+=w) {
            for (int y=-dy; y<SyntaxParser.getH(); y+=h) {
                g.drawImage(image, x, y, w, h, null);
            }
        }
        return canva;
    }
}
