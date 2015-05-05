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
		
	public ServiceKraft(Verkaufsraum verkaufsraum, Warteschlange warteschlange) {
		zaehler++;		
		this.setName("ServiceKraft-" + zaehler);
		this.verkaufsraum = verkaufsraum;
		this.warteschlange = warteschlange;
	}
	
	public void run() {
		while (!isInterrupted()) {
			verkaufsraum.bedienen(warteschlange);
		}
		System.out.println(this.getName() + " wurde beendet");
	}
	
	/**
	 * Die Anzahl der angenommenen Bestellungen
	 */
	private int bestellungen;
	
	/**
	 * Liefert die Anzahl der angenommenen Bestellungen zurück.
	 * 
	 * @return
	 */
	public int getBestellungen() {
		return 0;
	}
	
	int getBedienungzeit() {
		return Utility.random(MIN_BEDIENUNGSDAUER, MAX_BEDIENUNGSDAUER);
	}
	
	/**
	 * Erhöht die Anzahl der bisher angenommenen Bestellungen. 
	 */
	void bestellungenErhoehen() {
		
	}	
	
	void BurgerEntnehemen() {
		System.out.println("Weiter können die Service-Kräfte die Burger	nur in der Folge entnehmen,	wie	die	Burger auf das Lieferband gestellt worden sind (Queue).");
	}
	
	void BurgerUebernehmen() {
		System.out.println(" Die Service-Kraft übergibt die Burger erst, wenn der Kunde bezahlt hat.");
	}
}
