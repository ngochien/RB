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
 * Simuliert ein Rennauto.
 */
public class Car implements Runnable, Comparable<Car> {

	private String name;
	private int totalTime;
	
	public Car(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < SimRace.NUM_OF_LAPS; i++) {
				int time = (int) (Math.random() * 100);
				Thread.sleep(time);
				totalTime += time;
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public String toString() {
		return name + " - gebrauchte Zeit: " + totalTime;
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
