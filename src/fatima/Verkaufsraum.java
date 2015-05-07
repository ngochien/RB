package fatima;

import java.util.concurrent.Semaphore;

/**
 * @author le
 *
 */
public class Verkaufsraum {
	
	private Semaphore platz;
	
	private Kasse[] kassen;		
	private Kueche[] kuechen;	
	private Laufband laufband;
	
	private int abgewieseneKunden;
	private int aktuelleWarteschlange;

	public Verkaufsraum(int anzahlPlatz, int anzahlKasse, int anzahlKueche) {
		this.platz = new Semaphore(anzahlPlatz);	
		this.kassen = new Kasse[anzahlKasse];
		this.kuechen = new Kueche[anzahlKueche];
		this.laufband = new Laufband();		
	}

	public void oeffnen() {						
		for (int i = 0; i < kassen.length; i++) {			
			kassen[i] = new Kasse(laufband);
		}		
		
		for (int i = 0; i < kassen.length; i++) {	
			int andereKasse = i + 1;
			andereKasse = (andereKasse == kassen.length) ? 0 : andereKasse;
			kassen[i].setAndereKasse(kassen[andereKasse]);			
			kassen[i].start();
		}
		
		for (int i = 0; i < kuechen.length; i++) {
			kuechen[i] = new Kueche(laufband);
			kuechen[i].start();
		}
	}
	
	public void schliessen() {	
		try {
			for (int i = 0; i < kassen.length; i++) {
				kassen[i].interrupt();
				kassen[i].join();
			}

			for (int i = 0; i < kuechen.length; i++) {
				kuechen[i].interrupt();
				kuechen[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean betreten() {		
		if (platz.tryAcquire() == true && Thread.currentThread() instanceof Kunde) {			
			Kunde k = (Kunde) Thread.currentThread();
			System.out.println(k + " BETRITT den Verkaufsraum");
			kassen[aktuelleWarteschlange()].addKunde(k);
			return true;
		} else {
			synchronized (this) {
				abgewieseneKunden++;
			}
			System.out.println(Thread.currentThread().getName() + " GEHT WEG");
			return false;
		}
	}

	/**
	 * Liefert die Anzahl der bisher abgewiesenen Kunden zurück.
	 * 	 
	 */
	public synchronized int getAbgewieseneKunden() {
		return abgewieseneKunden;
	}

	public synchronized int aktuelleWarteschlange() {
		aktuelleWarteschlange++;
		if (aktuelleWarteschlange >= kassen.length) {
			aktuelleWarteschlange = 0;
		}
		return aktuelleWarteschlange;
	}
	
	public void verlassen() {
		System.out.println(Thread.currentThread().getName() + " FERTIG und VERLASST das Lokal");
		platz.release();
	}
}
