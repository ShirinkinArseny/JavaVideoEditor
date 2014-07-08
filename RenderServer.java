package JVE;

import JVE.Network.Client;
import JVE.Network.Server;
import JVE.Parsers.Macros;
import JVE.Parsers.MathParser;
import JVE.Parsers.SceneBlockParser;
import JVE.Rendering.Render;
import JVE.Rendering.Scene;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static JVE.Parsers.MathParser.getInjections;
import static JVE.Parsers.MathParser.runInjections;
import static JVE.Parsers.ParseUtils.exit;
import static JVE.Parsers.ParseUtils.getFiles;
import static JVE.Parsers.ParseUtils.printMessage;
import static JVE.ZipIO.*;

public class RenderServer {


    public static enum VideoFramesRenderType {Local, Server, Client}

    private static VideoFramesRenderType render=VideoFramesRenderType.Local;
    private static int port;
    private static String ip;

    public static boolean getNeedTranslating() {
        return render!=VideoFramesRenderType.Server;
    }

    public static void setIP(String ip) {
        RenderServer.ip=ip;
    }

    public static void setPort(int port) {
        RenderServer.port=port;
    }

    public static void setRender(VideoFramesRenderType render) {
        RenderServer.render=render;
    }


    private static void cleanTempDir() {
        cleanDirectory(new File(Main.tempDir));
    }

    private static void cleanDirectory(File dir) {
        String[] files = dir.list();
        for (String file : files) {
            File f = new File(dir, file);
            if (f.isDirectory()) {
                cleanDirectory(f);
            } else {
                f.delete();
            }
        }
    }

    public static void renderLocalScenes(ArrayList<Scene> scenes, int fps, int w, int h) throws Exception {
        cleanTempDir();
        runInjections();

        int framesCount = 0;
        int summary = 0;
        int scenesDone = 0;

        for (Scene s : scenes) {
            summary += s.getFrames();
        }

        for (Scene s : scenes) {
            s.render(framesCount);
            framesCount += s.getFrames();
            scenesDone++;
            System.out.println("Framerender: ready " + framesCount + "/" + summary + " frames, " + scenesDone + "/" + scenes.size() + " scenes");
        }


        Render.render_ffmpeg(fps, w, h, Main.tempDir + "out.mp4");
    }


    public static void renderServerScenes(ArrayList<Scene> scenes, int fps, int w, int h) throws Exception {
        printMessage("Starting server work...");

        cleanTempDir();

        ArrayList<Scene> scenesClone=new ArrayList<>();
        for (Scene s: scenes)
        scenesClone.add(s);

        Server server=new Server(port);
        final String[] command = {""};
        server.setOnInputEvent((c, message) -> {
            if (message.startsWith("END")) {
                if (command[0].startsWith("CONNECTED")) {
                    if (!getInjections().equals("")) {
                        c.sendMessage("INJECTION " + MathParser.getInjections());
                        c.sendMessage("END");
                    }
                    for (Macros m : Macros.getMacroses()) {
                        c.sendMessage("MACROS " + m.getCode());
                        c.sendMessage("END");
                    }
                    c.sendMessage("PREPARE");
                    c.sendMessage("END");
                    command[0]="";
                } else if (command[0].startsWith("REQUEST")) {

                    command[0]="";
                } else if (command[0].startsWith("FRAME")) {
                    String base64 = message.substring(5);
                    File fromBase = fromBase64(base64, "/home/nameless/fromBase.jzf");
                    unzip(fromBase, Main.tempDir);
                    command[0]="";
                    c.sendMessage("END");
                } else if (command[0].startsWith("DONE")) {
                    c.sendMessage("SCENE " + scenesClone.get(0).getSource());
                    c.sendMessage("END");
                    scenesClone.remove(0);
                    //todo: intelligent system
                    command[0]="";
                }
            }
            else command[0]+=message+"\n";
        });
    }

    public static void renderClientScenes(ArrayList<Scene> scenes, int fps, int w, int h) throws Exception {
        printMessage("Starting client work...");
        final String[] command = {""};
        Client client =new Client(ip, port, (c, message) -> {
            System.out.println("START CALCING");
            if (message.startsWith("END")) {
                if (command[0].startsWith("SCENE")) {
                    cleanTempDir();
                    String[] code = message.substring(5).split("\n");
                    ArrayList<String> src = new ArrayList<>();
                    Collections.addAll(src, code);
                    Scene s = SceneBlockParser.parseSceneBlock(src);
                    s.render(0);

                    ArrayList<File> files = getFiles(new File(Main.tempDir));
                    zip(files, Main.tempDir + "archive.jzf");
                    String base64 = toBase64(new File("/home/nameless/archive.jzf"));
                    c.sendMessage("FRAME " + base64);
                    c.sendMessage("END");
                    c.sendMessage("DONE");
                    c.sendMessage("END");
                } else if (command[0].startsWith("MACROS")) {
                    String[] commands = message.substring(6).split("\n");
                    ArrayList<String> macro = new ArrayList<>();
                    Collections.addAll(macro, commands);
                    try {
                        Macros.parseMacros(macro);
                    } catch (Exception e) {
                        exit("Failed to parse macros");
                    }
                } else if (command[0].startsWith("INJECTION")) {
                    MathParser.addInjection(message.substring(9));
                    try {
                        runInjections();
                    } catch (Exception e) {
                        exit("Failed to parse js-injection");
                    }
                } else if (command[0].startsWith("PREPARE")) {
                    c.sendMessage("DONE");
                    c.sendMessage("END");
                }
            }
            else command[0] +=message+"\n";
        });
        client.sendMessage("CONNECTED");
    }

    public static void renderScenes(ArrayList<Scene> scenes, int fps, int w, int h) throws Exception {
        switch (render) {

            case Local: renderLocalScenes(scenes, fps, w, h);
                break;
            case Server: renderServerScenes(scenes, fps, w, h);
                break;
            case Client: renderClientScenes(scenes, fps, w, h);
                break;
        }
    }

}
