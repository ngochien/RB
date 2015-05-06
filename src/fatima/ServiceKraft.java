/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class ServiceKraft extends Thread {

	private static int zaehler = 0;
	
	/**
	 * Die Annahme der Bestellung dauert zufällig zwischen 10 und 5 Sekunden. 
	 */
	private static final int MIN_BEDIENUNGSDAUER = 5;
	private static final int MAX_BEDIENUNGSDAUER = 10;	
	
//	private Verkaufsraum verkaufsraum;
	private Warteschlange warteschlange;
	
	private ServiceKraft kollege;
		
	public ServiceKraft(Warteschlange warteschlange) {
		zaehler++;		
		this.setName("ServiceKraft-" + zaehler);
//		this.verkaufsraum = verkaufsraum;
		this.warteschlange = warteschlange;		
	}
	
	public void setKollege(ServiceKraft kollege) {
		this.kollege = kollege;
	}
	
	public void run() {
		while (!isInterrupted()) {
			if (bedienen(warteschlange) != null) {
				erhoeheAnzahlBestellungen();
				System.out.format("\t\t\t\t%s hat bis jetzt %d Bestellungen ANGENOMMEN\n\n",
								Thread.currentThread().getName(), getAnzahlBestellungen());
				if (kollege.getAnzahlBestellungen() - this.getAnzahlBestellungen() >= 3) {
					System.err.format("\n----------PRIORITÄT FÜR %s----------\n\n", Thread.currentThread().getName());
					Thread.currentThread().setPriority(MAX_PRIORITY);
				} else {
					Thread.currentThread().setPriority(NORM_PRIORITY);
				}
			}
		}
		System.out.println(Thread.currentThread().getName() + " WURDE BEENDET");
	}
	
	/**
	 * Die Anzahl der angenommenen Bestellungen
	 */
	private int anzahlBestellungen;
	
	/**
	 * Liefert die Anzahl der angenommenen Bestellungen zurück.
	 * 
	 * @return
	 */
	public synchronized int getAnzahlBestellungen() {
		return anzahlBestellungen;
	}
	
	public synchronized void erhoeheAnzahlBestellungen() {
		anzahlBestellungen++;
	}
	
	public Bestellung bedienen(Warteschlange warteschlange) {
		Kunde naechsteKunde = warteschlange.remove();
		Bestellung bestellung = null;
		synchronized (naechsteKunde) {
			naechsteKunde.notify();
			try {
				while (naechsteKunde.getBestellung() == null) {
					System.out.println("\t\t\t\t" + Thread.currentThread().getName()
									+ " WARTET auf Bestellung von " + naechsteKunde.getName());
					naechsteKunde.wait();
				}
				bestellung = naechsteKunde.getBestellung();
				
				System.out.format("\t\t\t\t%s NIMMT gerade %s von %s AN...\n",
				Thread.currentThread().getName(), bestellung.getId(), naechsteKunde.getName());
				
				Thread.sleep(bestellung.getDauer());
				
				System.out.format("\t\t\t\t%s HAT %d Burger bei %s BESTELLT und WARTET nun...\n\n",
				naechsteKunde.getName(), bestellung.getAnzahl(), Thread.currentThread().getName());
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return null;
			}
		}						
		return bestellung;
	}
	
	int getBedienungzeit() {
		return Utility.random(MIN_BEDIENUNGSDAUER, MAX_BEDIENUNGSDAUER);
	}	
	
	void BurgerEntnehemen() {
		System.out.println("Weiter können die Service-Kräfte die Burger	nur in der Folge entnehmen,	wie	die	Burger auf das Lieferband gestellt worden sind (Queue).");
	}
	
	void BurgerUebernehmen() {
		System.out.println(" Die Service-Kraft übergibt die Burger erst, wenn der Kunde bezahlt hat.");
	}
}
