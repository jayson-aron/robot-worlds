package za.co.wethinkcode.robotworlds.client;

import za.co.wethinkcode.robotworlds.clientprotocols.ResponseProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class SessionHandler implements Runnable{
    private InstanceHandler instanceHandler;
    private Socket serverSocket;
    private BufferedReader in;

    public SessionHandler(Socket serverSocket, BufferedReader in, InstanceHandler instanceHandler) {
        this.serverSocket = serverSocket;
        this.in = in;
        this.instanceHandler = instanceHandler;
    }

    @Override
    public void run() {
        String messageFromServer;
        ResponseProtocol response;
        try {
            while (true) {
                System.out.print(" > ");
                messageFromServer = this.in.readLine();
                if (messageFromServer == null)
                    instanceHandler.shutDown();
                response = instanceHandler.parseResponse(messageFromServer);
                instanceHandler.printResponse(response);
                instanceHandler.processResponse(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
