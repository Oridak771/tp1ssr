import java.io.*;
import java.net.*;

public class server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            int numClients = 0;
            System.out.println("Le serveur est en attente de clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                numClients++;
                System.out.println("Nouveau client connecté : " + clientSocket.getInetAddress().getHostAddress() + " (total : " + numClients + ")");

                Thread clientThread = new Thread(new ClientHandler(clientSocket, numClients));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final int clientNum;

    public ClientHandler(Socket socket, int num) {
        this.clientSocket = socket;
        this.clientNum = num;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                // Lire les données envoyées par le client
                String message1 = in.readUTF();
                String message2 = in.readUTF();

                // Vérifier la présence de la deuxième chaine dans la première
                String response = message1.contains(message2) ? message1 + " contient " + message2 : message1 + " ne contient pas " + message2;

                // Envoyer la réponse au client
                out.writeUTF(response);

                // Envoyer le nombre de clients connectés au client
                out.writeInt(Thread.activeCount() - 1);

                // Attendre avant de recevoir une nouvelle requête
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            out.writeInt(Thread.activeCount() - 1);
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
