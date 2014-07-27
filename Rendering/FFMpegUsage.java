package JVE.Rendering;

import JVE.Parsers.Video;
import JVE.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

import static JVE.Utils.cleanByMask;

public class FFMpegUsage {

    public static BufferedImage makeFrame(String video, float time) throws Exception {
        String command="cd "+Video.getTempDirForIncludes()+" && ffmpeg " +
                "-i "+video+" -ss "+time+" -vframes 1  -y frame.png";
        Utils.printMessage("Running: " + command);
        ShellUsing.runCommand(command);
        return ImageIO.read(new File(Video.getTempDirForIncludes()+"frame.png"));
    }

    public static void renderFromFrames_ffmpeg(String filename) {
        try {
            String audio=Video.getAudio()!=null?" -i "+Video.getAudio():"";
            String command="cd "+ Video.getTempDir()+" && " +
                    "ffmpeg " +audio+
                    " -r "+ Video.getFPS()+" -i frame%04d.png -c:v libx264 -crf 0 -strict -2 -y "+filename;
            Utils.printMessage("Running: " + command);
            ShellUsing.runCommand(command);
            cleanByMask(Video.getTempDir(), ".png");
        } catch (Exception ex) {
            Utils.printMessage("Error in rendering");
        }
    }

    public static void renderFromVideos_ffmpeg(int count, String filename) {
        try {
            String audio=Video.getAudio()!=null?" -i "+Video.getAudio():"";
            String names="";
            for (int i=0; i<count; i++) {
                names+="file '"+i+".mp4'\n";
            }
            FileWriter fw = new FileWriter(Video.getTempDir()+"inputs.txt");
                fw.write(names);
            fw.close();
            String command="cd "+ Video.getTempDir()+" && "+
                    "ffmpeg "+audio+"-f concat -i inputs.txt -c copy "+filename;
            Utils.printMessage("Running: " + command);
            ShellUsing.runCommand(command);
        } catch (Exception ex) {
            Utils.printMessage("Error in rendering");
        }
    }

}
