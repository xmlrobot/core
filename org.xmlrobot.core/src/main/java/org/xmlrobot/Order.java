package org.xmlrobot;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.xmlrobot.numbers.Enumerator;

/**
 * @param <K>
 * @param <V>
 * 
 * @see {@link org.xmlrobot.TimeListener}{@code <K,V>}
 * @see {@link java.util.Map.Entry}{@code <K,V>}
 */
public abstract class Order<K,V> 
	extends Time<Entry<K,V>,Entry<V,K>> implements Entry<K,V> {

	private static final long serialVersionUID = -3847840125862223258L;

	private K key;
	
	@Override
	public K getKey() {
		return key;
	}
	@Override
	public K setKey(K key) {
		K old = this.key;
		this.key = key;
		return old;
	}
	@Override
	public V getValue() {
		return getChild().getKey();
	}
	@Override
	public V setValue(V value) {
		return getChild().setKey(value);
	}

	public Order() {
		super();
	}
	public Order(Parity parity) {
		super(parity);
	}
	public Order(Class<? extends Entry<V,K>> childClass, Parity parity, K key, V value) {
		super(childClass, parity);
		setKey(key);
		setValue(value);
	}
	public Order(Entry<K,V> parent) {
		super(parent);
	}
	public Order(Class<? extends Entry<V,K>> childClass, Entry<K,V> parent, K key, V value) {
		super(childClass, parent);
		setKey(key);
		setValue(value);
	}
	public Order(Entry<K,V> root, Parity parity) {
		super(root, parity);
	}
	public Order(Class<? extends Entry<V,K>> childClass, Entry<K,V> root, Parity parity, K key, V value) {
		super(childClass, root, parity);
		setKey(key);
		setValue(value);
	}
	
	@Override
	public V getValue(Object key) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				return entry.getValue();
			}
		}
		return null;
	}
	@Override
	public K getKey(Object value) {
		return getChild().getValue(value);
	}
	@Override
	public V getValueOrDefault(K key, V defaultValue) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				return entry.getValue();
			}
		}
		return defaultValue;
	}
	@Override
	public K getKeyOrDefault(V value, K defaultKey) {
		return getChild().getValueOrDefault(value, defaultKey);
	}
	@Override
	public Entry<K, V> getParentByKey(K key) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				return entry;
			}
		}
		return null;
	}
	@Override
	public Entry<V, K> getChildByValue(V value) {
		return getChild().getParentByKey(value);
	}
	@Override
	public int indexOfKey(K key) {
		int i = 0;
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			i++;
			if(key == entry.getKey()) {
				return i;
			}
		}
		return i;
	}
	@SuppressWarnings("deprecation")
	@Override
	public int indexOfValue(V value) {
		return getChild().indexOfKey(value);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Entry<K, V> putValue(K key, V value) {
		return (Entry<K,V>) create(getClass(), getParent(), key, value);
	}
	@Override
	public Entry<V, K> putKey(V value, K key) {
		return getChild().putValue(value, key);
	}
	@Override
	public Entry<K, V> putValueIfAbsent(K key, V value) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				return null;
			}
		}
		return putValue(key, value);
	}
	@Override
	public Entry<V, K> putKeyIfAbsent(V value, K key) {
		return getChild().putValueIfAbsent(value, key);
	}
	@Override
	public V replaceValue(K key, V value) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				return entry.setValue(value);
			}
		}
		return null;
	}
	@Override
	public K replaceKey(V value, K key) {
		return getChild().replaceValue(value, key);
	}
	@Override
	public boolean replaceValue(K key, V oldValue, V newValue) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				if(oldValue == entry.getValue()) {
					entry.setValue(newValue);
					return true;
				}
				return false;
			}
		}
		return false;
	}
	@Override
	public boolean replaceKey(V value, K oldKey, K newKey) {
		return getChild().replaceValue(value, oldKey, newKey);
	}
	@Override
	public void replaceAllValues(BiFunction<? super K, ? super V, ? extends V> function) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			entry.setValue(function.apply(entry.getKey(), entry.getValue()));
		}
	}
	@Override
	public void replaceAllKeys(BiFunction<? super V, ? super K, ? extends K> function) {
		getChild().replaceAllValues(function);
	}
	@Override
	public boolean containsKey(Object key) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean containsValue(Object value) {
		return getChild().containsKey(value);
	}
	@Override
	public V removeValue(K key) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements()) {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				entry.release();
				return entry.getValue();
			}
		}
		return null;
	}
	@Override
	public K removeKey(V key) {
		return getChild().removeValue(key);
	}
	@Override
	public boolean removeValue(K key, V value) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				if(value == entry.getValue()) {
					entry.release();
					return true;	
				}
				return false;
			}
		}
		return false;
	}
	@Override
	public boolean removeKey(V value, K key) {
		return getChild().removeValue(value, key);
	}
	@Override
	public void forEachValue(BiConsumer<? super K, ? super V> execution) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			execution.accept(entry.getKey(), entry.getValue());	
		}
	}
	@Override
	public void forEachKey(BiConsumer<? super V, ? super K> execution) {
		getChild().forEachValue(execution);
	}
	@Override
	public V computeValueIfAbsent(K key, Function<? super K, ? extends V> function) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				return null;
			}
		}
		V newValue;
		V oldValue = null;
		if((newValue = function.apply(key)) != null) {
			oldValue = getValue(key);
			removeValue(key);
			putValue(key, newValue);
		}
		return oldValue;
	}
	@Override
	public K computeKeyIfAbsent(V value, Function<? super V, ? extends K> function) {
		return getChild().computeValueIfAbsent(value, function);
	}
	@Override
	public V computeValueIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				V newValue;
				V oldValue = null;
				if((newValue = function.apply(key, getValue())) != null) {
					oldValue = getValue(key);
					removeValue(key);
					putValue(key, newValue);
				}
				return oldValue;
			}
		}
		return null;
	}
	@Override
	public K computeKeyIfPresent(V value, BiFunction<? super V, ? super K, ? extends K> function) {
		return getChild().computeValueIfPresent(value, function);
	}
	@Override
	public V computeValue(K key, BiFunction<? super K, ? super V, ? extends V> function) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				V newValue;
				if((newValue = function.apply(key, entry.getValue())) == null) {
					entry.release();
					return entry.getValue();
				}
				else return entry.setValue(newValue);
			}
		}
		return null;
	}
	@Override
	public K computeKey(V value, BiFunction<? super V, ? super K, ? extends K> function) {
		return getChild().computeValue(value, function);
	}
	@Override
	public V mergeValue(K key, V value, BiFunction<? super V, ? super V, ? extends V> function) {
		Enumerator<Entry<K,V>> en = enumerator();
		while(en.hasMoreElements())  {
			Entry<K,V> entry = en.nextElement();
			if(key == entry.getKey()) {
				return entry.setValue(function.apply(entry.getValue(), value));
			}
		}
		return null;
	}
	@Override
	public K mergeKey(V value, K key, BiFunction<? super K, ? super K, ? extends K> function) {
		return getChild().mergeValue(value, key, function);
	}
	
	/**
	 * The nested comparator.
	 */
	private transient Entry.Comparator<K,V> comparator;
	
	@Override
	public org.xmlrobot.Entry.Comparator<K, V> comparator() {
		return comparator == null ? comparator = new Matrix() : comparator;
	}
	@Override
	public org.xmlrobot.Entry.Comparator<K, V> comparator(V value, K key) {
		comparator = new Matrix(value, key);
		return comparator;
	}
	
	public class Matrix extends Generator implements Entry.Comparator<K,V> {

		public Matrix() {
			super();
		}
		@SuppressWarnings("unchecked")
		public Matrix(V value, K key) {
			super(create(getChild().getClass(), value, key));
		}

		@Override
		public void addParent(Entry<K, V> parent) {
			parent.setStem(source());
			parent.setRoot(source().getStem());
			parent.setParity(source().getParity().opposite());
			parent.getChild().setParity(source().getParity());
			source().submitChild(parent, parent.getChild());
		}

		@Override
		public void addChild(Entry<V, K> child) {
			child.setRoot(source());
			child.setStem(source().getStem());
			child.setParity(source().getParity());
			child.getChild().setParity(source().getParity().opposite());
			source().submitParent(child, child.getChild());
		}
		@Override
		public int compare(Entry<K,V> parent, Entry<V,K> child) {
			return 0;
		}
	}
}