/**
 * 
 */
package fatima;

import java.util.LinkedList;
import java.util.List;

/**
 * Ob diese Kalsse wirklich notwendig ist?
 * 
 * @author le
 *
 */
public class Warteschlange implements Puffer<Thread> {

	private static int zaehler = 0;
	
	private List<Thread> threads; // Liste als Speicher
	
	private final int id;

	public Warteschlange() {
		threads = new LinkedList<Thread>();
		zaehler++;
		id = zaehler;
	}

	@Override
	public synchronized void enter(Thread thread) {
		
		/* Item zum Puffer hinzufügen */
		threads.add(thread);
		System.out.format("\t\t\t\t" + Thread.currentThread().getName() + " geht in die "
						+ " Warteschlange-%d: %d Kunde(n) auch da \n", id, threads.size());

		/*
		 * Wartenden Consumer wecken --> es müssen ALLE Threads geweckt werden
		 * (evtl. auch andere Producer), da es nur eine Wait-Queue gibt!
		 */
		this.notifyAll();

		/*
		 * Pufferzugriff entsperren und ggf. Threads in Monitor-Queue wecken:
		 * geschieht automatisch durch Monitor-Austritt
		 */
	}

	@Override
	public synchronized Thread remove() {
		/*
		 * Pufferzugriff sperren (bzw. ggf. auf Zugriff warten): geschieht
		 * automatisch durch Monitor-Eintritt ("synchronized" entspricht
		 * synchronized(this){...})
		 */
		Thread thread;

		/* Solange Puffer leer ==> warten! */
		while (threads.size() == 0) {
			try {
				System.out.println(Thread.currentThread().getName() + " wartet...");
				this.wait(); // --> Warten in der Wait-Queue und Monitor des Puffers freigeben
			} catch (InterruptedException e) {
				/*
				 * Ausführender Thread hat Interrupt erhalten --> Interrupt-Flag
				 * im ausführenden Thread setzen und Methode beenden
				 */
				System.out.println(Thread.currentThread().getName() + " wurde beim Warten geweckt");
				Thread.currentThread().interrupt();
				return null;
			}
		}
		/* Item aus dem Buffer entfernen */
		thread = threads.remove(0);
		System.out.format("\t\t\t\t" + Thread.currentThread().getName() + " holt %s "
				+ "von Warteschlange-%d: %d Kunde(n) noch da \n", thread.getName(), id, threads.size());

		/*
		 * Wartenden Producer wecken --> es müssen ALLE Threads geweckt werden
		 * (evtl. auch andere Consumer), da es nur eine Wait-Queue gibt!
		 */
		this.notifyAll();

		return thread;
		/*
		 * Pufferzugriff entsperren und ggf. Threads in Monitor-Queue wecken:
		 * geschieht automatisch durch Monitor-Austritt
		 */
	}

}
