/**
 * 
 */
package fatima;

/**
 * @author le
 *
 */
public class Utility {

	/**
	 * Liefert eine zufällige Ganzzahl zwischen <code>min</code> und
	 * <code>max</code> zurück
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max) {
		return (int) (Math.random() * min + Math.random() * (max - min)) + 1;
	}

	

	/**
	 * Liefert die Länge der angegebene Warteschlange zurück.
	 * 
	 * @param id
	 *            ein eindeutige Nummer zum Identifizieren der angegebenen
	 *            Warteschlange
	 * @return
	 */
	int getWarteschlange(int id) {
		return 0;
	}

	int getMinVerweilszeit() {
		return 0;
	}

	int getMaxVerweilszeit() {
		return 0;
	}

	int getDurchschnittVerweilszeit() {
		return 0;
	}
}
