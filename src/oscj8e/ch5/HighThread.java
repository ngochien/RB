/**
 * An example of a thread that uses the setPriority() method
 * in the java.lang.Thread API
 *
 * Figure 5.17
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

public class HighThread implements Runnable
{
	public void run() {
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY + 3);
		/**
		 * Remainder of run() method
		 * ...
		 */
	}
}
