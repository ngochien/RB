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

	private Ingredient[] ingredients;
	
	public Table() {
		System.out.println("A table is here");
		ingredients = new Ingredient[2];
	}
	
	public boolean isEmpty() {
		boolean isEmpty = ingredients[0] == null && ingredients[1] == null;
		System.out.println(Thread.currentThread().getName() + " :Checking empty table : " + isEmpty);
		return isEmpty;
	}
	
	public synchronized void put(Ingredient[] ingredients) {
		System.out.println(Thread.currentThread().getName() + " :Trying to put " + ingredients[0] + " and " + ingredients[1]);
		while (!isEmpty() && !Thread.currentThread().isInterrupted()) {
			try {
				System.out.println(Thread.currentThread().getName() + " :Waiting");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Thread.currentThread().interrupt();
				System.err.println(Thread.currentThread().getName() + " :Error while waiting for putting");
//				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName() + " :Putting " + ingredients[0] + " and " + ingredients[1]);
		this.ingredients = ingredients;
		System.out.println(Thread.currentThread().getName() + " :Done with putting. Notifying all");
		notifyAll();
	}
	
	public synchronized Ingredient[] take() {
		System.out.println(Thread.currentThread().getName() + " :Trying to take " + this.ingredients[0] + " and " + this.ingredients[1]);
		while (isEmpty() && !Thread.currentThread().isInterrupted()) {
			try {
				System.out.println(Thread.currentThread().getName() + " :Waiting");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Thread.currentThread().interrupt();
				System.err.println(Thread.currentThread().getName() + " :Error while waiting for taking");
//				e.printStackTrace();
			}
		}
		ingredients[0] = null;
		ingredients[1] = null;
		System.out.println(Thread.currentThread().getName() + " :Done with taking. Notifying all");
		notifyAll();
		return ingredients;
	}
	
	public synchronized Ingredient[] look() {
//		System.out.println(Thread.currentThread().getName() + " :Trying to take " + this.ingredients[0] + " and " + this.ingredients[1]);
//		while (isEmpty() && !Thread.currentThread().isInterrupted()) {
//			try {
//				System.out.println(Thread.currentThread().getName() + " :Waiting");
//				wait();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				Thread.currentThread().interrupt();
//				System.err.println(Thread.currentThread().getName() + " :Error while waiting for taking");
////				e.printStackTrace();
//			}
//		}
//		System.out.println(Thread.currentThread().getName() + " :Done with taking. Notifying all");
//		notifyAll();
		return ingredients;
	}
	
	@Override
	public String toString() {
		return ingredients[0] + " and " + ingredients[1] + " are on the table"; 
	}
}
