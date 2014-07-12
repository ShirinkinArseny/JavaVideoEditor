package JVE;

import JVE.Network.*;
import JVE.Parsers.*;
import JVE.Rendering.Render;
import JVE.Rendering.Scene;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static JVE.Parsers.MathParser.getInjections;
import static JVE.Parsers.MathParser.runInjections;
import static JVE.Parsers.ParseUtils.exit;
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

    private static void cleanTempDirForIncludes() {
        cleanDirectory(new File(Main.tempDirForIncludes));
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

    public static void renderLocalScenes(ArrayList<Scene> scenes) throws Exception {
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

        Render.renderFromFrames_ffmpeg(Main.tempDir + "out.mp4");
    }

    public static void renderServerScenes(ArrayList<Scene> scenes) throws Exception {
        printMessage("Starting server work...");

        cleanTempDir();
        cleanTempDirForIncludes();

        final int[] number = {0};
        final int[] dutiesCount = {0};
        final int srcSize=scenes.size();

        new Server(port, c -> {

            if (!getInjections().equals("")) {
                c.sendMessage("INJECTION " + MathParser.getInjections());
                printMessage("Injections sent");
            }
            for (Macros m : Macros.getMacroses()) {
                c.sendMessage("MACROS " + m.getCode());
                printMessage("Macroses sent: "+m.getCode());
            }
            c.sendMessage("PREPARE " + SyntaxParser.getFPS() +
                    " " + SyntaxParser.getW() + " " + SyntaxParser.getH());
        }, c -> {
            printMessage("Client "+c.toString()+" disconnected, returning his task: "+c.getDuty());
            scenes.add(scenes.get(Integer.valueOf(c.getDuty())));
        }, (c, message) -> {

            printMessage("Incoming message: " + message);

            if (message.startsWith("DONE")) {
                if (number[0] < scenes.size()) {
                    printMessage("Starting send scene");
                    Scene copy = scenes.get(number[0]);

                    printMessage("Searching used files");
                    ArrayList<String> files = copy.getUsedFilesList();
                    printMessage("Files count: " + files.size());
                    if (!files.isEmpty()) {
                        zip(files, Main.tempDirForIncludes + "archive.zip");
                        printMessage("Stuff zipped");
                        c.sendFile(new File(Main.tempDirForIncludes + "archive.zip"));
                        printMessage("Stuff sent");
                    }

                    c.setDuty(String.valueOf(number[0]));
                    dutiesCount[0]++;
                    c.sendMessage("SCENE " + copy.getSource());
                    number[0]++;
                    printMessage("Scene sent: " + copy.getSource());
                    //todo: intelligent system
                    printMessage("Also: " + (scenes.size() - number[0]) + " scenes");
                } else {
                    if (dutiesCount[0] == 0) {
                        Render.renderFromVideos_ffmpeg(srcSize, "Out.mp4");
                        printMessage("All scenes are rendered");
                    }


                }
            }
        }, (c, f) -> {
            f.renameTo(new File(Main.tempDir + c.getDuty()+".mp4"));
            printMessage("MP4 named: " + c.getDuty()+".mp4");
            c.setDuty(null);
            dutiesCount[0]--;

            printMessage("MP4 catched");
        }, Main.tempDir);
    }

    public static void renderClientScenes() throws Exception {
        printMessage("Starting client work...");
        cleanTempDirForIncludes();
        ParseUtils.addPath(Main.tempDirForIncludes);
        new Client(ip, port, (c, message) -> {

                printMessage("Incoming message: " + message);

                if (message.startsWith("SCENE")) {
                    printMessage("Starting render scene");
                    cleanTempDir();
                    printMessage("Tempfolder cleaned");
                    String[] code = message.substring(6).split("\n");
                    ArrayList<String> src = new ArrayList<>();
                    Collections.addAll(src, code);
                    printMessage("Arguments divided");
                    Scene s = SceneBlockParser.parseSceneBlock(src);
                    printMessage("Scene parsed");
                    s.render(0);
                    printMessage("Scene has been rendered");
                    Render.renderFromFrames_ffmpeg("Scene.mp4");
                    c.sendFile(new File(Main.tempDir + "Scene.mp4"));
                    printMessage("MP4 sent");
                    c.sendMessage("DONE");
                    printMessage("Next task requested");
                } else if (message.startsWith("MACROS")) {
                    String[] commands = message.substring(7).split("\n");
                    ArrayList<String> macro = new ArrayList<>();
                    Collections.addAll(macro, commands);

                    printMessage("Macroses got: "+ Arrays.toString(commands));
                    try {
                        Macros.parseMacros(macro);
                        printMessage("Macro parsed");
                    } catch (Exception e) {
                        exit("Failed to parse macros");
                    }
                } else if (message.startsWith("INJECTION")) {
                    MathParser.addInjection(message.substring(9));
                    try {
                        runInjections();
                        printMessage("Injected");
                    } catch (Exception e) {
                        exit("Failed to parse js-injection");
                    }
                } else if (message.startsWith("PREPARE")) {
                    String[] values = message.split(" ");
                    SyntaxParser.setBases(MathParser.parseInt(values[1]),
                            MathParser.parseInt(values[2]), MathParser.parseInt(values[3]));
                    c.sendMessage("DONE");
                    printMessage("Prepared");
                }
        }, (c, f) -> {
                printMessage("Stuff catched");
                unzip(f, Main.tempDirForIncludes);
                printMessage("Stuff unzipped");
        }, Main.tempDirForIncludes);
    }

    public static void renderScenes(ArrayList<Scene> scenes) throws Exception {
        switch (render) {

            case Local: renderLocalScenes(scenes);
                break;
            case Server: renderServerScenes(scenes);
                break;
            case Client: renderClientScenes();
                break;
        }
    }

}
