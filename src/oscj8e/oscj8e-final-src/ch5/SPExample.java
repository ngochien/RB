/**
 * A example program illustrating a scheduled thread pool
 *
 * Figure 5.20
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.util.concurrent.*;

/**
 * This task will periodically check if there are entries
 * that need to be written from a log to a database.
 */
class Task implements Runnable
{
	public void run() {
		/**
		 * Check if there are any entries that
		 * need to be written from a log to
		 * a database.
		 */
		System.out.println("Checking for log entries ....");
	}
}

/**
 * This class creates a scheduled thread pool of size 1.
 * It creates a Task and schedules it to run once per second.
 */
public class SPExample
{

	public static void main(String[] args) {
		ScheduledExecutorService scheduler = java.util.concurrent.Executors.newScheduledThreadPool(1);

		Runnable task = new Task();

		// set the task to be scheduled once per second
		scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
	}
}
