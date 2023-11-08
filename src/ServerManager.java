import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ServerManager {
    public static int PORT = 14215;
    public static boolean isRunning = true;
    public static Set<Client> clients = Collections.synchronizedSet(new HashSet<>());

    public static Connect db = new Connect();

    ServerManager() {
        init();
        connect();
    }

    private void init() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("server.properties"));
            Object value = null;

            if ((value = properties.get("server.port")) != null) {
                ServerManager.PORT = Integer.parseInt(String.valueOf(value));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void connect() {
        try {
            ServerSocket serverSocket = new ServerSocket(ServerManager.PORT);
            System.out.println("Server is running port: " + ServerManager.PORT);
            while (ServerManager.isRunning) {
                Client client = new Client(serverSocket);
                ServerManager.clients.add(client);
                new Thread(new Session(client)).start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
