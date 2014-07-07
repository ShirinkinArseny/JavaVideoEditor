package JVE.Parsers;

import java.util.ArrayList;

import static JVE.Parsers.Macros.parseMacros;
import static JVE.Parsers.ParseUtils.exit;
import static JVE.Parsers.SyntaxParser.addScene;

public class VideoBlockParser {

    static void parseVideoBlock(ArrayList<String> code) throws Exception {
        for (int i = 0; i < code.size(); i++) {

            if (code.get(i).startsWith("\\begin{macros}")) {

                ArrayList<String> s = new ArrayList<>();
                boolean found = false;
                for (int j = i + 1; j < code.size(); j++) {
                    s.add(code.get(j));
                    if (code.get(j).startsWith("\\end{macros}")) {
                        s.remove(s.size() - 1);
                        parseMacros(s);
                        i = j;
                        found = true;
                        break;
                    }
                }
                if (!found) exit("Macros block is not closed");
                continue;
            }

            if (code.get(i).startsWith("\\begin{scene}")) {

                ArrayList<String> s = new ArrayList<>();
                boolean found = false;
                for (int j = i + 1; j < code.size(); j++) {
                    s.add(code.get(j));
                    if (code.get(j).startsWith("\\end{scene}")) {
                        s.remove(s.size() - 1);
                        addScene(s);
                        i = j;
                        found = true;
                        break;
                    }
                }
                if (!found) exit("Scene block is not closed");
                continue;
            }

            exit("Unknowable command in video block: [" + code.get(i) + "] Maybe it is placed wrong");
        }
    }

}
