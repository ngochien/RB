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
	private static final int MAX_BESTELLUNG = 8;
	
	private static final int MIN_BESTELLUNGSDAUER = 5 * 1000;
	private static final int MAX_BESTELLUNGSDAUER = 10 * 1000;	
	
	private static int zaehler = 0;
	private String id;
	private int anzahl;
	private int dauer;
	
	public Bestellung() {
		zaehler++;
		id = "Bestellung-" + zaehler;
		anzahl = Utility.random(MIN_BESTELLUNG, MAX_BESTELLUNG);
		dauer = Utility.random(MIN_BESTELLUNGSDAUER, MAX_BESTELLUNGSDAUER);
	}
	
	public String getId() {
		return id;
	}
	
	public int getAnzahl() {
		return anzahl;
	}
	
	public int getDauer() {
		return dauer;
	}
}
