/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class Bestellung {

	/**
	 * Ein Kunde bestellt zufällig verteilt zwischen 1 und 8 Burger. 
	 */
	private static final int MIN_BESTELLUNG = 1;
	private static final int MAX_BESTELLUNG = 3;
	
	private static final int MIN_BESTELLUNGSDAUER = 1 * 1000;
	private static final int MAX_BESTELLUNGSDAUER = 1 * 1000;	
	
	private static int zaehler = 0;
	private String id;
	private int anzahl;
	private int dauer;
	
	public Bestellung() {
		zaehler++;
		id = "Bestellung-" + zaehler;
		anzahl = Helper.random(MIN_BESTELLUNG, MAX_BESTELLUNG);
		dauer = Helper.random(MIN_BESTELLUNGSDAUER, MAX_BESTELLUNGSDAUER);
	}
	
	public String id() {
		return id;
	}
	
	public int anzahl() {
		return anzahl;
	}
	
	public int dauer() {
		return dauer;
	}
}
