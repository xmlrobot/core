package org.xmlrobot;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.xmlrobot.numbers.Enumerator;

public abstract class Time
	<K extends TimeListener<K,V>,V extends TimeListener<V,K>>
		extends Hyperspace<K,V> implements TimeListener<K,V> {

	private static final long serialVersionUID = -7899170718428479779L;
	
	private static final Random random = new Random();

	private K root;
	
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
	protected void sendEvent(Event e) {
		super.sendEvent(e);
		if(root != this)
			root.onEventReceived(this, e);
	}
	@Override
	public boolean hasParent(K parent) {
		Enumerator<K> it = enumerator();
		while(it.hasMoreElements())  {
			if(it.nextElement() == parent) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean hasChild(V child) {
		return getChild().hasParent(child);
	}
	@Override
	public boolean releaseParent(K parent) {
		Enumerator<K> it = enumerator();
		while(it.hasMoreElements()) {
			if(it.nextElement() == parent) {
				it.remove();
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean releaseChild(V child) {
		return getChild().releaseParent(child);
	}
	@Override
	public boolean addParent(K parent) {
		if(!hasParent(parent)) {
			recurParent(parent, parent.getChild());
			return true;
		} else return false;
	}
	@Override
	public boolean addChild(V child) {
		return getChild().addParent(child);
	}
	@Override
	public boolean hasAllParents(K parent) {
		Enumerator<K> it = parent.enumerator();
		while(it.hasMoreElements())
			if(!hasParent(it.nextElement()))
				return false;
		return false;
	}
	@Override
	public boolean hasAllChildren(V child) {
		return getChild().hasAllParents(child);
	}
	@Override
	public boolean addAllParents(K parent) {
		boolean modified = false;
		Enumerator<K> it = parent.enumerator();
		while (it.hasMoreElements())
			if (addParent(it.nextElement()))
				modified = true;
		return modified;
	}
	@Override
	public boolean addAllChildren(V child) {
		return getChild().addAllParents(child);
	}
	@Override
	public boolean releaseAllParents(K parent) {
		boolean modified = false;
		Enumerator<K> it = enumerator();
		while (it.hasMoreElements()) {
		    if (parent.hasParent(it.nextElement())) {
		        it.remove();
		        modified = true;
		    }
		}
		return modified;
	}
	@Override
	public boolean releaseAllChildren(V child) {
		return getChild().releaseAllParents(child);
	}
	@Override
	public boolean retainAllParents(K parent) {
		boolean modified = false;
		Enumerator<K> it = enumerator();
		while (it.hasMoreElements()) {
			if (!parent.hasParent(it.nextElement())) {
				it.remove();
				modified = true;
			}
		}
		return modified;
	}
	@Override
	public boolean retainAllChildren(V child) {
		return getChild().retainAllParents(child);
	}
	@Override
	public void recurParent(K parent, V child) {
		parent.setParent(getParent());
		child.setParent(getChild().getParent());
		child.setChild(getParent().call());
		getParent().put(parent);
		setParent(parent);
		getChild().setParent(child);
	}
	@Override
	public void recurChild(V child, K parent) {
		getChild().recurParent(child, parent);
	}
	@Override
	public void concurParent(K parent, V child) {
		call().setParent(parent);
		get().setParent(child);
		child.setChild(call());
		parent.setParent(getParent().call());
		child.setParent(getChild());
		put(parent);
	}
	@Override
	public void concurChild(V child, K parent) {
		getChild().concurParent(child, parent);
	}
	@Override
	public void permuteParent(K parent, V child) {
		if(parent == getParent()) {
			call().setParent(parent);
			get().setParent(child);
			setParent(parent.getParent());
			getChild().setParent(child.getParent());
			getParent().put(child.setChild(call()));
			put(parent);
			parent.setParent(getParent().call());
			child.setParent(getChild());
		}
		else if(parent == call()) {
			parent.setParent(getParent());
			child.setParent(getChild().getParent());
			put(child.getChild());
			call().setParent(getParent().put(parent));
			get().setParent(getChild());
			child.setChild(getParent().call());
			setParent(parent);
			getChild().setParent(child);
		}
		else {
			V value = child.setParent(getChild().getParent());
			K key = child.setChild(call());
			value.setChild(getParent().call());
			getParent().put(parent);
			setParent(parent.setParent(getParent()));
			getChild().setParent(value);
			call().setParent(parent);
			get().setParent(child);
			put(key);
			call().setParent(getParent().call());
			get().setParent(getChild());
		}
	}
	@Override
	public void permuteChild(V child, K parent) {
		getChild().permuteParent(child, parent);
	}
	@Override
	public void submitParent(K parent, V child) {
		if(getParity().equals(Parity.XY)) {
			concurParent(parent, child);
		} else {
			recurParent(parent, child);
		}
	}
	@Override
	public void submitChild(V child, K parent) {
		getChild().submitParent(child, parent);
	}
	@Override
	public V getChild(K parent) {
		Enumerator<K> en = enumerator();
		while(en.hasMoreElements()) {
			if(en.nextElement() == parent)
				return parent.getChild();
		}
		return null;
	}
	@Override
	public K getParent(V child) {
		return getChild().getChild(child);
	}
	@Override
	public V getChildOrDefault(K parent, V defaultChild) {
		V v;
		return (((v = getChild(parent)) != null) || hasParent(parent)) ? v : defaultChild;
	}
	@Override
	public K getParentOrDefault(V child, K defaultParent) {
		return getChild().getChildOrDefault(child, defaultParent);
	}
	@Override
	public V putChild(K parent, V child) {
		Enumerator<K> en = enumerator();
		while(en.hasMoreElements())  {
			if(en.nextElement() == parent) {
				child.setParent(parent.getParent().getChild());
				parent.get().setParent(child);
				child.setChild(parent.call());
				return parent.setChild(child);
			}
		}
		return null;
	}
	@Override
	public K putParent(V child, K parent) {
		return getChild().putChild(child, parent);
	}
	@Override
	public V putChildIfAbsent(K parent, V child) {
		V value = parent.getChild();
		if (value == null) {
		    value = putChild(parent, child);
		}
		return value;
	}
	@Override
	public K putParentIfAbsent(V child, K parent) {
		return getChild().putChildIfAbsent(child, parent);
	}
	@Override
	public void putAllChildren(TimeListener<? extends K, ? extends V> t) {
		Enumerator<? extends K> en = t.enumerator();
		while(en.hasMoreElements()) {
			K parent = en.nextElement();
			putChild(parent, parent.getChild());
		}
	}
	@Override
	public void putAllParents(TimeListener<? extends V, ? extends K> t) {
		getChild().putAllChildren(t);
	}
	@Override
	public V replaceChild(K parent, V child) {
		V curValue;
		if ((curValue = parent.getChild()) != null) {
		    curValue = putChild(parent, child);
		}
		return curValue;
	}
	@Override
	public K replaceParent(V child, K parent) {
		return getChild().replaceChild(child, parent);
	}
	@Override
	public boolean replaceChild(K parent, V oldChild, V newChild) {
		Object curChild = parent.getChild();
        if (!Objects.equals(curChild, oldChild) ||
            (curChild == null && !hasParent(parent))) {
            return false;
        }
        putChild(parent, newChild);
        return true;
	}
	@Override
	public boolean replaceParent(V child, K oldParent, K newParent) {
		return getChild().replaceChild(child, oldParent, newParent);
	}
	@Override
	public void replaceAllChildren(BiFunction<? super K, ? super V, ? extends V> function) {
		Objects.requireNonNull(function);
        forEachParent((k,v) -> {
            while(!replaceChild(k, v, function.apply(k, v))) {
                // v changed or k is gone
                if ( (v = k.getChild()) == null) {
                    // k is no longer in the time-listener.
                    break;
                }
            }
        });
	}
	@Override
	public void replaceAllParents(BiFunction<? super V, ? super K, ? extends K> function) {
		getChild().replaceAllChildren(function);
	}
	@Override
	public boolean releaseParent(K parent, V child) {
		Object curValue = parent.getChild();
		if (!Objects.equals(curValue, child) || (curValue == null && !hasParent(parent))) {
			return false;
		}
		parent.release();
		return true;
	}
	@Override
	public boolean releaseChild(V child, K parent) {
		return getChild().releaseParent(child, parent);
	}
	@Override
	public void forEachParent(BiConsumer<? super K, ? super V> execution) {
		Objects.requireNonNull(execution);
		Enumerator<K> en = enumerator();
		while(en.hasMoreElements())  {
			K parent = en.nextElement();
			execution.accept(parent, parent.getChild());
		}
	}
	@Override
	public void forEachChild(BiConsumer<? super V, ? super K> execution) {
		getChild().forEachParent(execution);
	}
	@Override
	public V computeChildIfAbsent(K parent, Function<? super K, ? extends V> function) {
		Objects.requireNonNull(function);
		V v, newValue;
		return ((v = parent.getChild()) == null && (newValue = function.apply(parent)) != null
				&& (v = putChildIfAbsent(parent, newValue)) == null) ? newValue : v;
	}
	@Override
	public K computeParentIfAbsent(V child, Function<? super V, ? extends K> function) {
		return getChild().computeChildIfAbsent(child, function);
	}
	@Override
	public V computeChildIfPresent(K parent, BiFunction<? super K, ? super V, ? extends V> function) {
		Objects.requireNonNull(function);
		V oldChild;
		while ((oldChild = parent.getChild()) != null) {
			V newChild = function.apply(parent, oldChild);
			if (newChild != null) {
				if (replaceChild(parent, oldChild, newChild))
					return newChild;
			} else if (releaseParent(parent, oldChild))
				return null;
		}
		return oldChild;
	}
	@Override
	public K computeParentIfPresent(V child, BiFunction<? super V, ? super K, ? extends K> function) {
		return getChild().computeChildIfPresent(child, function);
	}
	@Override
	public V computeChild(K parent, BiFunction<? super K, ? super V, ? extends V> function) {
		Objects.requireNonNull(function);
		V oldChild = parent.getChild();
		for (;;) {
			V newChild = function.apply(parent, oldChild);
			if (newChild == null) {
				// delete time-listener
				if (oldChild != null) {
					// parent to release
					if (releaseParent(parent, oldChild)) {
						// released the old child as expected
						return null;
					}
					// some parent child replaced old child. try again.
					oldChild = parent.getChild();
				} else {
					// nothing to program. Recur object as they were.
					return null;
				}
			} else {
				// add or replace old time-listener
				if (oldChild != null) {
					// replace
					if (replaceChild(parent, oldChild, newChild)) {
						// replaced as expected.
						return newChild;
					}
					// some parent child replaced old child. try again.
					oldChild = parent.getChild();
				} else {
					// add (replace if oldChild was null)
					if ((oldChild = putChildIfAbsent(parent, newChild)) == null) {
						// replaced
						return newChild;
					}
					// some parent child replaced old child. try again.
				}
			}
		}
	}
	@Override
	public K computeParent(V child, BiFunction<? super V, ? super K, ? extends K> function) {
		return getChild().computeChild(child, function);
	}
	@Override
	public V mergeChild(K parent, V child, BiFunction<? super V, ? super V, ? extends V> function) {
		Objects.requireNonNull(function);
		Objects.requireNonNull(child);
		V oldChild = parent.getChild();
		for (;;) {
			if (oldChild != null) {
				V newChild = function.apply(oldChild, child);
				if (newChild != null) {
					if (replaceChild(parent, oldChild, newChild))
						return newChild;
				} else if (releaseParent(parent, oldChild)) {
					return null;
				}
				oldChild = parent.getChild();
			} else {
				if ((oldChild = putChildIfAbsent(parent, child)) == null) {
					return child;
				}
			}
		}
	}
	@Override
	public K mergeParent(V child, K parent, BiFunction<? super K, ? super K, ? extends K> function) {
		return getChild().mergeChild(child, parent, function);
	}
	
	transient java.util.Map<K,V> inheritance;
	
	@Override
	public Map<K, V> inheritance() {
		return inheritance == null ? inheritance = new AbstractMap<K,V>() {
			transient Set<Entry<K,V>> entrySet;
			@Override
			public Set<Entry<K, V>> entrySet() {
				return entrySet == null ? entrySet = new AbstractSet<java.util.Map.Entry<K,V>>() {
					@Override
					public Iterator<Entry<K, V>> iterator() {
						Enumerator<K> en = Time.this.enumerator();
						return new Iterator<java.util.Map.Entry<K,V>>() {
							@Override
							public boolean hasNext() {
								return en.hasMoreElements();
							}
							@Override
							public Entry<K, V> next() {
								K entry = en.nextElement();
								return new java.util.Map.Entry<K,V>() {

									@Override
									public K getKey() {
										return entry;
									}
									@Override
									public V getValue() {
										return entry.getChild();
									}
									@Override
									public V setValue(V value) {
										return null;
									}
								};
							}
							@Override
							public void remove() {
								en.remove();
							}
						};
					}
					@Override
					public void clear() {
						release();
					}
					@Override
					@Deprecated
					public int size() {
						Enumerator<K> en = Time.this.enumerator();
						int i = 0;
						while(en.hasMoreElements()) {
							en.nextElement();
							i++;
						}
						return i;
					}
				}: entrySet;
			}
		}: inheritance;
	}
	
	@Override
	public abstract TimeListener.Comparator<K, V> comparator();
}