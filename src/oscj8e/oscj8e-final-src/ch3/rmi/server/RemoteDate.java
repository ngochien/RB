/**
 * The RemoteDate interface
 *
 * Figure 3.31
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.util.Date;
import java.rmi.*;

public interface RemoteDate extends Remote
{
	public Date getDate() throws RemoteException;
}
