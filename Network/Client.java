package JVE.Network;

public class Client {

    private ClientConnection client;
    private ConnectionEvent event;

    public void sendMessage(String s) {
        client.sendMessage(s);
    }

    public void setOnInputEvent(ConnectionEvent r) {
        event=r;
    }

    public Client(String ip, int port, ConnectionEvent onstart) throws Exception {
        client=new ClientConnection(ip, port, onstart);
    }
}