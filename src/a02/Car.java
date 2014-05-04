/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */
package a02;

/**
 * @author h13n
 *
 */
public class Car extends Thread implements Comparable<Car> {

	private int totalTime;

	public Car(String name) {
		super(name);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < SimRace.NUM_OF_LAPS; i++) {
			int time = (int) (Math.random() * 100);
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				interrupt();
				System.err.println("Error while sleeping");
			}
			totalTime += time;
		}
	}

	@Override
	public int compareTo(Car o) {
		return this.totalTime - o.totalTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Car ? this.compareTo((Car) obj) == 0 : false;
	}

	@Override
	public int hashCode() {
		return totalTime;
	}

	@Override
	public String toString() {
		return getName() + " Zeit: " + totalTime;
	}
}
