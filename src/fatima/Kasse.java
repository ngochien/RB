package fatima;

import java.util.LinkedList;
import java.util.List;
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
	
	private Scheduler scheduler;
	
	private Laufband laufband;	
	private Warteschlange warteschlange;
		
	private Kasse andereKasse;	
	private int anzahlBestellungen;			
		
	public Kasse(Warteschlange warteschlange, Laufband laufband) {
		zaehler++;		
		this.setName("KASSE-" + zaehler);
		this.laufband = laufband;
		this.scheduler = new Scheduler();
		this.warteschlange = warteschlange;		
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
					mehrBestellung();
					meldeBestellung(bestellung);					
				}				
				ausliefern();
			}
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE GEWECKT");
			Thread.currentThread().interrupt();							
		}		
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
				
				scheduler.addKunde(aktuelleKunde);	
				new Timer().schedule(new TimerTask() {					
					@Override
					public void run() {	
						System.err.print("TASK STARTET - ");
						scheduler.addWartendeKunde(aktuelleKunde);						
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
	}
	
	public void ausliefern() throws InterruptedException {	
		scheduler.setPrioritaet();
		Kunde naechsteKunde = scheduler.naechsteKunde();
		if (naechsteKunde != null) {
			ausliefern(naechsteKunde);				
		} else {
			synchronized (warteschlange) {
				System.err.format("\t\t\t\t%s WARTET... KEINE KUNDE, KEINE BESTELLUNG\n",
														Thread.currentThread().getName());
				warteschlange.wait();
			}
		}
	}

	private void ausliefern(Kunde kunde) throws InterruptedException {	
//		System.out.format("\t\t\t\t%s VERSUCHT nun Bestellung von %s AUSZULIEFERN\n",
//							Thread.currentThread().getName(), kunde.getName());
		synchronized (kunde) {								
			if(laufband.remove(kunde.getBestellung())) {				
				kunde.notify();
				
				System.out.format("\t\t\t\t%s WARTET AUF BEZAHLUNG VON %s\n",
									Thread.currentThread().getName(), kunde.getName());				
				kunde.wait();	// Warte bis der Kunde bezahlt hat
				
				scheduler.removeKunde(kunde);
				System.out.format("\t\t\t\t%s GIBT %d BURGER AN %s\n", Thread.currentThread().getName(),
									kunde.getBestellung(), kunde.getName());
				
				kunde.notify();	
			}
		}
	}
	
	private class Scheduler {
		Queue<Kunde> bestellungen;
		List<Kunde> wartendeKunden;		
		
		Scheduler() {
			bestellungen = new PriorityQueue<>(new Kunde.BestellungComparator());
			wartendeKunden = new LinkedList<>();
		}
		
		synchronized void addKunde(final Kunde kunde) {
			bestellungen.add(kunde);
//			 Wartezeit startet jetzt
			
		}
				
		synchronized void addWartendeKunde(Kunde kunde) {
			if (bestellungen.contains(kunde)) {
				bestellungen.remove(kunde);
				wartendeKunden.add(kunde);				
				kunde.setPriority(MAX_PRIORITY);
				System.err.println(kunde.getName() + " HAT ZU LANGE GEWARTET");
			}			
		}
		
		synchronized void removeKunde(Kunde kunde) {
			if (bestellungen.contains(kunde)) {
				bestellungen.remove(kunde);
			} else if (wartendeKunden.contains(kunde)) {
				kunde.setPriority(NORM_PRIORITY);				
				wartendeKunden.remove(kunde);
			}
		}
		
		/**
		 * @return null falls keine Bestellungen mehr
		 */
		synchronized Kunde naechsteKunde() {
			if (wartendeKunden.isEmpty()) {			
				return bestellungen.peek();				
			} else {				
				return wartendeKunden.get(0);
			}
		}
		
		synchronized void setPrioritaet() {
//			System.out.format("\t\t\t\tPRIORITÄT FÜR %s WIRD GEPRÜFT\n", Thread.currentThread().getName());
			boolean zuWenigeBestellungen = (andereKasse.anzahlBestellungen() - anzahlBestellungen() >= 3);
			boolean zuLangeWartendeKunden = (!wartendeKunden.isEmpty())
										&& (laufband.size() >= naechsteKunde().getBestellung());

			
			if (zuWenigeBestellungen || zuLangeWartendeKunden) {
				Thread.currentThread().setPriority(MAX_PRIORITY);
				System.err.format("\t\t\t\t-----PRIORITÄT FÜR %s-----\n", Thread.currentThread().getName());			
			} else {	
				Thread.currentThread().setPriority(NORM_PRIORITY);
			}
		}	
	}
}
