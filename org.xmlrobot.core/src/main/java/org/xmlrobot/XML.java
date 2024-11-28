package org.xmlrobot;

public abstract class XML 
	extends Object implements Entity {

	private static final long serialVersionUID = 5883267790321046625L;

	//attributes
	/**
	 * The parity
	 */
	private Parity parity;

	//properties
	@Override
	public Parity getParity() {
		return parity;
	}
	@Override
	public void setParity(Parity parity) {
		this.parity = parity;
	}
	@Override
	public abstract String getName();
	
	public XML() {
		this.parity = Parity.YY;
	}
	public XML(Parity parity) {
		this.parity = parity;
	}

	//object
	@Override
	public Entity clone() {
		try {
			Entity entity = getClass().getConstructor().newInstance();
			entity.setParity(parity);
			return entity;
		} catch (Throwable t) {
			throw new Error("org.xmlrobot.OrientedObject: clone exception.", t);
		}
	}
	@Override
	public final boolean equals(Object obj) {
		return this == obj;
	}

	//concurrence
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
	public Thread newThread(Runnable r) {
		return new Thread(r);
	}
	@Override
	public abstract void run();
	
	//create methods
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