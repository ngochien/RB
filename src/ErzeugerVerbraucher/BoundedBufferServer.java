package ErzeugerVerbraucher;

/* BoundedBufferServer.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Erzeugt eine Simulationsumgebung f�r ein Erzeuger/Verbrauchersystem
 */

import java.util.Date;
import java.util.LinkedList;

public class BoundedBufferServer {
  public final int NO_PRODUCER = 3; // Anzahl Erzeuger-Threads
  public final int NO_CONSUMER = 2; // Anzahl Verbraucher-Threads
  public final int BUFFER_SIZE = 2; // Gewuenschte max. Puffergroesse
  /* Synchronisationsmechanismus */
  public final BoundedBufferFactory.SyncType SYNC_TYPE = BoundedBufferFactory.SyncType.SEMAPHORE;
  public final int SIMULATION_TIME = 1000; // Gewuenschte Simulationsdauer in ms

  /*
   * Das Puffer-Objekt mit Elementtyp Date, Auswahl des
   * Synchronisationsmechanismus und vorgegebener Platzanzahl (Gr��e)
   */
  public BoundedBuffer<Date> buffer = new BoundedBufferFactory<Date>()
      .getInstance(SYNC_TYPE, BUFFER_SIZE);

  public static void main(String[] args) {
    /* Starte Simulation */
    new BoundedBufferServer().startSimulation();
  }

  public void startSimulation() {
    /* Starte und beende Threads */
    LinkedList<Producer> producerList = new LinkedList<Producer>();
    LinkedList<Consumer> consumerList = new LinkedList<Consumer>();

    System.err.println("-------------------- START -------------------");

    // Erzeuger - Threads erzeugen
    for (int i = 1; i <= NO_PRODUCER; i++) {
      Producer current = new Producer(buffer);
      current.setName("Erzeuger " + i);
      producerList.add(current);
      current.start();
    }
    
    // Verbraucher - Threads erzeugen
    for (int i = 1; i <= NO_CONSUMER; i++) {
      Consumer current = new Consumer(buffer);
      current.setName("Verbraucher " + i);
      consumerList.add(current);
      current.start();
    }

    // Laufzeit abwarten
    try {
      Thread.sleep(SIMULATION_TIME);

      System.err.println("-------------------- ENDE -------------------");

      // Erzeuger - Threads stoppen
      for (Producer current : producerList) {
        current.interrupt();
      }

      // Verbraucher - Threads stoppen
      for (Consumer current : consumerList) {
        current.interrupt();
      }

    } catch (InterruptedException e) {
    }

  }
}
