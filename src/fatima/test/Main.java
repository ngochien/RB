/**
 * 
 */
package fatima.test;

import fatima.Kunde;

/**
 * @author le
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Kunde x = new Kunde();
		x.setVerweilszeit(9);
		System.out.println(x.getVerweilszeit());
		zero(x);
		System.out.println(x.getVerweilszeit());
		f(9);
	}
	
	public static void zero(Kunde x) {
//		x.setVerweilszeit(0);
		x = new Kunde();
		x.setVerweilszeit(10);
//		System.out.println(x.getVerweilszeit());
	}
	
	public static int f( int x) {
		int i = 0;
		int z = 1;
		int y = 1;
		while (z < x) {
			System.out.println(z==y);
			i ++;
			z = z + y;
			y = y +y;
		}
		System.out.println(i);
		return i;
	}

}
