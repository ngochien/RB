package ShopSemaphore;

/*
 * Shop.java
 * Version 1.0
 Autor: M. H�bner
 Zweck: Stellt einen generischen Shop mit Zugriffsmethoden f�r Customer zur Verf�gung
 */

import java.util.concurrent.*;

public class Shop {
	private static final int ANZAHL_KOERBE = 3;

	private Semaphore Korbstapel;

	public Shop() {
		Korbstapel = new Semaphore(ANZAHL_KOERBE);
	}

	// Customer ruft die Methode ENTER auf
	public void enter() {
		// Versuche, einen Korb zu bekommen. Falls Stapel auf Null ==> Warten!
		try {
			Korbstapel.acquire();
		} catch (InterruptedException e) {
			// Erneutes Setzen des Interrupt-Flags f�r den ausf�hrenden Thread
			Thread.currentThread().interrupt();
			return;
		}
		// Einkaufen
		System.err
				.println("                                             "
						+ Thread.currentThread().getName() + " buys goods!");
		buyGoods();

		// Laden verlassen
		System.err
				.println("                                                                                   "
						+ Thread.currentThread().getName() + " leaves shop!");
		Korbstapel.release();
	}

	// Customer benutzen diese Methode, um einzukaufen
	public void buyGoods() {
		int sleepTime = (int) (1000 * Math.random());

		try {
			// Ausf�hrenden Thread blockieren
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// Erneutes Setzen des Interrupt-Flags f�r den ausf�hrenden Thread
			Thread.currentThread().interrupt();
		}
	}

}
