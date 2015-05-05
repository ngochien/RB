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
	
	private int anzahl;
	
	public Bestellung() {
		anzahl = Utility.random(MIN_BESTELLUNG, MAX_BESTELLUNG);
	}
	
	public int getAnzahl() {
		return anzahl;
	}
}
