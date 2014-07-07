package JVE.Parsers;

import JVE.Commands.Effects.*;
import JVE.Commands.Primitives.*;
import JVE.Commands.Simplificators.InjectJS;
import JVE.Commands.Simplificators.SetDefaultColor;
import JVE.Commands.Simplificators.SetDefaultFont;
import JVE.Rendering.SceneLayer;

import java.util.ArrayList;

import static JVE.Parsers.Macros.useMacros;
import static JVE.Parsers.ParseUtils.exit;
import static JVE.Parsers.ParseUtils.getArguments;

public class LayerBlockParser {

    static SceneLayer parseLayerBlock(ArrayList<String> code) throws Exception {

        SceneLayer s = new SceneLayer();

        for (int i = 0; i < code.size(); i++) {

            if (code.get(i).startsWith("\\begin{layer}")) {
                ArrayList<String> l = new ArrayList<>();
                boolean found = false;
                for (int j = i + 1; j < code.size(); j++) {
                    l.add(code.get(j));
                    if (code.get(j).startsWith("\\end{layer}")) {
                        l.remove(l.size() - 1);
                        SceneLayer l2 = parseLayerBlock(l);
                        s.addCommand(new DrawLayer(l2));
                        i = j;
                        found = true;
                        break;
                    }
                }
                if (!found) exit("Layer block is not closed");
                continue;
            }

            String command=code.get(i).substring(0, code.get(i).replace(" {", "{").indexOf('{'));

            switch (command) {
                case "\\injectJS": s.addCommand(new InjectJS(getArguments(code.get(i))));
                    break;
                case "\\useMacros": s.addCommand(new DrawLayer(useMacros(getArguments(code.get(i)))));
                    break;
                case "\\drawRectangle": s.addCommand(new DrawRectangle(getArguments(code.get(i))));
                    break;
                case "\\drawString":
                    s.addCommand(new DrawString(getArguments(code.get(i))));
                    break;
                case "\\drawNoise":
                    s.addCommand(new DrawNoise(getArguments(code.get(i))));
                    break;
                case "\\drawImage":
                    s.addCommand(new DrawImage(getArguments(code.get(i))));
                    break;
                case "\\fillImage":
                    s.addCommand(new FillImage(getArguments(code.get(i))));
                    break;
                case "\\setCurrentColor":
                    s.addCommand(new SetDefaultColor(getArguments(code.get(i))));
                    break;
                case "\\setCurrentFont":
                    s.addCommand(new SetDefaultFont(getArguments(code.get(i))));
                    break;
                case "\\blur":
                    s.addCommand(new ApplyBlur(getArguments(code.get(i))));
                    break;
                case "\\circuit":
                    s.addCommand(new ApplyContur());
                    break;
                case "\\negate":
                    s.addCommand(new ApplyNegate(getArguments(code.get(i))));
                    break;
                case "\\multiply":
                    s.addCommand(new ApplyMultiply(getArguments(code.get(i))));
                    break;
                case "\\add":
                    s.addCommand(new ApplyAdd(getArguments(code.get(i))));
                    break;
                case "\\setColor":
                    s.addCommand(new ApplySetColor(getArguments(code.get(i))));
                    break;
                case "\\setParametricColor":
                    s.addCommand(new ApplyParametricSetColor(getArguments(code.get(i))));
                    break;
                case "\\scale":
                    s.addCommand(new ApplyScale(getArguments(code.get(i))));
                    break;
                case "\\move":
                    s.addCommand(new ApplyMove(getArguments(code.get(i))));
                    break;
                default:
                    exit("Unknowable command in layer block: [" + code.get(i) + "] Maybe it is placed wrong");
            }

        }
        return s;
    }

}
