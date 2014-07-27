package JVE.Parsers;

import JVE.Rendering.SceneLayer;

import java.util.ArrayList;

import static JVE.Parsers.LayerBlockParser.parseLayerBlock;
import static JVE.Utils.getArguments;

public class Macros {

    private String name;
    private String[] params;
    private ArrayList<String> code;
    private String source;

    public String getCode() {
        return source;
    }

    public Macros(String name, String[] params, ArrayList<String> code, ArrayList<String> src) throws Exception {
        this.name=name;
        this.params=params;
        this.code=code;
        source="";
        for (String s: src)
            source+=s+"\n";
        source=source.substring(0, source.length()-1);
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
            throw new Exception("No macros with this name: "+args[0]);

        ArrayList<String> code2=new ArrayList<>();


        for (String s: m.code) {
            for (int i = 0; i < m.params.length; i++) {
                try {
                    s = s.replaceAll(m.params[i], args[i + 1]);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    throw new Exception("Wrong parameters number in command "+s);
                }
            }
            code2.add(s);
        }
        return parseLayerBlock(code2);
    }

    private static ArrayList<Macros> macroses=new ArrayList<>();

    public static ArrayList<Macros> getMacroses() {
        return macroses;
    }

    public static void parseMacros(ArrayList<String> code2) throws Exception {
        ArrayList<String> code=new ArrayList<>();
        for (String s: code2)
        code.add(s);

            String name;
            if (code.get(0).startsWith("\\name")) {
                name = getArguments(code.get(0))[0];
                code.remove(0);
            } else
                throw new Exception("Command before setting name: [" + code.get(0) + "] ");
            String[] params;
            if (code.get(0).startsWith("\\params")) {
                params = getArguments(code.get(0));
                code.remove(0);
            } else
                throw new Exception("Command before setting arguments: [" + code.get(0) + "] ");
            macroses.add(new Macros(name, params, code, code2));

    }

    public static void cleanMacroses() {
        macroses.clear();
    }

}
