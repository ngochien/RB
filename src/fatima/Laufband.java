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
	 * Auf dem Laufband	k�nnen maximal 12 Burger platziert sein.
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
	public synchronized void add(String item) {
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
				 * Ausf�hrender Thread hat Interrupt erhalten --> Interrupt-Flag
				 * im ausf�hrenden Thread setzen und Methode beenden
				 */
				Thread.currentThread().interrupt();
				return;
			}
		}
		/* Item zum Puffer hinzuf�gen */
		items.add(item);
		System.out.format("\n\t\t\t\t%s LEGT 1 BURGER auf das Laufband: %d Burger noch da\n\n",
							Thread.currentThread().getName(), items.size());

		/*
		 * Wartenden Consumer wecken --> es m�ssen ALLE Threads geweckt werden
		 * (evtl. auch andere Producer), da es nur eine Wait-Queue gibt!
		 */
		this.notifyAll();

		/*
		 * Pufferzugriff entsperren und ggf. Threads in Monitor-Queue wecken:
		 * geschieht automatisch durch Monitor-Austritt
		 */
	}
	
	@Override
	public synchronized String remove() {		
		String item;
		
		while (items.size() == 0) {
			try {
				System.out.println(Thread.currentThread().getName() + " WARTET... LAUFBAND LEER\n");
				this.wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return null;
			}
		}
		
		item = items.remove(0);		
		
		this.notifyAll();

		return item;		
	}
	
	public synchronized int size() {
		return items.size();
	}
	
	/**
	 * Sperrt das Laufband und nimmt eine bestimmte Anzahl von Burger raus, wenn genug da sind
	 */
	public synchronized boolean remove(int anzahl) {
		if (size() >= anzahl) {
			for (int i = 0; i < anzahl; i++) {
				remove();
			}
			System.out.format("\t\t\t\t%s ENTNIMMT 1 BURGER aus dem Laufband: %d Burger noch da\n",
								Thread.currentThread().getName(), items.size());
			return true;
		}
		return false;
	}
}
