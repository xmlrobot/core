package org.xmlrobot;

public abstract class Hyperspace
	<K extends Recursion<K,V>,V extends Recursion<V,K>>
		extends AbstractListener 
			implements Recursion<K,V> {

	private static final long serialVersionUID = 1499027297977083677L;

	public Hyperspace() {
		
	}
	
	@Override
	public Listener clone() {
		return super.clone();
	}
}