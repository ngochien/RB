package scheduler;

/**
 * Scheduler.java
 * 
 * This class is a simple round-robin scheduler using the Java thread priority
 * mechanism. The idea for this scheduler came from "Java Threads" by Oaks and
 * Wong (Oreilly, 1999). Author of this implementation: M. Huebner, HAW Hamburg
 * 
 */
public class Scheduler<T extends Thread & Reportable> extends Thread {
	private int timeSlice;
	private CircularList<T> queue; // the ready queue

	public Scheduler(int timeSlice) {
		this.timeSlice = timeSlice;
		queue = new CircularList<T>();
		setPriority(6);
	}

	/**
	 * adds a thread to the ready queue
	 * 
	 * @return void
	 */
	public void addThread(T t) {
		t.setPriority(Thread.MIN_PRIORITY);
		queue.addItem(t);
	}

	/**
	 * interrupts all threads in the ready queue
	 * 
	 * @return void
	 */
	public void interruptAllThreads() {
		for (T t : queue.getQueueList()) {
			t.interrupt();
		}
	}

	/**
	 * schedules all threads in the queue by periodically changing the thread
	 * priority
	 * 
	 * @return void
	 */
	@Override
	public void run() {
		T current;
		
		while (!isInterrupted()) {
			/* select next thread */
			current = queue.getNext();
			
			if ((current != null) && (current.isAlive())) {
				current.setPriority(4);
				/* Print Log */
				reportThreadState();
				System.err.println("********* New High Priority: "
						+ current.getName());
				/* wait until time slice is over */
				schedulerSleep();
				current.setPriority(2);
			}
		}
	}

	/* *********** Private Methods ************* */
	/**
	 * this method lets the scheduler sleep
	 * 
	 * @return void
	 */
	private void schedulerSleep() {
		try {
			Thread.sleep(timeSlice);
		} catch (InterruptedException e) {
			/* renew interrupt flag */
			interrupt();
		}
	}

	/**
	 * report the state of all threads
	 * 
	 * @return void
	 */
	private void reportThreadState() {
		for (T t : queue.getQueueList()) {
			t.reportState();
		}
	}
}
