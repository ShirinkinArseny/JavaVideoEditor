package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;
import JVE.Utils;
import JVE.Rendering.RenderModes;
import JVE.Rendering.Scene;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static JVE.Utils.getFile;

public class DrawImage extends Command {

    private int image;
    private String[] params;
    private static ArrayList<String> urls;
    private static ArrayList<BufferedImage> images;

    public static void init() {
        urls=new ArrayList<>();
        images=new ArrayList<>();
    }

    public static ArrayList<String> getUsedFilesList() {
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
            int alreadyHere=urls.indexOf(s[0]);
            if (alreadyHere==-1) {
                urls.add(s[0]);
                if (RenderModes.getType()!= RenderModes.VideoFramesRenderType.Server) {
                images.add(ImageIO.read(getFile(s[0])));
                Utils.printMessage("Loaded image: " + s[0]);  }
                else
                    Utils.printMessage("Added to URL list: " + s[0]);
                image =images.size()-1;
            }
            else
                image =alreadyHere;
        } catch (Exception e) {         
            throw new Exception("Failed to load image: " + s[0]);
        }
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {

        if (Scene.getObeyProp())
        canva.getGraphics().drawImage(images.get(image),
                (int) (parseTimedInt(params[1])*Scene.getProp()),
                (int) (parseTimedInt(params[2])*Scene.getProp()),
                (int) (parseTimedInt(params[3])*Scene.getProp()),
                (int) (parseTimedInt(params[4])*Scene.getProp()),
                null, null
        );
        else
            canva.getGraphics().drawImage(images.get(image),
                    parseTimedInt(params[1]),
                    parseTimedInt(params[2]),
                    parseTimedInt(params[3]),
                    parseTimedInt(params[4]),
                    null, null
            );
        return canva;
    }
}
