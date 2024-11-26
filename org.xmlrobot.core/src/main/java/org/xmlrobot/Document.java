package org.xmlrobot;

public abstract class Document<K,V> 
	extends Order<Entry<K,V>,Entry<V,K>> implements Entry<K,V> {

	private static final long serialVersionUID = -3847840125862223258L;
}