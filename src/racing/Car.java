/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 * bichngoc.nguyen@haw-hamburg.de
 */
package racing;

/**
 *	Simuliert ein Rennauto.
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
			}
			totalTime += time;
		}
	}

	@Override
	public String toString() {
		return getName() + " Zeit: " + totalTime;
	}

	@Override
	public int compareTo(Car that) {
		return this.totalTime - that.totalTime;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Car ? this.compareTo((Car) obj) == 0 : false;
	}

	@Override
	public int hashCode() {
		return totalTime;
	}

}
