/**
 * Worker.java
 * 
 * This thread is used to demonstrate the difference between notify() and
 * notifyAll().
 */

public class Worker implements Runnable {
	private DoWork pile;

	private String name;

	private int num;

	public Worker(DoWork p, String n, int i) {
		pile = p;
		name = n;
		num = i;
	}

	public void run() {
		while (true) {
			SleepUtilities.nap();
			pile.doWork(num);
		}
	}

}
