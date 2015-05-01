/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class KuecheKraft {

	/**
	 * jede  Hilfskraft  braucht   zufällig  verteilt zwischen  10   und  20
	 *  Sekunden  für  die  Fertigstellung  eines  Burger.
	 */
	static final int MIN_ZUBEREITUNGSZEIT = 10;
	static final int MAX_ZUBEREITUNGSZEIT = 20;

	int getZubereitungszeit() {
		return Utility.random(MIN_ZUBEREITUNGSZEIT, MAX_ZUBEREITUNGSZEIT);
	}

	void Burgerzubereiten() {
		
	}
	
	void BurgerAuflegen() {

	}
}
