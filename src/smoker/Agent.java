/*
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */

package smoker;

/**
 * Der Agent, der zwei zufällig ausgewählte Zutaten auf den gemeinsamen Tisch
 * legt.
 * 
 * @author Le, Nguyen.
 */
public class Agent implements Runnable {

	private Table table;

	public Agent(Table table) {
		System.out.println("The agent is ready");
		this.table = table;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {

			/* Wähle zufällig zwei Zutaten */
			Item item1 = Item.random();
			Item item2 = Item.random();
			while (item1 == item2) {
				item2 = Item.random();
			}

			/* Lege zwei gewählte Zutaten auf den Tisch */
			table.put(item1, item2);
//			System.out.println("Putted " + item1 + " " + item2);
		}
		System.err.println(Thread.currentThread().getName()
				+ " : The agent is interrupted");
	}
}
