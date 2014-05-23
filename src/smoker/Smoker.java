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
public class Smoker extends Thread {

	private Table table;

	private Ingredient ingredient;

	public Smoker(Table table, Ingredient ingredient) {
		System.out.println("Smoker with " + ingredient.name() + " is ready");
		this.table = table;
		this.ingredient = ingredient;
	}

	@Override
	public void run() {
		int i = 0;
		while (!isInterrupted()) {
			Ingredient[] ingredients = table.look();
			if (ingredients[0] != ingredient && ingredients[0] != null
				&& ingredients[1] != ingredient && ingredients[1] != null) {
				
				table.take();
				System.out.println("Smoker with " + ingredient.name() + " gonna smoke");
				try {
					Thread.sleep((int) (Math.random() * 100));
				} catch (InterruptedException e) {
					interrupt();
					System.err.println(Thread.currentThread().getName() + " :Error while sleeping (smoking)");
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " :Done with smoking");
				i++;
			}
		}
//		System.out.println(Thread.currentThread().getName() + " is interrupted");
		System.out.println(Thread.currentThread().getName() + " is done");
	}
}
