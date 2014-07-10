package JVE.Network;

import JVE.Parsers.ParseUtils;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connection {

    private OnInputCommandEvent commandEvent;
    private OnInputFileEvent fileEvent;
    private Runnable close;
    private OutputStream out;
    private InputStream in;
    AtomicBoolean isWorking = new AtomicBoolean(true);

    public void setOnCloseEvent(Runnable r) {
        close = r;
    }

    public void setOnMessageEvent(OnInputCommandEvent r) {
        commandEvent = r;
    }

    public void setOnFileEvent(OnInputFileEvent r) {
        fileEvent = r;
    }

    public void sendMessage(String s) throws IOException {
        out.write(("@command " + s.length() + '\n' + s + '\n').getBytes());
        out.flush();
    }

    public void sendFile(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f.getPath());
        long length=f.length();
        out.write(("@file " + length + ' ' + f.getName() + '\n').getBytes());
        out.flush();
        byte[] byteArray = new byte[1024];
        while (length > 0) {
            int i = fis.read(byteArray);
            out.write(byteArray, 0, i);
            length-= i;
        }
        out.flush();
    }

    public Connection(Socket s) throws IOException {
        in=s.getInputStream();
        out=s.getOutputStream();
    }

    private static String readLine(InputStream is) throws IOException {
        String res="";
        char c;
        do {
            c= (char) is.read();
            if (c!='\n') res+=c;
        }
        while (c!='\n');
        return res;
    }

    public void startWorking(String defaultInputFolder) throws Exception {
        new Thread(() -> {
            try {
                String input= readLine(in);
                while (input != null) {


                    String [] params=input.split(" ");
                    if (params[0].startsWith("@command")) {
                        long length = Long.parseLong(params[1]);
                        String command="";
                        do {
                            command += readLine(in)+'\n';
                            length-=command.length();
                        }
                        while (length>0);
                         commandEvent.run(this, command);
                    }
                    else if (params[0].startsWith("@file")) {
                        String name=params[2];
                        long length = Long.parseLong(params[1]);


                        File f = new File(defaultInputFolder + name);
                        f.createNewFile();
                        byte[] buffer = new byte[1024];
                        FileOutputStream os = new FileOutputStream(f);

                        int total=0;
                        while (total<length) {
                            int count = in.read(buffer, 0, (int) Math.min(1024, length-total));
                            total += count;
                            os.write(buffer, 0, count);
                            ParseUtils.printMessage("file: "+total+":"+length+":"+count);
                        }
                        os.flush();
                        os.close();

                        fileEvent.run(this, f);
                    }
                    input= readLine(in);
                }
            } catch (Exception e) {
                System.err.println("[Connection] Error pt1 : " + e);
                e.printStackTrace();
            }

            try {
                in.close();
            } catch (IOException e) {
                System.err.println("[Connection] Error pt 2: " + e);
            }
            isWorking.set(false);
            if (close != null) close.run();
        }).start();
    }
}
