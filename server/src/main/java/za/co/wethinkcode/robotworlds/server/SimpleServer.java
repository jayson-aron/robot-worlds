package za.co.wethinkcode.robotworlds.server;


import com.google.gson.Gson;
import za.co.wethinkcode.robotworlds.protocols.RequestProtocol;
import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SimpleServer implements Runnable {

    public static final int PORT = 5000;
    private final BufferedReader in;
    private final PrintStream out;
    private final String clientMachine;
    private final World world;
    private ResponseProtocol response;
    private boolean active = false;
    private final ArrayList<SimpleServer> clients;

    public SimpleServer(Socket socket, World world, ArrayList<SimpleServer> clients) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        this.world = world;
        this.clients = clients;

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
    }

    public void run() {
        Map<String, Object> data = new HashMap<>();
        String messageFromClient;
        Command command;

        try {
            while (true) {
                messageFromClient = in.readLine();
                try {
                    command = Command.create(parseRequest(messageFromClient));
                    response = world.handleCommand(command, this);
                } catch (IllegalArgumentException e) {
                    String result = "ERROR";
                    data.put("message", "Unsupported command");
                    response = new ResponseProtocol(result, data);
                }

                out.println(parseResponse(response));
            }
        } catch (IOException ex) {
            System.out.print("");
        } finally {
            closeQuietly();
        }
    }

    public void closeQuietly() {
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
        }
    }

    private RequestProtocol parseRequest(String request) {
        Gson gson = new Gson();
        return gson.fromJson(request, RequestProtocol.class);
    }

    private String parseResponse(ResponseProtocol response) {
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    public void activate() {
        this.active = true;
    }

    public void deActivate() {
        this.active = false;
    }

    public boolean isActive() {
        return this.active;
    }

    public void outToAllClients(ResponseProtocol response) {
        for (SimpleServer client : clients) {
            if (client.equals(this))
                continue;
            client.out.println(parseResponse(response));
        }
    }

    public void outToOneClient(SimpleServer client, ResponseProtocol response) {
        client.out.println(parseResponse(response));
    }

    public void delayClient(int seconds) throws InterruptedException {
        TimeUnit.SECONDS.sleep(seconds);
    }
}
