/**
 * 
 */
package fatima;

/**
 * Das Lokal beinhaltet den Verkaufsraum und das Personal
 * @author le
 *
 */
public class Lokal {

	public static final int SIMULATION_ZEIT = 5 * 1000;
	
	public static final int ANZAHL_PLATZ = 20;
	public static final int ZEITRAUM = 10 * 1000;
	public static final int MIN_ANZAHL_KUNDEN = 1;
	public static final int MAX_ANZAHL_KUNDEN = 5;
	
	public static final int ANZAHL_SERVICE_KRAFT = 2;
	public static final int ANZAHL_KUECHE_KRAFT = 3;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Verkaufsraum verkaufsraum = new Verkaufsraum(ANZAHL_PLATZ);
		Kundengenerator generator = new Kundengenerator(verkaufsraum, ZEITRAUM, MIN_ANZAHL_KUNDEN, MAX_ANZAHL_KUNDEN);
		generator.setName("Kundengenerator");
		
		System.err.println("-------------------- START -------------------");
		
		generator.start();
		
		try {
			Thread.sleep(SIMULATION_ZEIT);
			generator.interrupt();
			generator.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.err.println("-------------------- THE END -------------------");
	}

}
