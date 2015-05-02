/**
 * The RMI Client
 *
 * Figure 3.33
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.rmi.*;

public class RMIClient 
{  
	public static final String server = "127.0.0.1";

   	public static void main(String args[]) { 
    		try {
      		String host = "rmi://" + server + "/RMIDateObject";
      		RemoteDate dateServer = (RemoteDate)Naming.lookup(host);
      		System.out.println(dateServer.getDate());
    		}
    		catch (Exception e) {
        		System.err.println(e);
    		}
   	}
}

