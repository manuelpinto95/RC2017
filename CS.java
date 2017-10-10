import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
public class CS {
  public static void main(String[] args) throws Exception {
    ServerSocket serverSocket = new ServerSocket(58011, 50,
        InetAddress.getByName("localhost"));
    System.out.println("Server started  at:  " + serverSocket);

    while (true) {
      System.out.println("Waiting for a  connection...");

      final Socket activeSocket = serverSocket.accept();

      System.out.println("Received a  connection from  " + activeSocket);
      Runnable runnable = () -> handleClientRequest(activeSocket);
      new Thread(runnable).start(); // start a new thread
    }
  }

  public static void handleClientRequest(Socket socket) {
    try{
      BufferedReader socketReader = null;
      BufferedWriter socketWriter = null;
      socketReader = new BufferedReader(new InputStreamReader(
          socket.getInputStream()));
      socketWriter = new BufferedWriter(new OutputStreamWriter(
          socket.getOutputStream()));
      String inputMsg = null;
      while ((inputMsg = socketReader.readLine()) != null) {
        System.out.println("Received from  client: " + inputMsg);
        String outputMsg = inputMsg;
        socketWriter.write(outputMsg);
        socketWriter.write("\n");
        socketWriter.flush();
      }
      socket.close();
    }catch(Exception e){
      e.printStackTrace();
    }

  }
}
