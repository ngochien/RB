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
		System.out.println(Thread.currentThread().getName() + " wartet. "
				+ "Es kommen keine Kunden mehr. Nur noch aktuelle Kunden werden fertig bedient");
		for (Kunde k : kunden) {
			try {
				k.join();
			} catch (InterruptedException e) {
//				k.interrupt();
//				e.printStackTrace();
			}
		}
		
		System.out.println(Thread.currentThread().getName() + " wurde beendet. "
				+ "Es kommen keine Kunden mehr. Nur noch aktuelle Kunden werden fertig bedient");
	}
	
	public void generieren() {
		int anzahlKunden = Utility.random(min, max);
		System.out.println("\t\t" + anzahlKunden + " Kunden wurden generiert");
		for (int i = 1; i <= anzahlKunden; i++) {
			Kunde k = new Kunde(verkaufsraum);
			kunden.add(k);
			k.start();
		}
		try {
			Thread.sleep(zeitraum);
			System.out.println("Abgewiesene Kunden: " + verkaufsraum.getAbgewieseneKunden());
			System.out.println("\n--------------------NÄCHSTE RUNDE--------------------\n");
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " wurde beim Schlafen geweckt");
			System.out.println("Abgewiesene Kunden: " + verkaufsraum.getAbgewieseneKunden());
			Thread.currentThread().interrupt();				
		}
	}
}
