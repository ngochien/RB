import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Worker.java
 *
 * This is a thread that is used to demonstrate solutions
 * to the critical section problem using the test-and-set instruction.
 *
 */

public class Worker implements Runnable
{
	private String name; 		// the name of this thread
	private HardwareData mutex;		// shared mutex
	
	public Worker(String name, HardwareData mutex) {
		this.name = name;
		this.mutex = mutex;
	}
	
	/**
	 * This run() method tests the getAndSet() instruction
	 */
	public void run() {
		while (true) {
			System.out.println(name + " wants to enter CS");
			while (mutex.getAndSet(true))
				Thread.yield(); // wait

			System.out.println(name + " is in critical section");			
			MutualExclusionUtilities.criticalSection(name);
			System.out.println(name + " is out of critical section");			
			mutex.set(false);
			MutualExclusionUtilities.remainderSection(name);
		}
	}
	
	/**
	 * This run() method tests the swap() instruction
	public void run() {
		key = new HardwareData(true);
		
		while (true) {
			System.out.println(name + " wants to enter CS");
			key.set(true);
			
			do {
				mutex.swap(key);
			}
			while(key.get() == true);
			
			MutualExclusionUtilities.criticalSection(name);
			
			mutex.set(false);
			
			MutualExclusionUtilities.nonCriticalSection(name);
		}
	}
	 */
	
	
	
}
