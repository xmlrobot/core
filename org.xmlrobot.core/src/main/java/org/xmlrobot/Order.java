package org.xmlrobot;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @param <K>
 * @param <V>
 * 
 * @see {@link org.xmlrobot.TimeListener}
 * @see {@link java.util.Map.Entry}
 */
public abstract class Order<K,V> 
	extends Time<Entry<K,V>,Entry<V,K>> implements Entry<K,V> {

	private static final long serialVersionUID = -3847840125862223258L;

	@Override
	public K getKey() {
		return null;
	}
	@Override
	public K setKey(K key) {
		return null;
	}
	@Override
	public V getValue() {
		return null;
	}
	@Override
	public V setValue(V value) {
		return null;
	}

	@Override
	public V getValue(Object key) {
		return null;
	}
	@Override
	public K getKey(Object value) {
		return null;
	}
	@Override
	public V getValueOrDefault(K key, V defaultValue) {
		return null;
	}
	@Override
	public K getKeyOrDefault(V value, K defaultKey) {
		return null;
	}
	@Override
	public Entry<K, V> getParentByKey(K key) {
		return null;
	}
	@Override
	public Entry<V, K> getChildByValue(V value) {
		return null;
	}
	@Override
	public int indexOfKey(K key) {
		return 0;
	}
	@Override
	public int indexOfValue(V value) {
		return 0;
	}
	@Override
	public Entry<K, V> putValue(K key, V value) {
		return null;
	}
	@Override
	public Entry<V, K> putKey(V value, K key) {
		return null;
	}
	@Override
	public Entry<K, V> putValueIfAbsent(K key, V value) {
		return null;
	}
	@Override
	public Entry<V, K> putKeyIfAbsent(V value, K key) {
		return null;
	}
	@Override
	public V replaceValue(K key, V value) {
		return null;
	}
	@Override
	public K replaceKey(V value, K key) {
		return null;
	}
	@Override
	public boolean replaceValue(K key, V oldValue, V newValue) {
		return false;
	}
	@Override
	public boolean replaceKey(V value, K oldKey, K newKey) {
		return false;
	}
	@Override
	public void replaceAllValues(BiFunction<? super K, ? super V, ? extends V> function) {
		
	}
	@Override
	public void replaceAllKeys(BiFunction<? super V, ? super K, ? extends K> function) {
		
	}
	@Override
	public boolean containsKey(Object key) {
		return false;
	}
	@Override
	public boolean containsValue(Object value) {
		return false;
	}
	@Override
	public V removeValue(K key) {
		return null;
	}
	@Override
	public K removeKey(V key) {
		return null;
	}
	@Override
	public boolean removeValue(K key, V value) {
		return false;
	}
	@Override
	public boolean removeKey(V value, K key) {
		return false;
	}
	@Override
	public void forEachValue(BiConsumer<? super K, ? super V> action) {
		
	}
	@Override
	public void forEachKey(BiConsumer<? super V, ? super K> action) {
		
	}
	@Override
	public V computeValueIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		return null;
	}
	@Override
	public K computeKeyIfAbsent(V value, Function<? super V, ? extends K> mappingFunction) {
		return null;
	}
	@Override
	public V computeValueIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return null;
	}
	@Override
	public K computeKeyIfPresent(V value, BiFunction<? super V, ? super K, ? extends K> remappingFunction) {
		return null;
	}
	@Override
	public V computeValue(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return null;
	}
	@Override
	public K computeKey(V value, BiFunction<? super V, ? super K, ? extends K> remappingFunction) {
		return null;
	}
	@Override
	public V mergeValue(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return null;
	}
	@Override
	public K mergeKey(V value, K key, BiFunction<? super K, ? super K, ? extends K> remappingFunction) {
		return null;
	}
	@Override
	public org.xmlrobot.Entry.Comparator<K, V> comparator() {
		return null;
	}
	@Override
	public org.xmlrobot.Entry.Comparator<K, V> comparator(V value, K key) {
		return null;
	}
}