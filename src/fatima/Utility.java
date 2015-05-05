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
	 * Liefert eine zuf�llige Ganzzahl zwischen <code>min</code> und
	 * <code>max</code> zur�ck
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max) {
		return (int) (Math.random() * min + Math.random() * (max - min)) + 1;
	}

	/**
	 * Liefert die Anzahl der bisher abgewiesenen Kunden zur�ck.
	 * 
	 * @return
	 */
	int getAbgewieseneKunden() {
		System.out
				.println("Abgewiesene Kunden sind kumulativ zu protokollieren.");
		return 0;
	}

	/**
	 * Liefert die L�nge der angegebene Warteschlange zur�ck.
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
