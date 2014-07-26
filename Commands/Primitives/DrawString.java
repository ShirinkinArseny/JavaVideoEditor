package JVE.Commands.Primitives;

import JVE.Commands.Command;
import JVE.Parsers.MathParser;
import JVE.Rendering.Scene;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;

public class DrawString extends Command {

    private String[] params;
    private String[] text;
    private static String[] paramsLast = new String[]{"Sample text", "0", "0", "DejaVu Sans", "24", "0", "0", "0", "255", "left"};
    private Font f;
    private enum Formatting {left, center, right, leftLimited, centerLimited, rightLimited}
    private Formatting currentFormatting;
    private boolean limited=false;
    private static final int maxFontSize=10000;
    private static final int minFontSize=4;

    private static Font setFontSize(Font d, FontRenderContext frc, String[] text, float width) {
        String longestText=text[0];
        for (String s: text)
        if (s.length()>longestText.length())  //todo: there will be bug
        //F.e. in some fonts "WWWWWWWWWW" will be longer, than "..........."
            longestText=s;

        for (float i=minFontSize; i<maxFontSize; i++) {
            double textwidth = d.deriveFont(i).getStringBounds(longestText, frc).getWidth();
            if (textwidth>width) {
                return d.deriveFont(i-1);
            }
        }
        return null;
    }

    private double getFontWidth(Font d, FontRenderContext frc, String text) {
        return d.getStringBounds(text, frc).getWidth();
    }

    public DrawString(String[] s) throws Exception {
        params = new String[paramsLast.length];
        System.arraycopy(paramsLast, 0, params, 0, paramsLast.length);

        for (int i = 0; i < s.length; i++)  {
            if (i != 0 && i != 3 && i!=9)
                params[i] = MathParser.prepareExpression(s[i]);
                else params[i] = s[i];
            paramsLast[i]=params[i];
            }

        text=params[0].split("\\n");

        switch (params[9]) {
            case "left":
                currentFormatting=Formatting.left;
                break;
            case "center":
                currentFormatting=Formatting.center;
                break;
            case "right":
                currentFormatting=Formatting.right;
                break;
            case "left-limited":
                currentFormatting=Formatting.leftLimited;
                limited=true;
                break;
            case "center-limited":
                currentFormatting=Formatting.centerLimited;
                limited=true;
                break;
            case "right-limited":
                currentFormatting=Formatting.rightLimited;
                limited=true;
                break;
            default:
                throw new Exception("Unknown text format parameter: " + params[9]);
        }

            if (limited)
                f = Font.decode(params[3]);
            else
                f = Font.decode(params[3]).deriveFont(MathParser.parseFloat(params[4]));

    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        Graphics2D g = (Graphics2D) canva.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(
                parseTimedInt(params[5]),
                parseTimedInt(params[6]),
                parseTimedInt(params[7]),
                parseTimedInt(params[8])
        ));

            float scale;
            if (Scene.getObeyProp())
                scale = Scene.getProp();
            else scale = 1f;

        if (!limited) {
            f=new Font(params[3], Font.PLAIN, (int) (MathParser.parseInt(params[4]) * scale));
            g.setFont(f);
        }
        else {
            f=setFontSize(f, g.getFontRenderContext(), text, MathParser.parseFloat(params[4]) * scale);
            g.setFont(f);
        }

        for (int i=0; i<text.length; i++) {

            float x = 0;
            switch (currentFormatting) {
                case left:
                    x = parseTimedInt(params[1]) * scale;
                    break;
                case center:
                    x = (float) (parseTimedInt(params[1]) * scale
                            - getFontWidth(f, g.getFontRenderContext(), text[i]) / 2);
                    break;
                case right:
                    x = (float) (parseTimedInt(params[1]) * scale
                            - getFontWidth(f, g.getFontRenderContext(), text[i]));
                    break;
                case leftLimited:
                    x = parseTimedInt(params[1]) * scale;
                    break;
                case centerLimited:
                    x = (float) (parseTimedInt(params[1]) * scale
                            - getFontWidth(f, g.getFontRenderContext(), text[i]) / 2);
                    break;
                case rightLimited:
                    x = (float) (parseTimedInt(params[1]) * scale
                            - getFontWidth(f, g.getFontRenderContext(), text[i]));
                    break;
            }

            g.drawString(
                    text[i], x,
                    (parseTimedInt(params[2])+f.getSize()*i) * scale
            );
        }
        return canva;
    }
}
