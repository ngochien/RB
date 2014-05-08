/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 * bichngoc.nguyen@haw-hamburg.de
 */
package racing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simuliert ein Autorennen
 */
public class SimRace {

	public static final int NUM_OF_CARS = 5;
	public static final int NUM_OF_LAPS = 5;

	private Accident accident;
	private List<Car> cars = new ArrayList<Car>();

	public static void main(String[] args) {
		SimRace race = new SimRace();
		
//		race.startWithoutAccident();
		race.startWithAccident();
	}

	/**
	 * Simuliert ein Rennen ohne Unfall.
	 */
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

	/**
	 * Simuliert ein Rennen mit Unfall.
	 */
	public void startWithAccident() {
		accident = new Accident(cars);
		accident.start();
		startWithoutAccident();
	}

	/**
	 * Erzeugt teilnehmende Autos und lässt sie rennen.
	 */
	private void createCar() {
		for (int i = 0; i < NUM_OF_CARS; i++) {
			Car car = new Car("Wagen " + (i + 1));
			cars.add(car);
			car.start();
		}
	}

	private void printResult() {
		if (accident != null && accident.happenedDuringRace()) {
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
