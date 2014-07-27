package JVE.Rendering;

import JVE.Parsers.Video;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static JVE.Parsers.MathParser.setTimes;

public class Scene extends SceneLayer {

    private float duration;
    private int startFrame;
    private float startSec;
    private int frames;
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

    public void renderAndSave(RenderEvent changes) throws Exception {
        for (int i=0; i<frames; i++) {
            ImageIO.write(render(i), "png", new File(Video.getTempDir() +getFileName(startFrame + i)));
                    if (i%25==0) changes.run((startFrame+i)*1f/ Video.getFrames());
        }
    }

    public BufferedImage render(int frameNum) throws Exception {
        makePreview=false;
        BufferedImage f=new BufferedImage(Video.getW(), Video.getH(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        return getRendered(f, frameNum);
    }

    public static boolean getObeyProp() {
        return makePreview;
    }

    public static float getProp() {
        return prop;
    }

    private static boolean makePreview=false;
    private static float prop;
    public BufferedImage render(int frameNum, float prop) throws Exception {
        makePreview=true;
        Scene.prop=prop;
        BufferedImage f=new BufferedImage((int)(Video.getW()*prop), (int)(Video.getH()*prop), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        return getRendered(f, frameNum);
    }

    private BufferedImage getRendered(BufferedImage bg, int frameNum) throws Exception {
        float timeNormalised=frameNum*1.0f/frames;
        float absoluteTime=duration*timeNormalised;
        float absoluteVideoTime=startSec+absoluteTime;
        setTimes(timeNormalised, absoluteTime, absoluteVideoTime);
        return super.render(bg);
    }

    public int getFrames() {
        return frames;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float dur) {
        duration=dur;
        frames= (int) (dur*Video.getFPS());
    }

    public Scene(int startFrame, float startSec) {
        this.startFrame=startFrame;
        this.startSec=startSec;
    }

}
