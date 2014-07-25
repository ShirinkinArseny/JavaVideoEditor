package JVE;

import JVE.Parsers.ParseUtils;
import JVE.Parsers.Video;

public class Main {

    public static String tempDir="/home/nameless/Desktop/temp2/";
    public static String tempDirForIncludes="/home/nameless/Desktop/temp2/";

    public static void main(String [] args) throws Exception {
        new Video(args[0], state -> {}).render(state -> ParseUtils.printMessage("Rendered "+state*100+" %, "+(int)(state*Video.getFrames())+" frames."));
    }

}
