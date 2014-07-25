package JVE.Rendering;

import JVE.Main;
import JVE.Parsers.ParseUtils;
import JVE.Parsers.Video;

import java.io.FileWriter;

public class Render {

    public static void renderFromFrames_ffmpeg(String filename) {
        try {
            //String command="cxd "+JVE.tempDir+" && " +
            //        "ffmpeg "+audio+" -i frame%04d.png -r "+fps+" -c:v libx264 -crf 0 -strict -2 "+filename;
            String command="cd "+ Main.tempDir+" && " +
                    "ffmpeg -i /home/nameless/Projects/ЭОРСРЯ/partl.mp3" +
                    " -r "+ Video.getFPS()+" -i frame%04d.png -c:v libx264 -crf 0 -strict -2 -y "+filename;
            ParseUtils.printMessage("Running: " + command);
            ShellUsing.runCommand(command);
        } catch (Exception ex) {
            ParseUtils.printMessage("Error in rendering");
        }
    }

    public static void renderFromVideos_ffmpeg(int count, String filename) {
        try {
            String names="";
            for (int i=0; i<count; i++) {
                names+="file '"+i+".mp4'\n";
            }
            FileWriter fw = new FileWriter(Main.tempDir+"inputs.txt");
                fw.write(names);
            fw.close();
            String command="cd "+ Main.tempDir+" && "+
                    "ffmpeg -f concat -i inputs.txt -c copy "+filename;
            ParseUtils.printMessage("Running: " + command);
            ShellUsing.runCommand(command);
        } catch (Exception ex) {
            ParseUtils.printMessage("Error in rendering");
        }
    }

}
