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
public class SmokerSimulation {

	public static void main(String[] args) {
		
		Table table = new Table();
		
//		Smoker tobacco = new Smoker(table, Ingredient.TOBACCO);
//		Smoker paper = new Smoker(table, Ingredient.PAPER);
//		Smoker match = new Smoker(table, Ingredient.MATCH);
//		
//		Agent agent = new Agent(table);
		
//		Thread t1 = new Thread(agent, "agent");
//		Thread t2 = new Thread(tobacco, "tobacco");
//		Thread t3 = new Thread(paper, "paper");
//		Thread t4 = new Thread(match, "match");
		
		Thread t1 = new Smoker(table, Ingredient.TOBACCO); t1.setName("Tobacco");
		Thread t2 = new Smoker(table, Ingredient.PAPER); t2.setName("Paper");
		Thread t3 = new Smoker(table, Ingredient.MATCH); t3.setName("Match");
		Thread t4 = new Agent(table); t4.setName("Agent");
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("-----------------main-----------------");
		
//		t1.interrupt();
//		t2.interrupt();
//		t3.interrupt();
//		t4.interrupt();
	}

}
