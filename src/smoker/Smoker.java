/*
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */

package smoker;

/**
 * Der Raucher hat nur eine Zutat und wartet an dem Tisch auf die zwei fehlenden
 * Zutaten.
 * 
 * @author Le, Nguyen.
 */
public class Smoker implements Runnable {

	private Table table;

	private Item item;

	public Smoker(Table table, Item item) {
		System.out.println("Smoker with " + item + " is ready");
		this.table = table;
		this.item = item;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			table.take(item);
			try {
				Thread.sleep((int) (Math.random() * 100));
//				System.out.println(Thread.currentThread().getName()
//						+ " :Done with smoking");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// System.err.println(Thread.currentThread().getName()
				// + " :Error while sleeping (smoking)");
//				return; // not necessary?
			}
		}
		System.err
				.println(Thread.currentThread().getName() + " is interrupted");
	}
}
