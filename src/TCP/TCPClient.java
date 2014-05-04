package TCP;

/*
 * TCPClient.java
 *
 * Version 3.0
 * Autor: M. Hübner HAW Hamburg (nach Kurose/Ross)
 * Zweck: TCP-Client Beispielcode:
 *        TCP-Verbindung zum Server aufbauen, einen vom Benutzer eingegebenen
 *        String senden, den String in Großbuchstaben empfangen und ausgeben
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
    /* Portnummer */
    private final int serverPort;

    /* Hostname */
    private final String hostname;

    private Socket clientSocket; // TCP-Standard-Socketklasse

    private DataOutputStream outToServer; // Ausgabestream zum Server
    private BufferedReader inFromServer; // Eingabestream vom Server

    private boolean serviceRequested = true; // Client beenden?

    public TCPClient(String hostname, int serverPort) {
        this.serverPort = serverPort;
        this.hostname = hostname;
    }

    public void startJob() {
        /* Client starten. Ende, wenn quit eingegeben wurde */
        Scanner inFromUser;
        String sentence; // vom User übergebener String
        String modifiedSentence; // vom Server modifizierter String

        /* Ab Java 7: try-with-resources mit automat. close benutzen! */
        try {
            /* Socket erzeugen --> Verbindungsaufbau mit dem Server */
            clientSocket = new Socket(hostname, serverPort);

            /* Socket-Basisstreams durch spezielle Streams filtern */
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

            /* Konsolenstream (Standardeingabe) initialisieren */
            inFromUser = new Scanner(System.in);

            while (serviceRequested) {
                System.out.println("ENTER TCP-DATA: ");
                /* String vom Benutzer (Konsoleneingabe) holen */
                sentence = inFromUser.nextLine();

                /* String an den Server senden */
                writeToServer(sentence);

                /* Modifizierten String vom Server empfangen */
                modifiedSentence = readFromServer();

                /* Test, ob Client beendet werden soll */
                if (modifiedSentence.indexOf("QUIT") > -1) {
                    serviceRequested = false;
                }
            }

            /* Socket-Streams schließen --> Verbindungsabbau */
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Connection aborted by server!");
        }
        System.out.println("TCP Client stopped!");
    }

    private void writeToServer(String request) throws IOException {
        /* Sende eine Zeile zum Server */
        outToServer.writeBytes(request + '\n');
        System.out.println("TCP Client has sent the message: " + request);
    }

    private String readFromServer() throws IOException {
        /* Lies die Antwort (reply) vom Server */
        String reply = inFromServer.readLine();
        System.out.println("TCP Client got from Server: " + reply);
        return reply;
    }

    public static void main(String[] args) {
        /* Test: Erzeuge Client und starte ihn. */
        TCPClient myClient = new TCPClient("localhost", 56789);
        myClient.startJob();
    }
}
