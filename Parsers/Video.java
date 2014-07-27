package JVE.Parsers;

import JVE.Commands.Primitives.DrawImage;
import JVE.Rendering.RenderEvent;
import JVE.Rendering.Scene;
import JVE.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static JVE.Parsers.Macros.parseMacros;
import static JVE.Parsers.MathParser.addInjection;
import static JVE.Parsers.Preprocess.preprocess;
import static JVE.Parsers.VideoBlockParser.parseVideoBlock;
import static JVE.Rendering.RenderModes.*;
import static JVE.Utils.*;

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

    public static String getAudio() {
        return audio;
    }

    public static int getFrames() {
        return frames;
    }

    private static int w = 800;
    private static int h = 600;
    private static int fps = 30;
    private static int frames;
    private static String audio=null;
    private static ArrayList<Scene> scenes;

    private static String tempDir=null;
    private static String tempDirForIncludes=null;

    public static void setTempDir(String dir) throws Exception {
        tempDir=dir;
        if (!tempDir.endsWith("/"))
            tempDir+='/';
        tempDirForIncludes=tempDir+"includes/";
        cleanDirectory(new File(tempDir));
        cleanDirectory(new File(tempDirForIncludes));
    }

    public static String getTempDir() {
        return tempDir;
    }

    public static String getTempDirForIncludes() {
        return tempDirForIncludes;
    }

    public Video(String url, RenderEvent r) throws Exception {
        MathParser.init();
        DrawImage.init();
        Utils.cleanPathes();
        scenes = new ArrayList<>();
        frames=0;

        ArrayList<String> code = preprocess(url);

        for (int i = 0; i < code.size(); i++) {

            //todo plugins mechanism

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
                Utils.addPath(res);
                printMessage("Add path: " + res);
                continue;
            }

            if (code.get(i).startsWith("\\audio")) {
                audio = getArguments(code.get(i))[0];
                printMessage("Audio set to " + audio);
                continue;
            }

            if (code.get(i).startsWith("\\tempDir")) {
                String tmpDir=getArguments(code.get(i))[0];
                setTempDir(tmpDir);
                printMessage("Temp directory set to " + tmpDir);
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
                        Utils.printMessage("All scenes are parsed");
                        return;
                    }
                }
                throw new Exception("Video block is not closed");
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

            throw new Exception("Unknowable command in main block: [" + code.get(i) + "] Maybe it is placed wrong");
        }
    }

    public void render(RenderEvent change) throws Exception {
        renderScenes(scenes, change);
    }

    public BufferedImage render(float frameNumber, int scenename, float prop) throws Exception {
        if (scenename==-1) {
            int num= (int) (frames*frameNumber);
            for (Scene scene : scenes) {
                if (num < scene.getFrames()) {
                    return scene.render(num, prop);
                } else num -= scene.getFrames();
            }
            return null;
        }
        return scenes.get(scenename).render((int) (frameNumber*scenes.get(scenename).getFrames()), prop);
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

    static void addScene(Scene s) throws Exception{
        scenes.add(s);
    }

    public static void setBases(int fps, int w, int h) {
        Video.fps=fps;
        Video.w=w;
        Video.h=h;
    }
}
