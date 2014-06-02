/*
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */

package smoker;

/**
 * Der gemeinsame Tisch von dem Agenten und den drei Rauchern.
 * 
 * @author Le, Nguyen.
 */
public class Table {

	private Item item1;
	private Item item2;

	/**
	 * Der Agent ruft diese Methode auf, um zwei Zutaten auf den Tisch zu legen.
	 */
	public synchronized void put(Item item1, Item item2) {
		while (this.item1 != null && this.item2 != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
		this.item1 = item1;
		this.item2 = item2;
		System.out.println(Thread.currentThread().getName() + " hat " + item1
				+ " " + item2 + " auf den Tisch gelegt.");
		notifyAll();
	}

	/**
	 * Ein Raucher ruft diese Methode auf, um beide Zutaten auf dem Tisch wegzunehmen.
	 */
	public synchronized void take(Item missingItem) {
		
		while (item1 == null || item2 == null || item1 == missingItem
				|| item2 == missingItem) {

			try {
				wait();	
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
		
		/* Der Raucher raucht für eine gewisse Zeit. */
		try {
			Thread.sleep((int) (Math.random() * 100));
			System.out.println(Thread.currentThread().getName()
					+ " ist fertig mit dem Rauchen.");
			System.out.println("------------------------------");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		
		item1 = null;
		item2 = null;
		
		/* Nur wenn er mit dem Rauchen fertig ist, signalisiert er dies dem Agenten. */
		notifyAll();
	}

	@Override
	public String toString() {
		return item1 + " and " + item2 + " are on the table";
	}
}
