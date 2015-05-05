package fatima;

/* Producer.java
 Version 1.0
 Autor: M. Hï¿½bner
 Zweck: Code der Erzeuger-Threads fï¿½r ein Erzeuger/Verbrauchersystem
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

			// Puffer-Zugriffsmethode aufrufen --> Synchronisation über den Puffer!
			puffer.enter(item);

			if (!isInterrupted()) {
				/* Fï¿½r unbestimmte Zeit anhalten */
				pause();
			}
		}
	}

	public void statusmeldungZugriffswunsch() {
		/* Gib einen Zugriffswunsch auf der Konsole aus */
		System.err.println("                                           "
				+ this.getName() + " mï¿½chte auf den Puffer zugreifen!");
	}

	public void pause() {
		/*
		 * Erzeuger benutzen diese Methode, um fï¿½r eine Zufallszeit untï¿½tig
		 * zu sein
		 */
		int sleepTime = (int) (MAX_IDLE_TIME * Math.random());

		try {
			// Thread blockieren
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// Erneutes Setzen des Interrupt-Flags fï¿½r den eigenen Thread
			this.interrupt();
		}
	}
}
