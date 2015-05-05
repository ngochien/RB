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
		while (!isInterrupted()) {
			generieren();
		}
		System.out.println(Thread.currentThread().getName() + " wurde beendet");
	}
	
	public void generieren() {
		int anzahlKunden = Utility.random(min, max);
		List<Kunde> kunden = new LinkedList<Kunde>();
		for (int i = 1; i <= anzahlKunden; i++) {
			Kunde k = new Kunde(verkaufsraum);
			k.setName("Kunde " + i);
			kunden.add(k);
			k.start();
		}
		try {
			Thread.sleep(zeitraum);
			System.out.println("---------------NÄCHSTE RUNDE---------------");
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " wurde geweckt");
			Thread.currentThread().interrupt();
			for (Kunde k : kunden) {
				k.interrupt();
			}
			e.printStackTrace();
		}
	}
}
