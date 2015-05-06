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
	
	private static final int MIN_BESTELLUNGSDAUER = 1 * 1000;
	private static final int MAX_BESTELLUNGSDAUER = 1 * 1000;	
	
	private Warteschlange warteschlange;
	private Laufband laufband;
	
	private ServiceKraft kollege;
		
	public ServiceKraft(Warteschlange warteschlange, Laufband laufband) {
		zaehler++;		
		this.setName("ServiceKraft-" + zaehler);
		this.warteschlange = warteschlange;		
		this.laufband = laufband;
	}
	
	public void setKollege(ServiceKraft kollege) {
		this.kollege = kollege;
	}
	
	public void run() {
		while (!isInterrupted()) {
//			if (kann ausliefern) {
//				ausliefern
//			laufband.remove();
//			kassieren...
//			} else {
//				weiter bedienen
			Bestellung bestellung = bedienen(warteschlange);
			if (bestellung != null) {
				erhoeheAnzahlBestellungen();
				System.out.format("\t\t\t\t%s hat bis jetzt %d Bestellungen ANGENOMMEN\n\n",
								Thread.currentThread().getName(), anzahlBestellungen());
				if (kollege.anzahlBestellungen() - this.anzahlBestellungen() >= 3) {
					System.err.format("\n----------PRIORITÄT FÜR %s----------\n\n", Thread.currentThread().getName());
					Thread.currentThread().setPriority(MAX_PRIORITY);
				} else {
					Thread.currentThread().setPriority(NORM_PRIORITY);
					synchronized (KuecheKraft.class) {
						KuecheKraft.mehrBurger(bestellung.anzahl());
						KuecheKraft.class.notifyAll();						
					}
				}
			}
//		}			
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
	public synchronized int anzahlBestellungen() {
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
				Thread.currentThread().getName(), bestellung.id(), naechsteKunde.getName());
				
				Thread.sleep(bestellung.dauer());
				
				System.out.format("\t\t\t\t%s HAT %d Burger bei %s BESTELLT und WARTET nun...\n\n",
				naechsteKunde.getName(), bestellung.anzahl(), Thread.currentThread().getName());
				
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return null;
			}
		}						
		return bestellung;
	}	
	
	void BurgerEntnehemen() {
		System.out.println("Weiter können die Service-Kräfte die Burger	nur in der Folge entnehmen,	wie	die	Burger auf das Lieferband gestellt worden sind (Queue).");
	}
	
	void BurgerUebernehmen() {
		System.out.println(" Die Service-Kraft übergibt die Burger erst, wenn der Kunde bezahlt hat.");
	}
}
