/*
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */

package smoker;

/**
 * @author Le, Nguyen.
 */
public enum Item {
	TOBACCO, PAPER, MATCH;

	/**
	 * Eins von den drei Zutaten wird zufällig gewählt.
	 */
	public static Item random() {
		// System.out.println(Thread.currentThread().getName() +
		// " :Preparing an item");
		Item item = Item.values()[(int) (Math.random() * 3)];
		// System.out.println(Thread.currentThread().getName() + " :Returning "
		// + item);
		return item;
	}
}
