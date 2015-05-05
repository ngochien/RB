/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class Kunde extends Thread {

	private static int zaehler = 0;	
	
	/**
	 * Die Zeit, die der Kunde nach dem Erhalt der Ware braucht, bis er das Lokal verlassen hat.
	 */	
	
	private Verkaufsraum verkaufsraum;
	
	private int verweilszeit;
	
	public Kunde(Verkaufsraum verkaufsraum) {
		zaehler++;
		this.verkaufsraum = verkaufsraum;
		this.setName("Kunde-" + zaehler);
		
	}
	
	public int getVerweilszeit() {
		return verweilszeit;
	}
	
	public void setVerweilszeit(int verweilszeit) {
		this.verweilszeit = verweilszeit;
	}
	
	public void run() {
		System.out.println(Thread.currentThread().getName() + " kommt");
		verkaufsraum.betreten();
		if (!isInterrupted()) {
			verkaufsraum.sichEinreihen();
		}
		System.out.println(this.getName() + " wurde beendet");
	}
		
	
	void bezahlen() {
		System.out.println("Der Kunde zahlt erst, wenn er die Ware vollständig sieht.");
	}
	
	void verlassen() {
		System.out.println("Nach dem Erhalt der Ware braucht der Kunde weitere 10 bis 20  Sekunden (zufällig) bis er das Lokal verlassen hat.");
	}
	
//	int getZeitZuVerlassen() {
//		return Utility.random(MIN_ZEIT_ZU_VERLASSEN, MAX_ZEIT_ZU_VERLASSEN);
//	}
}
