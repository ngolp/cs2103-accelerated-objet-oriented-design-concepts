class HeapImpl<T extends Comparable<? super T>> implements Heap<T> 
{
	private static final int INITIAL_CAPACITY = 128;
	private T[] _storage;
	private int _numElements;

	@SuppressWarnings("unchecked")
	public HeapImpl () 
	{
		_storage = (T[]) new Comparable[INITIAL_CAPACITY];
		_numElements = 0;
	}

	@SuppressWarnings("unchecked")
	public void add (T data) 
	{
		// Grow the array
		if(_storage.length == _numElements)
		{
			T[] newStorage = (T[]) new Comparable[_numElements * 2];
			for(int i = 0; i < _numElements; i++)
			{
				newStorage[i] = _storage[i];
			}

			_storage = newStorage;
		}

		_storage[_numElements] = data;
		_numElements++;
		bubbleUp(_numElements - 1);
	}

    /**
	 * Removes the first element of the heap while maintianing the heap condition
	 * @return The node at the top of the heap
	 */
	public T removeFirst () 
	{
		T removedNode = _storage[0];
		_storage[0] = _storage[_numElements - 1];
		_numElements--;
		trickleDown(0);

		return removedNode;
	}

	public int size () 
	{
		return _numElements;
	}

	/**
	 * Moves an element up the heap until it's in the correct spot.
	 * @param initialIndex the initial index of the element to bubble up
	 */
	private void bubbleUp(int initialIndex)
	{
		int index = initialIndex;
		int parentIndex = (index - 1) / 2;
		T indexNode = _storage[index];
		T parentNode = _storage[parentIndex];

		while(parentIndex >= 0 && indexNode.compareTo(parentNode) > 0)
		{
			_storage[index] = parentNode;
			_storage[parentIndex] = indexNode;

			index = parentIndex;
			indexNode = _storage[index];

			parentIndex = (index - 1) / 2;
			if(parentIndex >= 0)
				parentNode = _storage[parentIndex];
		}
	}

	/**
	 * Moves the element at the given index down the heap until it's in the correct location.
	 * @param initialIndex initial index of the element to trickle down
	 */
	private void trickleDown(int initialIndex)
	{
		int index = initialIndex;
		
		while(getLeftIndex(index) < _numElements && (compareChild(index, getLeftIndex(index)) < 0 || compareChild(index, getRightIndex(index)) < 0)) 
		{
			int largerChildIndex = getLargerChildIndex(index);

			if(_storage[index].compareTo(_storage[largerChildIndex]) < 0) 
			{
				T largerChildNode = _storage[largerChildIndex];
				_storage[largerChildIndex] = _storage[index];
				_storage[index] = largerChildNode;

				index = largerChildIndex;
			}
		}		
	}

	/**
	 * Compares a heap element to one of its children
	 * @param index the index of the heap element
	 * @param childIndex the index of the child element
	 * @return the compareTo value between the two elements
	 */
	private int compareChild(int index, int childIndex)
	{
		if(index < _numElements && childIndex < _numElements)
		{
			return _storage[index].compareTo(_storage[childIndex]);
		}

		return 0;
	}

	/**
	 * Compares the two children of a node in the heap and returns the larger one
	 * @param index the index of the node in question
	 * @return the index of the largest child
	 */
	private int getLargerChildIndex(int index)
	{
		if(_storage[getLeftIndex(index)].compareTo(_storage[getRightIndex(index)]) > 0)
		{
			return getLeftIndex(index);
		}
		else
		{
			return getRightIndex(index);
		}
	}

	private static int getLeftIndex(int index)
	{
		return 2 * index + 1;
	} 

	private static int getRightIndex(int index)
	{
		return 2 * index + 2;
	}
}
