package JVE.Parsers;

import JVE.Commands.Primitives.DrawLayer;
import JVE.Rendering.Scene;

import java.util.ArrayList;

import static JVE.Parsers.MathParser.parseFloat;
import static JVE.Parsers.LayerBlockParser.parseLayerBlock;
import static JVE.Parsers.ParseUtils.exit;
import static JVE.Parsers.ParseUtils.getArguments;
import static JVE.Parsers.SyntaxParser.getFPS;
import static JVE.Parsers.SyntaxParser.getH;
import static JVE.Parsers.SyntaxParser.getW;
import static JVE.RenderServer.getNeedTranslating;

public class SceneBlockParser {

    public static Scene parseSceneBlock(ArrayList<String> code) throws Exception {
        Scene s = new Scene(getFPS(), getW(), getH());
        ArrayList<String> codeClone=new ArrayList<>();
        codeClone.addAll(code);
            if (code.get(0).startsWith("\\duration")) {
                s.setDuration(parseFloat(getArguments(code.get(0))[0]));
                codeClone.remove(0);
            } else
                exit("Command before setting duration: [" + code.get(0) + "] ");

            s.addCommand(new DrawLayer(parseLayerBlock(codeClone)));
        s.setSource(code);
        return s;
    }

}
