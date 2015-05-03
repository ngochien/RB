/**
 * An interface for reader-writer locks.
 *
 * Figure 6.17
 * In the text wedo not have readers and writers
 * pass their number into each method. However we do so
 * here to aid in output messages.
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010. 
 */

public interface ReadWriteLock
{
	void acquireReadLock(int readerNum);

	void acquireWriteLock(int writerNum);

	void releaseReadLock(int readerNum);

	void releaseWriteLock(int writerNum);
}
