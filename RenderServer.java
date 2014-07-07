package JVE;

import JVE.Network.Server;
import JVE.Rendering.Render;
import JVE.Rendering.Scene;

import java.io.File;
import java.util.ArrayList;

public class RenderServer {


    public static enum VideoFramesRenderType {Local, Server, Client}

    private static VideoFramesRenderType render=VideoFramesRenderType.Local;
    private static int port;
    private static String ip;

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
        cleanTempDir();

        Server server=new Server(port);


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

    public static void renderScenes(ArrayList<Scene> scenes, int fps, int w, int h) throws Exception {
        switch (render) {

            case Local: renderLocalScenes(scenes, fps, w, h);
                break;
            case Server: renderServerScenes(scenes, fps, w, h);
                break;
            case Client:
                break;
        }
    }

}
