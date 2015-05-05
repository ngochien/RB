/**
 * 
 */
package fatima;

import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse generiert für einen angegebenen Zeitraum eine zufällige Anzahl von Kunden
 * 
 * @author le
 *
 */
public class Kundengenerator extends Thread {

	/**
	 * Für den angegebenen Zeitraum zufällig min bis max Elemente generieren
	 */
	private int min;
	private int max;
	private int zeitraum;

	private Verkaufsraum verkaufsraum;

	public Kundengenerator(Verkaufsraum verkaufsraum, int zeitraum, int min, int max) {
		this.verkaufsraum = verkaufsraum;
		this.zeitraum = zeitraum;
		this.min = min;
		this.max = max;
	}

	public void run() {
		int rundeNr = 0;
		while (!isInterrupted()) {
			rundeNr++;
			generieren(rundeNr);
		}
		System.out.println(Thread.currentThread().getName() + " wurde beendet. "
				+ "Es kommen keine Kunden mehr. Nur noch aktuelle Kunden werden fertig bedient");
	}
	
	public void generieren(int rundeNr) {
		int anzahlKunden = Utility.random(min, max);
		for (int i = 1; i <= anzahlKunden; i++) {
			Kunde k = new Kunde(verkaufsraum);
			k.setName("Kunde " + i + " (Runde " + rundeNr + ")");
			k.start();
		}
		try {
			Thread.sleep(zeitraum);
			System.out.println("Abgewiesene Kunden: " + verkaufsraum.getAbgewieseneKunden());
			System.out.println("--------------------NÄCHSTE RUNDE--------------------");
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " wurde geweckt");
			Thread.currentThread().interrupt();			
		}
	}
}
