package JVE.Network;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connection {

    private OnInputCommandEvent commandEvent;
    private OnInputFileEvent fileEvent;
    private Runnable close;
    private OutputStream out;
    private InputStream in;
    private AtomicBoolean isWorking = new AtomicBoolean(true);
    private String adress;

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    private String duty;

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
        out.write(("@command " + s.length() + '\n' + s + '\n').getBytes("UTF-8"));
        out.flush();
    }

    public void sendFile(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f.getPath());
        long length = f.length();
        out.write(("@file " + length + ' ' + f.getName() + '\n').getBytes("UTF-8"));
        out.flush();
        byte[] byteArray = new byte[1024];
        while (length > 0) {
            int i = fis.read(byteArray);
            out.write(byteArray, 0, i);
            length -= i;
        }
        out.flush();
    }

    public Connection(Socket s) throws IOException {
        in = s.getInputStream();
        out = s.getOutputStream();
        adress = s.getInetAddress().toString();
    }

    private static String readLine(InputStream is) throws IOException {
        byte[] b = new byte[100];
        byte c;
        int length = 0;
        do {
            c = (byte) is.read();
            if (c != '\n') {
                b[length] = c;
                length++;
                if (b.length <= length)
                    b = java.util.Arrays.copyOf(b, length + 100);
            }
        }
        while (c != '\n');
        return new String(java.util.Arrays.copyOf(b, length), "UTF-8");
    }

    public void startWorking(String defaultInputFolder) throws Exception {
        new Thread(() -> {
            try {
                String input = readLine(in);
                while (input != null) {


                    String[] params = input.split(" ");
                    if (params[0].startsWith("@command")) {
                        long length = Long.parseLong(params[1]);
                        String command = "";
                        while (length > command.length()) {
                            command += readLine(in) + '\n';
                        }
                        commandEvent.run(this, command);
                    } else if (params[0].startsWith("@file")) {
                        String name = params[2];
                        long length = Long.parseLong(params[1]);


                        File f = new File(defaultInputFolder + name);
                        while (f.exists()) {
                            f = new File(defaultInputFolder + f.getName() + "_");
                        }
                        f.createNewFile();
                        byte[] buffer = new byte[1024];
                        FileOutputStream os = new FileOutputStream(f);

                        int total = 0;
                        while (total < length) {
                            int count = in.read(buffer, 0, (int) Math.min(1024, length - total));
                            total += count;
                            os.write(buffer, 0, count);
                        }
                        os.flush();
                        os.close();

                        fileEvent.run(this, f);
                    }
                    input = readLine(in);
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

    public String toString() {
        return adress;
    }
}
