package fatima;

/** 
 * Interface für einen generischen Datenpuffer mit synchronisierten Zugriffsmethoden und Fifo-Verarbeitung
 */

public interface Puffer<E> {
  
  /* Lege ein Item in den Puffer */
  public void add(E element);

  /* Entnimm dem Puffer das Element */
  public E remove();

}
