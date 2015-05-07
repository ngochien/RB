/**
 * 
 */
package fatima;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ob diese Kalsse wirklich notwendig ist?
 * 
 * @author le
 *
 */
public class Warteschlange implements Puffer<Kunde> {

	private static int zaehler = 0;
	
	private final Lock warteschlangeLock = new ReentrantLock();	 
	
	private List<Kunde> kunden; // Liste als Speicher
	
	private int id;	
		
	public Warteschlange() {
		kunden = new LinkedList<>();
		zaehler++;
		id = zaehler;		
	}	
	
	@Override
	public void add(Kunde kunde) {	
		// Zugriff auf Buffer sperren
	    warteschlangeLock.lock();
	    
		/* Item zum Puffer hinzufügen */
		kunden.add(kunde);		
		System.out.println(Thread.currentThread().getName() +" ENTER " +
				" in die Warteschlange-" + id + ": Länge = " + kunden.size());				

	    // Zugriff auf Buffer freigeben
	    warteschlangeLock.unlock();	
	    
		Verkaufsraum.lock.lock();
		// Gezielt einen wartenden Consumer wecken (spezielle Warteschlange!)
		Verkaufsraum.busy.signalAll();
		Verkaufsraum.lock.unlock();
	}

	@Override
	public Kunde remove() {
		// Zugriff auf Buffer sperren
		warteschlangeLock.lock();

		Kunde kunde = null;

		if (kunden.size() == 0) {
			warteschlangeLock.unlock();
			
			try {
				Verkaufsraum.lock.lock();
				System.out.println(Thread.currentThread().getName() + " WARTET... Keine Kunde da");
				Verkaufsraum.busy.await();
				Verkaufsraum.lock.unlock();
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + " WURDE beim Warten GEWECKT");
				Thread.currentThread().interrupt();
			}
		} else {
			kunde = kunden.remove(0);
			System.out.println("\t\t\t\t" + Thread.currentThread().getName() + " HOLT "
							+ kunde.getName() + " aus der Warteschlange-" + id + ": Länge = " + kunden.size());

			warteschlangeLock.unlock();
		}
		
		return kunde;
	}
}
