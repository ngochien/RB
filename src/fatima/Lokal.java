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

	public static final int SIMULATION_ZEIT = 60 * 1000;
	
	public static final int ANZAHL_PLATZ = 1;
	public static final int ZEITRAUM = 5 * 1000;
	public static final int MIN_ANZAHL_KUNDEN = 5;
	public static final int MAX_ANZAHL_KUNDEN = 15;
	
	public static final int ANZAHL_SERVICE_KRAFT = 2;
	public static final int ANZAHL_KUECHE_KRAFT = 3;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Verkaufsraum verkaufsraum = new Verkaufsraum(ANZAHL_PLATZ);
		Kundengenerator generator = new Kundengenerator(verkaufsraum, ZEITRAUM, MIN_ANZAHL_KUNDEN, MAX_ANZAHL_KUNDEN);
		generator.setName("Kundengenerator");
		
		System.out.println("-------------------- START -------------------");
		
		generator.start();
		
		try {
			Thread.sleep(SIMULATION_ZEIT);
			generator.interrupt();
			generator.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("-------------------- THE END -------------------");
	}

}
