import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
  private String name;
  private int chatType;
  private List<String> messages = new ArrayList<>();

  public Client(ServerSocket serverSocket) throws Exception {
    this.socket = serverSocket.accept();
    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    String clientName = in.readLine();
    String chatType = in.readLine();

    System.out.println("Client " + clientName + " connected");

    this.name = clientName;
    this.chatType = Integer.parseInt(chatType);
  }

  public PrintWriter getWriter() {
    return out;
  }

  public void close() throws IOException {
    this.socket.close();
  }

  public InetAddress getIP() {
    return socket.getInetAddress();
  }

  public boolean isOnline() {
    return socket.isConnected();
  }

  public boolean isClosed() {
    return socket.isClosed();
  }

  public int getPort() {
    return socket.getPort();
  }

  public BufferedReader getReader() {
    return in;
  }

  public void addMessage(String message) {
    messages.add(message);
  }

  public List<String> getMessages() {
    return messages;
  }

  public String getName() {
    return name;
  }

  public int getChatType() {
    return chatType;
  }
}
