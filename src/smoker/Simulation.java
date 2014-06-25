/*
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */

package smoker;

/**
 * Testing class.
 * 
 * @author h13n
 */
public class Simulation {
	
	public static final int SIMULATION_TIME = 10000;
	
	public static void main(String[] args) {
		Table table = new Table();
		
		Agent agent = new Agent(table);
		
		Smoker tobacco = new Smoker(table, Item.TOBACCO);
		Smoker paper = new Smoker(table, Item.PAPER);
		Smoker match = new Smoker(table, Item.MATCH);

		Thread t1 = new Thread(agent, "Agent");
		Thread t2 = new Thread(tobacco, "Tobacco-man");
		Thread t3 = new Thread(paper, "Paper-man");
		Thread t4 = new Thread(match, "Match-man");

		t1.start();
		t2.start();
		t3.start();
		t4.start();

		try {
			Thread.sleep(SIMULATION_TIME);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
		 
		 t1.interrupt();
		 t2.interrupt();
		 t3.interrupt();
		 t4.interrupt();
		 System.out.println("--------------------Simulation endet--------------------");
	}
}
