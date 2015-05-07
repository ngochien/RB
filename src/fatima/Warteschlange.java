/**
 * 
 */
package fatima;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ob diese Kalsse wirklich notwendig ist?
 * 
 * @author le
 *
 */
public class Warteschlange implements Puffer<Kunde> {

	private static int zaehler = 0;
	
	private final Lock bufferLock = new ReentrantLock();
	private final Condition notFull = bufferLock.newCondition();
	private final Condition notEmpty = bufferLock.newCondition();
	
	private List<Kunde> kunden; // Liste als Speicher
	
	private int id;	
	
	private String enterMessage;	
	private String removeMessage;
	private String waitingMessage;	
	private String interruptedMessage;

	public Warteschlange() {
		kunden = new LinkedList<>();
		zaehler++;
		id = zaehler;		
	}	
	
	@Override
	public synchronized void add(Kunde kunde) {				
		/* Item zum Puffer hinzufügen */
		kunden.add(kunde);
		enterMessage = Thread.currentThread().getName() +" ENTER " +
				" in die Warteschlange-" + id + ": Länge = " + kunden.size();
		System.out.println(enterMessage);
		
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
				System.out.println(Thread.currentThread().getName() + " WARTET auf neue Kunde");
				this.wait(); // --> Warten in der Wait-Queue und Monitor des Puffers freigeben
			} catch (InterruptedException e) {
				/*
				 * Ausführender Thread hat Interrupt erhalten --> Interrupt-Flag
				 * im ausführenden Thread setzen und Methode beenden
				 */
				interruptedMessage = Thread.currentThread().getName() + " WURDE beim Warten GEWECKT";
				System.out.println(interruptedMessage);
				Thread.currentThread().interrupt();
				return null;
			}
		}
		/* Item aus dem Buffer entfernen */
		kunde = kunden.remove(0);		
		removeMessage = "\t\t\t\t" + Thread.currentThread().getName() +" HOLT "
						+ kunde.getName() + " aus der Warteschlange-" + id + ": Länge = " + kunden.size();
		System.out.println(removeMessage);

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
