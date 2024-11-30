package org.xmlrobot;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.xmlrobot.genesis.DNA;
import org.xmlrobot.numbers.Enumerator;

public abstract class Parent<K,V>
	extends Order<K,V> implements DNA<K,V> {
	private static final long serialVersionUID = 3114549892332588729L;
	
	public Parent() {
		super();
	}
	public Parent(Parity parity) {
		super(parity);
	}
	public Parent(Class<? extends Child<V,K>> childClass, Parity parity, K key, V value) {
		super(childClass, parity, key, value);
	}
	public Parent(Parent<K,V> parent) {
		super(parent);
	}
	public Parent(Class<? extends Child<V,K>> childClass, Parent<K,V> parent, K key, V value) {
		super(childClass, parent, key, value);
	}
	public Parent(Parent<K,V> root, Parity parity) {
		super(root, parity);
	}
	public Parent(Class<? extends Child<V,K>> childClass, Parent<K,V> root, Parity parity, K key, V value) {
		super(childClass, root, parity, key, value);
	}
	
	@Override
	public V get(Object key) {
    	return getValue(key);
    }
	@Override
	public V put(K key, V value) {
    	if(!containsKey(key)) {
    		putValue(key, value);
    		return null;
    	}
    	else return replaceValue(key, value);
    }
	@Override
	public void putAll(java.util.Map<? extends K,? extends V> m) {
    	for (Entry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }
	@Override
	public V remove(Object key) {
		Enumerator<org.xmlrobot.Entry<K,V>> en = enumerator();
		org.xmlrobot.Entry<K, V> correctEntry = null;
		while (correctEntry == null && en.hasMoreElements()) {
			org.xmlrobot.Entry<K,V> e = en.nextElement();
			if (key.equals(e.getKey()))
				correctEntry = e;
		}
		V oldValue = null;
		if (correctEntry != null) {
			oldValue = correctEntry.getValue();
			correctEntry.release();
		}
		return oldValue;
	}
	@Override
	public void clear() {
		release();
	}
	
	transient Set<K> keySet;
	transient Collection<V> values;
	transient Set<Entry<K,V>> entrySet;
	
	public Set<K> keySet() {
		return keySet == null ? keySet = new java.util.AbstractSet<K>() {
			@Override
			public Iterator<K> iterator() {
				return new KeyIterator(getParent());
			}
			@Deprecated
			public int size() {
				return Parent.this.size();
			}
			public boolean isEmpty() {
                return Parent.this.isEmpty();
            }
            public void clear() {
            	Parent.this.clear();
            }
            public boolean contains(Object k) {
                return Parent.this.containsKey(k);
            }
		} : keySet;
	}
	public Collection<V> values() {
		return values == null ? values = new java.util.AbstractCollection<V>() {
			public Iterator<V> iterator() {
				return new ValueIterator(getParent());
            }
            public int size() {
                return Parent.this.size();
            }
            public boolean isEmpty() {
                return Parent.this.isEmpty();
            }
            public void clear() {
            	Parent.this.clear();
            }
            public boolean contains(Object v) {
                return Parent.this.containsValue(v);
            }
		}: values;
	}
	public Set<Entry<K,V>> entrySet() {
		return entrySet == null ? entrySet = new java.util.AbstractSet<Entry<K,V>>() {
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new Iterator<Entry<K,V>>() {
					private Enumerator<org.xmlrobot.Entry<K,V>> en = Parent.this.enumerator();
					@Override
					public boolean hasNext() {
						return en.hasMoreElements();
					}
					@Override
					public java.util.Map.Entry<K,V> next() {
						return en.nextElement();
					}
					@Override
					public void remove() {
						en.remove();
					}
				};
			}
			@Override
			public int size() {
				return Parent.this.size();
			}
			public boolean isEmpty() {
                return Parent.this.isEmpty();
            }
            public void clear() {
            	Parent.this.clear();
            }
            public boolean contains(Object k) {
                return Parent.this.containsKey(k);
            }
		}: entrySet;
	}
	protected class KeyIterator implements Iterator<K> {

		/**
		 * The current entry.
		 */
		org.xmlrobot.Entry<K,V> current;

		/**
		 * The next entry.
		 */
		org.xmlrobot.Entry<K,V> next;

		/**
		 * If this recursor has next entry.
		 */
		boolean hasNext;

		public KeyIterator(org.xmlrobot.Entry<K,V> parent) {
			next = current = parent;
			hasNext = true;
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}
		@Override
		public K next() {
			org.xmlrobot.Entry<K,V> entry = next;
			current = entry;
			next = entry.getParent();
			if (entry == Parent.this)
				hasNext = false;
			else
				hasNext = true;
			return entry.getKey();
		}
		@Override
		public void remove() {
			org.xmlrobot.Entry<K,V> entry = next;
			current.release();
			if (!entry.isEmpty()) {
				current = entry;
				next = entry.getParent();
			} else
				hasNext = false;
		}
	}
	protected class ValueIterator implements Iterator<V> {

		/**
		 * The current entry.
		 */
		org.xmlrobot.Entry<K,V> current;

		/**
		 * The next entry.
		 */
		org.xmlrobot.Entry<K,V> next;

		/**
		 * If this recursor has next entry.
		 */
		boolean hasNext;

		public ValueIterator(org.xmlrobot.Entry<K,V> parent) {
			next = current = parent;
			hasNext = true;
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}
		@Override
		public V next() {
			org.xmlrobot.Entry<K,V> entry = next;
			current = entry;
			next = entry.getParent();
			if (entry == Parent.this)
				hasNext = false;
			else
				hasNext = true;
			return entry.getValue();
		}
		@Override
		public void remove() {
			org.xmlrobot.Entry<K,V> entry = next;
			current.release();
			if (!entry.isEmpty()) {
				current = entry;
				next = entry.getParent();
			} else
				hasNext = false;
		}
	}
	@Deprecated
	@Override
	public int size() {
		Enumerator<org.xmlrobot.Entry<K,V>> en = enumerator();
		int i = 0;
		while(en.hasMoreElements()) {
			en.nextElement();
			i++;
		}
		return i;
	}
}