package ThreadTests;

/* ThreadTest1a.java
 Version 1.0
 Autor: M. H�bner
 Zweck: Beispiel f�r das Starten von mehreren Threads 
 mit Threaderzeugung �ber Ableitung aus der Thread-Klasse
 */

public class ThreadTest1a {
  /* Beispiel f�r das Starten von mehreren Threads */

  public static void main(String[] args) {
    /* Main: wird vom Hauptthread ausgef�hrt */

    /* Erzeuge Thread-Objekte (nur Java-Objekte) */
    MyThreadZahl threadZahl = new MyThreadZahl();
    MyThreadText threadText = new MyThreadText();
    System.err.println("-- Noch nichts passiert!--");

    /* Starte Threads */
    threadZahl.start();
    threadText.start();

    System.err.println("-- Hauptthread wird beendet!--");
  }
}

/* Eigene Klasse */
class MyThreadZahl extends Thread {
  /* Hochz�hlen und Zahlen ausgeben */
  @Override
public void run() {
    for (int i = 0; i < 100; i++) {
      System.err.println(i);
    }
  }
}

/* Eigene Klasse */
class MyThreadText extends Thread {
  /* Intelligenten Text ausgeben */
  @Override
public void run() {
    for (int i = 0; i < 100; i++) {
      System.err.println("------------ Ich bin auch noch da! ");
    }
  }
}