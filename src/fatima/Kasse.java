package fatima;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Fatima
 *
 */
public class Kasse extends Thread {

	private static int zaehler = 0;
	
	private static final int MIN_BESTELLUNGSDAUER = 1 * 1000;
	private static final int MAX_BESTELLUNGSDAUER = 1 * 1000;	
	
	private List<Kunde> warteschlange;
	private Queue<Kunde> bestellungen;
	private Laufband laufband;	
	
	private Kasse andereKasse;
	
	private boolean prioritaet;
	
	public Kasse(Laufband laufband) {
		zaehler++;		
		this.setName("Kasse-" + zaehler);
		this.bestellungen = new PriorityQueue<>(new Kunde.BestellungComparator());
		this.warteschlange = new LinkedList<>();		
		this.laufband = laufband;
	}
	
	public void setAndereKasse(Kasse andereKasse) {
		this.andereKasse = andereKasse;
	}
	
	public void run() {
		System.out.format("%s STARTET...\n", Thread.currentThread().getName());
		while (!isInterrupted()) {
//			if (kann ausliefern) {
//				ausliefern
//			laufband.remove();
//			kassieren...
//			} else {
//				weiter bedienen
			int bestellung = bedienen();
			if (bestellung > 0) {
				erhoeheAnzahlBestellungen();				
				meldeBestellung(bestellung);							
			}
			if (!prioritaet) {
				ausliefern();	// Wenn er keine Priorität hat, dann arbeitet er normal weiter,
				// ansonsten versucht er sofort neue Bestellung anzunehmen.
			}	
		}
//		}
		// alle Kunden fertig bedient.
		System.out.println(Thread.currentThread().getName() + " BEENDET");
	}

	public synchronized void addKunde(Kunde kunde) {
		warteschlange.add(kunde);		
		System.out.println(Thread.currentThread().getName() +" ENTER " + kunde.getName() + 
				" in die Warteschlange von" + getName() + ": Länge = " + warteschlange.size());
	}
	
	/**
	 * @param bestellung
	 */
	public void meldeBestellung(int bestellung) {
		synchronized (Kueche.class) {
			Kueche.mehrBurger(bestellung);						
			Kueche.class.notifyAll();						
		}
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
		
		if (andereKasse.anzahlBestellungen() - this.anzahlBestellungen() >= 3) {
			System.err.format("\n----------PRIORITÄT FÜR %s----------\n\n", Thread.currentThread().getName());
			Thread.currentThread().setPriority(MAX_PRIORITY);
			prioritaet = true;
		} else {
			Thread.currentThread().setPriority(NORM_PRIORITY);
			prioritaet = false;
		}
	}
	
	public int bedienen() {
		Kunde aktuelleKunde = warteschlange.remove(0);
		int bestellung = 0;
		if (aktuelleKunde != null) {
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
					
					System.out.format("%s HAT %d Burger bei %s BESTELLT und WARTET nun...\n\n",
					aktuelleKunde.getName(), aktuelleKunde.getBestellung(), Thread.currentThread().getName());
					
					bestellungen.add(aktuelleKunde); // Wartezeit startet hier
					
				} catch (InterruptedException e) {
					System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
					Thread.currentThread().interrupt();
					e.printStackTrace();
					return 0;
				}
			}
		}
		return bestellung;
	}	
	
	public void ausliefern() {
		if (bestellungen.isEmpty() == false) {
			Kunde kunde = bestellungen.element();
			int anzahl = kunde.getBestellung();
			synchronized (kunde) {								
				if(laufband.remove(anzahl)) {
					kunde.notify();
					try {
						System.out.format("\t\t\t\t%s WARTET AUF BEZAHLUNG VON %s",
								Thread.currentThread().getName(), kunde.getName());
						kunde.wait();	// Warte bis der Kunde bezahlt hat
						bestellungen.remove(kunde);
						System.out.format("\t\t\t\t%s GIBT %d BURGER AN %s", Thread.currentThread().getName(),
											kunde.getBestellung(), kunde.getName());
					} catch (InterruptedException e) {
						System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
						Thread.currentThread().interrupt();
						e.printStackTrace();
					}						
				}
			}			
			
		}		
//		System.out.println("Die Service-Kraft übergibt die Burger erst, wenn der Kunde bezahlt hat.");
	}
}
