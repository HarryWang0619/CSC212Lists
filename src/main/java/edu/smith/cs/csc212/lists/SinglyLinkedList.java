package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
//import me.jjfoley.adt.errors.TODOErr;

/**
 * A Singly-Linked List is a list that has only knowledge of its very first
 * element. Elements after that are chained, ending with a null node.
 * 
 * @author jfoley
 *
 * @param <T> - the type of the item stored in this list.
 */
public class SinglyLinkedList<T> extends ListADT<T> {
	/**
	 * The start of this list. Node is defined at the bottom of this file.
	 */
	Node<T> start;

	@Override
	public T removeFront() {
		checkNotEmpty();
		T v = this.start.value;
		this.start = this.start.next;
		return v;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		if(this.start.next==null) {
			T v = start.value;
			start = null;
			return v;
		}

		Node<T> t = this.start;
		while (t.next.next != null) {
			t = t.next;
		}
		T v = t.next.value;
		t.next.value = null;
		t.next = null;
		return v;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();

		if(index == 0) {
			return removeFront();
		} 
		// else check the pointers of its linked nodes

		Node<T> temp = this.start;
		for (int i = 1; i < index; i++) {
			temp = temp.next;
		}
		T v = temp.next.value;
		temp.next.value = null;
		temp.next = temp.next.next;

		return v;
	}

	@Override
	public void addFront(T item) {
		this.start = new Node<T>(item, start);
	}

	@Override
	public void addBack(T item) {
		if (this.start == null) {
			this.start = new Node<T>(item, null);
		} else {
			Node<T> now = this.start;
			while (now.next != null) {
				now = now.next;
			}
			now.next = new Node<T>(item, null);
		}
	}

	@Override
	public void addIndex(int index, T item) {
		if (index > this.size() || index < 0) {
			throw new BadIndexError(index);
		} else if (index == 0) {
			this.addFront(item);
		} else {
			Node<T> now = this.start;
			for (int i = 0; i < index-1; i++) {
				now = now.next;
			}			
			Node<T> temp = now.next;
			now.next = new Node<T>(item, temp);
		}
	}
 
	@Override
	public T getFront() {
		checkNotEmpty();
		return start.value;
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		Node<T> now = this.start;
		while (now.next != null) {
			now = now.next;
		}
		return now.value;
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			if (at++ == index) {
				return n.value;
			}
		}
		throw new BadIndexError(index);
	}

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		if (index > this.size()-1 || index < 0) {
			throw new BadIndexError(index);
		}
		int curr = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			if (curr == index) {
				n.value = value;
			}
			curr++;
		}
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null;
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of SinglyLinkedList to make a node.
	 * 
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;

		/**
		 * Create a node with no friends.
		 * 
		 * @param value - the value to put in it.
		 * @param next - the successor to this node.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

}
