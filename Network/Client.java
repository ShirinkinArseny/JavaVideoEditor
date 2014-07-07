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

    public Client(String ip, int port){
        client=new ClientConnection(ip, port);
        client.setOnInputEvent(new Runnable() {
            @Override
            public void run() {
                event.run(client, client.getLastInputMessage());
            }
        });
    }
}