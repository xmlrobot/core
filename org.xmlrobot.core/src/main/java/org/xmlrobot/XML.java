package org.xmlrobot;

import org.xmlrobot.numbers.Set;

import java.util.Iterator;

import org.xmlrobot.numbers.AbstractSet;

public abstract class XML
	extends Message implements Listener {
	
	private static final long serialVersionUID = 3811280142467995386L;
	
	/**
	 * The event type
	 */
	private int eventType;
	
	/**
	 * The listeners set
	 */
	private Set<Listener> listeners;
	
	//properties
	@Override
	public int getEventType() {
		return eventType;
	}
	@Override
	public void setEventType(int eventType) {
		this.eventType = eventType;
		sendEvent(new Event(this, eventType));
	}
	
	/**
	 * {@link XML} default class constructor
	 */
	public XML() {
		super();
		this.eventType = GENESIS;
	}
	/**
	 * {@link XML} class constructor.
	 * @param parity {@link Parity} the parity
	 */
	public XML(Parity parity) {
		super(parity);
		this.eventType = GENESIS;
	}
	
	//event listeners
	@Override
	public void addListener(Listener listener) {
		if(listeners == null) {
			listeners = new XML.Listeners();
		}
		listeners.add(listener);
	}
	@Override
	public void removeListener(Listener listener) {
		if(listeners == null) {
			return;
		}
		listeners.remove(listener);
	}
	//event
	@Override
	public void onEventReceived(Object sender, Event e) {
		sendEvent(e);
	}
	protected void sendEvent(Event e) {
		if(listeners != null) {
			Iterator<Listener> iterator = listeners.iterator();
			while(iterator.hasNext()) {
				iterator.next().onEventReceived(this, e);
			}
		}
	}
	//runnable
	@Override
	public void run() {
		switch (getEventType()) {
		case LISTEN:
			setEventType(TRANSFER);
			break;
		default:
			setEventType(LISTEN);
			break;
		}
	}
	/**
	 * The {@link java.util.Set} listeners.
	 */
	public class Listeners extends AbstractSet<Listener> {

		private static final long serialVersionUID = 414566453327042951L;
		
		@Override
		public String getName() {
			return XML.this.getName();
		}
		
		public Listeners() {
			super();
		}
		public Listeners(Listeners parent, Listener element) {
			super(parent, element);
			setElement(element);
		}
		
		@Override
		public boolean add(Listener e) {
			if(isEmpty()) {
				setElement(e);
				return true;
			} else if(!contains(e)) {
				create(getClass(), getParent(), e);
				return true;
			} else return false;
		}
		static <X> X create(Class<X> type, Object parent, Listener element) {
			try {
				return type.getDeclaredConstructor(parent.getClass(), Listener.class).newInstance(parent, element);
			} catch(Throwable t) {
				throw new Error(t);
			}
		}
	}
}