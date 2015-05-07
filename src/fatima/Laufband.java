/**
 * 
 */
package fatima;

import java.util.LinkedList;
import java.util.List;

/**
 * @author le
 *
 */
public class Laufband implements Puffer<String>{

	/**
	 * Auf dem Laufband	können maximal 12 Burger platziert sein.
	 */
	private static final int DEFAULT_SIZE = 12;
	
	private final int maxSize;
	private List<String> items; // Liste als Speicher

	public Laufband() {
		maxSize = DEFAULT_SIZE;
		items = new LinkedList<String>();
	}

	/* Producer (Erzeuger) rufen die Methode ENTER auf */
	@Override
	public synchronized void enter(String item) {
		/*
		 * Pufferzugriff sperren (bzw. ggf. auf Zugriff warten): geschieht
		 * automatisch durch Monitor-Eintritt ("synchronized" entspricht
		 * synchronized(this){...})
		 */

		/* Solange Puffer voll ==> warten! */
		while (items.size() == maxSize) {
			try {
				System.out.println(Thread.currentThread().getName() + " WARTET... LAUFBAND VOLL\n");
				this.wait(); // --> Warten in der Wait-Queue und Monitor des Puffers freigeben
			} catch (InterruptedException e) {
				/*
				 * Ausfï¿½hrender Thread hat Interrupt erhalten --> Interrupt-Flag
				 * im ausfï¿½hrenden Thread setzen und Methode beenden
				 */
				Thread.currentThread().interrupt();
				return;
			}
		}
		/* Item zum Puffer hinzufï¿½gen */
		items.add(item);
		System.out.format("\t\t\t\t%s LEGT 1 BURGER auf das Laufband: %d Burger noch da\n",
							Thread.currentThread().getName(), items.size());

		/*
		 * Wartenden Consumer wecken --> es mï¿½ssen ALLE Threads geweckt werden
		 * (evtl. auch andere Producer), da es nur eine Wait-Queue gibt!
		 */
		this.notifyAll();

		/*
		 * Pufferzugriff entsperren und ggf. Threads in Monitor-Queue wecken:
		 * geschieht automatisch durch Monitor-Austritt
		 */
	}

	/* Consumer (Verbraucher) rufen die Methode REMOVE auf */
	@Override
	public synchronized String remove() {
		/*
		 * Pufferzugriff sperren (bzw. ggf. auf Zugriff warten): geschieht
		 * automatisch durch Monitor-Eintritt ("synchronized" entspricht
		 * synchronized(this){...})
		 */
		String item;

		/* Solange Puffer leer ==> warten! */
		while (items.size() == 0) {
			try {
				System.out.println(Thread.currentThread().getName() + " WARTET... LAUFBAND LEER\n");
				this.wait(); // --> Warten in der Wait-Queue und Monitor des Puffers freigeben
			} catch (InterruptedException e) {
				/*
				 * Ausfï¿½hrender Thread hat Interrupt erhalten --> Interrupt-Flag
				 * im ausfï¿½hrenden Thread setzen und Methode beenden
				 */
				Thread.currentThread().interrupt();
				return null;
			}
		}
		/* Item aus dem Buffer entfernen */
		item = items.remove(0);
		System.out.format("\t\t\t\t%s ENTNIMMT 1 BURGER aus dem Laufband: %d Burger noch da\n",
						Thread.currentThread().getName(), items.size());

		/*
		 * Wartenden Producer wecken --> es mï¿½ssen ALLE Threads geweckt werden
		 * (evtl. auch andere Consumer), da es nur eine Wait-Queue gibt!
		 */
		this.notifyAll();

		return item;
		/*
		 * Pufferzugriff entsperren und ggf. Threads in Monitor-Queue wecken:
		 * geschieht automatisch durch Monitor-Austritt
		 */
	}
}
