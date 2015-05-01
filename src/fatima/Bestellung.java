/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class Bestellung {

	static final int MIN_BURGER = 1;
	static final int MAX_BURGER = 8;
	
	private int anzahlBurger;
	
	public Bestellung() {
		anzahlBurger = Utility.random(MIN_BURGER, MAX_BURGER);
	}
	
	int getAnzahlBurger() {
		return anzahlBurger;
	}
}
