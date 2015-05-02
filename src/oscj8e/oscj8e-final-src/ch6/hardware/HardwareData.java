/**
 * HardwareData.java
 *
 * Figure 6.4
 *
 * Generic value structure that is used by the test-and-set and swap
 * instructions for hardware solutions of the critical section problem.
 *
 * Note these solutions are NOT thread-safe and are intended entirely
 * for purposes of demonstrating the get-and-set functionality provided
 * by certain architectures.
 *
 * Thread safety would require these operations perform atomically.
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */


public class HardwareData
{
	private boolean value = false;
	
	public HardwareData(boolean value) {
		this.value = value;
	}

	public boolean get() {
		return value;
	}

	public void set(boolean newValue) {
		this.value = newValue;
	}

	public boolean getAndSet(boolean newValue) {
		boolean oldValue = this.get();
		this.set(newValue);

		return oldValue;
	}

	
	public void swap(HardwareData other) {
		boolean temp = this.get();

		this.set(other.get());
		other.set(temp);
	}
}
