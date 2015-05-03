/**
 * The RemoteDate interface
 *
 * Figure 3.31
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface RemoteDate extends Remote
{
	Date getDate() throws RemoteException;
}
