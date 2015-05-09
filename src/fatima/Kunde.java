/**
 * 
 */
package fatima;

import java.util.Comparator;

/**
 * @author Fatima
 *
 */
public class Kunde extends Thread {

	private static int zaehler = 0;
	
	/**
	 * Ein Kunde bestellt zufällig verteilt zwischen 1 und 8 Burger. 
	 */
	private static final int MIN_BESTELLUNG = 2;
	private static final int MAX_BESTELLUNG = 9;
	
	/**
	 * Die Zeit, die der Kunde nach dem Erhalt der Ware braucht, bis er das Lokal verlassen hat.
	 */	
	private static final int MIN_ZEIT = 1 * 1000;
	private static final int MAX_ZEIT = 2 * 1000;	
	
	private Verkaufsraum verkaufsraum;
	private int bestellung;	
	
	public Kunde(Verkaufsraum verkaufsraum) {
		zaehler++;
		this.verkaufsraum = verkaufsraum;
		this.setName("KUNDE-" + zaehler);
		
	}	
	
	public void run() {	
		try {
			if (kommen()) {				
				sichEinreihen(verkaufsraum.getAktuelleWarteschlange());
				bestellen();
				bezahlen();
				verlassen();
				System.out.println();
			}
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
			Thread.currentThread().interrupt();			
		}	
		System.err.println(Thread.currentThread().getName() + " WURDE BEENDET");
	}

	public boolean kommen() {
		System.out.println(Thread.currentThread().getName() + " KOMMT ");
		return verkaufsraum.betreten();
	}
	
	public synchronized void sichEinreihen(Warteschlange warteschlange) throws InterruptedException {
		warteschlange.add(this);	
		this.wait();
	}	
	
	public synchronized void bestellen() throws InterruptedException {
		bestellung = Helper.random(MIN_BESTELLUNG, MAX_BESTELLUNG);
		System.out.println(Thread.currentThread().getName() + " BESTELLT " + bestellung + " BURGER");
		this.notify();						
		this.wait();	// Wartet auf notify von Kasse
	}
			
	public synchronized void bezahlen() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " BEZAHLT " + bestellung + " BURGER");
		this.notify();
		this.wait();
	}
	
	public synchronized void verlassen() throws InterruptedException {
		Thread.sleep(Helper.random(MIN_ZEIT, MAX_ZEIT));
		verkaufsraum.verlassen();
	}
		
	public synchronized int getBestellung() {
		return bestellung;
	}
	
	public static class BestellungComparator implements Comparator<Kunde> {
		@Override
		public int compare(Kunde k1, Kunde k2) {
			return k1.getBestellung() - k2.getBestellung();
		}
	}
}
