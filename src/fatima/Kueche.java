/**
 * 
 */
package fatima;

/**
 * @author Fatima
 *
 */
public class Kueche extends Thread {

	/* Dummy-Zähler für internen Gebrauch */
	private static int zaehler1 = 0;
	private static int zaehler2 = 0;
	
	/**
	 * Jede  Hilfskraft  braucht   zufällig  verteilt zwischen  10   und  20
	 *  Sekunden  für  die  Fertigstellung  eines  Burger.
	 */
	private static final int MIN_ZUBEREITUNGSZEIT = 3 * 1000;
	private static final int MAX_ZUBEREITUNGSZEIT = 5 * 1000;
	
	/**
	 * Anzahl der zu produzierenden Burger wenn keine Kundenbestellung
	 */
	private static final int MIN_ANZAHL_BURGER = 2;
	private static final int MAX_ANZAHL_BURGER = 5;		
	
	/*
	 * Anzahl der noch zu produzierenden Burger.
	 * 
	 * Am Anfang werden zufällig 2 bis 5 Burger produziert. Da alle Bestellungen schließlich
	 * ausgeliefert und keine Burger weggeschmissen werden, bleiben diese vorproduzierte Burger
	 * immer auf dem Laufband. Somit ist die Firmenpolitik auch erfüllt worden: niemals mehr als 5
	 * und mindestens 2 Burger ohne Bestellungen vorzuhalten!
	 */
	private static int anzahlBurger = Helper.random(MIN_ANZAHL_BURGER, MAX_ANZAHL_BURGER);
	
	private Laufband laufband;
	
	public Kueche(Laufband laufband) {
		zaehler1++;		
		this.setName("KUECHE-" + zaehler1);				
		this.laufband = laufband;
	}

	public void run() {
		System.out.format("%s STARTET...\n", Thread.currentThread().getName());
		while (!isInterrupted()) {	
			arbeiten();
		}
		System.err.println(Thread.currentThread().getName() + " WURDE BEENDET ");
	}
	
	public void arbeiten() {
		try {
		synchronized (getClass()) {
			while (anzahlBurger() == 0) {				
				System.out.println(Thread.currentThread().getName() + " WARTET... KEINE BESTELLUNG\n");
				getClass().wait();						
			}
			wenigerBurger();	// Ein Burger wird sicherlich gemacht!
		}
		// außerhalb des Synchronized-Blocks, damit alle parallel arbeiten können
		Thread.sleep(Helper.random(MIN_ZUBEREITUNGSZEIT, MAX_ZUBEREITUNGSZEIT));
		laufband.add("Burger-" + ++zaehler2);
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
			Thread.currentThread().interrupt();			
		}
	}
	
	public static synchronized int anzahlBurger() {		
		return anzahlBurger;
	}
	
	public static synchronized void mehrBurger(int anzahl) {
		anzahlBurger = anzahlBurger + anzahl;
		System.out.format("\t\t\t\t%s MELDET eine Bestellung von %d BURGER - " + "NOCH ZU MACHEN: %d BURGER\n",
							Thread.currentThread().getName(), anzahl , anzahlBurger);
	}
	
	public static synchronized void wenigerBurger() {
		anzahlBurger = anzahlBurger - 1;
		System.out.format("%s MACHT nun 1 BURGER... - NOCH ZU MACHEN: %d BURGER\n",
							Thread.currentThread().getName(), anzahlBurger);
	}
	
	public static void main(String[] args) {
		
	}
}
