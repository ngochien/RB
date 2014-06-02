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
 * Simuliert einen Unfall während eines Rennens.
 */
public class Accident implements Runnable {

	private Thread race;

	public Accident(Thread race) {
		this.race = race;
	}

	@Override
	public void run() {
		try {
			Thread.sleep((int) (Math.random() * SimRace.NUM_OF_LAPS * 100));
			race.interrupt();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
