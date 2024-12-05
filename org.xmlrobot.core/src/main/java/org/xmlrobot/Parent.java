package org.xmlrobot;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.xmlrobot.genesis.DNA;
import org.xmlrobot.numbers.Enumerator;

public abstract class Parent<K,V>
	extends Hyperspace<K,V> implements DNA<K,V> {
	
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
	public V get(K key) {
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
    	for (java.util.Map.Entry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }
	@Override
	public V remove(K key) {
		Enumerator<org.xmlrobot.Entry<K,V>> enumerator = enumerator();
		org.xmlrobot.Entry<K,V> entry = null;
		while (entry == null && enumerator.hasMoreElements()) {
			org.xmlrobot.Entry<K,V> e = enumerator.nextElement();
			if (key.equals(e.getKey()))
				entry = e;
		}
		V oldValue = null;
		if (entry != null) {
			oldValue = entry.getValue();
			entry.clear();
		}
		return oldValue;
	}
	
	transient Set<K> keySet;
	transient Collection<V> values;
	transient Set<Entry<K,V>> entrySet;
	
	public Set<K> keySet() {
		return keySet == null ? keySet = new java.util.AbstractSet<K>() {
			@Override
			public Iterator<K> iterator() {
				return new KeyIterator(enumerator());
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
		} : keySet;
	}
	public Collection<V> values() {
		return values == null ? values = new java.util.AbstractCollection<V>() {
			public Iterator<V> iterator() {
				return new ValueIterator(enumerator());
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
		}: values;
	}
	public Set<org.xmlrobot.Entry<K,V>> entrySet() {
		return entrySet == null ? entrySet = new java.util.AbstractSet<Entry<K,V>>() {
			@Override
			public Iterator<Entry<K,V>> iterator() {
				return new Iterator<Entry<K,V>>() {
					private Enumerator<org.xmlrobot.Entry<K,V>> en = Parent.this.enumerator();
					@Override
					public boolean hasNext() {
						return en.hasMoreElements();
					}
					@Override
					public org.xmlrobot.Entry<K,V> next() {
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
		}: entrySet;
	}
	protected class KeyIterator implements Iterator<K> {
		
		private Enumerator<org.xmlrobot.Entry<K,V>> enumerator;

		KeyIterator(Enumerator<org.xmlrobot.Entry<K,V>> enumerator) {
			this.enumerator = enumerator;
		}

		@Override
		public boolean hasNext() {
			return enumerator.hasMoreElements();
		}
		@Override
		public K next() {
			return enumerator.nextElement().getKey();
		}
		@Override
		public void remove() {
			enumerator.remove();
		}
	}
	private class ValueIterator implements Iterator<V> {
		
		private Enumerator<org.xmlrobot.Entry<K,V>> enumerator;

		ValueIterator(Enumerator<org.xmlrobot.Entry<K,V>> enumerator) {
			this.enumerator = enumerator;
		}

		@Override
		public boolean hasNext() {
			return enumerator.hasMoreElements();
		}
		@Override
		public V next() {
			return enumerator.nextElement().getValue();
		}
		@Override
		public void remove() {
			enumerator.remove();
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