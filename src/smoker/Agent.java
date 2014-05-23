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
public class Agent extends Thread {

	private Table table;

	public Agent(Table table) {
		System.out.println("The agent is ready");
		this.table = table;
	}

	@Override
	public void run() {
		int i = 0;
		while (!isInterrupted()) {
			table.put(Ingredient.getCoupleOfIngredients());
			System.out.println("**********" + i + "************");
			i++;
		}
//		System.out.println(Thread.currentThread().getName() + " is interrupted");
		System.out.println(Thread.currentThread().getName() + " is done");
	}
}
