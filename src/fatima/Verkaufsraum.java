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
	private Warteschlange[] warteschlangen;	
	
	private int abgewieseneKunden;
	private int aktuelleWarteschlange;

	public Verkaufsraum(int anzahlPlatz, int anzahlKasse, int anzahlKueche) {		
		this.laufband = new Laufband();
		this.kassen = new Kasse[anzahlKasse];
		this.kuechen = new Kueche[anzahlKueche];
		this.platz = new Semaphore(anzahlPlatz);
		this.warteschlangen = new Warteschlange[anzahlKasse];
	}

	public void oeffnen() {				
		for (int i = 0; i < kassen.length; i++) {
			warteschlangen[i] = new Warteschlange();
			kassen[i] = new Kasse(warteschlangen[i], laufband);
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
		for (int i = 0; i < kassen.length; i++) {			
			try {
				kassen[i].interrupt();			
				kassen[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		
		for (int i = 0; i < kuechen.length; i++) {
			try {
				kuechen[i].interrupt();			
				kuechen[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean betreten() {		
		if (platz.tryAcquire() == true) {
			System.out.println(Thread.currentThread().getName() + " BETRITT den Verkaufsraum");	
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
	 */
	public synchronized int getAbgewieseneKunden() {
		return abgewieseneKunden;
	}

	public synchronized Warteschlange getAktuelleWarteschlange() {
		aktuelleWarteschlange++;
		if (aktuelleWarteschlange >= kassen.length) {
			aktuelleWarteschlange = 0;
		}
		return warteschlangen[aktuelleWarteschlange];
	}
	
	public void verlassen() {
		System.out.println(Thread.currentThread().getName() + " FERTIG und VERLASST das Lokal");
		platz.release();
	}
}
