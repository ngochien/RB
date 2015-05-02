/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class Kunde {

	/**
	 * Die Zeit, die der Kunde nach dem Erhalt der Ware braucht, bis er das Lokal verlassen hat.
	 */
	static final int MIN_ZEIT_ZU_VERLASSEN = 10;
	static final int MAX_ZEIT_ZU_VERLASSEN = 20;
	
	int verweilszeit;
	
	public int getVerweilszeit() {
		return verweilszeit;
	}
	
	public void setVerweilszeit(int verweilszeit) {
		this.verweilszeit = verweilszeit;
	}
	
	public Kunde() {
		// TODO Auto-generated constructor stub
	}
	
	void bestellen() {
		System.out.println("Ein Kunde bestellt zufällig verteilt zwischen 1 und 8 Burger.");
	}
	
	void bezahlen() {
		System.out.println("Der Kunde zahlt erst, wenn er die Ware vollständig sieht.");
	}
	
	void verlassen() {
		System.out.println("Nach dem Erhalt der Ware braucht der Kunde weitere 10 bis 20  Sekunden (zufällig) bis er das Lokal verlassen hat.");
	}
	
	int getZeitZuVerlassen() {
		return Utility.random(MIN_ZEIT_ZU_VERLASSEN, MAX_ZEIT_ZU_VERLASSEN);
	}
}
