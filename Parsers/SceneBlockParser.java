package JVE.Parsers;

import JVE.Commands.Primitives.DrawLayer;
import JVE.Rendering.Scene;

import java.util.ArrayList;

import static JVE.Parsers.LayerBlockParser.parseLayerBlock;
import static JVE.Parsers.MathParser.parseFloat;
import static JVE.Utils.getArguments;

public class SceneBlockParser {

    public static Scene parseSceneBlock(ArrayList<String> code, int startFrame, float startSec) throws Exception {

        Scene s = new Scene(startFrame, startSec);
        ArrayList<String> codeClone=new ArrayList<>();
        codeClone.addAll(code);
            if (codeClone.get(0).startsWith("\\duration")) {
                s.setDuration(parseFloat(getArguments(codeClone.get(0))[0]));
                codeClone.remove(0);
            } else
                throw new Exception("Command before setting duration: [" + codeClone.get(0) + "] ");

            if (codeClone.get(0).startsWith("\\name")) {
                s.setName(getArguments(codeClone.get(0))[0]);
                codeClone.remove(0);
            }
            else s.setName("SCENE "+Video.getScenesCount());

            s.addCommand(new DrawLayer(parseLayerBlock(codeClone)));
        s.setSource(code);
        return s;
    }

}
