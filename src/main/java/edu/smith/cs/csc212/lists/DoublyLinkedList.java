package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * A Doubly-Linked List is a list based on nodes that know of their successor and predecessor.
 * @author jfoley
 *
 * @param <T>
 */
public class DoublyLinkedList<T> extends ListADT<T> {
	/**
	 * This is a reference to the first node in this list.
	 */
	private Node<T> start;
	/**
	 * This is a reference to the last node in this list.
	 */
	private Node<T> end;
	
	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
	}
	

	@Override
	public T removeFront() {
		checkNotEmpty();
		T v = this.start.value;
		if (this.start.after == null) {
			this.end = null;
			this.start = null;
			return v;		
		} else {
			this.start = this.start.after;
			this.start.before = null;
		}
		return v;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T v = this.end.value;
		if (this.size() == 1) {
			this.end = null;
			this.start = null;
			return v;
		} else {
			this.end = this.end.before;
			this.end.after = null;
		}
		return v;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		T v;
		int i = 0;
		if (index == 0) {
			v = this.removeFront();
			return v;
		} else if (index == this.size() - 1) {
			v = this.removeBack();
			return v;
		} else {
			for (Node<T> now = this.start; now != null; now = now.after) {
				if (index == i){
					v = now.value;
					Node<T> last = now.before;
					Node<T> next = now.after;
					last.after = next;
					next.before = last;
					return v;
				}
				i++;
			}
			throw new BadIndexError(index);
		}

	}

	@Override
	public void addFront(T item) {
		Node<T> add = new Node<T>(item);
		if (isEmpty()) {
			this.start = add;
			this.end = add;
		} else {
			this.start.before = add;
			add.after = this.start;
			this.start = add; 
		}
	}

	@Override
	public void addBack(T item) {
		if (isEmpty()) {
			this.start = new Node<T>(item);
			this.end = this.start;
		} else {
			Node<T> secondLast = this.end;
			end = new Node<T>(item);
			end.before = secondLast;
			secondLast.after = end;
		}
	}

	@Override
	public void addIndex(int index, T item) {
		Node<T> newNode= new Node<T>(item);
		int i = 0;
		if (index == 0) {
			this.addFront(item);
			return;
		} else if (index == this.size()) {
			this.addBack(item);
			return;
		} else {
			for (Node<T> last = this.start; last != null; last = last.after) {
				if (index-1 == i){
					newNode.after = last.after;
					newNode.before = last; 
					newNode.after.before = newNode;
					last.after = newNode;
					return;
				}
				i++;
			}
			throw new BadIndexError(index);
		}

	}

	@Override
	public T getFront() {
		this.checkNotEmpty();
		return this.start.value;
	}

	@Override
	public T getBack() {
		this.checkNotEmpty();
		return this.end.value;
	}
	
	@Override
	public T getIndex(int index) {
		this.checkNotEmpty();
		int i = 0;
		// if (index == 0) {
		// 	return this.start.value;
		// }
		// if (index == this.size()-1) {
		// 	return this.end.value;
		// }
		for (Node<T> now = this.start; now != null; now = now.after) {
			if (index == i){
				return now.value;
			}
			i++;
		}
		throw new BadIndexError(index);
	}
	
	public void setIndex(int index, T value) {
		// if (index < 0) {
		// 	throw new BadIndexError(index);
		// }
		int i = 0;
		Node<T> newNode = new Node<T>(value);
		if (index == 0) {
			newNode.after = this.start.after;
			newNode.after.before = newNode;
			this.start = newNode;
			return;
		}
		this.checkNotEmpty();
		if (index == this.size()-1) {
			newNode.before = this.end.before;
			newNode.before.after = newNode;
			this.end = newNode;
			return;
		}
		for (Node<T> now = this.start; now != null; now = now.after) {
			if (index == i){
				newNode.after = now.after;
				newNode.before = now.before;
				newNode.before.after = newNode;
				newNode.after.before = newNode;
				return;
			}
			i++;
		}
		throw new BadIndexError(index);
	}

	@Override
	public int size() {
		if (isEmpty()) {
			return 0;
		}
		int size = 0;
		for (Node<T> now = this.start; now != null; now = now.after) {
			size++;
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null && this.end == null;
	}

	/**
	 * The node on any linked list should not be exposed.
	 * Static means we don't need a "this" of DoublyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}
}
