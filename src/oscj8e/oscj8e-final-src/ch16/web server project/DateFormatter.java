/**
 * DateFormatter.java
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateFormatter
{
	public static void main(String[] args) {
	   SimpleDateFormat formatter = new SimpleDateFormat("EEE','dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z");
	   Date rightNow = new Date();
	   String output;
	   
	   output = formatter.format(rightNow);
	   System.out.println(output);
	}
}
