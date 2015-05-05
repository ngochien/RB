/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class ServiceKraft extends Thread {

	private static int zaehler = 0;
	
	/**
	 * Die Annahme der Bestellung dauert zufällig zwischen 10 und 5 Sekunden. 
	 */
	private static final int MIN_BEDIENUNGSDAUER = 5;
	private static final int MAX_BEDIENUNGSDAUER = 10;	
	
	private Verkaufsraum verkaufsraum;
	private Warteschlange warteschlange;
	
	private ServiceKraft kollege;
		
	public ServiceKraft(Verkaufsraum verkaufsraum, Warteschlange warteschlange) {
		zaehler++;		
		this.setName("ServiceKraft-" + zaehler);
		this.verkaufsraum = verkaufsraum;
		this.warteschlange = warteschlange;		
	}
	
	public void setKollege(ServiceKraft kollege) {
		this.kollege = kollege;
	}
	
	public void run() {
		while (!isInterrupted()) {
			if (verkaufsraum.bedienen(warteschlange) != null) {
				anzahlBestellungen++;
				System.out.format("\t\t\t\t%s hat bis jetzt %d Bestellungen angenommen\n\n",
								Thread.currentThread().getName(), anzahlBestellungen);
				if (kollege.anzahlBestellungen - this.anzahlBestellungen >= 3) {
					System.err.format("----------PRIORITÄT FÜR %s----------\n\n", Thread.currentThread().getName());
					Thread.currentThread().setPriority(MAX_PRIORITY);
				} else {
					Thread.currentThread().setPriority(NORM_PRIORITY);
				}
			}
		}
		System.out.println(Thread.currentThread().getName() + " wurde beendet");
	}
	
	/**
	 * Die Anzahl der angenommenen Bestellungen
	 */
	private int anzahlBestellungen;
	
	/**
	 * Liefert die Anzahl der angenommenen Bestellungen zurück.
	 * 
	 * @return
	 */
	public int getAnzahlBestellungen() {
		return anzahlBestellungen;
	}
	
	int getBedienungzeit() {
		return Utility.random(MIN_BEDIENUNGSDAUER, MAX_BEDIENUNGSDAUER);
	}	
	
	void BurgerEntnehemen() {
		System.out.println("Weiter können die Service-Kräfte die Burger	nur in der Folge entnehmen,	wie	die	Burger auf das Lieferband gestellt worden sind (Queue).");
	}
	
	void BurgerUebernehmen() {
		System.out.println(" Die Service-Kraft übergibt die Burger erst, wenn der Kunde bezahlt hat.");
	}
}
