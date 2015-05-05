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

	private int abgewieseneKunden;

	public Verkaufsraum(int anzahlPlatz, int anzahlWarteschlangen) {
		this.platz = new Semaphore(anzahlPlatz);
		this.anzahlWarteschlangen = anzahlWarteschlangen;
		this.warteschlangen = new Warteschlange[anzahlWarteschlangen];
		for (int i = 0; i< anzahlWarteschlangen; i++) {
			warteschlangen[i] = new Warteschlange();
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
		
		try {
			System.out.println("\t\t\t\t"+ Thread.currentThread().getName() + " bestellt und wartet");
			Thread.sleep(Utility.random(5000, 10000));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}

		System.out.println("\t\t\t\t" + Thread.currentThread().getName() + " ist fertig");
		platz.release();
	}
}
