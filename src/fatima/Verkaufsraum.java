/**
 * 
 */
package fatima;

import java.util.concurrent.Semaphore;

/**
 * @author le
 *
 */
public class Verkaufsraum {	
	
	private Semaphore platz;
	
	private int abgewieseneKunden;
	
	public Verkaufsraum(int anzahlPlatz) {
		platz = new Semaphore(anzahlPlatz);
	}
	
	public void betreten() {		
		if (platz.tryAcquire() == true) {
			
			System.out.println(Thread.currentThread().getName() + " geht rein");
			try {
				System.out.println("\t\t\t\t" + Thread.currentThread().getName() + " bestellt und wartet");
				Thread.sleep(Utility.random(5000, 9000));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
			
			System.out.println("\t\t\t\t" + Thread.currentThread().getName() + " ist fertig");
			platz.release();
		} else {
			abgewieseneKunden++;
			System.out.println(Thread.currentThread().getName() + " geht weg");
//			Thread.currentThread().interrupt();			
		}
	}
	
	/**
	 * Liefert die Anzahl der bisher abgewiesenen Kunden zurück.
	 * 
	 * @return
	 */
	public int getAbgewieseneKunden() {		
		return abgewieseneKunden;
	}
}
