/**
 * Barrier.java
 */

public interface Barrier 
{
    /**
	* Each thread calls this method when it reaches the barrier.
     * All threads are released to continue processing when the 
     * last thread calls this method.
	*/
	void waitForOthers();


	/**
	 * Release all threads from waiting for the barrier.
	 * Any future calls to waitForOthers() will not wait
	 * until the Barrier is set again with a call to the constructor.
	 */
	void freeAll();
}
