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
		int i = 0;
		while (!Thread.currentThread().isInterrupted()) {
			synchronized (table) {
				
			
			Item item1 = table.takeItem1();
			Item item2 = table.takeItem2();
				if (item1 != item && item2 != item) {
//					table.reset();
					System.out.println(Thread.currentThread().getName() + " is gonna smoke");
					try {
						Thread.sleep((int) (Math.random() * 100));
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						System.err.println(Thread.currentThread().getName()
								+ " :Error while sleeping (smoking)");
						// return; // not necessary?
					}
					System.out.println(Thread.currentThread().getName()
							+ " :Done with smoking");
					table.reset();
					i++;
//					System.err.println(Thread.currentThread().getName() + " " + i + ". smoking");
			}
			}
		}
		System.err
				.println(Thread.currentThread().getName() + " is interrupted");
	}
}
