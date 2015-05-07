/**
 * 
 */
package fatima;

import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author le
 *
 */
public class Kasse extends Thread {

	private static int zaehler = 0;
	
	private static final int MIN_BESTELLUNGSDAUER = 1 * 1000;
	private static final int MAX_BESTELLUNGSDAUER = 1 * 1000;	
	
	private BestellungQueue bestellungen;
	private Warteschlange warteschlange;
	private Laufband laufband;	
	
	private Kasse naechsteKasse;
		
	public Kasse(Warteschlange warteschlange, BestellungQueue bestellungen, Laufband laufband) {
		zaehler++;		
		this.setName("Kasse-" + zaehler);		
		this.warteschlange = warteschlange;				
		this.bestellungen = bestellungen;
		this.laufband = laufband;
	}
	
	public void setNaechsteKasse(Kasse naechsteKasse) {
		this.naechsteKasse = naechsteKasse;
	}
	
	public void run() {
		while (!isInterrupted()) {
//			if (kann ausliefern) {
//				ausliefern
//			laufband.remove();
//			kassieren...
//			} else {
//				weiter bedienen
			int bestellung = bedienen(warteschlange);
			if (bestellung >= 0) {
				erhoeheAnzahlBestellungen();				
				if (naechsteKasse.anzahlBestellungen() - this.anzahlBestellungen() >= 3) {
					System.err.format("\n----------PRIORITÄT FÜR %s----------\n\n", Thread.currentThread().getName());
					Thread.currentThread().setPriority(MAX_PRIORITY);
				} else {
					Thread.currentThread().setPriority(NORM_PRIORITY);
					synchronized (Kueche.class) {
						Kueche.mehrBurger(bestellung);						
						Kueche.class.notifyAll();						
					}
					ausliefern();
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
		anzahlBestellungen = anzahlBestellungen() + 1;
		System.out.format("\t\t\t\t%s HAT bis jetzt %d Bestellung(en) ANGENOMMEN\n\n",
							Thread.currentThread().getName(), anzahlBestellungen());
	}
	
	public int bedienen(Warteschlange warteschlange) {
		Kunde aktuelleKunde = warteschlange.remove();
		int bestellung = 0;
		synchronized (aktuelleKunde) {
			aktuelleKunde.notify();
			try {
				while (aktuelleKunde.getBestellung() == 0) {
					System.out.println("\t\t\t\t" + Thread.currentThread().getName()
									+ " WARTET auf Bestellung von " + aktuelleKunde.getName());
					aktuelleKunde.wait();
				}
				bestellung = aktuelleKunde.getBestellung();
				
				System.out.format("\t\t\t\t%s NIMMT gerade Bestellung von %s AN...\n",
									Thread.currentThread().getName(), aktuelleKunde.getName());
				
				Thread.sleep(Utility.random(MIN_BESTELLUNGSDAUER, MAX_BESTELLUNGSDAUER));
				
				System.out.format("\t\t\t\t%s HAT %d Burger bei %s BESTELLT und WARTET nun...\n\n",
				aktuelleKunde.getName(), aktuelleKunde.getBestellung(), Thread.currentThread().getName());
				
				bestellungen.enter(aktuelleKunde); // Wartezeit startet hier
				
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return 0;
			}
		}						
		return bestellung;
	}	
	
	public void ausliefern() {
//		System.out.println("Die Service-Kraft übergibt die Burger erst, wenn der Kunde bezahlt hat.");
	}
}
