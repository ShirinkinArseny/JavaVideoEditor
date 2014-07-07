package JVE.Parsers;

import JVE.Rendering.SceneLayer;

import java.util.ArrayList;

import static JVE.Parsers.LayerBlockParser.parseLayerBlock;
import static JVE.Parsers.ParseUtils.exit;
import static JVE.Parsers.ParseUtils.getArguments;

public class Macros {

    private String name;
    private String[] params;
    private ArrayList<String> code;

    public Macros(String name, String[] params, ArrayList<String> code) {
        this.name=name;
        this.params=params;
        this.code=code;
    }

    public static SceneLayer useMacros(String[] args) throws Exception {
        Macros m = null;
        for (Macros m2: macroses) {
            if (m2.name.equals(args[0]))
            {
                m=m2;
                break;
            }
        }
        if (m==null)
            exit("No macros with this name: "+args[0]);

        ArrayList<String> code2=new ArrayList<>();


        for (String s: m.code) {
            for (int i = 0; i < m.params.length; i++) {
                s=s.replaceAll(m.params[i], args[i+1]);
            }
            code2.add(s);
        }
        return parseLayerBlock(code2);
    }

    private static ArrayList<Macros> macroses=new ArrayList<>();

    public static void parseMacros(ArrayList<String> code) throws Exception {
        String name = null;
        if (code.get(0).startsWith("\\name")) {
            name=getArguments(code.get(0))[0];
            code.remove(0);
        } else
            exit("Command before setting name: [" + code.get(0) + "] ");
        String[] params = new String[0];
        if (code.get(0).startsWith("\\params")) {
            params=getArguments(code.get(0));
            code.remove(0);
        } else
            exit("Command before setting arguments: [" + code.get(0) + "] ");
        macroses.add(new Macros(name, params, code));
    }

}
