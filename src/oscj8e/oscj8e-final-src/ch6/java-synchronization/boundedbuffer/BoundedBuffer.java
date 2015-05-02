/**
 * BoundedBuffer.java
 * 
 * This program implements the bounded buffer using Java synchronization.
 *
 * Figure 6.31
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Eighth Edition
 * Copyright John Wiley & Sons - 2010.
 * 
 */
@SuppressWarnings("unchecked")

public class BoundedBuffer<E> implements Buffer<E>
{
	private static final int BUFFER_SIZE = 5;

	private int count; // number of items in the buffer

	private int in; // points to the next free position in the buffer

	private int out; // points to the next full position in the buffer

	private E[] buffer;

	public BoundedBuffer() {
		// buffer is initially empty
		count = 0;
		in = 0;
		out = 0;
		buffer = (E[]) new Object[BUFFER_SIZE];
	}

	public synchronized void insert(E item) {
		while (count == BUFFER_SIZE) {
			try {
				wait();
			} catch (InterruptedException e) { }
		}

		// add an item to the buffer
		buffer[in] = item;
		in = (in + 1) % BUFFER_SIZE;
		++count;

		if (count == BUFFER_SIZE)
			System.out.println("Producer Entered " + item + " Buffer FULL");
		else
			System.out.println("Producer Entered " + item + " Buffer Size = "
					+ count);

		notify();
	}

	// consumer calls this method
	public synchronized E remove() {
		E item;

		while (count == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		// remove an item from the buffer
		item = buffer[out];
		out = (out + 1) % BUFFER_SIZE;
		--count;

		if (count == 0)
			System.out.println("Consumer Consumed " + item + " Buffer EMPTY");
		else
			System.out.println("Consumer Consumed " + item + " Buffer Size = "
					+ count);

		notify();

		return item;
	}

}
