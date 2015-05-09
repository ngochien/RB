/**
 * 
 */
package fatima;

import java.util.Observable;
import java.util.Observer;

import scheduler.Reportable;

/**
 * 
 * Teilt die Kunden in die Warteschlangen auf
 * 
 * @author Fatima
 *
 */
public class Verteiler extends Thread implements Observer {

	private int anzahlWarteschlangen;

	private Warteschlange[] warteschlange;

	private Kunde kunde;
	
	public Verteiler(int anzahlWarteschlangen) {
		this.anzahlWarteschlangen = anzahlWarteschlangen;
		warteschlange = new Warteschlange[anzahlWarteschlangen];
	}

	@Override
	public void run() {
		/*
		 * Erzeuge Objekte und lege sie in den Puffer.
		 */

		while (!isInterrupted()) {

			// Puffer-Zugriffsmethode aufrufen --> Synchronisation über den
			// Puffer!
			warteschlange[0].add(kunde);

			if (!isInterrupted()) {

			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
}
