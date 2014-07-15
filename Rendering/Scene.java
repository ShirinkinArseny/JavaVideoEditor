package JVE.Rendering;

import JVE.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Scene extends SceneLayer {

    private float fps;
    private float duration;
    private int frames;
    private int width, height;
    private String source;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(ArrayList<String> src) {
        source="";
        for (String c: src)
            source+=c+"\n";
        source=source.substring(0, source.length()-1);
    }

    private static String getFileName(int i) {
        String num= String.valueOf(i);
        while (num.length()<4)
            num='0'+num;
        return "frame"+num+".png";
    }

    public void renderAndSave(int startFrame) throws Exception {
        for (int i=0; i<frames; i++) {
                    ImageIO.write(render(i), "png", new File(Main.tempDir+getFileName(startFrame + i)));
            }
        }

    public BufferedImage render(int frameNum) throws Exception {
        BufferedImage f=new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        float timeNormalised=frameNum*1.0f/frames;
        float absoluteTime=duration*timeNormalised;
        return super.render(f, timeNormalised, absoluteTime);
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
