/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */
package racing;

import java.util.List;

/**
 *
 */
public class Accident extends Thread {
	
	private boolean happenedDuringRace = false;
	private List<Car> cars;
	
	public boolean happenedDuringRace() {
		return happenedDuringRace;
	}

	public Accident(List<Car> cars) {
		this.cars = cars;
	}

	@Override
	public void run() {
		try {
			Thread.sleep((int) (Math.random() * SimRace.NUM_OF_LAPS * 100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (Car car : cars) {
			if (car.isAlive()) {
				car.interrupt();
				happenedDuringRace = true;
			}
		}
	}
}
