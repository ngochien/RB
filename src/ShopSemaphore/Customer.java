package ShopSemaphore;

/*
 * Customer.java
 * Version 1.0
 * Autor: M. H�bner
 * Zweck: Simuliert das Verhalten eines Shop-Kunden
 */

public class Customer extends Thread {
	private Shop currentShop;

	public Customer(Shop s) {
		currentShop = s;
	}

	public void run() {

		while (!isInterrupted()) {

			// Versuche, in das Gesch�ft einzutreten
			System.err.println(this.getName() + " wants to enter the shop!");
			currentShop.enter();

			if (!isInterrupted()) {
				// F�r unbestimmte Zeit schlafen
				enjoyLife();
			}

		}
	}

	// Customer benutzen diese Methode, um sich zu vergn�gen
	public void enjoyLife() {
		int sleepTime = (int) (1000 * Math.random());

		try {
			// Thread blockieren
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// Erneutes Setzen des Interrupt-Flags f�r den eigenen Thread
			this.interrupt();
		}
	}
}
