package org.xmlrobot;

import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Time
	<K extends TimeListener<K,V>,V extends TimeListener<V,K>>
		extends Hyperspace<K,V> implements TimeListener<K,V> {

	private static final long serialVersionUID = -7899170718428479779L;
	
	private static final Random random = new Random();
	
	/**
	 * Your root.
	 */
	K root;
	
	@Override
	public K getRoot() {
		return root;
	}
	@Override
	public K setRoot(K root) {
		K old = this.root;
		this.root = root;
		return old;
	}
	@Override
	public V getStem() {
		return getChild().getRoot();
	}
	@Override
	public V setStem(V stem) {
		return getChild().setRoot(stem);
	}
	
	public Time() {
		super();
	}
	public Time(Parity parity) {
		super(parity);
	}
	public Time(Class<? extends V> childClass, Parity parity) {
		super(parity, create(childClass, parity.opposite()));
		setRoot(getParent());
		setStem(getChild());
	}
	public Time(K parent) {
		super(parent);
		setRoot(parent.getRoot());
	}
	public Time(Class<? extends V> childClass, K parent) {
		super(parent, create(childClass, parent.getChild()));
		setRoot(parent.getRoot());
	}
	public Time(K root, Parity parity) {
		super(parity);
		setRoot(root);
	}
	public Time(Class<? extends V> childClass, K root, Parity parity) {
		super(parity, create(childClass, root.getStem(), parity.opposite()));
		setRoot(root);
	}
	@Override
	public boolean isRoot() {
		return getRoot() == this;
	}
	@Override
	public boolean isStem() {
		return getChild() == getStem();
	}
	protected Random random() {
		return random;
	}
	@Override
	protected void triggerEvent(EventArgs e) {
		super.triggerEvent(e);
		if(root != this)
			root.event(this, e);
	}
	@Override
	public boolean hasParent(K parent) {
		return false;
	}
	@Override
	public boolean hasChild(V child) {
		return getChild().hasParent(child);
	}
	@Override
	public boolean releaseParent(K parent) {
		return false;
	}
	@Override
	public boolean releaseChild(V child) {
		return false;
	}
	@Override
	public boolean addParent(K parent) {
		return false;
	}
	@Override
	public boolean addChild(V child) {
		return false;
	}
	@Override
	public boolean hasAllParents(K parent) {
		return false;
	}
	@Override
	public boolean hasAllChildren(V child) {
		return false;
	}
	@Override
	public boolean addAllParents(K parent) {
		return false;
	}
	@Override
	public boolean addAllChildren(V child) {
		return false;
	}
	@Override
	public boolean releaseAllParents(K parent) {
		return false;
	}
	@Override
	public boolean releaseAllChildren(V child) {
		return false;
	}
	@Override
	public boolean retainAllParents(K parent) {
		return false;
	}
	@Override
	public boolean retainAllChildren(V child) {
		return false;
	}
	@Override
	public void recurParent(K parent, V child) {
		
	}
	@Override
	public void recurChild(V child, K parent) {
		
	}
	@Override
	public void concurParent(K parent, V child) {
		
	}
	@Override
	public void concurChild(V child, K parent) {
		
	}
	@Override
	public void permuteParent(K parent, V child) {
		
	}
	@Override
	public void permuteChild(V child, K parent) {
		
	}
	@Override
	public void submitParent(K parent, V child) {
		
	}
	@Override
	public void submitChild(V child, K parent) {
		
	}
	@Override
	public V getChild(K parent) {
		return null;
	}
	@Override
	public K getParent(V child) {
		return null;
	}
	@Override
	public V getChildOrDefault(K parent, V defaultChild) {
		return null;
	}
	@Override
	public K getParentOrDefault(V value, K defaultKey) {
		return null;
	}
	@Override
	public V putChild(K parent, V child) {
		return null;
	}
	@Override
	public K putParent(V child, K parent) {
		return null;
	}
	@Override
	public V putChildIfAbsent(K parent, V child) {
		return null;
	}
	@Override
	public K putParentIfAbsent(V child, K parent) {
		return null;
	}
	@Override
	public void putAllChildren(TimeListener<? extends K, ? extends V> t) {
		
	}
	@Override
	public void putAllParents(TimeListener<? extends V, ? extends K> t) {
		
	}
	@Override
	public V replaceChild(K parent, V child) {
		return null;
	}
	@Override
	public K replaceParent(V child, K parent) {
		return null;
	}
	@Override
	public boolean replaceChild(K parent, V oldChild, V newChild) {
		return false;
	}
	@Override
	public boolean replaceParent(V child, K oldParent, K newParent) {
		return false;
	}
	@Override
	public void replaceAllChildren(BiFunction<? super K, ? super V, ? extends V> function) {
		
	}
	@Override
	public void replaceAllParents(BiFunction<? super V, ? super K, ? extends K> function) {
		
	}
	@Override
	public boolean releaseParent(K parent, V child) {
		return false;
	}
	@Override
	public boolean releaseChild(V value, K key) {
		return false;
	}
	@Override
	public void forEachParent(BiConsumer<? super K, ? super V> execution) {
		
	}
	@Override
	public void forEachChild(BiConsumer<? super V, ? super K> execution) {
		
	}
	@Override
	public V computeParentIfAbsent(K parent, Function<? super K, ? extends V> function) {
		return null;
	}
	@Override
	public K computeChildIfAbsent(V child, Function<? super V, ? extends K> function) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public V computeParentIfPresent(K parent, BiFunction<? super K, ? super V, ? extends V> function) {
		return null;
	}
	@Override
	public K computeChildIfPresent(V child, BiFunction<? super V, ? super K, ? extends K> function) {
		return null;
	}
	@Override
	public V computeParent(K parent, BiFunction<? super K, ? super V, ? extends V> function) {
		return null;
	}
	@Override
	public K computeChild(V child, BiFunction<? super V, ? super K, ? extends K> function) {
		return null;
	}
	@Override
	public V mergeParent(K parent, V child, BiFunction<? super V, ? super V, ? extends V> function) {
		return null;
	}
	@Override
	public K mergeChild(V child, K parent, BiFunction<? super K, ? super K, ? extends K> function) {
		return null;
	}
	@Override
	public Map<K, V> inheritance() {
		return null;
	}
	@Override
	public TimeListener.Comparator<K, V> comparator() {
		return null;
	}
	@Override
	public TimeListener.Comparator<K, V> comparator(V source) {
		return null;
	}
	
}