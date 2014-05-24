/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */
package smoker;

/**
 * @author h13n
 * 
 */
public class Table {

	private Item item1;
	private Item item2;

	public synchronized boolean hasTwoItem() {
		boolean hasTwo = item1 != null && item2 != null;
//		System.out.println(Thread.currentThread().getName()
//				+ " :Checking if there are two items on the table? " + hasTwo);
		return hasTwo;
	}

	public synchronized void reset() {
//		System.out.println(Thread.currentThread().getName() + " :reseting table");
		item1 = null;
		item2 = null;
//		System.out.println(Thread.currentThread().getName() + " :Reset done. Notifying all");
		notifyAll();
	}

	public synchronized void put(Item item1, Item item2) {
//		System.out.println(Thread.currentThread().getName()
//				+ " :Trying to put " + item1 + " and " + item2);

		while (hasTwoItem() && !Thread.currentThread().isInterrupted()) {
			try {
//				System.out.println(Thread.currentThread().getName() + " :Waiting for putting");
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.err.println(Thread.currentThread().getName()
						+ " :Error while waiting for putting");
				return;
			}
		}
//		System.out.println(Thread.currentThread().getName() + " :Putting " + item1 + " and " + item2);
		this.item1 = item1;
		this.item2 = item2;
//		System.out.println(Thread.currentThread().getName() + " :" + this + ". Notifying all. ");
		notifyAll();
	}

	public synchronized Item takeItem1() {
//		System.out.println(Thread.currentThread().getName()
//				+ " :Trying to take item1 - " + item1);
		while (!hasTwoItem() && !Thread.currentThread().isInterrupted()) {
			try {
//				System.out.println(Thread.currentThread().getName()
//						+ " :Waiting for item1 - " + item1);
				wait();
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName()
						+ " :Error while waiting for item1 - " + item1);
				Thread.currentThread().interrupt();
				return null;
			}
		}
//		System.out.println(Thread.currentThread().getName() + " :Taked "
//				+ item1 + " OK. NOT Notifying all");
//		notifyAll();
		return item1;
	}

	public synchronized Item takeItem2() {
//		System.out.println(Thread.currentThread().getName()
//				+ " :Trying to taking item2 - " + item2);
		while (!hasTwoItem() && !Thread.currentThread().isInterrupted()) {
			try {
//				System.out.println(Thread.currentThread().getName()
//						+ " :Waiting for item2 - " + item2);
				wait();
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName()
						+ " :Error while waiting for item2 - " + item2);
				Thread.currentThread().interrupt();
				return null;
			}
		}
//		System.out.println(Thread.currentThread().getName() + " :Taked "
//				+ item2 + " OK. NOT Notifying all");
//		notifyAll();
		return item2;
	}

	private synchronized Item checkItem(Item anItem) {
		System.out.println(Thread.currentThread().getName() + " :Checking "
				+ anItem);
		Item item = anItem;
		while (!hasTwoItem() && !Thread.currentThread().isInterrupted()) {
			try {
				System.out.println(Thread.currentThread().getName()
						+ " :Waiting");
				wait();
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName()
						+ " :Error while checking " + anItem);
				Thread.currentThread().interrupt();
				return null;
			}
		}
		System.out.println(Thread.currentThread().getName() + " :Checking "
				+ anItem + " OK. Notifying all");
		notifyAll();
		return item;
	}

	// public synchronized Ingredient[] take() {
	// System.out.println(Thread.currentThread().getName()
	// + " :Trying to take " + this.ingredients[0] + " and "
	// + this.ingredients[1]);
	// while (isEmpty() && !Thread.currentThread().isInterrupted()) {
	// try {
	// System.out.println(Thread.currentThread().getName()
	// + " :Waiting");
	// wait();
	// } catch (InterruptedException e) {
	// Thread.currentThread().interrupt();
	// System.err.println(Thread.currentThread().getName()
	// + " :Error while waiting for taking");
	// }
	// }
	// ingredients[0] = null;
	// ingredients[1] = null;
	// System.out.println(Thread.currentThread().getName()
	// + " :Done with taking. Notifying all");
	// notifyAll();
	// return ingredients;
	// }

	public synchronized Item[] look() {
		// System.out.println(Thread.currentThread().getName() +
		// " :Trying to take " + this.ingredients[0] + " and " +
		// this.ingredients[1]);
		// while (isEmpty() && !Thread.currentThread().isInterrupted()) {
		// try {
		// System.out.println(Thread.currentThread().getName() + " :Waiting");
		// wait();
		// } catch (InterruptedException e) {
		// //
		// Thread.currentThread().interrupt();
		// System.err.println(Thread.currentThread().getName() +
		// " :Error while waiting for taking");
		// // e.printStackTrace();
		// }
		// }
		// System.out.println(Thread.currentThread().getName() +
		// " :Done with taking. Notifying all");
		// notifyAll();
		return null;
	}

	@Override
	public String toString() {
		return item1 + " and " + item2 + " are on the table";
	}
}
