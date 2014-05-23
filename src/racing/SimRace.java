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

	public static final boolean ACCIDENT_MODUS = true;

	public static final int NUM_OF_CARS = 50;
	public static final int NUM_OF_LAPS = 50;

	private boolean crashed;
	private List<Car> cars = new ArrayList<>();
	private List<Thread> carThreads = new ArrayList<>();


	public static void main(String[] args) {
		SimRace simulation = new SimRace();
		simulation.start();
	}

	/**
	 * Simuliert ein Rennen
	 */
	public void start() {
		Thread accident = new Thread(new Accident(Thread.currentThread()));
		if (ACCIDENT_MODUS) {
			accident.start();
		}
		startRacing();
		waitToFinish();
		printResult();
		accident.interrupt();
	}

	/*
	 * Erzeugt teilnehmende Autos und lässt sie rennen.
	 */
	private void startRacing() {
		for (int i = 0; i < NUM_OF_CARS; i++) {
			cars.add(new Car("Wagen " + (i + 1)));
			carThreads.add(new Thread(cars.get(i)));
			carThreads.get(i).start();
		}
	}

	/*
	 * Wartet auf die rennenden Autos, bis sie ans Ziel kommen. Falls sich ein
	 * Unfall ereignet, stoppt alle noch laufenden Autos.
	 */
	private void waitToFinish() {
		try {
			for (Thread t : carThreads) {
				t.join();
			}
		} catch (InterruptedException e) {
			crashed = true;
			for (Thread t : carThreads) {
				t.interrupt();
				
			}
			Thread.currentThread().interrupt();
		}
	}

	private void printResult() {
		if (crashed) {
			System.out.println("UNFALL");
		} else {
			System.out.println("***** Endstand *****");
			Collections.sort(cars);
			for (int i = 0; i < cars.size(); i++) {
				System.out.println((i + 1) + ". Platz: " + cars.get(i));
			}
		}
	}
}
