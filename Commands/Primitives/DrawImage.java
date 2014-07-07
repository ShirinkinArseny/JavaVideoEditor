package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static JVE.Parsers.ParseUtils.getFile;

public class DrawImage extends Command {

    private java.awt.image.BufferedImage image;
    private String[] params;

    public DrawImage(String[] s) throws Exception {

        params=new String[s.length];
        for (int i=0; i<s.length; i++)
            if (i!=0)
            params[i]= MathParser.prepareExpression(s[i]);
        try {
            image= ImageIO.read(getFile(s[0]));
        } catch (Exception e) {
            System.err.println("Failed to load image: "+s[0]);
            throw e;
        }
    }

    @Override
    public BufferedImage doAction(BufferedImage canva, float normalisedTime, float absoluteTime) throws Exception {

        canva.getGraphics().drawImage(image,
                parseTimedInt(params[1], normalisedTime, absoluteTime),
                parseTimedInt(params[2], normalisedTime, absoluteTime),
                parseTimedInt(params[3], normalisedTime, absoluteTime),
                parseTimedInt(params[4], normalisedTime, absoluteTime),
                null, null
                );
        return canva;
    }
}
