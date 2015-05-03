/**
 * An example of a thread that uses the yield() method
 * in the java.lang.Thread API
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

public class YieldThread implements Runnable
{
	public void run() {
		// perform a CPU-intensive task
		// ...
		// now yield control of the CPU to another thread

		Thread.yield();
	}
}
