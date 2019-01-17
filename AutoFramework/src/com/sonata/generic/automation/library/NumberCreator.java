package com.sonata.generic.automation.library;

import java.util.Random;

/**
 * NumberCreator class.
 * 
 * Creates random integer and double numbers.
 * 
 * @author Rodolfo
 *
 */
public final class NumberCreator {
	/**
	 * The constructor was made private so it can never be instantiated.
	 */
	private NumberCreator() {
	}
	
	/**
	 * Random generator.
	 */	
	private static Random random = new Random();
	
	/**
	 * Gets the next random integer as string.
	 * 
	 * @param maximum
	 * 		Maximum random number 
	 * @return <li><code>A string</code> that represents a number 
	 * between 1 and maximum</li>
	 */
	public static String nextIntegerAsString(final Integer maximum) {

		int aRandom = random.nextInt(maximum) + 1;

		return String.valueOf(aRandom);
	}
	
	/**
	 * Gets the next random double as string.
	 * 
	 * @param maximum
	 * 		Maximum random number 
	 * @return <li><code>A string</code> that represents a number 
	 * between 1.0 and maximum (exclusive)</li>
	 */
	public static String nextDoubleAsString(final Double maximum) {

		double aRandom = random.nextDouble() * maximum + 0.1;

		return String.valueOf(aRandom);
	}
	
}
