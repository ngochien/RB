/**
 * DoWork.java
 * 
 * This method is used to distinguish between the notify() and notifyAll()
 * methods. Run the program using notify(). The program will shortly hang as the
 * incorrect thread (i.e. the thread whose turn is not next!) receives the
 * notification. After that, change the call to notify() to notifyAll(). The
 * program should run correctly using notifyAll().
 *
 * Figure 6.32
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 * 
 */

public class DoWork {
	private int turn = 1;
	
	// myNumber is the number of the thread that wishes to do some work
	public synchronized void doWork(int myNumber) {
		while (turn != myNumber) {
			try {
				wait();
			} catch (InterruptedException e) { }
		}
		
		// do some work for awhile
		System.out.println("Worker " + myNumber + " will do some work");
		SleepUtilities.nap();
		
		// ok, we're finished. Now indicate to the next waiting
		// thread that it is their turn to do some work.
		System.out.println("Worker " + myNumber + " is done working");
		
		if (turn < 5)
			++turn;
		else
			turn = 1;
		
		// change this to notifyAll() to see it run correctly!
		notify();
	}
	
}
