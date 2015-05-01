/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class ServiceKraft {

	/**
	 * Die Annahme der Bestellung dauert zuf�llig zwischen 10 und 5 Sekunden. 
	 */
	static final int MIN_BEDIENUNGSZEIT = 5;
	static final int MAX_BEDIENUNGSZEIT = 10;
	
	private String name;
	
	/**
	 * Die Anzahl der angenommenen Bestellungen
	 */
	private int bestellungen;
	
	public String getName() {
		return name;
	}
	
	/**
	 * Liefert die Anzahl der angenommenen Bestellungen zur�ck.
	 * 
	 * @return
	 */
	public int getBestellungen() {
		return 0;
	}
	
	int getBedienungzeit() {
		return Utility.random(MIN_BEDIENUNGSZEIT, MAX_BEDIENUNGSZEIT);
	}
	
	/**
	 * Erh�ht die Anzahl der bisher angenommenen Bestellungen. 
	 */
	void bestellungenErhoehen() {
		
	}
	
	void bedienen() {
		// TODO
		// Die �Bestellungen �werden ��an �die �K�che 
		// weitergeleitet �und �nach �Einreichungszeitpunkt �abgearbeitet. �
	}
	
	void BurgerEntnehemen() {
		System.out.println("Weiter k�nnen die Service-Kr�fte die Burger	nur in der Folge entnehmen,	wie	die	Burger auf das Lieferband gestellt worden sind (Queue).");
	}
	
	void BurgerUebernehmen() {
		System.out.println(" Die Service-Kraft �bergibt die Burger erst, wenn der Kunde bezahlt hat.");
	}
}
