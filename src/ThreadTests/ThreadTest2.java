package ThreadTests;

/* ThreadTest2.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Beispiel f�r die nicht sinnvolle Verwendung der stop-Methode
 */

public class ThreadTest2 {
  public static void main(String[] args) {
    MyThread2 testThread = new MyThread2();
    testThread.start();
    try {
      /* F�r 2000 ms anhalten */
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      // nichts
    }
    testThread.stop(); // STOP sollte nicht mehr verwendet werden!!
    System.err.println("Es wurde gestoppt!");
  }
}

class MyThread2 extends Thread {
  /* Hochz�hlen und Zahlen ausgeben */
  public void run() {
    int i = 0;
    while (true) {
      System.err.println(i++);
    }
  }
}
