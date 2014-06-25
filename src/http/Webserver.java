/*
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */
package http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple multithreaded Webserver base on HTTP/1.0.
 * 
 * @author Le Nguyen
 */
public class Webserver {

	/** Der Pfad von dem Verzeichnis, wo sich alle verfügbaren Dateien befinden. */
	private static String ROOT;

	/** Das Trennzeichen vom Dateisystem. */
	private static final String separator = File.separator;

	/** Einige Media-Typen, die dieser Webserver unterstützt. */
	private static final String[][] MIME = { { ".html", "text/html" }, { ".txt", "text/plain" },
		{ ".gif", "image/gif" }, { ".jpeg", "image/jpeg" }, { ".jpg", "image/jpeg" },
		{ ".ico", "image/x‐icon" }, { ".pdf", "application/pdf" } };

	/** Aktuelle Anzahl an Client-Anfragen. */
	private int counter;

	/** Maximale Anzahl an Client-Anfragen. */
	private int max;

	private int port;

	public Webserver(int max, int port) {
		this.max = max;
		this.port = port;
	}

	public void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.format("The server is waiting for connection on port %d.", port);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				new WorkerThread(clientSocket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	private synchronized void increaseCounter() {
		while (counter >= max) {
			try {
				System.out.println(Thread.currentThread().getName() + " Waiting");
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		counter++;
	}

	private synchronized void decreaseCounter() {
		counter--;
		notify();
	}

	public static void main(String[] args) {
		/* Der Pfad vom Web-Verzeichnis ist von Benutzern einzugeben */
		if (args.length != 1) {
			System.err.println("Usage: java Webserver <root>");
			System.err.println("<root> : The directory that is designated for holding your web pages.");
			System.exit(1);
		}
		ROOT = args[0];
		new Webserver(10, 8080).startServer();
	}

	/* ----------------------------------------------------------------------- */

	/**
	 * Arbeitsthread, der eine existierende Socket-Verbindung zur Bearbeitung erhält.
	 * 
	 * @author Le Nguyen
	 */
	class WorkerThread extends Thread {

		private Socket socket;

		private BufferedReader inFromClient;

		private DataOutputStream outToClient;

		public WorkerThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println(getName() + " started.");
			increaseCounter();
			try {
				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new DataOutputStream(socket.getOutputStream());

				// Der absolute Pfad von der gewünschten Datei.
				String path = ROOT + separator + receiveRequest();

				// Holt die Datei vom Pfad und schickt sie an Client.
				sendResponse(path);

				// Schließt Socket und Streams.
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Connection error: " + e.getMessage());
			} finally {
				decreaseCounter();
				System.out.println(getName() + " stopped.");
			}
		}

		/**
		 * Bearbeitet die Anfrage vom Client und liefert die angeforderte Ressource zurück.
		 */
		private String receiveRequest() throws IOException {
			// Die Request-Zeile vom Client musst zu diesem Pattern passen -> Behandlung vom GET-Requests.
			Pattern requestLine = Pattern.compile("GET\\s/(.*)\\sHTTP/1\\.[01]");

			String resource = null;
			String line = inFromClient.readLine(); // Liest die Request-Zeile.
			Matcher matcher = requestLine.matcher(line);
			if (matcher.matches()) {
				resource = matcher.group(1); // Findet die angeforderte Ressource.
				System.out.println("Requested resource from client: " + resource);
			} else {
				httpError(400, "Bad Request"); // Anfrage kann nicht analysiert werden.
			}

			/* Protokollieren aller vom Client übergebener Header‐Fields. */
			System.out.println("Request from client :");
			for (line = inFromClient.readLine(); !line.isEmpty(); line = inFromClient.readLine()) {
				System.out.println("\t" + line);
				if (line.startsWith("User-Agent") && !line.contains("Firefox")) {
					// WARNUNG : der Client‐Browser ist nicht Firefox
					System.err.println("\tWARNING from server: non-Firefox user.");
				}
			}
			return resource;
		}

		/**
		 * Schickt die Antwort vom Webserver an Client zurück.
		 */
		private void sendResponse(String path) throws IOException {
			try (FileInputStream inFile = new FileInputStream(path)) {

				long contentLength = new File(path).length();

				// Media-Type anhand des Dateinamens finden.
				String contentType = "text/html"; // Default, falls nichts gefunden.
				for (int i = 0; i < MIME.length; ++i) {
					if (path.endsWith(MIME[i][0])) {
						contentType = MIME[i][1];
						break;
					}
				}

				/* Rückgabe der Header‐Fields und Protokoll auf der Konsole */
				System.out.println("Response from server: ");

				outToClient.writeBytes("HTTP/1.0 200 OK\n");
				System.out.print("\tHTTP/1.0 200 OK\n");

				outToClient.writeBytes("Content-Length: " + contentLength + "\n");
				System.out.print("\tContent-Length: " + contentLength + "\n");

				outToClient.writeBytes("Content-Type: " + contentType + "\n");
				System.out.print("\tContent-Type: " + contentType + "\n");

				outToClient.writeBytes("Set-Cookie: Previous-WorkerThread=" + getName() + "\n");
				System.out.print("\tSet-Cookie: Previous-WorkerThread=" + getName() + "\n");

				outToClient.writeBytes("\n");

				/* Schickt die gewünschte Datei an Client. */
				byte[] buffer = new byte[1024];
				int len;
				while ((len = inFile.read(buffer)) > 0) {
					outToClient.write(buffer, 0, len);
				}
			} catch (FileNotFoundException e) {
				httpError(404, "File Not Found"); // Die gewünschte Datei ist nicht verfügbar.
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}

		// private void sendData(String path) {
		// try (FileInputStream inFile = new FileInputStream(path)) {
		// byte[] buffer = new byte[1024];
		// int len;
		// while ((len = inFile.read(buffer)) > 0) {
		// outToClient.write(buffer, 0, len);
		// }
		// } catch (FileNotFoundException e) {
		// httpError(404, "File Not Found");
		// e.printStackTrace();
		// System.err.println(e.getMessage());
		// } catch (IOException e) {
		// httpError(404, "IOException");
		// e.printStackTrace();
		// System.err.println(e.getMessage());
		// } catch (Exception e) {
		// httpError(404, "Unknown Exception");
		// e.printStackTrace();
		// System.err.println(e.getMessage());
		// }
		// }

		/**
		 * Eine Fehlerseite an den Browser senden.
		 */
		private void httpError(int code, String description) {
			String html = "<html><head><title>RBPAufgabe4</title></head><body>"
					+ "<h1>HTTP/1.0 " + code + "</h1><h3>" + description + "</h3></body></html>";

			try {
				System.out.println("Response from server: ");

				outToClient.writeBytes("HTTP/1.0 " + code + " " + description + "\n");
				System.out.print("\tHTTP/1.0 " + code + " " + description + "\n");

				outToClient.writeBytes("Content-Type: " + "text/html" + "\n");
				System.out.print("\tHTTP/1.0 " + code + " " + description + "\n");

				outToClient.writeBytes("\n");

				outToClient.writeBytes(html);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
	}
}

