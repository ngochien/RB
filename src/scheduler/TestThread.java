package scheduler;

/**
 * TestThread.java
 * 
 * This file defines the behavior of the test threads
 * 
 */
public class TestThread extends Thread implements Reportable {
  private long counter = 0;

  public void incCounter() {
    counter++;
  }

  public void resetCounter() {
    counter = 0;
  }

  public long getCounter() {
    return counter;
  }

  @Override
  public void reportState() {
    /* Print the counter state and reset the counter */
    System.err.printf("%14s: %,10d\n", getName(), getCounter());
    resetCounter();
  }

  public void run() {
    /* Count activity */
    while (!isInterrupted()) {
      incCounter();
      Math.random();
    }
  }
}
