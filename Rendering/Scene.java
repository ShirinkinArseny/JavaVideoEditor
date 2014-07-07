package JVE.Rendering;

import JVE.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Scene extends SceneLayer {

    private float fps;
    private float duration;
    private int frames;
    private int width, height;
    private String audioURL=null;

    private static String getName(int i) {
        String num= String.valueOf(i);
        while (num.length()<4)
            num='0'+num;
        return "frame"+num+".png";
    }

    private static String timeToString(int time) {
        String s= String.valueOf(time);
        while (s.length()<2)
            s='0'+s;
        return s;
    }

    private static String timeToString(float time) {
        int sec=(int) time;
        int ms=(int)(100*(time-sec));
        int min=sec/60;
        sec%=60;
        int hours=min/60;
        min%=60;
        return timeToString(hours)+":"+timeToString(min)+":"+timeToString(sec)+"."+timeToString(ms);
    }

    public void render(int startFrame) throws Exception {

        for (int i=0; i<frames; i++) {
            BufferedImage f=new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            float timeNormalised=i*1.0f/frames;
            float absoluteTime=duration*timeNormalised;
            ImageIO.write(super.render(f, timeNormalised, absoluteTime),
                    "png", new File(Main.tempDir+getName(startFrame+i)));
        }
    }

    public int getFrames() {
        return frames;
    }

    public void setDuration(float dur) {
        duration=dur;
        frames= (int) (dur*fps);
    }

    public Scene(float fps, int w, int h) {
        this.fps=fps;
        width=w;
        height=h;
    }

}
