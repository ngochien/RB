/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */
package racing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 */
public class SimRace {

	public static final int NUM_OF_CARS = 5;
	public static final int NUM_OF_LAPS = 5;

	private Accident accident;
	private List<Car> cars = new ArrayList<Car>();

	public static void main(String[] args) {
		SimRace race = new SimRace();

		long start = System.currentTimeMillis();

//		race.startWithoutAccident();
		race.startWithAccident();

		long stop = System.currentTimeMillis();
		System.out.println("Time required : " + (stop - start));
	}

	public void startWithoutAccident() {
		createCar();
		for (Car car : cars) {
			try {
				car.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		printResult();
	}

	public void startWithAccident() {
		accident = new Accident(cars);
		accident.start();
		startWithoutAccident();
	}

	private void createCar() {
		for (int i = 0; i < NUM_OF_CARS; i++) {
			Car car = new Car("Wagen " + (i + 1));
			cars.add(car);
			car.start();
		}
	}

	private void printResult() {
		if (accident.happenedDuringRace()) {
			System.out.println("UNFALL!!! RENNEN WURDE ABGEBROCHEN.");
		} else {
			System.out.println("***** Endstand *****");
			Collections.sort(cars);
			for (int i = 0; i < cars.size(); i++) {
				System.out.println((i + 1) + ". Platz: " + cars.get(i));
			}
		}
	}
}
