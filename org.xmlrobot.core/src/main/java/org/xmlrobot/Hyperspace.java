package org.xmlrobot;

import org.xmlrobot.numbers.Enumerator;
import org.xmlrobot.time.Recursion;

public abstract class Hyperspace
	<K extends Recursion<K,V>,V extends Recursion<V,K>>
		extends Document implements Recursion<K,V> {

	private static final long serialVersionUID = 1499027297977083677L;

	K parent;
	V child;
	
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
	@Override
	public K call() {
		return getChild().getChild();
	}
	@Override
	public K put(K past) {
		return getChild().setChild(past);
	}
	@Override
	public V get() {
		return getChild().call();
	}
	@Override
	public V set(V future) {
		return getChild().put(future);
	}

	public Hyperspace() {
		
	}
	public Hyperspace(Parity parity) {
		super(parity);
	}
	@SuppressWarnings("unchecked")
	public Hyperspace(Parity parity, V child) {
		super(parity);
		setParent((K) this);
		setChild(child);
		child.setChild(getParent());
		child.setParent(child);
	}
	public Hyperspace(K parent) {
		super(parent.getParity());
		setParent(parent);
		setChild(parent.getChild());
	}
	@SuppressWarnings("unchecked")
	public Hyperspace(K parent, V child) {
		super(parent.getParity());
		setParent(parent);
		setChild(child);
		parent.put((K) this);
		call().setParent(parent.call());
		get().setParent(child);
	}

	@Override
	public abstract int compareTo(V o);
	
	@SuppressWarnings("unchecked")
	@Override
	public K clone() {
		try {
			K k = (K) super.clone();
			V v = (V) getChild().getClass().getConstructor().newInstance();
			k.setParent(k);
			v.setParent(v);
			k.setChild(v);
			v.setChild(k);
			return k;
		} catch (Throwable t) {
			throw new Error("org.xmlrobot.Hyperspace: clone exception.", t);
		}
	}
	@Override
	public void release() {
		call().setParent(getParent());
		get().setParent(getParent().getChild());
		put(getParent().put(call()));
		setParent(call());
		getChild().setParent(getChild());
	}
	@Override
	public boolean isEmpty() {
		return getParent() == this;
	}
	@Override
	public void run() {
		if(!isEmpty())
			execute(getChild());
		super.run();
	}
	@Override
	public Enumerator<K> enumerator() {
		return new ParentEnumerator(getParent());
	}

	protected final class ParentEnumerator implements Enumerator<K> {

		/**
		 * The current time-listener.
		 */
		K current;

		/**
		 * The next time-listener.
		 */
		K next;
		
		/**
		 * If this recursor has next time-listener.
		 */
		boolean hasNext;

		public ParentEnumerator(K past) {
			next = current = past;
			hasNext = true;
		}

		@Override
		public boolean hasMoreElements() {
			return hasNext;
		}

		@Override
		public K nextElement() {
			K parent = next;
			current = parent;
			next = parent.getParent();
			if (parent == Hyperspace.this)
				hasNext = false;
			else
				hasNext = true;
			return parent;
		}

		@Override
		public void remove() {
			K parent = next;
			current.release();
			if (!parent.isEmpty()) {
				current = parent;
				next = parent.getParent();
			} else
				hasNext = false;
		}
	}
}