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
	
	private int anzahlServiceKrafts;
	private ServiceKraft[] serviceKrafts;

	private BestellungQueue bestellungen;
	
	private Laufband laufband;
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
		this.bestellungen = new BestellungQueue();
		this.laufband = new Laufband();
				
		for (int i = 0; i < anzahlWarteschlangen; i++) {
			warteschlangen[i] = new Warteschlange();
			serviceKrafts[i] = new ServiceKraft(warteschlangen[i], bestellungen, laufband);
		}		
		
		for (int i = 0; i < anzahlServiceKrafts; i++) {	
			int kollege = i + 1;
			kollege = (kollege == anzahlServiceKrafts) ? 0 : kollege;
			serviceKrafts[i].setKollege(serviceKrafts[kollege]);			
			serviceKrafts[i].start();
		}
		
		for (int i = 0; i < anzahlKuecheKrafts; i++) {
			kuecheKrafts[i] = new KuecheKraft(bestellungen, laufband);
			kuecheKrafts[i].start();
		}
	}
	
	public void schliessen() {			
		for (int i = 0; i < anzahlServiceKrafts; i++) {			
			try {
				serviceKrafts[i].interrupt();			
				serviceKrafts[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		
		for (int i = 0; i < anzahlKuecheKrafts; i++) {
			try {
				kuecheKrafts[i].interrupt();			
				kuecheKrafts[i].join();
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
