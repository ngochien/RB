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
		// System.out.println(Thread.currentThread().getName()
		// + " :Trying to put " + item1 + " and " + item2);

		while (this.item1 != null && this.item2 != null) {
			try {
				// System.out.println(Thread.currentThread().getName()
				// + " :Waiting for putting");
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// System.err.println(Thread.currentThread().getName()
				// + " :Error while waiting for putting");
				return;
			}
		}
		// System.out.println(Thread.currentThread().getName() + " :Putting "
		// + item1 + " and " + item2);
		this.item1 = item1;
		this.item2 = item2;
		System.out.println(Thread.currentThread().getName() + " :" + this
				+ ". Notifying all. ");
		notifyAll();
	}

	/**
	 * Ein Raucher ruft diese Methode auf, um beide Zutaten auf dem Tisch
	 * wegzunehmen.
	 */
	public synchronized void take(Item missingItem) {
		// System.out.println(Thread.currentThread().getName()
		// + " :Trying to take and smoke with " + missingItem);

		while (item1 == null || item2 == null || item1 == missingItem
				|| item2 == missingItem) {
			try {
				// System.out.println(Thread.currentThread().getName()
				// + " :Waiting for smoking");
				wait();
			} catch (InterruptedException e) {
				// System.err.println(Thread.currentThread().getName()
				// + " :Error while waiting for item1 - " + item1);
				Thread.currentThread().interrupt();
				return;
			}
		}
		item1 = null;
		item2 = null;
		System.out.println(Thread.currentThread().getName()
				+ " :Taked all. Notifying all");
		/*
		 * Der Raucher raucht für eine gewisse Zeit.
		 */
		try {
			Thread.sleep((int) (Math.random() * 100));
			System.out.println(Thread.currentThread().getName()
					+ " ist fertig.");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		/*
		 * Nur wenn er mit dem Rauchen fertig ist, signalisiert er dies dem
		 * Agenten.
		 */
		notifyAll();
	}

	@Override
	public String toString() {
		return item1 + " and " + item2 + " are on the table";
	}
}
