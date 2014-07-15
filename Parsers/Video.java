package JVE.Parsers;

import JVE.Rendering.RenderEvent;
import JVE.Rendering.Scene;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static JVE.Parsers.Macros.parseMacros;
import static JVE.Parsers.MathParser.addInjection;
import static JVE.Parsers.ParseUtils.exit;
import static JVE.Parsers.ParseUtils.getArguments;
import static JVE.Parsers.ParseUtils.printMessage;
import static JVE.Parsers.Preprocess.preprocess;
import static JVE.Parsers.SceneBlockParser.parseSceneBlock;
import static JVE.Parsers.VideoBlockParser.parseVideoBlock;
import static JVE.Rendering.RenderModes.*;

public class Video {

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
    private static int frames=0;
    private static ArrayList<Scene> scenes = new ArrayList<>();

    public Video(String url, RenderEvent r) throws Exception {
        MathParser.init();

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
                        parseVideoBlock(s, r);
                        for (Scene scene: scenes)
                            frames+=scene.getFrames();
                        System.out.println("All scenes are parsed");
                        return;
                    }
                }
                exit("Video block is not closed");
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
                if (!found) exit("Macros block is not closed");
                continue;
            }

            exit("Unknowable command in main block: [" + code.get(i) + "] Maybe it is placed wrong");
        }
    }

    public void render(RenderEvent change) throws Exception {
        renderScenes(scenes, change);
    }

    public BufferedImage render(float frameNumber) throws Exception {
        int num= (int) (frames*frameNumber);
        for (Scene scene : scenes) {
            if (num < scene.getFrames()) {
                return scene.render(num);
            } else num -= scene.getFrames();
        }
        return null;
    }

    public BufferedImage render(float frameNumber, int scenename) throws Exception {
        return scenes.get(scenename).render((int) (frameNumber*scenes.get(scenename).getFrames()));
    }

    public static String[] getSceneNames() {
        String[] names=new String[scenes.size()];
        for (int i=0; i<scenes.size(); i++) {
            names[i]=scenes.get(i).getName();
        }
        return names;
    }

    public static int getScenesCount() {
        return scenes.size();
    }

    static void addScene(ArrayList<String> code) throws Exception{
        scenes.add(parseSceneBlock(code));
    }

    public static void setBases(int fps, int w, int h) {
        Video.fps=fps;
        Video.w=w;
        Video.h=h;
    }
}
