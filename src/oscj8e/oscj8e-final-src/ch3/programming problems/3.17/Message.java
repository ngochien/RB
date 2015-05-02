/**
 * Message interface
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */

public interface Message 
{
	/**
 	 * Set the number of characters and digits in the message
	 */
	public void setCounts();

	/**
	 * Get the number of characters a .. z A .. Z
	 */
	public int getCharacterCount();

	/**
	 * Get the number of digits 0 .. 9
	 */
	public int getDigitCount();
}
