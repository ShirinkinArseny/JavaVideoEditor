package JVE.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static JVE.Parsers.ParseUtils.printMessage;


public class Server {

    private ArrayList<ServerConnection> clients;
    private ConnectionEvent event;

    public void sendGlobalMessage(String s) {
       for (ServerConnection c: clients)
           c.sendMessage(s);
    }

    public void setOnInputEvent(ConnectionEvent r) {
        event=r;
    }

    public Server(int port){
        new Thread(() -> {
            clients=new ArrayList<>();
            ServerSocket server;
            try {
                server = new ServerSocket(port);
                while (true) {
                    Socket client = server.accept();
                    ServerConnection cl = new ServerConnection(client);
                    clients.add(cl);
                    cl.setOnInputEvent(new ConnectionEvent() {
                        @Override
                        public void run(Connection c, String message) throws Exception {
                            event.run(c, message);
                            printMessage("[Server] Incoming message: " + message);
                        }
                    });
                    cl.setOnCloseEvent(() -> {
                        printMessage("[Server] Disconnected client: " + client.getInetAddress().toString());
                        clients.remove(cl);
                    });
                    event.run(cl, "CONNECTED");
                    printMessage("[Server] Got client: " + client.getInetAddress());



                }     }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        new Server(1234);
    }

}