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
	 * jede �Hilfskraft �braucht ��zuf�llig �verteilt zwischen �10 ��und �20
	 * �Sekunden �f�r �die �Fertigstellung �eines �Burger.
	 */
	static final int MIN_ZUBEREITUNGSZEIT = 10;
	static final int MAX_ZUBEREITUNGSZEIT = 20;

	
	public KuecheKraft() {
		// TODO Auto-generated constructor stub
	}
	
	int getZubereitungszeit() {
		return Utility.random(MIN_ZUBEREITUNGSZEIT, MAX_ZUBEREITUNGSZEIT);
	}

	void Burgerzubereiten() {
		
	}
	
	void BurgerAuflegen() {

	}
}
