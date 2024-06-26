/**
 * Copyright 2010 Sven Diedrichsen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jollycar.tests;

import org.jollycar.util.ResourceUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The Class ISOCodesTest.
 *
 * @author Sven
 */
class ISOCodesTest {

	private static final int NUMBER_OF_ISOCOUNTRIES = 248;

	private Locale defaultLocale;

	private ResourceUtil resourceUtil;

	/**
	 * Inits
	 */
	@BeforeEach
	void init() {
		defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		resourceUtil = new ResourceUtil();
	}

	/**
	 * Cleanup.
	 */
	@AfterEach
	void cleanup() {
		Locale.setDefault(defaultLocale);
	}

	/**
	 * Test iso codes.
	 */
	@Test
	void testISOCodes() {
		Locale.setDefault(defaultLocale);
		Set<String> isoCodes = resourceUtil.getISOCodes();
		assertNotNull(isoCodes);
		assertEquals(NUMBER_OF_ISOCOUNTRIES, isoCodes.size(), "Wrong number of ISO codes.");
	}

	/**
	 * Test iso codes.
	 */
	@Test
	void testISOCodesEN() {
		Set<String> isoCodes = resourceUtil.getISOCodes();
		assertNotNull(isoCodes);
		assertEquals(NUMBER_OF_ISOCOUNTRIES, isoCodes.size(), "Wrong number of ISO codes.");
	}

	/**
	 * Test iso codes.
	 */
	@Test
	void testISOCodesDE() {
		Locale.setDefault(Locale.GERMANY);
		Set<String> isoCodes = resourceUtil.getISOCodes();
		assertNotNull(isoCodes);
		assertEquals(NUMBER_OF_ISOCOUNTRIES, isoCodes.size(), "Wrong number of ISO codes.");
	}

	/**
	 * Test iso codes compare en with de.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	void testISOCodesCompareENWithDE() throws IOException {
		ResourceBundle en = load(Locale.ENGLISH);
		ResourceBundle de = load(Locale.GERMANY);
		compareL1WithL2(en, de);
		compareL1WithL2(de, en);
	}

	/**
	 * Compare l1 with l2.
	 *
	 * @param l1
	 *            the first language
	 * @param l2
	 *            the second language
	 */
	private void compareL1WithL2(ResourceBundle l1, ResourceBundle l2) {
		Locale locale = "".equals(l2.getLocale().getCountry()) ? Locale.ENGLISH : l2.getLocale();
		Enumeration<String> keys = l1.getKeys();
		Set<String> l2KeySet = new HashSet<>(Collections.list(l2.getKeys()));
		StringBuilder misses = new StringBuilder();
		while (keys.hasMoreElements()) {
			String propertyName = keys.nextElement();
			if (!l2KeySet.contains(propertyName)) {
				misses.append(locale).append(" misses ").append(propertyName).append('\n');
			}
		}
		if (misses.length() > 0) {
			fail(misses.toString());
		}
	}

	/**
	 * Load.
	 *
	 * @param locale
	 *            the locale
	 *
	 * @return the properties
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private ResourceBundle load(Locale locale) throws IOException {
		return ResourceBundle.getBundle("descriptions.country_descriptions", locale);
	}

}
