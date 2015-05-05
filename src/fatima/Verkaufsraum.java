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
			serviceKrafts[i] = new ServiceKraft(this, warteschlangen[i]);
		}		
		
		for (int i = 0; i < anzahlServiceKrafts; i++) {	
			int kollege = i + 1;
			kollege = (kollege == anzahlServiceKrafts) ? 0 : kollege;
			serviceKrafts[i].setKollege(serviceKrafts[kollege]);			
			serviceKrafts[i].start();
		}
		
		for (int i = 0; i < anzahlKuecheKrafts; i++) {
			kuecheKrafts[i] = new KuecheKraft();
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
	
	public Bestellung bedienen(Warteschlange warteschlange) {
		Thread naechsteThread = warteschlange.remove();		
		Bestellung bestellung = null;
		
		if (naechsteThread instanceof Kunde) {
			Kunde naechsteKunde = (Kunde) naechsteThread;
			
			synchronized (naechsteKunde) {
				bestellung = naechsteKunde.getBestellung();
				while (bestellung == null) {
					try {
//						naechsteKunde.notify();
						System.out.println(Thread.currentThread().getName() + " wartet auf Bestellung von " + naechsteKunde.getName());
						naechsteKunde.wait();
						bestellung = naechsteKunde.getBestellung();
					} catch (InterruptedException e) {
						System.out.println(Thread.currentThread().getName()+ " wurde beim Warten geweckt");
						Thread.currentThread().interrupt();
						e.printStackTrace();
						return null;
					}
				}	
				System.out.format("\t\t\t\t%s von %s wird angenommen... \n\n", bestellung.getId(), naechsteKunde.getName());
				try {
					Thread.sleep(bestellung.getDauer());
				} catch (InterruptedException e) {
					System.out.println(Thread.currentThread().getName()+ " wurde beim Schlafen geweckt");
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
				System.out.format("%s hat %d Burger bei %s bestellt und wartet nun... \n\n",
						 naechsteKunde.getName(), bestellung.getAnzahl(), Thread.currentThread().getName());
				
			}				
		}
		return bestellung;
		
	}
	
	public void verlassen() {
		System.out.println(Thread.currentThread().getName() + " ist fertig und verlasst das Lokal");
		platz.release();
	}
}
