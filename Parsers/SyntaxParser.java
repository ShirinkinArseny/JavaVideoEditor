package JVE.Parsers;

import JVE.Rendering.Scene;

import java.util.ArrayList;

import static JVE.Parsers.MathParser.addInjection;
import static JVE.Parsers.ParseUtils.exit;
import static JVE.Parsers.ParseUtils.getArguments;
import static JVE.Parsers.ParseUtils.printMessage;
import static JVE.Parsers.Preprocess.preprocess;
import static JVE.Parsers.SceneBlockParser.parseSceneBlock;
import static JVE.Parsers.VideoBlockParser.parseVideoBlock;
import static JVE.RenderServer.*;

public class SyntaxParser {

    public static int getW() {
        return w;
    }

    public static int getH() {
        return h;
    }

    public static int getFPS() {
        return fps;
    }

    private static int w = 800;
    private static int h = 600;
    private static int fps = 30;
    private static ArrayList<Scene> scenes = new ArrayList<>();

    public static void parse(String url) throws Exception {

        ArrayList<String> code = preprocess(url);

        for (int i = 0; i < code.size(); i++) {

            if (code.get(i).startsWith("\\frameRatio")) {
                fps = Integer.valueOf(getArguments(code.get(i))[0]);
                printMessage("FPS set to " + fps);
                continue;
            }

            if (code.get(i).startsWith("\\frameSize")) {
                String[] res = getArguments(code.get(i));
                w = Integer.valueOf(res[0]);
                h = Integer.valueOf(res[1]);
                printMessage("Width set to " + w + ", height set to " + h);
                continue;
            }

            if (code.get(i).startsWith("\\becomeServer")) {
                int port = Integer.valueOf(getArguments(code.get(i))[0]);
                printMessage("Becoming server, port: " + port);
                setPort(port);
                setRender(VideoFramesRenderType.Server);
                continue;
            }

            if (code.get(i).startsWith("\\becomeClient")) {
                String ip = getArguments(code.get(i))[0];
                int port = Integer.valueOf(getArguments(code.get(i))[1]);
                printMessage("Becoming client, port: " + port+", server ip: "+ip);
                setPort(port);
                setIP(ip);
                setRender(VideoFramesRenderType.Client);
                continue;
            }

            if (code.get(i).startsWith("\\addFilePath")) {
                String res = getArguments(code.get(i))[0];
                ParseUtils.addPath(res);
                printMessage("Add path: " + res);
                continue;
            }

            if (code.get(i).startsWith("\\injectJS")) {
                addInjection(getArguments(code.get(i))[0]);
                continue;
            }

            if (code.get(i).startsWith("\\begin{video}")) {
                ArrayList<String> s = new ArrayList<>();
                for (int j = i + 1; j < code.size(); j++) {
                    s.add(code.get(j));
                    if (code.get(j).startsWith("\\end{video}")) {
                        s.remove(s.size() - 1);
                        parseVideoBlock(s);
                        System.out.println("All scenes are parsed");
                        renderScenes(scenes, fps, w, h);
                        return;
                    }
                }
                exit("Video block is not closed");
                continue;
            }

            exit("Unknowable command in main block: [" + code.get(i) + "] Maybe it is placed wrong");
        }

    }

    static void addScene(ArrayList<String> code) throws Exception{
        scenes.add(parseSceneBlock(code));
    }

    public static void setBases(int fps, int w, int h) {
        SyntaxParser.fps=fps;
        SyntaxParser.w=w;
        SyntaxParser.h=h;
    }
}
