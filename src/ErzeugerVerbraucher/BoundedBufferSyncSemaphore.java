package ErzeugerVerbraucher;

/* BoundedBufferMonitor.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Stellt einen generischen Datenpuffer mit Zugriffsmethoden und 
 Synchronisation �ber Semaphore zur Verf�gung
 */
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBufferSyncSemaphore<E> implements BoundedBuffer<E> {
  /* Datenpuffer f�r Elemente vom Typ E mit Zugriffsmethoden enter und remove */
  private int bufferMaxSize; // maximale Puffergr��e
  private LinkedList<E> buffer; // Liste als Speicher

  private ReentrantLock mutex_S; // = S: Synchronisation des Zugriffs
  private Semaphore sem_F; // = F: Anzahl freier Pl�tze
  private Semaphore sem_B; // = B: Anzahl belegter Pl�tze

  /* Konstruktor */
  public BoundedBufferSyncSemaphore(int bufferSize) {
    bufferMaxSize = bufferSize;
    buffer = new LinkedList<E>();

    mutex_S = new ReentrantLock();
    sem_F = new Semaphore(bufferMaxSize);
    sem_B = new Semaphore(0);
  }

  /* Producer (Erzeuger) rufen die Methode ENTER auf */
  @Override
public void enter(E item) {
    try {
      // Versuche, die Anzahl freier Pl�tze zu erniedrigen. Falls auf Null
      // ==> Warten!
      sem_F.acquire();
      // Buffer f�r Zugriff gesperrt? Evtl. ==> Warten!
      mutex_S.lockInterruptibly();
    } catch (InterruptedException e) {
      /*
       * Ausf�hrender (wartender) Thread hat Interrupt erhalten -->
       * Interrupt-Flag im ausf�hrenden Thread setzen und Methode beenden
       */
      Thread.currentThread().interrupt();
      return;
    }

    /* Item zum Puffer hinzuf�gen */
    buffer.add(item);
    System.err
        .println("          ENTER: "
            + Thread.currentThread().getName()
            + " hat ein Objekt in den Puffer gelegt. Aktuelle Puffergr��e: "
            + buffer.size());

    // Buffer entsperren und ggf.wartenden Producer/Consumer wecken
    mutex_S.unlock();
    // Anzahl belegter Pl�tze erh�hen und ggf.wartenden Consumer wecken
    sem_B.release();
  }

  /* Consumer (Verbraucher) rufen die Methode REMOVE auf */
  @Override
public E remove() {
    E item;
    try {
      // Versuche, die Anzahl belegter Pl�tze zu erniedrigen. Falls auf
      // Null ==> Warten!
      sem_B.acquire();
      // Buffer f�r Zugriff gesperrt? Evtl. ==> Warten!
      mutex_S.lockInterruptibly();
    } catch (InterruptedException e) {
      /*
       * Ausf�hrender (wartender) Thread hat Interrupt erhalten -->
       * Interrupt-Flag im ausf�hrenden Thread setzen und Methode beenden
       */
      Thread.currentThread().interrupt();
      return null;
    }

    /* Item aus dem Buffer entfernen */
    item = buffer.removeFirst();
    System.err
        .println("          REMOVE: "
            + Thread.currentThread().getName()
            + " hat ein Objekt aus dem Puffer entnommen. Aktuelle Puffergr��e: "
            + buffer.size());

    // Buffer entsperren und ggf.wartenden Producer/Consumer wecken
    mutex_S.unlock();
    // Anzahl freier Pl�tze erh�hen und ggf.wartenden Producer wecken
    sem_F.release();

    return item;
  }
}
