/**
 * 
 */
package fatima;

import java.util.Comparator;

/**
 * @author le
 *
 */
public class Kunde extends Thread {

	private static int zaehler = 0;
	
	/**
	 * Ein Kunde bestellt zufällig verteilt zwischen 1 und 8 Burger. 
	 */
	private static final int MIN_BESTELLUNG = 3;
	private static final int MAX_BESTELLUNG = 7;
	
	/**
	 * Die Zeit, die der Kunde nach dem Erhalt der Ware braucht, bis er das Lokal verlassen hat.
	 */	
	private static final int MIN_ZEIT = 3 * 1000;
	private static final int MAX_ZEIT = 7 * 1000;	
	
	private Verkaufsraum verkaufsraum;
	private int bestellung;
	private int verweilszeit;
	
	public Kunde(Verkaufsraum verkaufsraum) {
		zaehler++;
		this.verkaufsraum = verkaufsraum;
		this.setName("Kunde-" + zaehler);
		
	}
	
	public int getVerweilszeit() {
		return verweilszeit;
	}
	
	public void setVerweilszeit(int verweilszeit) {
		this.verweilszeit = verweilszeit;
	}	
	
	public synchronized int getBestellung() {
		return bestellung;
	}

	public synchronized void bestellen() throws InterruptedException {
		
		bestellung = Utility.random(MIN_BESTELLUNG, MAX_BESTELLUNG);
		System.out.println(Thread.currentThread().getName() + " BESTELLT " + bestellung + " BURGER");
		this.notify();						
		this.wait();	// Wartet auf notify von Kasse
	}
	
	public void run() {	
		try {
			if (kommen()) {								
				bestellen();
				bezahlen();
				verlassen();							
			}
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT und HAUT AB");
			Thread.currentThread().interrupt();
			e.printStackTrace();	
		}	
		System.out.println(Thread.currentThread().getName() + " WURDE BEENDET");
	}

	/**
	 * 
	 */
	public boolean kommen() {
		System.out.println(Thread.currentThread().getName() + " KOMMT ");
		return verkaufsraum.betreten();
	}

	/**
	 * @throws InterruptedException 
	 * 
	 */
	public synchronized void verlassen() throws InterruptedException {
		Thread.sleep(Utility.random(MIN_ZEIT, MAX_ZEIT));
		verkaufsraum.verlassen();
	}	
	
	public synchronized void bezahlen() {
		System.out.println(Thread.currentThread().getName() + " BEZAHLT " + bestellung + " BURGER");
		this.notify();
	}

	public static class BestellungComparator implements Comparator<Kunde> {
		@Override
		public int compare(Kunde k1, Kunde k2) {
			return k1.getBestellung() - k2.getBestellung();
		}
	}
}
