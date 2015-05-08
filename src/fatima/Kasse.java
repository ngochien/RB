package fatima;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

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
	
	private Kasse andereKasse;
	private Kunde wartendeKunde;
	private int anzahlBestellungen;		
	private boolean zuWenigeBestellung;
		
	public Kasse(Warteschlange warteschlange, Laufband laufband) {
		zaehler++;		
		this.setName("KASSE-" + zaehler);
		this.bestellungen = new PriorityQueue<>(new Kunde.BestellungComparator());
		this.warteschlange = warteschlange;		
		this.laufband = laufband;
	}		
	
	public void setAndereKasse(Kasse andereKasse) {
		this.andereKasse = andereKasse;
	}
	
	public synchronized Kunde getWartendeKunde() {
		return wartendeKunde;
	}
	
	public synchronized void setWartendeKunde(Kunde wartendeKunde) {
		System.err.println(wartendeKunde.getName() + " HAT ZU LANGE GEWARTET");
		this.wartendeKunde = wartendeKunde;
	}
	
	public void run() {
		System.out.format("%s STARTET...\n", Thread.currentThread().getName());
		try {
			while (!isInterrupted()) {				
				int bestellung = bedienen(warteschlange);
				if (bestellung > 0) {
					mehrBestellung();
					meldeBestellung(bestellung);					
				}
				ausliefern();
			}
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
			Thread.currentThread().interrupt();							
		}
		// alle Kunden fertig bedient.
		System.err.println(Thread.currentThread().getName() + " WURDE BEENDET ");
	}

	public int bedienen(Warteschlange warteschlange) throws InterruptedException {
		final Kunde aktuelleKunde = warteschlange.remove();
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
		
				new Timer().schedule(new TimerTask() {					
					@Override
					public void run() {
						if (bestellungen.contains(aktuelleKunde)) {							
							setWartendeKunde(aktuelleKunde);						
						}
					}
				}, Lokal.MAX_WARTEZEIT);
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
	
	public synchronized void mehrBestellung() {
		anzahlBestellungen = anzahlBestellungen() + 1;
		
		System.out.format("\t\t\t\t%s HAT bis jetzt %d Bestellung(en) ANGENOMMEN\n\n",
							Thread.currentThread().getName(), anzahlBestellungen());
		
		checkPrioritaet();
	}
	
	private void checkPrioritaet() {
		zuWenigeBestellung = (andereKasse.anzahlBestellungen() - this.anzahlBestellungen() >= 3);
		
		if (zuWenigeBestellung || getWartendeKunde() != null) {
			Thread.currentThread().setPriority(MAX_PRIORITY);
			System.err.format("\n----------PRIORITÄT FÜR %s----------\n\n", Thread.currentThread().getName());			
		} else {	
			Thread.currentThread().setPriority(NORM_PRIORITY);
		}
	}
	
	public void ausliefern() throws InterruptedException {
		if (bestellungen.isEmpty()) {
			synchronized (warteschlange) {
				System.err.format("\t\t\t\t%s WARTET... KEINE KUNDE, KEINE BESTELLUNG\n",
														Thread.currentThread().getName());
				warteschlange.wait();
			}
		} else if (zuWenigeBestellung) {
			System.err.format("\t\t\t\t%s HAT mehr als 3 KUNDEN WENIGER BEDIENT. Erst WEITER BEDIENEN. "
							+ " AUSLIEFERUNG VERZÖGERN\n", Thread.currentThread().getName());
		} else if (getWartendeKunde() != null && ausliefern(wartendeKunde) == true) {
			setWartendeKunde(null);
			Thread.currentThread().setPriority(NORM_PRIORITY);
		} else {
			ausliefern(bestellungen.peek());				
		}
	}

	private boolean ausliefern(Kunde kunde) throws InterruptedException {		
		synchronized (kunde) {								
			if(laufband.remove(kunde.getBestellung())) {
				kunde.notify();
				
				System.out.format("\t\t\t\t%s WARTET AUF BEZAHLUNG VON %s\n",
									Thread.currentThread().getName(), kunde.getName());				
				kunde.wait();	// Warte bis der Kunde bezahlt hat
				
				bestellungen.remove(kunde);
				System.out.format("\t\t\t\t%s GIBT %d BURGER AN %s\n", Thread.currentThread().getName(),
									kunde.getBestellung(), kunde.getName());
				
				kunde.notify();
				return true;
			} else {
				return false;
			}
		}
	}
}
