package com.sonata.generic.automation.library;
/**
 * Accpac Report String Converter class.
 * 
 * Converts accpac report string like (1,234,234.00) to -1234234.00.
 * 
 * @author Helen
 *
 */
public final class StringConverter {

	/**
	 * The constructor was made private so it can never be instantiated.
	 */
	private StringConverter() {
	}
	
	 /**
	 * Gets a Double Value from a String Amount.
	 *  
	 * @param amount
	 * 		string to be converted to double
	 * @return
	 * 		<li><code> Returns a Double Amount after converting from String
	 *  Amount </code></li>
	 */
	public static Double convertToDouble(String amount) {
		
		Double doubleAmount;
		
		//Removes the "-", ",","(",")" symbols form the number 
		//and converted string to number
		if (amount.contains("(")) {

			amount = amount.replace("(", "");
			amount = amount.replace(")", "");
			amount = amount.replace(",", "");
			doubleAmount = Double.parseDouble(amount);
			doubleAmount = 0 - doubleAmount;
			
		} else {
						
			amount = amount.replace(",", "");
			doubleAmount = Double.parseDouble(amount);
				
		}
		return doubleAmount;
		
	}
}
