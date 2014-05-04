package scheduler;

/**
 * CircularList.java
 *
 * This class implements a circular list using the ArrayList class
 *
 */
import java.util.*;

public class CircularList<E> {
	
	private ArrayList<E> myList;
	private int index;  // saves the index current state

	public CircularList() {
		myList = new ArrayList<E>();
		index = 0;
	}

	/**
	 * This method returns the next element in the list. The next element of the
	 * last element is the first one. Returns null only if the list is empty.
	 */
	public E getNext() {
		E nextElement = null;  // result

		if (myList.size() > 0) {
			if (index == myList.size()) {
				/* last element --> first element */
				index = 0;
			}

			nextElement = myList.get(index);
			index++;
		}

		return nextElement;
	}

	/**
	 * this method adds an item to the list
	 */
	public void addItem(E item) {
		myList.add(item);
	}

	/**
	 * this method returns the queue as an array list
	 */
	public ArrayList<E> getQueueList() {
		return myList;
	}
}
