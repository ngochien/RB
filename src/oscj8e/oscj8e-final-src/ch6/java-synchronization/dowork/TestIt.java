/**
 * TestIt.java
 *
 * This program creates the threads that are used to demonstrate
 * the difference between notify() and notifyAll().
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Sixth Edition
 * Copyright John Wiley & Sons - 2003.
 */

public class TestIt
{
   public static void main(String args[]) {
      DoWork pile = new DoWork();

      Thread[] bees = new Thread[5];

      for (int i = 1; i <= 5; i++)
         bees[i-1] = new Thread(new Worker(pile, "Worker " + (new Integer(i)).toString(), i) );

      for (int i = 1; i <= 5; i++)
         bees[i-1].start();
   }
}

