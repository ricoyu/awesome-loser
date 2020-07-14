package com.loserico.defaultstaticmethod;

import java.util.function.Supplier;

/**
 * Interface’s Default and Static Methods<p>
 * 
 * Java 8 extends interface declarations with two new concepts: default and
 * static methods.<br/>
 * 
 * The difference between default methods and abstract methods is that abstract
 * methods are required to be implemented. But default methods are not.
 * Instead, each interface must provide so called default implementation and
 * all the implementers will inherit it by default (with a possibility to
 * override this default implementation if needed).<p>
 * 
 * Another interesting feature delivered by Java 8 is that interfaces can
 * declare (and provide implementation) of static methods.<br/>
 * 
 * Default methods implementation on JVM is very efficient and is supported by
 * the byte code instructions for method invocation. Default methods allowed
 * existing Java interfaces to evolve without breaking the compilation process.
 * The good examples are the plethora of methods added to java.util.Collection
 * interface: stream(), parallelStream(), forEach(), removeIf(), . . .
 * 
 * @author Loser
 * @since Jul 10, 2016
 * @version
 *
 */
public class DefaultInterface {

	private interface Defaulable {
		default String notRequired() {
			return "default Implememtation";
		}
	}

	private static class DefaultableImpl implements Defaulable {
	}

	private static class OverridableImpl implements Defaulable {

		@Override
		public String notRequired() {
			return "Overridden implementation";
		}

	}

	private interface DefaulableFactory {
		static Defaulable create(Supplier<Defaulable> supplier) {
			return supplier.get();
		}
	}

	public static void main(String[] args) {
		Defaulable defaulable = DefaulableFactory.create(DefaultableImpl::new);
		System.out.println(defaulable.notRequired());

		defaulable = DefaulableFactory.create(OverridableImpl::new);
		System.out.println(defaulable.notRequired());
	}
}
