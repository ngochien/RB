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
public enum Item {
	TOBACCO,
	PAPER,
	MATCH;
	
	public static Item randomItem() {
//		System.out.println(Thread.currentThread().getName() + " :Preparing an item");
		Item item = Item.values()[(int)(Math.random() * 3)];
//		System.out.println(Thread.currentThread().getName() + " :Returning " + item);
		return item;
	}
}
