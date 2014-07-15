package JVE.Rendering;

import JVE.Main;
import JVE.Parsers.Video;

import java.io.FileWriter;
import java.io.IOException;

public class Render {

    public static void render_mencoder(int fps, int w, int h, String filename) {
        try {
            ShellUsing.runCommand("cd " + Main.tempDir + "&& mencoder mf://*.png -mf w=" + w + ":h=" + h + ":fps=" + fps + ":type=png -ovc lavc \\ -lavcopts vcodec=mpeg4:mbd=2:trell -oac copy -o " + filename);
        } catch (IOException ex) {
            System.out.println("Error in rendering");
        }
    }

    public static void renderFromFrames_ffmpeg(String filename) {
        try {
            //String command="cxd "+JVE.tempDir+" && " +
            //        "ffmpeg "+audio+" -i frame%04d.png -r "+fps+" -c:v libx264 -crf 0 -strict -2 "+filename;
            String command="cd "+ Main.tempDir+" && " +
                    "ffmpeg "+//-i /home/nameless/hs2.ogg" +
                    " -r "+ Video.getFPS()+" -i frame%04d.png -c:v libx264 -crf 0 -strict -2 -y "+filename;
            System.out.println("Running: "+command);
            ShellUsing.runCommand(command);
        } catch (Exception ex) {
            System.out.println("Error in rendering");
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
            System.out.println("Running: "+command);
            ShellUsing.runCommand(command);
        } catch (Exception ex) {
            System.out.println("Error in rendering");
        }
    }

}
