package org.xmlrobot.numbers;

public abstract class AbstractSet<E> 
	extends AbstractCollection<E> implements Set<E> {

	private static final long serialVersionUID = -231490701981622079L;

	public AbstractSet() {
		super();
	}
	public AbstractSet(Set<E> parent, E element) {
		super(parent, element);
	}

	@Override
	public boolean add(E e) {
		if(!contains(e)) {
			return super.add(e);
		}
		else return false;
	}
}