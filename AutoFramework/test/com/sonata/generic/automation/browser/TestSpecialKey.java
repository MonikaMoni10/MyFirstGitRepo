/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openqa.selenium.Keys;

import com.sonata.generic.automation.browser.SpecialKey;

/**
 * The <code>TestSpecialKey</code> class provides JUnit testing of the
 * {@link SpecialKey} class.
 * 
 */
public class TestSpecialKey {
	/**
	 * Confirms things about the public names, such as ensuring collision
	 * prevention, not exporting hidden words and concepts etc.
	 */
	@Test
	public void publicNamesAreAcceptable() {
		Utility.confirmPublicNamesAreAcceptable(SpecialKey.class);
	}

	/**
	 * The SpecialKey allows us to disassociate the Browser API from the
	 * Selenium API, by re-exporting the Selenium special keys.
	 * <p>
	 * They need not be in the same order and do not need to include all
	 * Selenium keys (e.g. I doubt if we are really interested in the
	 * Zenkaku/Hankaku key).
	 * <p>
	 * However they must all equate to one and it would be very strange if two
	 * keys mapped to the same Selenium key.
	 */
	@Test
	public void eachSpecialKeyRepresentsADifferentSeleniumKey() {
		Set<Keys> seleniumKeysSeenSoFar = new HashSet<Keys>();
		for (SpecialKey key : SpecialKey.values()) {
			final Keys seleniumKeys = key.getKeys();
			assertNotNull(seleniumKeys);
			assertFalse(seleniumKeysSeenSoFar.contains(seleniumKeys));
			seleniumKeysSeenSoFar.add(seleniumKeys);
		}
	}

}
