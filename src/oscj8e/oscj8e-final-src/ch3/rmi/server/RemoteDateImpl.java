/**
 * Implementation of the RemoteDate interface
 *
 * Figure 3.32
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class RemoteDateImpl extends UnicastRemoteObject implements RemoteDate
{
    public RemoteDateImpl() throws RemoteException {  }
    
    public  Date getDate() throws RemoteException {
        return new Date();
    }
    
   public static void main(String[] args)  {
        try {
         RemoteDate dateServer = new RemoteDateImpl();

         // Bind this object instance to the name "RMIDateObject"
         Naming.rebind("RMIDateObject", dateServer);

         System.out.println("RMIDateObject bound in registry");
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
  
   
   
}
