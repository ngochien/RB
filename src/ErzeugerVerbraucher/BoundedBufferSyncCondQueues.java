package ErzeugerVerbraucher;

/* BoundedBufferMonitor.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Stellt einen generischen Datenpuffer mit Zugriffsmethoden und 
 Synchronisation �ber Java-Condition Queues zur Verf�gung
 */
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBufferSyncCondQueues<E> implements BoundedBuffer<E> {
  /* Datenpuffer f�r Elemente vom Typ E mit Zugriffsmethoden enter und remove */
  private int bufferMaxSize; // maximale Puffergr��e
  private LinkedList<E> buffer; // Liste als Speicher

  private final Lock bufferLock = new ReentrantLock();
  private final Condition notFull = bufferLock.newCondition();
  private final Condition notEmpty = bufferLock.newCondition();

  /* Konstruktor */
  public BoundedBufferSyncCondQueues(int bufferSize) {
    bufferMaxSize = bufferSize;
    buffer = new LinkedList<E>();
  }

  /* Producer (Erzeuger) rufen die Methode ENTER auf */
  @Override
public void enter(E item) {
    // Zugriff auf Buffer sperren
    bufferLock.lock();

    /* Solange Puffer voll ==> warten! */
    while (buffer.size() == bufferMaxSize) {
      try {
        notFull.await(); // Warte auf Bedingung "not full" (--> eigene
                  // Warteschlange!) und gib Zugriff frei
      } catch (InterruptedException e) {
        // Puffer wieder im Zugriff (lock)
        /*
         * Ausf�hrender Thread hat Interrupt erhalten --> Interrupt-Flag
         * im ausf�hrenden Thread setzen und Methode beenden
         */
        Thread.currentThread().interrupt();
        // Zugriff auf Buffer freigeben
        bufferLock.unlock();
        return;
      }
    }
    /* Item zum Puffer hinzuf�gen */
    buffer.add(item);
    System.err
        .println("          ENTER: "
            + Thread.currentThread().getName()
            + " hat ein Objekt in den Puffer gelegt. Aktuelle Puffergr��e: "
            + buffer.size());

    // Gezielt einen wartenden Consumer wecken (spezielle Warteschlange!)
    notEmpty.signal();

    // Zugriff auf Buffer freigeben
    bufferLock.unlock();
  }

  /* Consumer (Verbraucher) rufen die Methode REMOVE auf */
  @Override
public E remove() {
    E item;
    // Zugriff auf Buffer sperren
    bufferLock.lock();

    /* Solange Puffer leer ==> warten! */
    while (buffer.size() == 0) {
      try {
        notEmpty.await(); // Warte auf Bedingung "not empty" (--> eigene
        // Warteschlange!) und gib Zugriff frei
      } catch (InterruptedException e) {
        // Puffer wieder im Zugriff (lock)
        /*
         * Ausf�hrender Thread hat Interrupt erhalten --> Interrupt-Flag
         * im ausf�hrenden Thread setzen und Methode beenden
         */
        Thread.currentThread().interrupt();
        // Zugriff auf Buffer freigeben
        bufferLock.unlock();
        return null;
      }
    }
    /* Item aus dem Buffer entfernen */
    item = buffer.removeFirst();
    System.err
        .println("          REMOVE: "
            + Thread.currentThread().getName()
            + " hat ein Objekt aus dem Puffer entnommen. Aktuelle Puffergr��e: "
            + buffer.size());

    // Gezielt einen wartenden Producer wecken (spezielle Warteschlange!)
    notFull.signal();

    // Zugriff auf Buffer freigeben
    bufferLock.unlock();

    return item;
  }
}
