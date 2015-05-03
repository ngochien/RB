/**
 * GetAndSetFactory.java
 *
 * This program tests the get-and-set instructions.
 */

public class GetAndSetFactory
{
	public static void main(String args[]) {
		HardwareData lock = new HardwareData(false);

		for (int i = 0; i < 5; i++)
			new Thread(new Worker("worker " + new Integer(i).toString(),  lock )).start();
	}
}
