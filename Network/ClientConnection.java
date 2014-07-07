package JVE.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection extends Connection{

    public ClientConnection(String ip, int port) {
        try {
            Socket clientSocket;
            clientSocket = new Socket(ip, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            startWorking(in, out);
        } catch (IOException e) {
            System.err.println("[ClientConnection] failed to init");
        }
    }

}