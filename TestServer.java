package JVE;

public class TestServer {

    public static void main(String[] args) throws Exception {
        Main.tempDir="/home/nameless/Desktop/temp/";
        Main.tempDirForIncludes="/home/nameless/Desktop/tempS/";
        Main.main(new String[]{"/home/nameless/Desktop/testServer.tex"});

        /*MathParser.init();
        BufferedImage b= ImageIO.read(new File("/home/nameless/Octocat.png"));

        short[] threshold = new short[256];
        for (int i = 0; i < 256; i++)
              threshold[i] = (short)(255 - i);
        BufferedImageOp thresholdOp =new LookupOp(new ShortLookupTable(0, threshold), null);
        BufferedImage destination = thresholdOp.filter(b, b);

        ImageIO.write(destination, "png", new File("/home/nameless/Desktop/temp2/frame.png"));*/
    }

}
