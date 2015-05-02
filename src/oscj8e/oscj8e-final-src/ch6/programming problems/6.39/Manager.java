/**
 * Manager.java
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

public class Manager
{
	public static final int MAX_RESOURCES = 5;
	private int availableResources = MAX_RESOURCES;

	/**
	 * Decrease available resources by count resources.
	 * Return 0 if sufficient resources available,
	 * otherwise return -1.
	 */
	 public synchronized int decreaseCount(int count) {
		if (availableResources < count) 
			return -1;
		else {
			availableResources -= count;

			return 1;
		}
	}

	/**
	 Increase available resources by count resources.
	 */
	public synchronized void increaseCount(int count) {
		availableResources += count;
	}
}
