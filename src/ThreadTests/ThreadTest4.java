package ThreadTests;

/* ThreadTest4.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Beispiel f�r die sinnvolle Verwendung des Interrupt-Flags 
 mit Abfangen der Interrupted-Exception
 */

public class ThreadTest4 {
  public static void main(String[] args) {
    MyThread4 testThread = new MyThread4();
    testThread.start();
    try {
      /* F�r 2012,8 ms anhalten */
      Thread.sleep(2012, 800);
    } catch (InterruptedException e) {
      // nichts
    }
    /* Thread unterbrechen (Interrupt-Flag setzen) */
    testThread.interrupt();
    System.err.println("Es wurde gestoppt!");
  }
}

class MyThread4 extends Thread {
  /* Hochz�hlen und Zahlen ausgeben */
  @Override
public void run() {
    int i = 0;
    
    /* Interrupt-Flag abfragen */
    while (!isInterrupted()) {
      System.err.println(i++);
      try {
        /* F�r 100 ms anhalten */
        Thread.sleep(100);
      } catch (InterruptedException e) {
        /*
         * Erneutes Setzen des Interrupt-Flags ist n�tig f�r die
         * Abfrage!
         */
        System.err.println("MyThread4 wurde durch Interrupt geweckt!");
        this.interrupt();
      }
    }
    System.err.println("MyThread4 wird beendet!");
  }
}
