/**
 * 
 */
package fatima;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Fatima
 */
public class Warteschlange implements Puffer<Kunde> {

	private static int zaehler = 0;
	
	private List<Kunde> kunden;
	private int id;	

	public Warteschlange() {
		kunden = new LinkedList<>();
		zaehler++;
		id = zaehler;		
	}	
	
	@Override
	public synchronized void add(Kunde kunde) {		
		kunden.add(kunde);
		System.out.println(Thread.currentThread().getName() +" ENTER " +
				" in die Warteschlange-" + id + ": Länge = " + kunden.size());
	
		this.notifyAll();	
	}

	@Override
	public synchronized Kunde remove() {				
		Kunde kunde = null;
//		while (kunden.size() == 0) {
//			try {
//				System.out.println(Thread.currentThread().getName() + " WARTET auf neue Kunde");
//				this.wait();
//			} catch (InterruptedException e) {				
//				System.err.println(Thread.currentThread().getName() + " WURDE beim Warten GEWECKT");
//				Thread.currentThread().interrupt();
//				return null;
//			}
//		}
		
		if (kunden.size() > 0) {
			kunde = kunden.remove(0);		
			System.out.println("\t\t\t\t" + Thread.currentThread().getName() +" HOLT "
						+ kunde.getName() + " aus der Warteschlange-" + id + ": Länge = " + kunden.size());		
		}
		return kunde;
	}

}
