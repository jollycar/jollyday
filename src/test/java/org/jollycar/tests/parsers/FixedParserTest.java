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
import org.jollycar.config.Fixed;
import org.jollycar.config.Holidays;
import org.jollycar.config.Month;
import org.jollycar.config.MovingCondition;
import org.jollycar.config.Weekday;
import org.jollycar.config.With;
import org.jollycar.parser.impl.FixedParser;
import org.jollycar.util.CalendarUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Sven
 *
 */
class FixedParserTest {

	private FixedParser fixedParser = new FixedParser();
	private CalendarUtil calendarUtil = new CalendarUtil();

	@Test
	void testFixedWithValidity() {
		Holidays h = createHolidays(createFixed(1, Month.JANUARY), createFixed(3, Month.MARCH),
				createFixed(5, Month.MAY, 2011, null));
		Set<Holiday> set = new HashSet<>();
		fixedParser.parse(2010, set, h);
		containsAll(new ArrayList<>(set), calendarUtil.create(2010, 1, 1), calendarUtil.create(2010, 3, 3));
	}

	@Test
	void testFixedWithMoving() {
		Holidays h = createHolidays(
				createFixed(8, Month.JANUARY, createMoving(Weekday.SATURDAY, With.PREVIOUS, Weekday.FRIDAY)),
				createFixed(23, Month.JANUARY, createMoving(Weekday.SUNDAY, With.NEXT, Weekday.MONDAY)));
		Set<Holiday> set = new HashSet<>();
		fixedParser.parse(2011, set, h);
		containsAll(new ArrayList<>(set), calendarUtil.create(2011, 1, 7), calendarUtil.create(2011, 1, 24));
	}

	@Test
	void testCyle2YearsInvalid() {
		Fixed fixed = createFixed(4, Month.JANUARY);
		fixed.setValidFrom(2010);
		fixed.setEvery("2_YEARS");
		Holidays holidays = createHolidays(fixed);
		Set<Holiday> set = new HashSet<>();
		fixedParser.parse(2011, set, holidays);
		assertTrue(set.isEmpty(), "Expected to be empty.");
	}

	@Test
	void testCyle3Years() {
		Fixed fixed = createFixed(4, Month.JANUARY);
		fixed.setValidFrom(2010);
		fixed.setEvery("3_YEARS");
		Holidays holidays = createHolidays(fixed);
		Set<Holiday> set = new HashSet<>();
		fixedParser.parse(2013, set, holidays);
		assertEquals(1, set.size(), "Wrong number of holidays.");
	}

	private void containsAll(List<Holiday> list, LocalDate... dates) {
		assertEquals(dates.length, list.size(), "Number of holidays.");
		List<LocalDate> expected = new ArrayList<>(Arrays.asList(dates));
		Collections.sort(expected);
		Collections.sort(list, new HolidayComparator());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i), list.get(i).getDate(), "Missing date.");
		}
	}

	Holidays createHolidays(Fixed... fs) {
		Holidays h = new Holidays();
		h.getFixed().addAll(Arrays.asList(fs));
		return h;
	}

	/**
	 * @return
	 */
	Fixed createFixed(int day, Month m, MovingCondition... mc) {
		Fixed f = new Fixed();
		f.setDay(day);
		f.setMonth(m);
		f.getMovingCondition().addAll(Arrays.asList(mc));
		return f;
	}

	Fixed createFixed(int day, Month m, Integer validFrom, Integer validUntil, MovingCondition... mc) {
		Fixed f = createFixed(day, m, mc);
		f.setValidFrom(validFrom);
		f.setValidTo(validUntil);
		return f;
	}

	MovingCondition createMoving(Weekday substitute, With with, Weekday weekday) {
		MovingCondition mc = new MovingCondition();
		mc.setSubstitute(substitute);
		mc.setWith(with);
		mc.setWeekday(weekday);
		return mc;
	}

}
