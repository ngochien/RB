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
	
	public BestellungQueue() {
		bestellungen = new PriorityQueue<>(new Kunde.BestellungComparator());
	}
	
	@Override
	public synchronized void add(Kunde kunde) {
		bestellungen.add(kunde);
		
		System.out.println("\t\t\t\t" + Thread.currentThread().getName() +" LEGT Bestellung von "
				+ kunde.getName() + " in die BestellungQueue: " + bestellungen.size() + " Bestellung(en) da");
		
		this.notifyAll();	
	}
	
	@Override
	public synchronized Kunde remove() {
		Kunde kunde;
		while (bestellungen.size() == 0) {
			try {				
				System.out.println(Thread.currentThread().getName() + " WARTET auf neue Bestellung");
				this.wait();
			} catch (InterruptedException e) {			
				System.out.println(Thread.currentThread().getName() + " WURDE beim Warten GEWECKT");
				Thread.currentThread().interrupt();
				return null;
			}
		}
		kunde = bestellungen.remove();
		
		System.out.println("\t\t\t\t" + Thread.currentThread().getName() +" NIMMT Bestellung von "
				+ kunde.getName() + " aus der BestellungQueue: " + bestellungen.size() + " Bestellung(en) da");
		
		this.notifyAll();
		return kunde;
	}
	
	public static void main(String[] args) {
		
		BestellungQueue q = new BestellungQueue();
		Kunde k1 = new Kunde(null); k1.bestellen();
		Kunde k2 = new Kunde(null); k2.bestellen();
		Kunde k3 = new Kunde(null); k3.bestellen();
		
		q.add(k1); q.add(k2); q.add(k3);
		
		System.out.println(q.remove().getBestellung());
		System.out.println(q.remove().getBestellung());
		System.out.println(q.remove().getBestellung());
	}
}
