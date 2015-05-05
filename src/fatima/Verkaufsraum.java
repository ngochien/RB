package fatima;

import java.util.concurrent.Semaphore;

/**
 * @author le
 *
 */
public class Verkaufsraum {

	private Semaphore platz;

	private int anzahlWarteschlangen;
	private Warteschlange[] warteschlangen;
	private int aktuelleWarteschlange = 0;
	
	private int anzahlServiceKrafts;
	private ServiceKraft[] serviceKrafts;

	private int anzahlKuecheKrafts;
	private KuecheKraft[] kuecheKrafts;
	
	private int abgewieseneKunden;

	public Verkaufsraum(int anzahlPlatz, int anzahlServiceKrafts, int anzahlKuecheKrafts) {
		this.platz = new Semaphore(anzahlPlatz);
		this.anzahlWarteschlangen = anzahlServiceKrafts;
		this.anzahlServiceKrafts = anzahlServiceKrafts;
		this.anzahlKuecheKrafts = anzahlKuecheKrafts;
	}

	public void oeffnen() {
		this.warteschlangen = new Warteschlange[anzahlWarteschlangen];
		this.serviceKrafts = new ServiceKraft[anzahlServiceKrafts];		
		this.kuecheKrafts = new KuecheKraft[anzahlKuecheKrafts];		
				
		for (int i = 0; i < anzahlWarteschlangen; i++) {
			warteschlangen[i] = new Warteschlange();
		}
		
		for (int i = 0; i < anzahlServiceKrafts; i++) {
			serviceKrafts[i] = new ServiceKraft(this, warteschlangen[i]);
			serviceKrafts[i].start();
		}
		
		for (int i = 0; i < anzahlKuecheKrafts; i++) {
			kuecheKrafts[i] = new KuecheKraft();
		}
	}
	
	public void schliessen() throws InterruptedException {			
		for (int i = 0; i < anzahlServiceKrafts; i++) {			
			serviceKrafts[i].interrupt();
			serviceKrafts[i].join();
		}	
		for (int i = 0; i < anzahlKuecheKrafts; i++) {
			kuecheKrafts[i] = null;
		}
	}

	public void betreten() {		
		if (platz.tryAcquire() == true) {
			System.out.println(Thread.currentThread().getName() + " geht rein");						
		} else {
			synchronized (this) {
				abgewieseneKunden++;
			}
			System.out.println(Thread.currentThread().getName() + " geht weg");
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

	public synchronized void sichEinreihen() {		
			warteschlangen[aktuelleWarteschlange].enter(Thread.currentThread());
			aktuelleWarteschlange++;
			if (aktuelleWarteschlange >= anzahlWarteschlangen) {
				aktuelleWarteschlange = 0;
			}			
	}
	
	public Bestellung bestellen() {	
		return new Bestellung();
	}
	
	public void bedienen(Warteschlange warteschlange) {
		Thread naechsteKunde = warteschlange.remove();
		if (naechsteKunde != null) {
			try {
				System.out.println("\t\t\t\t" + naechsteKunde.getName()
						+ " bestellt und wartet");
				Thread.sleep(Utility.random(5000, 10000));
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName()+ " wurde beim Schlafen geweckt");
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}

			System.out.println(naechsteKunde.getName() + " ist fertig");
		}
		platz.release();
	}
}
