package org.xmlrobot;

public abstract class Order
	<K extends TimeListener<K,V>,V extends TimeListener<V,K>>
		extends Hyperspace<K,V> implements TimeListener<K,V> {

	private static final long serialVersionUID = -7899170718428479779L;

}