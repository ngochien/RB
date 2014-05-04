package ShopSemaphore;

/*
 * ShopServer.java
 * Version 1.0
 * Autor: M. H�bner
 * Zweck: Erzeugt eine Simulationsumgebung f�r ein Shop-System
 */
import java.util.LinkedList;

public class ShopServer {
	public final int NO_CUSTOMER = 6; // Anzahl Kunden-Threads

	public static void main(String[] args) {
		/* Starte Simulation */
		new ShopServer().startSimulation();
	}

	public void startSimulation() {
		/* Starte und beende Threads */
		Shop server = new Shop();

		// Thread-Liste erzeugen
		LinkedList<Customer> customerList = new LinkedList<Customer>();

		System.err.println("-------------------- START -------------------");

		// Kunden - Threads erzeugen
		for (int i = 1; i <= NO_CUSTOMER; i++) {
			Customer current = new Customer(server);
			current.setName("Customer " + i);
			customerList.add(current);
			current.start();
		}

		// Laufzeit abwarten
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		// Kunden - Threads stoppen
		for (Customer current : customerList) {
			current.interrupt();
		}

		System.err.println("-------------------- THE END -------------------");
	}

}
