/**
 * An implementation of the Buffer interface 
 * illustrating shared memory.
 *
 * Figure 3.18
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 */


@SuppressWarnings("unchecked")

public class BufferImpl<E> implements Buffer<E>
{
	private static final int BUFFER_SIZE = 5;
	private E[] elements;
	private int in, out, count;

	public BufferImpl() {
		count = 0;
		in = 0;
		out = 0;

		elements = (E[]) new Object[BUFFER_SIZE];
	}


	// producers call this method
	public void insert(E item) {
		while (count == BUFFER_SIZE)
			; // do nothing -- no free space

		// add an element to the buffer
		elements[in] = item;
		in = (in + 1) % BUFFER_SIZE;
		++count;
	}

	// consumers call this method
	public E remove() {
		E item;

		while (count == 0)
			; // do nothing - nothing to consume

		// remove an item from the buffer
		item = elements[out];
		out = (out + 1) % BUFFER_SIZE;
		--count;

		return item;
	}
}
