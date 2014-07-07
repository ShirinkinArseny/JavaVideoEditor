package JVE.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connection {

    private LinkedList<String> messages = new LinkedList<>();
    private String lastInput;
    private Runnable event;
    private Runnable close;
    AtomicBoolean isWorking = new AtomicBoolean(true);

    public boolean getIsWorking() {
        return isWorking.get();
    }

    public void setOnCloseEvent(Runnable r) {
        close = r;
    }

    public void setOnInputEvent(Runnable r) {
        event = r;
    }

    public String getLastInputMessage() {
        return lastInput;
    }

    public void sendMessage(String s) {
        messages.add(s);
    }

    private void sendMessages(PrintWriter out) {
        while (!messages.isEmpty()) {
            out.println(messages.get(0));
            messages.remove(0);
        }
    }

    public void startWorking(BufferedReader in, PrintWriter out) throws IOException {
        new Thread(() -> {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    lastInput = input;
                    event.run();
                }
            } catch (IOException e) {
                System.err.println("[Connection] Error pt1 : " + e);
            }

            try {
                in.close();
            } catch (IOException e) {
                System.err.println("[Connection] Error pt 2: " + e);
            }
            out.close();
            isWorking.set(false);
            if (close != null) close.run();
        }).start();


        new Thread(() -> {

                Timer t=new Timer();
                t.schedule(
                new TimerTask() {
                    public void run() {
                        sendMessages(out);
                        if (!isWorking.get())
                            this.cancel();
                    }
                }, 0, 5);

        }).start();
    }
}
