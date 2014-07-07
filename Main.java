package JVE;

import JVE.Parsers.MathParser;
import JVE.Parsers.SyntaxParser;

public class Main {

    public static final String tempDir="/home/nameless/Desktop/temp2/";

    public static void main(String [] args) throws Exception {
        MathParser.init();
        SyntaxParser.parse(args[0]);
    }

}
