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
		System.out.println(Thread.currentThread().getName() + " BESTELLT " + bestellung + " Burger");
		this.notify();						
		this.wait();	
	}
	
	public void run() {
		System.out.println(Thread.currentThread().getName() + " KOMMT ");
		verkaufsraum.betreten();
		if (!isInterrupted()) {	
			try {
				sichEinreihen(verkaufsraum.getAktuelleWarteschlange());
				bestellen();
				// ...
				verkaufsraum.verlassen();
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName() + " WURDE beim Warten GEWECKT und HAUT AB");
				Thread.currentThread().interrupt();
				e.printStackTrace();	
			}						
		}
		System.out.println(Thread.currentThread().getName() + " WURDE BEENDET");
	}
		
	public synchronized void sichEinreihen(Warteschlange warteschlange) throws InterruptedException {
		warteschlange.add(this);	
		this.wait();
	}
	
	void bezahlen() {
		System.out.println("Der Kunde zahlt erst, wenn er die Ware vollständig sieht.");
	}
	
	void verlassen() {
		System.out.println("Nach dem Erhalt der Ware braucht der Kunde weitere 10 bis 20  Sekunden (zufällig) bis er das Lokal verlassen hat.");
	}
	
//	int getZeitZuVerlassen() {
//		return Utility.random(MIN_ZEIT_ZU_VERLASSEN, MAX_ZEIT_ZU_VERLASSEN);
//	}

	public static class BestellungComparator implements Comparator<Kunde> {
		@Override
		public int compare(Kunde k1, Kunde k2) {
			return k1.getBestellung() - k2.getBestellung();
		}
	}
}
