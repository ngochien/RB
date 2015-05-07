/**
 * 
 */
package fatima;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 
 * Nicht so effiziente Datenstruktur zum Speichern und Verarbeiten von Bestellungen nach dem SRTN-Verfahren
 * 
 * @author Fatima
 *
 */
public class BestellungQueue implements Puffer<Kunde>{
	
	private Queue<Kunde> bestellungen;
		
	private String enterMessage;	
	private String removeMessage;
	private String waitingMessage;	
	private String interruptedMessage;
	
	public BestellungQueue() {
		bestellungen = new PriorityQueue<>(new Kunde.BestellungComparator());
	}
	
	@Override
	public synchronized void enter(Kunde kunde) {
		bestellungen.add(kunde);
		enterMessage = "\t\t\t\t" + Thread.currentThread().getName() +" LEGT Bestellung von "
		+ kunde.getName() + " in die BestellungQueue: " + bestellungen.size() + " Bestellung(en) da";
		System.out.println(enterMessage);		
		this.notifyAll();	
	}
	
	@Override
	public synchronized Kunde remove() {
		Kunde kunde;
		while (bestellungen.size() == 0) {
			try {
				waitingMessage = Thread.currentThread().getName() + " WARTET auf neue Bestellung";
				System.out.println(waitingMessage);
				this.wait();
			} catch (InterruptedException e) {
				interruptedMessage = Thread.currentThread().getName() + " WURDE beim Warten GEWECKT";
				System.out.println(interruptedMessage);
				Thread.currentThread().interrupt();
				return null;
			}
		}
		kunde = bestellungen.remove();
		removeMessage = "\t\t\t\t" + Thread.currentThread().getName() +" NIMMT Bestellung von "
		+ kunde.getName() + " aus der BestellungQueue: " + bestellungen.size() + " Bestellung(en) da";
		System.out.println(removeMessage);		
		this.notifyAll();
		return kunde;
	}
	
	public static void main(String[] args) {
		
		BestellungQueue q = new BestellungQueue();
		Kunde k1 = new Kunde(null); k1.bestellen();
		Kunde k2 = new Kunde(null); k2.bestellen();
		Kunde k3 = new Kunde(null); k3.bestellen();
		
		q.enter(k1); q.enter(k2); q.enter(k3);
		
		System.out.println(q.remove().getBestellung());
		System.out.println(q.remove().getBestellung());
		System.out.println(q.remove().getBestellung());
	}
}
