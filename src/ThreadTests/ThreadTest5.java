package ThreadTests;

/* ThreadTest5.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Beispiel f�r die Verwendung der join-Methode / isAlive-Methode
 */

public class ThreadTest5 {
  /* Beispiel f�r die Verwendung der join-Methode / isAlive-Methode */
  public static void main(String[] args) {
    MyThread5 testThread = new MyThread5();
    testThread.setName("Der einzige MyThread5");
    
    System.err.println("Name des ausgef�hrten Threads: " + Thread.currentThread().getName());
    System.err.println("Name des Test-Threads: " + testThread.getName());
    System.err.println("TestThread alive? " + testThread.isAlive());
    
    testThread.start();
    System.err.println("TestThread alive? " + testThread.isAlive());

    /* Startzeit ermitteln */
    long startTime = System.nanoTime();
    try {
      /* main-Thread auf Ende des testThreads (Zielobjekt) warten lassen */
      testThread.join();
    } catch (InterruptedException e) {
      // nichts
    }
    System.err.println("TestThread alive? " + testThread.isAlive());
    /* Ende mit Ausgabe der Wartezeit */
    long usedTime = System.nanoTime() - startTime;
    System.err.println("Der main-Thread hat brav " + usedTime / 1000000.0
        + " ms gewartet!");
  }
}

class MyThread5 extends Thread {
  /* Bis 100 hochz�hlen und Zahlen ausgeben */
  @Override
public void run() {
    int i;
    for (i = 0; i < 10; i++) {
      System.err.println(i);
    }
    System.err.println("MyThread5 wird beendet!");
  }
}
