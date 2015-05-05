package fatima;

/**
 * Diese Klasse generiert f�r einen angegebenen Zeitraum eine zuf�llige Anzahl von Kunden
 * 
 * @author le
 *
 */
public class Kundengenerator extends Thread {

	/**
	 * F�r den angegebenen Zeitraum zuf�llig min bis max Elemente generieren
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
		System.out.println(Thread.currentThread().getName() + " wurde beendet. "
				+ "Es kommen keine Kunden mehr. Nur noch aktuelle Kunden werden fertig bedient");
	}
	
	public void generieren() {
		int anzahlKunden = Utility.random(min, max);
		System.out.println("\t\t" + anzahlKunden + " Kunden wurden generiert");
		for (int i = 1; i <= anzahlKunden; i++) {
			Kunde k = new Kunde(verkaufsraum);			
			k.start();
		}
		try {
			Thread.sleep(zeitraum);
			System.out.println("Abgewiesene Kunden: " + verkaufsraum.getAbgewieseneKunden());
			System.out.println("\n--------------------N�CHSTE RUNDE--------------------\n");
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " wurde beim Schlafen geweckt");
			Thread.currentThread().interrupt();				
		}
	}
}
