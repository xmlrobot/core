package org.xmlrobot;

import org.xmlrobot.numbers.Enumerator;

public abstract class Time
	<K extends TimeListener<K,V>,V extends TimeListener<V,K>>
		extends Object implements TimeListener<K,V> {

	private static final long serialVersionUID = 1499027297977083677L;

	private K parent;
	private V child;
	
	@Override
	public K getParent() {
		return parent;
	}
	@Override
	public K setParent(K parent) {
		K old = this.parent;
		this.parent = parent;
		return old;
	}
	@Override
	public V getChild() {
		return child;
	}
	@Override
	public V setChild(V child) {
		V old = this.child;
		this.child = child;
		return old;
	}

	public Time() {
		super();
	}
	public Time(Parity parity) {
		super(parity);
	}
	@SuppressWarnings("unchecked")
	public Time(Parity parity, V child) {
		super(parity);
		setParent((K) this);
		setChild(child);
		child.setChild(getParent());
		child.setParent(child);
	}
	public Time(K parent) {
		super(parent.getParity());
		setParent(parent);
		setChild(parent.getChild());
	}
	@SuppressWarnings("unchecked")
	public Time(K parent, V child) {
		super(parent.getParity());
		setParent(parent);
		setChild(child);
		parent.getChild().setChild((K) this);
		getChild().getChild().setParent(parent.getChild().getChild());
		getChild().getChild().getChild().setParent(child);
	}

	@SuppressWarnings("unchecked")
	@Override
	public K clone() {
		try {
			K parent = (K) super.clone();
			V child = (V) this.child.getClass().getConstructor().newInstance();
			parent.setParent(parent);
			parent.setChild(child);
			child.setParent(child);
			child.setChild(parent);
			return parent;
		} catch (Throwable t) {
			throw new Error("org.xmlrobot.Time: clone exception.", t);
		}
	}
	@Override
	public void clear() {
		child.getChild().setParent(parent);
		child.getChild().getChild().setParent(parent.getChild());
		child.setChild(parent.getChild().setChild(child.getChild()));
		child.setParent(child);
		setParent(child.getChild());
	}
	@Override
	public boolean isEmpty() {
		return parent == this;
	}
	@Override
	public void run() {
		execute(child);
		super.run();
	}
	@Override
	public Enumerator<K> enumerator() {
		return new ParentEnumerator(parent);
	}
	
	private final class ParentEnumerator implements Enumerator<K> {

		/**
		 * The current recursion.
		 */
		private K current;

		/**
		 * The next recursion.
		 */
		private K parent;
		
		/**
		 * If this recursor has next recursion.
		 */
		private boolean hasNext;

		ParentEnumerator(K parent) {
			this.parent = current = parent;
			hasNext = true;
		}
		
		@Override
		public boolean hasMoreElements() {
			return hasNext;
		}
		@Override
		public K nextElement() {
			current = parent;
			parent = current.getParent();
			if (current == Time.this) {
				hasNext = false;
			} else hasNext = true;
			return current;
		}
		@Override
		public void remove() {
			current.clear();
			if (!parent.isEmpty()) {
				current = parent;
				parent = parent.getParent();
			} else hasNext = false;
		}
	}
	
	@Override
	public abstract TimeListener.Comparator<K,V> comparator();
	@Override
	public abstract int compareTo(V o);
	
	protected abstract class Generator implements TimeListener.Comparator<K,V> {
		
		private V source;
		
		@Override
		public V source() {
			return source;
		}
		
		public Generator() {
			super();
		}
		public Generator(V source) {
			super();
			this.source = source;
		}
		
		@Override
		public abstract int compare(K parent, V child);
	}
}