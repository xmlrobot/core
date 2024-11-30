package org.xmlrobot;

import java.io.Serializable;

import org.xmlrobot.numbers.Enumerator;
import org.xmlrobot.time.Recurrence;

public abstract class Number<K extends Recurrence<K>>
	extends Message implements Recurrence<K>, Serializable {

	private static final long serialVersionUID = -3609491763992988642L;
	
	//attributes
	/**
	 * The parent recurrence
	 */
	K parent;
	
	/**
	 * The past recurrence
	 */
	K past;

	//properties
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
	public K call() {
		return past;
	}
	@Override
	public K put(K past) {
		K old = this.past;
		this.past = past;
		return old;
	}
	
	@Override
	public abstract String getName();
	
	@SuppressWarnings("unchecked")
	public Number() {
		parent = past = (K) this;
	}
	@SuppressWarnings("unchecked")
	public Number(K parent) {
		setParent(parent);
		put(parent.call());
		parent.call().setParent((K) this);
		parent.put(parent.call().getParent());
	}
	
	//recurrence
	@SuppressWarnings("unchecked")
	@Override
	public void release() {
		getParent().put(call());
		call().setParent(getParent());
		this.parent = this.past = (K) this;
	}
	@Override
	public boolean isEmpty() {
		return getParent() == this;
	}
	@SuppressWarnings("unchecked")
	protected void recur(K parent) {
		parent.setParent(getParent());
		parent.put((K) this);
		getParent().put(parent);
		setParent(parent);
	}
	@SuppressWarnings("unchecked")
	protected void concur(K parent) {
		parent.setParent((K) this);
		parent.put(call());
		call().setParent(parent);
		put(parent);
	}
	
	//runnable
	@Override
	public void run() {
		// do nothing
	}
	
	//enumerator
	@Override
	public Enumerator<K> enumerator() {
		return new ParentEnumerator(getParent());
	}
	protected final class ParentEnumerator implements Enumerator<K> {

		/**
		 * The current number.
		 */
		K current;

		/**
		 * The next number.
		 */
		K next;
		
		/**
		 * If this enumerator has next number.
		 */
		boolean hasNext;

		public ParentEnumerator(K parent) {
			next = current = parent;
			hasNext = true;
		}

		@Override
		public boolean hasMoreElements() {
			return hasNext;
		}
		@Override
		public K nextElement() {
			K c = next;
			current = c;
			next = c.getParent();
			if (c == Number.this)
				hasNext = false;
			else
				hasNext = true;
			return c;
		}
		@Override
		public void remove() {
			K k = next;
			current.release();
			if (!k.isEmpty()) {
				current = k;
				next = k.getParent();
			} else
				hasNext = false;
		 }
	}
}