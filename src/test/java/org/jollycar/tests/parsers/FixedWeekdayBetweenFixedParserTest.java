/**
 * Copyright 2011 Sven Diedrichsen
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
package org.jollycar.tests.parsers;

import org.jollycar.Holiday;
import org.jollycar.config.FixedWeekdayBetweenFixed;
import org.jollycar.config.Holidays;
import org.jollycar.config.Month;
import org.jollycar.config.Weekday;
import org.jollycar.parser.impl.FixedWeekdayBetweenFixedParser;
import org.jollycar.util.CalendarUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author svdi1de
 *
 */
public class FixedWeekdayBetweenFixedParserTest extends FixedParserTest {

	private FixedWeekdayBetweenFixedParser parser = new FixedWeekdayBetweenFixedParser();
	private CalendarUtil calendarUtil = new CalendarUtil();

	@Test
	public void testEmpty() {
		Set<Holiday> holidays = new HashSet<>();
		Holidays config = new Holidays();
		parser.parse(2010, holidays, config);
		assertTrue(holidays.isEmpty(), "Expected to be empty.");
	}

	@Test
	public void testInvalid() {
		Set<Holiday> holidays = new HashSet<>();
		Holidays config = new Holidays();
		FixedWeekdayBetweenFixed e = new FixedWeekdayBetweenFixed();
		e.setValidTo(2009);
		config.getFixedWeekdayBetweenFixed().add(e);
		parser.parse(2010, holidays, config);
		assertTrue(holidays.isEmpty(), "Expected to be empty.");
	}

	@Test
	public void testWednesday() {
		Set<Holiday> holidays = new HashSet<>();
		Holidays config = new Holidays();
		FixedWeekdayBetweenFixed e = new FixedWeekdayBetweenFixed();
		e.setFrom(createFixed(17, Month.JANUARY));
		e.setTo(createFixed(21, Month.JANUARY));
		e.setWeekday(Weekday.WEDNESDAY);
		config.getFixedWeekdayBetweenFixed().add(e);
		parser.parse(2011, holidays, config);
		assertEquals(1, holidays.size(), "Wrong number of results.");
		assertEquals(calendarUtil.create(2011, 1, 19), holidays.iterator().next().getDate(), "Wrong date.");
	}

}
