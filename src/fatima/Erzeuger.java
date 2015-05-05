package fatima;

/* Producer.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Code der Erzeuger-Threads f�r ein Erzeuger/Verbrauchersystem
 */
import java.util.Date;

public class Erzeuger<E> extends Thread {

	private Puffer<E> puffer;
	private E item;

	public Erzeuger(Puffer<E> puffer) {
		this.puffer = puffer;
	}

	@Override
	public void run() {
		/*
		 * Erzeuge Objekte und lege sie in den Puffer.
		 */

		while (!isInterrupted()) {			

			// Puffer-Zugriffsmethode aufrufen --> Synchronisation �ber den Puffer!
			puffer.enter(item);

			if (!isInterrupted()) {
				/* F�r unbestimmte Zeit anhalten */
				pause();
			}
		}
	}

	public void statusmeldungZugriffswunsch() {
		/* Gib einen Zugriffswunsch auf der Konsole aus */
		System.err.println("                                           "
				+ this.getName() + " m�chte auf den Puffer zugreifen!");
	}

	public void pause() {
		/*
		 * Erzeuger benutzen diese Methode, um f�r eine Zufallszeit unt�tig
		 * zu sein
		 */
		int sleepTime = (int) (MAX_IDLE_TIME * Math.random());

		try {
			// Thread blockieren
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// Erneutes Setzen des Interrupt-Flags f�r den eigenen Thread
			this.interrupt();
		}
	}
}
