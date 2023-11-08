import java.io.IOException;

public class Session implements Runnable {
    private Client client;

    public Session(Client client) {
        this.client = client;
    }

    private void handleSendText(String message)
    {
            switch (client.getChatType()) {
                case 0:
                    try {
                        Util.HandleText(message, client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        Util.HandleTextUser(message, client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
    }

    @Override
    public void run() {
        try {
            String message;
            while (!client.isClosed()) {

                if (client.getReader() == null) {
                    break;
                }

                message = client.getReader().readLine();

                if (message == null) {
                    break;
                }

//                System.out.println("From: " + client.getIP() + ":" + client.getPort() + " " + message);
                System.out.println(client.getName() + ": " + message);
                handleSendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
