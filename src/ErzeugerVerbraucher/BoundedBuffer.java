package ErzeugerVerbraucher;

/* BoundedBuffer.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Interface f�r einen generischen Datenpuffer mit synchronisierten Zugriffsmethoden und Fifo-Verarbeitung
 */

public interface BoundedBuffer<E> {
  
  /* Lege ein Item in den Puffer */
  public void enter(E item);

  /* Entnimm dem Puffer das Item */
  public E remove();

}
