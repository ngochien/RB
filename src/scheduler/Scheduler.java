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
		
		// Der Scheduler bekommt die höchste Prioritaet,
		// damit er andere Threads "steuern" kann.
		setPriority(MAX_PRIORITY);
		
		while (!isInterrupted()) {
			/* select next thread */
			current = queue.getNext();
			
			if ((current != null) && (current.isAlive())) {
				
				/* Print Log */
				reportThreadState();
				System.err.println("********* New High Priority: "
						+ current.getName());
				
				// Der aktuelle Thread bekommt höhere Prioritaet im Vergleich mit anderen
				// in der Queue, damit er die Chance hat, den CPU benutzen zu dürfen.
				current.setPriority(NORM_PRIORITY+1);
				
				/* wait until time slice is over */
				schedulerSleep();
				
				// Nach dem Aufwachen des Schedulers, der aktuelle Thread kriegt wieder
				// die niedrigste Prioritaet.
				current.setPriority(MIN_PRIORITY);
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
