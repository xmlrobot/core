package org.xmlrobot.numbers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import org.xmlrobot.Number;

public abstract class AbstractCollection<E>
	extends Number<Collection<E>> implements Collection<E> {

	private static final long serialVersionUID = -6677746041265829156L;
	public static final int MAX_ARRAY_SIZE = 2147483647;
	
	E element;

	@Override
	public E getElement() {
		return element;
	}
	@Override
	public E setElement(E element) {
		E old = this.element;
		this.element = element;
		return old;
	}

	public AbstractCollection() {
		super();
	}
	public AbstractCollection(Collection<E> parent, E element) {
		super(parent);
		setElement(element);
	}

	@Override
	public void clear() {
		setElement(getParent().getElement());
		super.clear();
	}
	@Override
	public boolean contains(Object o) {
		Objects.requireNonNull(o);
		Iterator<E> en = iterator();
		while (en.hasNext())
            if (o.equals(en.next()))
                return true;
        return false;
	}
	@Override
	public boolean isEmpty() {
		return getParent() == this;
	}
	@Override
	public Iterator<E> iterator() {
		return new ElementIterator(getParent());
	}
	@Override
	public boolean add(E e) {
		Objects.requireNonNull(e);
		if(isEmpty()) {
			setElement(e);
			return true;
		}
		return create(getClass(), this, e) != null;
	}
	@Override
	public boolean remove(Object o) {
		Objects.requireNonNull(o);
		Iterator<E> en = iterator();
		while (en.hasNext()) {
			if (o.equals(en.next())) {
				en.remove();
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean containsAll(java.util.Collection<?> c) {
		Objects.requireNonNull(c);
		for (Object o : c)
			if (!contains(o))
				return false;
		return true;
	}
	@Override
	public boolean addAll(java.util.Collection<? extends E> c) {
		Objects.requireNonNull(c);
		boolean modified = false;
		Iterator<? extends E> en = c.iterator();
		while(en.hasNext())
			if (add(en.next()))
				modified = true;
		return modified;
	}
	@Override
	public boolean removeAll(java.util.Collection<?> c) {
		Objects.requireNonNull(c);
		boolean modified = false;
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			if (c.contains(it.next())) {
				it.remove();
				modified = true;
			}
		}
		return modified;
	}
	@Override
	public boolean retainAll(java.util.Collection<?> c) {
		Objects.requireNonNull(c);
		boolean modified = false;
		Iterator<E> en = iterator();
		while (en.hasNext()) {
			if (!c.contains(en.next())) {
				en.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	protected final class ElementIterator implements Iterator<E> {
		
		/**
		 * The current collection.
		 */
		private Collection<E> current;
		
		/**
		 * The next collection.
		 */
		private Collection<E> parent;
		
		/**
		 * If this iterator has next collection.
		 */
		private boolean hasParent;
		
		public ElementIterator(Collection<E> c) {
			parent = current = c;
			hasParent = true;
		}
		@Override
		public boolean hasNext() {
			return hasParent;
		}
		@Override
		public E next() {
			current = parent;
			parent = parent.getParent();
			if(current == AbstractCollection.this)
				hasParent = false;
			else hasParent = true;
			return current.getElement();
		}
		@Override
		public void remove() {
			current.clear();
			if (!current.isEmpty()) {
				parent = current.getParent();
			} else
				hasParent = false;
		}
	}
	@Deprecated
	@Override
	public int size() {
		int i = 0;
		Iterator<E> it = iterator();
		while(it.hasNext()) {
			it.next();
			i++;
		}
		return i;
	}
	@Deprecated
	@Override
	public Object[] toArray() {
		// Estimate size of array; be prepared to see more or fewer elements
		Object[] r = new Object[size()];
		Iterator<E> it = iterator();
		for (int i = 0; i < r.length; i++) {
			if (! it.hasNext()) // fewer elements than expected
				return Arrays.copyOf(r, i);
			r[i] = it.next();
		}
		return it.hasNext() ? finishToArray(r, it) : r;
	}
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		// Estimate size of array; be prepared to see more or fewer elements
		int size = size();
		T[] r = a.length >= size ? a :
			(T[])java.lang.reflect.Array
			.newInstance(a.getClass().getComponentType(), size);
		Iterator<E> it = iterator();
		for (int i = 0; i < r.length; i++) {
			if (! it.hasNext()) { // fewer elements than expected
				if (a == r) {
					r[i] = null; // null-terminate
				} else if (a.length < i) {
					return Arrays.copyOf(r, i);
				} else {
					System.arraycopy(r, 0, a, 0, i);
					if (a.length > i) {
						a[i] = null;
					}
				}
				return a;
			}
			r[i] = (T)it.next();
		}
		// more elements than expected
		return it.hasNext() ? finishToArray(r, it) : r;
	}
	/**
	 * Reallocates the array being used within toArray when the iterator
	 * returned more elements than expected, and finishes filling it from
	 * the iterator.
	 *
	 * @param r the array, replete with previously stored elements
	 * @param it the in-progress iterator over this collection
	 * @return array containing the elements in the given array, plus any
	 *         further elements returned by the iterator, trimmed to size
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
		int i = r.length;
		while (it.hasNext()) {
			int cap = r.length;
			if (i == cap) {
				int newCap = cap + (cap >> 1) + 1;
				// overflow-conscious code
				if (newCap - MAX_ARRAY_SIZE > 0)
					newCap = hugeCapacity(cap + 1);
				r = Arrays.copyOf(r, newCap);
			}
			r[i++] = (T)it.next();
		}
		// trim if overallocated
		return (i == r.length) ? r : Arrays.copyOf(r, i);
	}
	@Deprecated
	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) // overflow
			throw new OutOfMemoryError
			("Required array size too large");
		return (minCapacity > MAX_ARRAY_SIZE) ?
				Integer.MAX_VALUE :
					MAX_ARRAY_SIZE;
	}
}