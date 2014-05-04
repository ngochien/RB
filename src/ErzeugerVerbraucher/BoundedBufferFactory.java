package ErzeugerVerbraucher;

/* BoundedBufferServerFactory.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Erzeugt ein BoundedBuffer-Object (Factory-Methode getInstance) 
 mit implementierungsabh�ngigem Synchronisationsmechanismus 
 */
public class BoundedBufferFactory<E> {

  /* Varianten f�r die Implementierung des Synchronisationsmechanismus */
  enum SyncType {
    SEMAPHORE, MONITOR, COND_QUEUES
  }

  /* Erzeuge ein BoundedBuffer-Object (Factory)-Methode */
  public BoundedBuffer<E> getInstance(SyncType typ, int bufferMaxSize) {
    BoundedBuffer<E> instance = null;
    switch (typ) {
    /* Weise den gew�nschten Objekt-Typ der Variablen vom Interface-Typ zu */
    case SEMAPHORE:
      instance = new BoundedBufferSyncSemaphore<E>(bufferMaxSize);
      break;
    case MONITOR:
      instance = new BoundedBufferSyncMonitor<E>(bufferMaxSize);
      break;
    case COND_QUEUES:
      instance = new BoundedBufferSyncCondQueues<E>(bufferMaxSize);
      break;
    }
    return instance;
  }
}
