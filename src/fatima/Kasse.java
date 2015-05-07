package fatima;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Fatima
 *
 */
public class Kasse extends Thread {

	private static int zaehler = 0;
	
	private static final int MIN_BESTELLUNGSDAUER = 5 * 1000;
	private static final int MAX_BESTELLUNGSDAUER = 10 * 1000;	
	
	private Warteschlange warteschlange;
	private Queue<Kunde> bestellungen;
	private Laufband laufband;	
	
	/**
	 * Die Anzahl der angenommenen Bestellungen
	 */
	private int anzahlBestellungen;
	
	private Kasse andereKasse;
	
	private boolean prioritaet;
	
	public Kasse(Warteschlange warteschlange, Laufband laufband) {
		zaehler++;		
		this.setName("Kasse-" + zaehler);
		this.bestellungen = new PriorityQueue<>(new Kunde.BestellungComparator());
		this.warteschlange = warteschlange;		
		this.laufband = laufband;
	}	
	
	public void setAndereKasse(Kasse andereKasse) {
		this.andereKasse = andereKasse;
	}
	
	public void run() {
		System.out.format("%s STARTET...\n", Thread.currentThread().getName());
		try {
			while (!isInterrupted()) {				
				int bestellung = bedienen(warteschlange);
				if (bestellung > 0) {
					erhoeheAnzahlBestellungen();				
					meldeBestellung(bestellung);							
				}
				if (!prioritaet) {
					ausliefern();	// Wenn er keine Priorität hat, dann arbeitet er normal weiter,
									// ansonsten versucht er sofort neue Bestellung anzunehmen.
				}			
			}
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
			Thread.currentThread().interrupt();							
		}
		// alle Kunden fertig bedient.
		System.err.println(Thread.currentThread().getName() + " WURDE BEENDET ");
	}

	public int bedienen(Warteschlange warteschlange) throws InterruptedException {
		Kunde aktuelleKunde = warteschlange.remove();
		int bestellung = 0;
		if (aktuelleKunde != null) {
			synchronized (aktuelleKunde) {
				aktuelleKunde.notify();				
				while (aktuelleKunde.getBestellung() == 0) {
					System.out.println("\t\t\t\t" + Thread.currentThread().getName()
									+ " WARTET auf Bestellung von " + aktuelleKunde.getName());
					aktuelleKunde.wait();
				}
				bestellung = aktuelleKunde.getBestellung();
				
				System.out.format("\t\t\t\t%s NIMMT gerade Bestellung von %s AN...\n",
									Thread.currentThread().getName(), aktuelleKunde.getName());
				
				Thread.sleep(Helper.random(MIN_BESTELLUNGSDAUER, MAX_BESTELLUNGSDAUER));
				
				System.out.format("%s HAT %d Burger bei %s BESTELLT und WARTET nun...\n\n",
				aktuelleKunde.getName(), aktuelleKunde.getBestellung(), Thread.currentThread().getName());
				
				bestellungen.add(aktuelleKunde); // Wartezeit startet hier							
			}
		}
		return bestellung;
	}	
	
	public void meldeBestellung(int bestellung) {
		synchronized (Kueche.class) {
			Kueche.mehrBurger(bestellung);						
			Kueche.class.notifyAll();						
		}
	}
		
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
	
	public void ausliefern() throws InterruptedException {
		if (bestellungen.isEmpty() == false) {
			Kunde kunde = bestellungen.peek();
			int anzahl = kunde.getBestellung();
			synchronized (kunde) {								
				if(laufband.remove(anzahl)) {
					kunde.notify();					
					System.out.format("\t\t\t\t%s WARTET AUF BEZAHLUNG VON %s\n",
							Thread.currentThread().getName(), kunde.getName());
					
					kunde.wait();	// Warte bis der Kunde bezahlt hat
					bestellungen.remove(kunde);
					System.out.format("\t\t\t\t%s GIBT %d BURGER AN %s\n", Thread.currentThread().getName(),
										kunde.getBestellung(), kunde.getName());
					kunde.notify();										
				}
			}			
			
		}
	}
}
