package ThreadTests;

/* ThreadTest5.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Beispiel f�r die Verwendung der setPriority-Methode
 */


public class ThreadTest6 {
  /* Beispiel f�r die Verwendung der setPriority-Methode */
  public static void main(String[] args) {
    MyThread6a threadZahl = new MyThread6a();
    MyThread6b threadText = new MyThread6b();
    System.err.println("-- Noch nichts passiert!--");

    /* Priorit�t des main-Threads auf Maximum setzen */
    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    /* Priorit�t des Zahl-Threads auf Minimum setzen und starten */
    threadZahl.setPriority(Thread.MIN_PRIORITY);
    threadZahl.start();

    /* Priorit�t des Text-Threads nahezu auf Maximum setzen und starten */
    threadText.setPriority(Thread.MAX_PRIORITY - 1);
    threadText.start();

    try {
      /* Main-Thread f�r 0,3 Sekunden anhalten */
      Thread.sleep(300);
    } catch (InterruptedException e) {
      // nichts
    }

    /* Threads beenden */
    threadZahl.interrupt();
    threadText.interrupt();
  }
}

class MyThread6a extends Thread {
  /* Hochz�hlen und Zahlen ausgeben */
  public void run() {
    int i = 0;
    /* Interrupt-Flag abfragen */
    while (!isInterrupted()) {
      System.err.println(i++);
    }
  }
}

class MyThread6b extends Thread {
  /* Intelligenten Text ausgeben */
  public void run() {
    /* Interrupt-Flag abfragen */
    while (!isInterrupted()) {
      System.err.println("------------ Hallo! --------------");
    }
  }
}
