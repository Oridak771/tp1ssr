import java.io.*;
import java.net.*;

public class client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Lire les chaines de caractères depuis le clavier
                System.out.println("Entrez la première chaine :");
                String message1 = keyboard.readLine();
                System.out.println("Entrez la deuxième chaine :");
                String message2 = keyboard.readLine();

                // Envoyer les chaines de caractères au serveur
                out.writeUTF(message1);
                out.writeUTF(message2);

                // Lire la réponse du serveur
                String response = in.readUTF();
                System.out.println(response);

                // Lire le nombre de clients connectés
                int numClients = in.readInt();
                System.out.println("Nombre de clients connectés : " + numClients);

                // Attendre avant d'envoyer une nouvelle requête
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
