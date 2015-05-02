/**
 * An interface for a PID manager.
 *
 * The PID manager manages the set of 
 * process identifiers that are returned
 * when a new process is created (i.e. fork().)
 * When a process exists, its PID is returned
 * to the PID manager.
 *
 * The range of allowable PID's is
 * MIN_PID .. MAX_PID (inclusive)
 *
 * An implementation of this interface must 
 * thread safety.
 */

public interface PIDManager
{
	public static final int MIN_PID = 4;
     public static final int MAX_PID = 127;

	/**
	 * Obtains a pid from the PID manager.
	 * If no PID is available, return -1
	 * otherwise return a PID in the range
 	 * MIN_PID .. MAX_PID (inclusive)
	 */
	public int getPID();

	/**
	 * Obtains a pid from the PID manager.
	 * This implementation will wait until a
	 * valid PID in the range 
 	 * MIN_PID .. MAX_PID (inclusive)
	 * is available.
	 */
	public int getPIDWait();

	/**
	 * Returns a pid to the PID manager 
	 */
	public void releasePID(int pid);
}
