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

	private int requests;

	private int maxRequests;

	private int port;

	public WebServer(int maxRequests, int port) {
		this.maxRequests = maxRequests;
		this.port = port;
	}

	public void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				new WorkerThread(requests(), clientSocket, this).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	private synchronized int requests() {
		return requests;
	}

	private synchronized void increaseRequests() {

		while (requests == maxRequests) {
			try {
				System.out.println(Thread.currentThread().getName() +
						" Waitingggggggggggggggggggggggggggggggggggggggggggggggg");
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName() + " Incr");
		requests++;
	}

	private synchronized void decreaseRequests() {
		requests--;
		notify();
		System.out.println(Thread.currentThread().getName() + " Decr");
	}

	public static void main(String[] args) {
		new WebServer(10, 1331).startServer();
	}

	/* ----------------------------------------------------------------------- */

	class WorkerThread extends Thread {

		private int id;

		private Socket socket;

		private WebServer server;

		private BufferedReader inFromClient;

		private DataOutputStream outToClient;

		private String statusLine = "HTTP/1.0 400 Bad\n";

		public WorkerThread(int id, Socket socket, WebServer server) {
			this.id = id;
			this.socket = socket;
			this.server = server;
		}

		@Override
		public void run() {
			increaseRequests();
			System.out.println("Thread " + id);
			try {
				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new DataOutputStream(socket.getOutputStream());
				sendResponse(receiveRequest());
				socket.close();
				System.out.println("Thread closed " + id);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e);
			}
			decreaseRequests();
		}

		private String receiveRequest() {
			Pattern requestLine = Pattern.compile("GET\\s+/(.*)\\s+HTTP/1\\.[01]");

			String resource = null;
			try {
				String line = inFromClient.readLine();
				Matcher matcher = requestLine.matcher(line);
				if (matcher.matches()) {
					resource = matcher.group(1);
					System.out.println("URL : " + resource);
				}

				System.out.println("Request header fields from client :");
				for (line = inFromClient.readLine(); !line.isEmpty(); line = inFromClient.readLine()) {
					System.out.println("\t" + line);
					if (line.startsWith("User-Agent") && !line.contains("Firefox")) {
						System.err.println("WARNING: Non Firefox-user");
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return resource;
		}

		private void sendResponse(String resource) {
			try {
				outToClient.writeBytes(statusLine);
				outToClient.writeBytes("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			// sendData(resource);
		}

		private void sendData(String absolutePath) {
			String separator = File.separator;
			absolutePath = ROOT_DIR + separator + absolutePath;
			try (FileInputStream inFile = new FileInputStream(absolutePath)) {
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
	}
}

