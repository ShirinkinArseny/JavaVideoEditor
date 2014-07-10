package JVE.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static JVE.Parsers.ParseUtils.printMessage;


public class Server {

    private ArrayList<Connection> clients;

    public void sendGlobalMessage(String s) throws IOException {
       for (Connection c: clients)
           c.sendMessage(s);
    }

    public Server(int port, OnConnectionEvent c, OnInputCommandEvent m, OnInputFileEvent r, String defInputFOlder){
        new Thread(() -> {
            clients=new ArrayList<>();
            ServerSocket server;
            try {
                server = new ServerSocket(port);
                while (true) {
                    Socket client = server.accept();
                    Connection cl=new Connection(client);
                    cl.setOnMessageEvent(m);
                    cl.setOnFileEvent(r);
                    cl.setOnCloseEvent(() -> {
                        printMessage("[Server] Disconnected client: " + client.getInetAddress().toString());
                        clients.remove(cl);
                    });
                    c.run(cl);
                    cl.startWorking(defInputFOlder);
                    clients.add(cl);
                    printMessage("[Server] Got client: " + client.getInetAddress());
                }     }
            catch (Exception e) {
                System.err.println(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

}