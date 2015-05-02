/**
 * Program for use with exercise 6.38 
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.net.*;
import java.io.*;

class Worker implements Runnable
{
	// the duration (in seconds) to hold the connection open
	public static final int DURATION = 10;
	private int sleepTime = DURATION;

	private Socket connection;

	public Worker(Socket connection) {
		this.connection = connection;
	}

	public void run() {
		try {
			PrintWriter pout = new PrintWriter(connection.getOutputStream(), true);

			while (sleepTime > 0) {	
				String s = (sleepTime == 1) ? " second." : " seconds.";
				//System.out.println("Sleeping " + sleepTime + " more " + s);
				pout.println("Sleeping " + sleepTime + " more " + s);
				pout.flush();
				Thread.sleep(1000);
				sleepTime -= 1;
			}

			// now close the socket connection
			connection.close();
		}
		catch (InterruptedException ie) { }
		catch (IOException ioe) { }
	}
}


public class TimedServer
{
	public static final int PORT = 2500;
	public static final int MAX_CONNECTIONS = 3;

	public static void main(String[] args) {
		Socket connection;
		
		try {
			ServerSocket server = new ServerSocket(PORT);

			while (true) {
				connection = server.accept();

				Thread worker = new Thread(new Worker(connection));
				worker.start();
			}
		}
		catch (java.io.IOException ioe) { }
	}
}
