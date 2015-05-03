/**
 * RemoteFileObject interface
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteFileObject extends Remote
{
	void open(String fileName) throws RemoteException;

	String readLine() throws RemoteException;

	void close() throws RemoteException;
}
