/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */
package scheduler;

/**
 * @author h13n
 * 
 */
public class Main {
	public static void main(String[] args) throws Exception {
		Thread thread1 = new Thread(new TesttThread(1));
		Thread thread2 = new Thread(new TesttThread(2));

		thread1.setPriority(Thread.MAX_PRIORITY);
		thread2.setPriority(Thread.MIN_PRIORITY);

		thread1.start();
		thread2.start();

		thread1.join();
		thread2.join();
	}

}

class TesttThread implements Runnable {
	int id;

	public TesttThread(int id) {
		this.id = id;
	}

	public void run() {
		for (int i = 1; i <= 10; i++) {
			System.out.println("Thread" + id + ": " + i);
		}
	}
}
