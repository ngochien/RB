/**
 * Database.java
 *
 * This class contains the methods the readers and writers will use
 * to coordinate access to the database. Access is coordinated using semaphores.
 *
 * Figure 6.18
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010. 
 */


public class Database implements ReadWriteLock
{
	// the number of active readers
	private int readerCount;
	
	Semaphore mutex;  // controls access to readerCount
	Semaphore db;     // controls access to the database
	
	public Database() {
		readerCount = 0;
		
		mutex = new Semaphore(1);
		db = new Semaphore(1);
	}
	
	public void acquireReadLock(int readerNum) {
		mutex.acquire();
		
		++readerCount;
		
		// if I am the first reader tell all others
		// that the database is being read
		if (readerCount == 1)
			db.acquire();
		
		System.out.println("Reader " + readerNum + " is reading. Reader count = " + readerCount);
		mutex.release();
	}
	
	public void releaseReadLock(int readerNum) {
		mutex.acquire();
		
		--readerCount;
		
		// if I am the last reader tell all others
		// that the database is no longer being read
		if (readerCount == 0)
			db.release();
		
		System.out.println("Reader " + readerNum + " is done reading. Reader count = " + readerCount);
		
		mutex.release();
	}
	
	public void acquireWriteLock(int writerNum) {
		db.acquire();
		System.out.println("writer " + writerNum + " is writing.");
	}
	
	public void releaseWriteLock(int writerNum) {
		System.out.println("writer " + writerNum + " is done writing.");
		db.release();
	}
	
	
}
