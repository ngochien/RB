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
public class Warteschlange implements Puffer<Kunde> {

	private static int zaehler = 0;
	
	private List<Kunde> kunden; // Liste als Speicher
	
	private final int id;

	public Warteschlange() {
		kunden = new LinkedList<Kunde>();
		zaehler++;
		id = zaehler;
	}

	@Override
	public synchronized void enter(Kunde kunde) {
		
		/* Item zum Puffer hinzufügen */
		kunden.add(kunde);
		System.out.format(Thread.currentThread().getName() + " ENTER "
						+ " Warteschlange-%d: %d Kunde(n) da \n", id, kunden.size());

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
	public synchronized Kunde remove() {
		/*
		 * Pufferzugriff sperren (bzw. ggf. auf Zugriff warten): geschieht
		 * automatisch durch Monitor-Eintritt ("synchronized" entspricht
		 * synchronized(this){...})
		 */
		Kunde kunde;

		/* Solange Puffer leer ==> warten! */
		while (kunden.size() == 0) {
			try {
				System.out.println(Thread.currentThread().getName() + " wartet auf Kunde");
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
		kunde = kunden.remove(0);
		System.out.format("\t\t\t\t" + Thread.currentThread().getName() + " HOLT %s "
				+ "von Warteschlange-%d: %d Kunde(n) noch da \n", kunde.getName(), id, kunden.size());

		/*
		 * Wartenden Producer wecken --> es müssen ALLE Threads geweckt werden
		 * (evtl. auch andere Consumer), da es nur eine Wait-Queue gibt!
		 */
		this.notifyAll();

		return kunde;
		/*
		 * Pufferzugriff entsperren und ggf. Threads in Monitor-Queue wecken:
		 * geschieht automatisch durch Monitor-Austritt
		 */
	}

}
