package JVE.Rendering;

import JVE.Commands.Primitives.DrawImage;
import JVE.Network.Client;
import JVE.Network.Server;
import JVE.Parsers.Macros;
import JVE.Parsers.MathParser;
import JVE.Parsers.SceneBlockParser;
import JVE.Parsers.Video;
import JVE.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static JVE.Parsers.MathParser.getInjections;
import static JVE.Parsers.MathParser.runInjections;
import static JVE.Utils.*;

public class RenderModes {


    public static enum VideoFramesRenderType {Local, Server, Client}

    public static VideoFramesRenderType getType() {
        return render;
    }

    private static VideoFramesRenderType render=VideoFramesRenderType.Local;
    private static int port;
    private static String ip;

    public static boolean getNeedTranslating() {
        return render!=VideoFramesRenderType.Server;
    }

    public static void setIP(String ip) {
        RenderModes.ip=ip;
    }

    public static void setPort(int port) {
        RenderModes.port=port;
    }

    public static void setRender(VideoFramesRenderType render) {
        RenderModes.render=render;
    }


    private static void cleanTempDir() throws Exception {
        cleanDirectory(new File(Video.getTempDir()));
    }

    private static void cleanTempDirForIncludes() throws Exception {
        cleanDirectory(new File(Video.getTempDirForIncludes()));
    }

    public static void renderLocalScenes(ArrayList<Scene> scenes, RenderEvent changes) throws Exception {
        cleanTempDir();
        runInjections();

        int framesCount = 0;
        int summary = 0;
        int scenesDone = 0;

        for (Scene s : scenes) {
            summary += s.getFrames();
        }

        changes.run(0);
        for (Scene s : scenes) {
            s.renderAndSave(changes);
            framesCount += s.getFrames();
            scenesDone++;
            Utils.printMessage("Framerender: ready " + framesCount + "/" + summary + " frames, " + scenesDone + "/" + scenes.size() + " scenes");
            changes.run(framesCount*1f/summary);
        }

        FFMpegUsage.renderFromFrames_ffmpeg(Video.getTempDir() + "out.mp4");
    }

    public static void renderServerScenes(ArrayList<Scene> scenes, RenderEvent changes) throws Exception {
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

            printMessage("Searching used files");
            ArrayList<String> files = DrawImage.getUsedFilesList();
            printMessage("Files count: " + files.size());
            if (!files.isEmpty()) {
                zip(files, Video.getTempDirForIncludes() + "archive.zip");
                printMessage("Stuff zipped");
                c.sendFile(new File(Video.getTempDirForIncludes() + "archive.zip"));
                printMessage("Stuff sent");
            }

            c.sendMessage("PREPARE " + Video.getFPS() +
                    " " + Video.getW() + " " + Video.getH());
        }, c -> {
            printMessage("Client "+c.toString()+" disconnected, returning his task: "+c.getDuty());
            scenes.add(scenes.get(Integer.valueOf(c.getDuty())));
        }, (c, message) -> {

            printMessage("Incoming message: " + message);

            if (message.startsWith("DONE")) {
                if (number[0] < scenes.size()) {
                    printMessage("Starting send scene");
                    Scene copy = scenes.get(number[0]);

                    c.setDuty(String.valueOf(number[0]));
                    dutiesCount[0]++;
                    c.sendMessage("SCENE " + copy.getSource());
                    number[0]++;
                    printMessage("Scene sent: " + copy.getSource());
                    printMessage("Remaining " + (scenes.size() - number[0]) + " scenes");
                } else {
                    if (dutiesCount[0] == 0) {
                        FFMpegUsage.renderFromVideos_ffmpeg(srcSize, "Out.mp4");
                        printMessage("All scenes are rendered");
                    }


                }
            }
        }, (c, f) -> {
            f.renameTo(new File(Video.getTempDir() + c.getDuty()+".mp4"));
            printMessage("MP4 named: " + c.getDuty()+".mp4");
            c.setDuty(null);
            dutiesCount[0]--;

            printMessage("MP4 catched");
        }, Video.getTempDir());
    }

    public static void renderClientScenes(RenderEvent changes) throws Exception {
        printMessage("Starting client work...");
        cleanTempDirForIncludes();
        Utils.addPath(Video.getTempDirForIncludes());
        new Client(ip, port, (c, message) -> {

                printMessage("Incoming message: " + message);

                if (message.startsWith("SCENE")) {
                    printMessage("Starting renderAndSave scene");
                    cleanTempDir();
                    printMessage("Tempfolder cleaned");
                    String[] code = message.substring(6).split("\n");
                    ArrayList<String> src = new ArrayList<>();
                    Collections.addAll(src, code);
                    printMessage("Arguments divided");
                    //todo: starttime
                    Scene s = SceneBlockParser.parseSceneBlock(src, 0, 0);
                    printMessage("Scene parsed");
                    s.renderAndSave(changes);
                    printMessage("Scene has been rendered");
                    FFMpegUsage.renderFromFrames_ffmpeg("Scene.mp4");
                    c.sendFile(new File(Video.getTempDir() + "Scene.mp4"));
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
                        throw new Exception("Failed to parse macros: "+macro);
                    }
                } else if (message.startsWith("INJECTION")) {
                    MathParser.addInjection(message.substring(9));
                    try {
                        runInjections();
                        printMessage("Injected");
                    } catch (Exception e) {
                        throw new Exception("Failed to inject js: "+getInjections());
                    }
                } else if (message.startsWith("PREPARE")) {
                    String[] values = message.split(" ");
                    Video.setBases(MathParser.parseInt(values[1]),
                            MathParser.parseInt(values[2]), MathParser.parseInt(values[3]));
                    c.sendMessage("DONE");
                    printMessage("Prepared");
                }
        }, (c, f) -> {
                printMessage("Stuff catched");
                unzip(f, Video.getTempDirForIncludes());
                printMessage("Stuff unzipped");
        }, Video.getTempDirForIncludes());
    }

    public static void renderScenes(ArrayList<Scene> scenes, RenderEvent changes) throws Exception {
        switch (render) {

            case Local: renderLocalScenes(scenes, changes);
                break;
            case Server: renderServerScenes(scenes, changes);
                break;
            case Client: renderClientScenes(changes);
                break;
        }
    }

}
