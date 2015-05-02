/**
 * A example program illustrating a cached thread pool
 *
 * Figure 5.20
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.util.concurrent.*;

public class TPExample
{
	public static void main(String[] args) {
		int numTasks = Integer.parseInt(args[0].trim());
		
		// create the thread pool
		ExecutorService pool = java.util.concurrent.Executors.newCachedThreadPool();

		// run each task using a thread in the pool
		for (int i = 0; i < 5; i++)
			pool.execute(new Task());
		
		// sleep for 5 seconds
		try { Thread.sleep(5000); } catch (InterruptedException ie) { }

		pool.shutdown();
	}
}


class Task implements Runnable
{
	public void run() {
		System.out.println("I am working on a task.");
	}
}
