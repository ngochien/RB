/**
 * A program that tests your implementation that
 * lists all thread groups 
 * - and threads within each group -
 * in the JVM.
 */


public class CreateThreadGroups
{
	public CreateThreadGroups() {
		// create a few thread groups
		ThreadGroup alpha = new ThreadGroup("alpha");
		ThreadGroup beta = new ThreadGroup("beta");
		ThreadGroup theta = new ThreadGroup(alpha, "theta");

		// create a few threads belonging to the above thread groups	
		(new Thread(alpha, new GroupWorker())).start();
		(new Thread(alpha, new GroupWorker())).start();
		(new Thread(alpha, new GroupWorker())).start();
		(new Thread(beta, new GroupWorker())).start();
		(new Thread(theta, new GroupWorker())).start();
		(new Thread(theta, new GroupWorker())).start();
	}

	class GroupWorker implements Runnable
	{
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
					for (int i = 0; i < 1000000; i++)
						;
				}
				catch (InterruptedException ie) { }
			}
		}
	}
}
