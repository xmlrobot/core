package org.xmlrobot;

public abstract class AbstractListener implements Listener {
	
	private static final long serialVersionUID = 3811280142467995386L;
	
	/**
	 * The parity
	 */
	private Parity parity;
	
	/**
	 * The event type
	 */
	private int eventType;
	
	@Override
	public abstract String getName();

	@Override
	public int getEventType() {
		return eventType;
	}
	@Override
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	@Override
	public Parity getParity() {
		return parity;
	}
	@Override
	public void setParity(Parity parity) {
		this.parity = parity;
	}
	
	/**
	 * {@link AbstractListener} default class constructor
	 */
	public AbstractListener() {
		super();
		this.eventType = GENESIS;
		this.parity = Parity.YY;
	}
	
	/**
	 * {@link AbstractListener} class constructor.
	 * @param message {@link Parity} the parity
	 */
	public AbstractListener(Parity parity) {
		super();
		this.eventType = GENESIS;
		this.parity = parity;
	}

	/*LISTENERS*/
	@Override
	public void addListener(Listener listener) {
		
	}

	@Override
	public void removeListener(Listener listener) {
		
	}
	
	/*EVENT*/
	@Override
	public void event(Object sender, Argument e) {
		triggerEvent(e);
	}
	
	protected void triggerEvent(Argument e) {
		
	}

	/*THREADING*/
	@Override
	public void execute(Runnable command) {
		try {
			newThread(command).start();
		}
		catch (Throwable t) {
			throw new Error(t);
		}
	}
	
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
	
	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r);
	}

	/*OBJECT*/
	@Override
	public Listener clone() {
		try {
			Listener listener = getClass().getConstructor().newInstance();
			listener.setParity(parity);
			return listener;
		} catch (Throwable t) {
			throw new Error("org.xmlrobot.AbstractListener: clone exception.", t);
		}
	}

	@Override
	public final boolean equals(Object obj) {
		return this == obj;
	}
	
	/*CREATE METHOD*/
	/**
	 * Creates new object of type X with the given arguments.
	 * @param <X> the parameter type of the returned object
	 * @param type the {@link Class} of the object.
	 * @param object the arguments of the construction of the object
	 * @return the new <X> instance
	 */
	protected static <X> X create(Class<X> type, Object... arguments) {
		try {
			return type.getDeclaredConstructor(getClasses(arguments)).newInstance(arguments);
		}
		catch(Throwable t) {
			throw new Error(t);
		}
	}
	
	private static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		for(int i = 0; i < objects.length; i++) {
			classes[i] = objects[i].getClass();
		}
		return classes;
	}
}