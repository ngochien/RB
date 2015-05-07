/**
 * 
 */
package fatima.test;

import java.util.concurrent.PriorityBlockingQueue;

import fatima.Kunde;
import fatima.Utility;

/**
 * @author le
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		PriorityBlockingQueue<Kunde> q = new PriorityBlockingQueue<>(11, new Kunde.BestellungComparator());
//		Kunde k1 = new Kunde(null); k1.bestellen();
//		Kunde k2 = new Kunde(null); k2.bestellen();
//		Kunde k3 = new Kunde(null); k3.bestellen();
//		
//		q.put(k1); q.put(k2); q.put(k3);
//		
//		System.out.println(q.remove().getBestellung());
//		System.out.println(q.remove().getBestellung());
//		System.out.println(q.remove().getBestellung());
		
		System.out.println(Utility.random(3, 6));
	}
}
