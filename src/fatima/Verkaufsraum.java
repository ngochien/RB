package fatima;

import java.util.concurrent.Semaphore;

/**
 * @author le
 *
 */
public class Verkaufsraum {

	private Semaphore platz;

	private int anzahlWarteschlangen;
	private int aktuelleWarteschlange;
	private Warteschlange[] warteschlangen;	
	
	private int anzahlKasse;
	private Kasse[] kassen;	
	
	private Laufband laufband;
	private int anzahlKueche;
	private Kueche[] kuechen;	
	
	private int abgewieseneKunden;

	public Verkaufsraum(int anzahlPlatz, int anzahlKasse, int anzahlKueche) {
		this.platz = new Semaphore(anzahlPlatz);	
		this.anzahlKasse = anzahlKasse;
		this.anzahlKueche = anzahlKueche;
		this.anzahlWarteschlangen = anzahlKasse;
	}

	public void oeffnen() {		
		this.laufband = new Laufband();
		this.kassen = new Kasse[anzahlKasse];		
		this.kuechen = new Kueche[anzahlKueche];
		this.warteschlangen = new Warteschlange[anzahlWarteschlangen];
				
		for (int i = 0; i < anzahlKasse; i++) {
			warteschlangen[i] = new Warteschlange();
			kassen[i] = new Kasse(warteschlangen[i], laufband);
		}		
		
		for (int i = 0; i < anzahlKasse; i++) {	
			int naechsteKasse = i + 1;
			naechsteKasse = (naechsteKasse == anzahlKasse) ? 0 : naechsteKasse;
			kassen[i].setNaechsteKasse(kassen[naechsteKasse]);			
			kassen[i].start();
		}
		
		for (int i = 0; i < anzahlKueche; i++) {
			kuechen[i] = new Kueche(laufband);
			kuechen[i].start();
		}
	}
	
	public void schliessen() {			
		for (int i = 0; i < anzahlKasse; i++) {			
			try {
				kassen[i].interrupt();			
				kassen[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		
		for (int i = 0; i < anzahlKueche; i++) {
			try {
				kuechen[i].interrupt();			
				kuechen[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void betreten() {		
		if (platz.tryAcquire() == true) {
			System.out.println(Thread.currentThread().getName() + " BETRITT den Verkaufsraum");						
		} else {
			synchronized (this) {
				abgewieseneKunden++;
			}
			System.out.println(Thread.currentThread().getName() + " GEHT WEG");
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Liefert die Anzahl der bisher abgewiesenen Kunden zurück.
	 * 
	 * @return
	 */
	public synchronized int getAbgewieseneKunden() {
		return abgewieseneKunden;
	}

	public synchronized Warteschlange getAktuelleWarteschlange() {
		aktuelleWarteschlange++;
		if (aktuelleWarteschlange >= anzahlWarteschlangen) {
			aktuelleWarteschlange = 0;
		}
		return warteschlangen[aktuelleWarteschlange];
	}
	
	public void verlassen() {
		System.out.println(Thread.currentThread().getName() + " ist fertig und verlasst das Lokal");
		platz.release();
	}
}
