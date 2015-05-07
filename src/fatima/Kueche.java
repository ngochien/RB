/**
 * 
 */
package fatima;

/**
 * @author le
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
	private static int anzahlBurger = Utility.random(MIN_ANZAHL_BURGER, MAX_ANZAHL_BURGER);
	
	private Laufband laufband;
	private BestellungQueue bestellungen;
	
	public Kueche(BestellungQueue bestellungen, Laufband laufband) {
		zaehler1++;		
		this.setName("KuecheKraft-" + zaehler1);		
		this.bestellungen = bestellungen;
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
				wenigerBurger();	// Ein Burger wird sicherlich gemacht!
			}
			arbeiten();	// außerhalb des Synchronized-Blocks, damit alle parallel arbeiten können
		}
	}
	
	public void arbeiten() {
		try {
			Thread.sleep(Utility.random(MIN_ZUBEREITUNGSZEIT, MAX_ZUBEREITUNGSZEIT));
			laufband.enter("Burger-" + ++zaehler2);			
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE beim Schlafen GEWECKT");
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}
	
	public static synchronized int anzahlBurger() {
		System.out.format("\t\t\t\tNOCH ZU MACHEN: %d BURGER\n", anzahlBurger);
		return anzahlBurger;
	}
	
	public static synchronized void mehrBurger(int anzahl) {
		anzahlBurger = anzahlBurger + anzahl;
		System.out.format("\t\t\t\t%s MELDET eine Bestellung von %d BURGER\n",
							Thread.currentThread().getName(), anzahl);
		System.out.format("\t\t\t\tNOCH ZU MACHEN: %d BURGER\n", anzahlBurger);
	}
	
	public static synchronized void wenigerBurger() {
		anzahlBurger = anzahlBurger - 1;
		
		/* Wenn keine Burger zu machen sind (d.h Kasse meldet noch keine Bestellung -
		 * egal ob das Laufband leer oder voll ist), werden zufällig 2 bis 5 Burger produziert
		 */
//		if (anzahlBurger() < 0) {			
//			anzahlBurger = Utility.random(MIN_ANZAHL_BURGER, MAX_ANZAHL_BURGER);
//			System.out.format("\t\t\t\tKEINE BESTELLUNG - %d BURGER\n", anzahlBurger());
//		}
		System.out.format("\t\t\t\t%s MACHT nun 1 BURGER...\n", Thread.currentThread().getName());
		System.out.format("\t\t\t\tNOCH ZU MACHEN: %d BURGER\n", anzahlBurger);
	}
	
	public static void main(String[] args) {
		
	}
}
