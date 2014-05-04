package ThreadTests;

/* ThreadTest7.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Beispiel f�r die Verwendung des Semaphor-Mechanismus zum wechselseitigen Ausschluss
 */
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest7 {
	/*
	 * Beispiel f�r die Verwendung des Semaphor-Mechanismus zum wechselseitigen
	 * Ausschluss
	 */
	public static void main(String[] args) {
		OutputServer7 outputServer = new OutputServer7();
		
		MyThread7a threadZahl = new MyThread7a(outputServer);
		threadZahl.setName("Zahl-Thread");
		
		MyThread7b threadText = new MyThread7b(outputServer);
		threadText.setName("Hallo-Thread");

		outputServer.showOutput("-- Noch nichts passiert!--");

		threadZahl.start();
		threadText.start();

		try {
			/* Main-Thread anhalten */
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// nichts
		}

		/* Threads beenden */
		threadZahl.interrupt();
		threadText.interrupt();
	}
}

class OutputServer7 {
	/* Output ausgeben */

	private ReentrantLock mutex = new ReentrantLock();

	public void showOutput(Object output) {
		mutex.lock();   // P(mutex)

		/* 1. Ausgabenteil */
		showThreadName();

		try {
			/* Thread anhalten */
			Thread.sleep(2);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		/* 2. Ausgabenteil */
		System.err.println(output);

		mutex.unlock();   // V(mutex)
	}

	public void showThreadName() {
		/* Zeige aktuellen Threadnamen an */
		mutex.lock();   // P(mutex)
		System.err.println("Output von " + Thread.currentThread().getName());
		mutex.unlock();   // V(mutex)
	}
}

class MyThread7a extends Thread {
	/* Hochz�hlen und Zahlen ausgeben */
	private OutputServer7 outputServer;

	public MyThread7a(OutputServer7 outputServer) {
		this.outputServer = outputServer;
	}

	public void run() {
		int i = 0;
		/* Interrupt-Flag abfragen */
		while (!isInterrupted()) {
			outputServer.showOutput(i++);
		}
	}
}

class MyThread7b extends Thread {
	/* Intelligenten Text ausgeben */

	private OutputServer7 outputServer;

	public MyThread7b(OutputServer7 outputServer) {
		this.outputServer = outputServer;
	}

	public void run() {
		/* Interrupt-Flag abfragen */
		while (!isInterrupted()) {
			outputServer.showOutput("------------ Hallo! --------------");
		}
	}
}
