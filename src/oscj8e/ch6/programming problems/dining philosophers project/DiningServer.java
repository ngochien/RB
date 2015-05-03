/**
 * DiningServer.java
 *
 * This class contains the methods called by the  philosophers
 */

public interface DiningServer 
{  
   // called by a philosopher when they wish to eat 
   void takeForks(int philosopherNumber);
  
   // called by a philosopher when they are finished eating 
   void returnForks(int philosopherNumber);
}
