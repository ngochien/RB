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
	private List<Kunde> kunden;

	public Kundengenerator(Verkaufsraum verkaufsraum, int zeitraum, int min, int max) {
		this.kunden = new LinkedList<Kunde>();
		this.verkaufsraum = verkaufsraum;
		this.zeitraum = zeitraum;
		this.min = min;
		this.max = max;
	}

	public void run() {		
		while (!isInterrupted()) {			
			generieren();
		}
		for (Kunde k : kunden) {
			k.interrupt();
		}
		System.err.println(Thread.currentThread().getName() + " WURDE BEENDET ");
	}
	
	public void generieren() {
		int anzahlKunden = Helper.random(min, max);
		System.out.println("\n\t\t" + anzahlKunden + " Kunde(n) wurden generiert\n");
		for (int i = 1; i <= anzahlKunden; i++) {
			Kunde k = new Kunde(verkaufsraum);
			kunden.add(k);
			k.start();
		}
		try {
			Thread.sleep(zeitraum);
			System.out.println("\nAbgewiesene Kunden: " + verkaufsraum.getAbgewieseneKunden());
			System.out.println("--------------------NÄCHSTE RUNDE--------------------\n");
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName() + " WURDE beim Schlafen GEWECKT");
			System.out.println("\nAbgewiesene Kunden: " + verkaufsraum.getAbgewieseneKunden());
			Thread.currentThread().interrupt();				
		}
	}
}
