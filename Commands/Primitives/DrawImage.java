package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static JVE.Parsers.ParseUtils.getFile;

public class DrawImage extends Command {

    private java.awt.image.BufferedImage image;
    private String[] params;

    public ArrayList<String> getUsedFilesList() {
        ArrayList<String> urls = new ArrayList<>();
        urls.add(params[0]);
        return urls;
    }

    private static String[] paramsLast = new String[]{"Filename.png", "0", "0", "200", "200"};

    public DrawImage(String[] s) throws Exception {

        params = new String[paramsLast.length];
        System.arraycopy(paramsLast, 0, params, 0, paramsLast.length);
        for (int i = 0; i < s.length; i++) {
            if (i != 0)
                params[i] = MathParser.prepareExpression(s[i]);
            else
                params[i] = s[i];
            paramsLast[i]=params[i];
        }
        try {
            image = ImageIO.read(getFile(s[0]));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + s[0]);
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
