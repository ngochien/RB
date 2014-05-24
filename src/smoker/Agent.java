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
public class Agent implements Runnable {

	private Table table;

	public Agent(Table table) {
		System.out.println("The agent is ready");
		this.table = table;
	}

	@Override
	public void run() {
		int i = 0;
		while (!Thread.currentThread().isInterrupted()) {
			Item item1 = Item.randomItem();
			Item item2 = Item.randomItem();
			while (item1 == item2) {
				item2 = Item.randomItem();
			}
			table.put(item1, item2);
			System.out.println("Putted " + item1 + " " + item2);
//			System.err.println("********************AGENT:" + i + "********************");
			i++;
		}
		System.err.println(Thread.currentThread().getName() + " : The agent is interrupted");
	}
}
