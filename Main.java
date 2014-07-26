package JVE;

import JVE.Parsers.Video;

public class Main {

    public static void main(String [] args) throws Exception {
        new Video(args[0], state -> {}).render(state -> Utils.printMessage("Rendered " + state * 100 + " %, " + (int) (state * Video.getFrames()) + " frames."));
    }

}
