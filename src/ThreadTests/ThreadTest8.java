package ThreadTests;

/* ThreadTest8.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Beispiel f�r die Verwendung des Java-Monitor-Mechanismus zum wechselseitigen Ausschluss
 */

public class ThreadTest8 {
  /*
   * Beispiel f�r die Verwendung des Java-Monitor-Mechanismus zum
   * wechselseitigen Ausschluss
   */
  public static void main(String[] args) {
    OutputServer8 outputServer = new OutputServer8();

    MyThread8a threadZahl = new MyThread8a(outputServer);
    threadZahl.setName("Zahl-Thread");

    MyThread8b threadText = new MyThread8b(outputServer);
    threadText.setName("Hallo-Thread");

    outputServer.showOutput("-- Noch nichts passiert!--");

    threadZahl.start();
    threadText.start();

    try {
      /* Main-Thread Sekunden anhalten */
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // nichts
    }

    /* Threads beenden */
    threadZahl.interrupt();
    threadText.interrupt();
  }
}

class OutputServer8 {
  /* Output ausgeben */

  public void showOutput(Object output) {
    synchronized (this) { // P(dieses OutputServer8-Objekt)

      /* 1. Ausgabenteil */
      showThreadName();

      try {
        /* Thread anhalten */
        Thread.sleep(2);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      /* 2. Ausgabenteil */
      System.err.println(output);

    } // V(dieses OutputServer8-Objekt)
  }

  public synchronized void showThreadName() {
    /* Zeige aktuellen Threadnamen an */
    System.err.println("Output von " + Thread.currentThread().getName());
  }
}

class MyThread8a extends Thread {
  /* Hochz�hlen und Zahlen ausgeben */
  private OutputServer8 outputServer;

  public MyThread8a(OutputServer8 outputServer) {
    this.outputServer = outputServer;
  }

  public void run() {
    int i = 0;
    /* Interrupt-Flag abfragen */
    while (!isInterrupted()) {
      outputServer.showOutput(i++);
    }
  }
}

class MyThread8b extends Thread {
  /* Intelligenten Text ausgeben */

  private OutputServer8 outputServer;

  public MyThread8b(OutputServer8 outputServer) {
    this.outputServer = outputServer;
  }

  public void run() {
    /* Interrupt-Flag abfragen */
    while (!isInterrupted()) {
      outputServer.showOutput("------------ Hallo! --------------");
    }
  }
}
