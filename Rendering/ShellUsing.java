package JVE.Rendering;

import JVE.Parsers.ParseUtils;

import java.io.IOException;
import java.io.InputStream;

public class ShellUsing {

    public static void runCommand(String cmd) throws IOException {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            pb.redirectErrorStream(true);
            Process shell = pb.start();
            InputStream shellIn = shell.getInputStream();
            int c;
            while ((c = shellIn.read()) != -1) {
                ParseUtils.printMessage(c);
            }
            shellIn.close();
    }

}
