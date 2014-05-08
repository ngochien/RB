package scheduler;

/**
 * SchedulerSimulation.java
 * 
 * This program demonstrates how the scheduler operates. This creates and starts
 * the scheduler and the example threads.
 * 
 */
public class SchedulerSimulation {
	static final int NUMBER_OF_PROCESSORS = 4;
	static final int NUMBER_OF_TESTTHREADS = NUMBER_OF_PROCESSORS * 5;
	static final int DEFAULT_TIME_SLICE = 300; // Zeitscheibe in ms
	static final int SIMULATION_TIME = NUMBER_OF_TESTTHREADS
			* DEFAULT_TIME_SLICE;

	private Scheduler<TestThread> cpuScheduler;

	/**
	 * Create scheduler and test threads and perform the simulation
	 */
	private void startSimulation() {

		/* create scheduler */
		cpuScheduler = new Scheduler<TestThread>(DEFAULT_TIME_SLICE);

		/* create and start test threads */
		for (int i = 0; i < NUMBER_OF_TESTTHREADS; i++) {
			TestThread t = new TestThread();
			t.setName("TestThread " + i);
			cpuScheduler.addThread(t);
			t.start();
		}

		/* start scheduler */
		cpuScheduler.start();

		try {
			Thread.sleep(SIMULATION_TIME);
		} catch (InterruptedException e) {
		}

		/* stop scheduler and test threads */
		cpuScheduler.interrupt();
		cpuScheduler.interruptAllThreads();
		System.out.println("Alles gestoppt!");
	}

	public static void main(String[] args) {
		SchedulerSimulation myTest = new SchedulerSimulation();
		myTest.startSimulation();
	}

}
