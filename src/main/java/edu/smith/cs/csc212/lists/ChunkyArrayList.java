package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.EmptyListError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * This is a data structure that has an array inside each node of an ArrayList.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyArrayList<T> extends ListADT<T> {
	/**
	 * How big is each chunk?
	 */
	private int chunkSize;
	/**
	 * Where do the chunks go?
	 */
	private GrowableList<FixedSizeList<T>> chunks;

	/**
	 * Create a ChunkedArrayList with a specific chunk-size.
	 * @param chunkSize - how many items to store per node in this list.
	 */
	public ChunkyArrayList(int chunkSize) {
		this.chunkSize = chunkSize;
		chunks = new GrowableList<>();
	}
	
	private FixedSizeList<T> makeChunk() {
		return new FixedSizeList<>(chunkSize);
	}

	@Override
	public T removeFront() {
		T v = this.getIndex(0);
		this.removeIndex(0);
		return v;
	}

	@Override
	public T removeBack() {
		T v = this.getIndex(this.size()-1);
		this.removeIndex(this.size()-1);
		return v;
	}

	@Override
	public T removeIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		int chunkIndex = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			
			int end = start + chunk.size();
			if (start <= index && index < end) {
				T v = chunk.removeIndex(index - start);
				if (chunk.isEmpty()) {
					chunks.removeIndex(chunkIndex);
				}
				return v;
			}
			chunkIndex++;
			start = end;
		}
		throw new BadIndexError(index);
	}

	@Override
	public void addFront(T item) {
		this.addIndex(0, item);
	}

	@Override
	public void addBack(T item) {
		//this.addIndex(this.size(),item);
		if(isEmpty()) {
			this.chunks.addBack(makeChunk());
			chunks.getBack().addBack(item);
		}
		else {
			if(chunks.getBack().isFull()) {
				this.chunks.addBack(makeChunk());
				chunks.getBack().addBack(item);
			} else {
				chunks.getBack().addBack(item);
			}
		}
	}

	@Override
	public void addIndex(int index, T item) {
		// THIS IS THE HARDEST METHOD IN CHUNKY-ARRAY-LIST.
		// DO IT LAST.
		
		int chunkIndex = 0;
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index <= end) {
				if (chunk.isFull()) {
					// check can roll to next
					// or need a new chunk
					FixedSizeList<T> next;
					
					if (chunks.getBack() == chunk ||
							chunks.getIndex(chunkIndex+1).isFull()) {
						next = makeChunk();
						chunks.addIndex(chunkIndex+1, next);
					} else {
						next = chunks.getIndex(chunkIndex+1);
					}
						
					if (index == end) {
						next.addFront(item);
					} else {
						//should be remove something.
					}
					
				} else {
					// put right in this chunk, there's space.
					chunk.addIndex(index - start, item);
				}
				// upon adding, return.
				return;
			}
			
			// update bounds of next chunk.
			start = end;
			chunkIndex++;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public T getFront() {
		return this.getIndex(0);
	}

	@Override
	public T getBack() {
		return this.getIndex(this.size()-1);
	}


	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public void setIndex(int index, T value) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		
		for (FixedSizeList<T> chunk : this.chunks) {
			int end = start + chunk.size();
			if (start <= index && index < end) {
				chunk.setIndex(index - start, value);
				return;				
			}
			start = end;
		}
		throw new BadIndexError(index);
	}

	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	@Override
	public boolean isEmpty() {
		return this.chunks.isEmpty();
	}
}