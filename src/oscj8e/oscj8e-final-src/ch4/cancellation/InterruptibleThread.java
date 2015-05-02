/**
 * An example program illustrating thread interruption
 *
 * Figure 4.16
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

public class InterruptibleThread implements Runnable 
{
	/**
	 * This thread will continue to run as long
	 * as it is not interrupted.
	 */
	public void run() {
		while (true) {
			/**
			 * do some work for awhile
			 */

			if (Thread.currentThread().isInterrupted()) {
				System.out.println("I'm interrupted!");
				break;
			}
		}
		// clean up and terminate
	}

	public static void main(String[] args) {
        Thread worker = new Thread (new InterruptibleThread());
        worker.start();
        
        // now wait 3 seconds before interrupting it
        try { Thread.sleep(3000); } catch (InterruptedException ie) { }
        
        worker.interrupt();
    }
}
