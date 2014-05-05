/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */
package racing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author h13n
 * 
 */
public class SimRace {

	public static final int NUM_OF_CARS = 5;
	public static final int NUM_OF_LAPS = 5;

	private Accident accident;
	private List<Car> cars = new LinkedList<Car>();

	public List<Car> getCars() {
		return cars;
	}

	public void printSortedListOfCars() {
		if (!accident.happenedDuringRace()) {
			System.out.println("***** Endstand *****");
			Collections.sort(cars);
			for (int i = 0; i < cars.size(); i++) {
				System.out.println((i + 1) + ". Platz: " + cars.get(i));
			}
		} else {
			System.out.println("UNFALL!!! RENNEN WURDE ABGEBROCHEN.");
		}
	}

	public void startRacing() {
		for (int i = 0; i < NUM_OF_CARS; i++) {
			Car car = new Car("Wagen " + (i + 1));
			cars.add(car);
			car.start();
		}
		
		accident = new Accident(cars);
		accident.start();
		try {
			accident.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		for (Car c : cars) {
//			try {
//				c.join();
//			} catch (InterruptedException e) {
//				System.err.println("Error while joinning");
//			}
//		}
	}

	public static void main(String[] args) {
		SimRace race = new SimRace();
		long start = System.currentTimeMillis();
		race.startRacing();
		race.printSortedListOfCars();
		long stop = System.currentTimeMillis();
		System.out.println("Time required : " + (stop - start));
		System.out.println("Ended!");
	}
}
