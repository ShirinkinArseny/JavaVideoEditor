package JVE.Network;

import java.net.Socket;

public class Client {

    public Client(String ip, int port, OnInputCommandEvent m, OnInputFileEvent f, String defInputFOlder) throws Exception {
        Connection client = new Connection(new Socket(ip, port));
        client.setOnMessageEvent(m);
        client.setOnFileEvent(f);
        client.startWorking(defInputFOlder);
    }
}