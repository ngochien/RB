/**
 * DoWork.java
 *
 * Figure 6.38
 *
 * This implements the doWork() method using condition variables.
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010. 
 */

import java.util.concurrent.locks.*;

public class DoWork
{
	private int turn;
	private Lock lock;
	private Condition[] condVars;

	public DoWork() {
		turn = 0;
		lock = new ReentrantLock();
		condVars = new Condition[5];

		for (int i = 0; i < 5; i++)
			condVars[i] = lock.newCondition();
	}


	// myNumber is the number of the thread that wishes to do some work
	public void doWork(int myNumber) {
		lock.lock();

		try {
			// if it's not my turn, then wait 
			// until I'm signaled
			if (myNumber != turn)
				condVars[myNumber].await();

			// do some work for awhile
			System.out.println("Worker " + myNumber + " will do some work");
			SleepUtilities.nap();

			// now signal to the next waiting thread
			turn = (turn + 1) % 5;
			condVars[turn].signal();
		}
		catch (InterruptedException ie) { }
		finally {
			lock.unlock();
		}
	}

}
