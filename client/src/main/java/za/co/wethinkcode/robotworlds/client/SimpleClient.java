package za.co.wethinkcode.robotworlds.client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Properties;

public class SimpleClient {
    private static String ipAddress;
    private static int port;

    public static void main(String[] args) throws ClassNotFoundException {

        try (FileReader reader = new FileReader("client_config")) {
            Properties properties = new Properties();
            properties.load(reader);

            ipAddress = properties.getProperty("ipAddress");
            port = Integer.parseInt(properties.getProperty("port"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                Socket socket = new Socket(ipAddress, port);
                PrintStream out = new PrintStream(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            InstanceHandler instanceHandler = new InstanceHandler(out);
            instanceHandler.printWelcomeMessage();
            SessionHandler sessionHandler = new SessionHandler(socket, in, instanceHandler);
            new Thread(sessionHandler).start();
            String command;
            String request;
            while (true) {
                command = keyboard.readLine().toLowerCase();
                request = instanceHandler.parseRequest(command);
                out.println(request);
            }

        } catch (ConnectException e) {
            System.out.println("\n*************** TOY ROBOT WORLDS ***************\n");
            System.out.println("Toy Robot Worlds Server currently unavailable!");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
