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
 * @author Le
 */
public class WebServer {

	private static final String ROOT_DIR =
			"/media/sda6/@Studium/Bachelor/BWI/RB_SS14/Praktikum/Aufgabe4/Testweb";

	private static final String DEFAULT_FILE = "index.html";

	private int counter;

	private int max;

	private int port;

	public WebServer(int max, int port) {
		this.max = max;
		this.port = port;
	}

	public void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				new WorkerThread(clientSocket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void increaseCounter() {
		while (counter >= max) {
			try {
				System.out.println(Thread.currentThread().getName() +
						" Waitingggggggggggggggggggggggggggggggggggggggggggggggg");
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName() + " Incr");
		counter++;
	}

	private synchronized void decreaseCounter() {
		counter--;
		notify();
		System.out.println(Thread.currentThread().getName() + " Decr");
	}

	public static void main(String[] args) {
		new WebServer(10, 1331).startServer();
	}

	/* ----------------------------------------------------------------------- */

	class WorkerThread extends Thread {

		private Socket socket;

		private BufferedReader inFromClient;

		private DataOutputStream outToClient;

		private String statusLine = "HTTP/1.0 200 OK\n";

		private String contentType = "text/html\n";

		private String setCookie = "Set-Cookie: " + getName();

		public WorkerThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			increaseCounter();
			System.out.println(getName() + " started.");
			try {
				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new DataOutputStream(socket.getOutputStream());
				sendResponse(receiveRequest());
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Connection error: " + e.getMessage());
			} finally {
				System.out.println(getName() + " stopped!");
				decreaseCounter();
			}
		}

		private String receiveRequest() throws IOException {
			Pattern requestLine = Pattern.compile("GET\\s+/(.*)\\s+HTTP/1\\.[01]");
			String resource = null;
			String line = inFromClient.readLine();
			Matcher matcher = requestLine.matcher(line);
			if (matcher.matches()) {
				resource = matcher.group(1);
				System.out.println("Requested resource from client: " + resource);
			} else {
				httpError(400, "Bad Request");
			}
			System.out.println("Request header fields from client :");
			for (line = inFromClient.readLine(); !line.isEmpty(); line = inFromClient.readLine()) {
				System.out.println("\t" + line);
				if (line.startsWith("User-Agent") && !line.contains("Firefox")) {
					System.err.println("\tWARNING from server: non-Firefox user.");
				}
			}
			return resource;
		}

		private void sendResponse(String resource) throws IOException {
			// byte[] data = getData(resource);
			// if (data == null) {
			// httpError(404, "Not Found");
			// } else {
			outToClient.writeBytes(statusLine);
			outToClient.writeBytes("Content-Length: " + 1234);
			outToClient.writeBytes("Content-Type: " + contentType);
			// outToClient.writeBytes(setCookie);
			outToClient.writeBytes("\n\n");
			sendData(resource);
		}

		// private byte[] getData(String resource) {
		// String separator = File.separator;
		// resource = ROOT_DIR + separator + resource;
		// try {
		// return Files.readAllBytes(Paths.get(resource));
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// // httpError(404, "Not Found");
		// return null;
		// }
		// }

		private void sendData(String resource) {
			String separator = File.separator;
			resource = ROOT_DIR + separator + resource;
			try (FileInputStream inFile = new FileInputStream(resource)) {
				byte[] buffer = new byte[1024];
				int contentLeng = 0;
				int len;
				while ((len = inFile.read(buffer)) > 0) {
					outToClient.write(buffer, 0, len);
					contentLeng += len;
				}
				System.out.println(contentLeng);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Eine Fehlerseite an den Browser senden.
		 * 
		 * @throws IOException
		 */
		private void httpError(int code, String description) throws IOException {
			String html = "<html><head><title>RBPAufgabe4</title></head><body>"
					+ "<h1>HTTP/1.0 " + code + "</h1><h3>" + description + "</h3></body></html>";

			statusLine = "HTTP/1.0 " + code + " " + description + "\n";
			outToClient.writeBytes(statusLine);
			outToClient.writeBytes("Content-Type: " + contentType);
			outToClient.writeBytes(setCookie);
			outToClient.writeBytes("\n\n");
			outToClient.writeBytes(html);
		}
	}
}

