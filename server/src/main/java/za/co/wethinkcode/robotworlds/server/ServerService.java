package za.co.wethinkcode.robotworlds.server;

import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class ServerService implements Runnable {
    public final static ArrayList<SimpleServer> clients = new ArrayList<>();
    public static World world;
    private final ConfigHandler configurations;
    private final ServerSocket serverSocket;

    public ServerService() throws IOException {
        configurations = configurations();
        world = new World(configurations);
        this.serverSocket = new ServerSocket(SimpleServer.PORT);
    }

    @Override
    public void run() {
        while(true) {
            try {
                Socket socket = this.serverSocket.accept();

                SimpleServer simpleServer = new SimpleServer(socket, world, clients);
                Runnable r = simpleServer;
                clients.add(simpleServer);
                Thread task = new Thread(r);
                task.start();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private ConfigHandler configurations() {
        int topLeftX = 0;
        int topLeftY = 0;
        int visibility = 0;
        int shieldRepairTime = 0;
        int weaponReloadTime = 0;
        int mineSetTime = 0;
        int maxShieldStrength = 0;

        try (FileReader reader = new FileReader("worlds_config")) {
            Properties properties = new Properties();
            properties.load(reader);

            topLeftX = Integer.parseInt(properties.getProperty("topLeftX"));
            topLeftY = Integer.parseInt(properties.getProperty("topLeftY"));
            visibility = Integer.parseInt(properties.getProperty("visibility"));
            shieldRepairTime = Integer.parseInt(properties.getProperty("shieldRepairTime"));
            weaponReloadTime = Integer.parseInt(properties.getProperty("weaponReloadTime"));
            mineSetTime = Integer.parseInt(properties.getProperty("mineSetTime"));
            maxShieldStrength = Integer.parseInt(properties.getProperty("maxShieldStrength"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ConfigHandler(topLeftX, topLeftY,visibility, shieldRepairTime,weaponReloadTime,
                mineSetTime, maxShieldStrength);
    }
}
