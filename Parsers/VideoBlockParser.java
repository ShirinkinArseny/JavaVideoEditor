package JVE.Parsers;

import JVE.Rendering.RenderEvent;

import java.util.ArrayList;

import static JVE.Parsers.Macros.parseMacros;
import static JVE.Parsers.Video.addScene;

public class VideoBlockParser {

    static void parseVideoBlock(ArrayList<String> code, RenderEvent r) throws Exception {
        for (int i = 0; i < code.size(); i++) {
            r.run(i*1f/code.size());

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
                if (!found) throw new Exception("Scene block is not closed");
                continue;
            }

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
                if (!found) throw new Exception("Macros block is not closed");
                continue;
            }

            throw new Exception("Unknowable command in video block: [" + code.get(i) + "] Maybe it is placed wrong");
        }
    }

}
