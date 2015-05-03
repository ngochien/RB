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
	void setCounts();

	/**
	 * Get the number of characters a .. z A .. Z
	 */
	int getCharacterCount();

	/**
	 * Get the number of digits 0 .. 9
	 */
	int getDigitCount();
}
