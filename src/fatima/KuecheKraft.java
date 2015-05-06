/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class KuecheKraft extends Thread {

	/**
	 * jede  Hilfskraft  braucht   zufällig  verteilt zwischen  10   und  20
	 *  Sekunden  für  die  Fertigstellung  eines  Burger.
	 */
	private static final int MIN_ZUBEREITUNGSZEIT = 3 * 1000;
	private static final int MAX_ZUBEREITUNGSZEIT = 5 * 1000;
	
	/**
	 * Dummy-Zähler für internen Gebrauch
	 */
	private static int zaehler1 = 0;
	private static int zaehler2 = 0;		
	
	/**
	 * Anzahl der noch zu produzierenden Burger
	 */
	private static int anzahlBurger = 0;
	
	private Laufband laufband;
	
	public KuecheKraft(Laufband laufband) {
		zaehler1++;		
		this.setName("KuecheKraft-" + zaehler1);
		this.laufband = laufband;
	}

	public void run() {
		while (!isInterrupted()) {
			synchronized (getClass()) {
				while (anzahlBurger() == 0) {
					try {
						System.out.println(Thread.currentThread().getName() + " WARTET, weil nichts zu tun ist...");
						getClass().wait();
					} catch (InterruptedException e) {
						System.err.println(Thread.currentThread().getName() + " WURDE beim Warten GEWECKT");
						Thread.currentThread().interrupt();
						e.printStackTrace();
					}
				}
				arbeiten();
			}
		}
	}
	
	public void arbeiten() {
		try {
			Thread.sleep(Utility.random(MIN_ZUBEREITUNGSZEIT, MAX_ZUBEREITUNGSZEIT));
			laufband.enter("Burger-" + ++zaehler2);
			wenigerBurger();
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE beim Schlafen GEWECKT");
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}
	
	public static synchronized int anzahlBurger() {
		return anzahlBurger;
	}
	
	public static synchronized void mehrBurger(int anzahl) {
		anzahlBurger += anzahl;
	}
	
	public static synchronized void wenigerBurger() {
		anzahlBurger --;
	}
}
