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
package org.jollycar.parser.impl;

import org.jollycar.Holiday;
import org.jollycar.HolidayType;
import org.jollycar.config.FixedWeekdayRelativeToFixed;
import org.jollycar.config.Holidays;
import org.jollycar.config.When;
import org.jollycar.parser.AbstractHolidayParser;
import org.threeten.extra.Days;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Set;

import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

/**
 * Parses fixed weekday relative to fixed date.
 * 
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class FixedWeekdayRelativeToFixedParser extends AbstractHolidayParser {

	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (FixedWeekdayRelativeToFixed f : config.getFixedWeekdayRelativeToFixed()) {
			if (!isValid(f, year)) {
				continue;
			}
			LocalDate day = calendarUtil.create(year, f.getDay());
			day = moveDateToFirstOccurrenceOfWeekday(f, day);
			int days = determineNumberOfDays(f);
			day = f.getWhen() == When.AFTER ? day.plusDays(days) : day.minusDays(days);
			HolidayType type = xmlUtil.getType(f.getLocalizedType());
			holidays.add(new Holiday(day, f.getDescriptionPropertiesKey(), type));
		}
	}

	/**
	 * Moves the day to the first/next occurrence of the weekday and direction specified
	 * @param f the specification of the weekday and direction of movement
	 * @param day the day to move
	 * @return the day moved to the weekday and in the direction as specified
	 */
	private LocalDate moveDateToFirstOccurrenceOfWeekday(FixedWeekdayRelativeToFixed f, LocalDate day) {
		final DayOfWeek weekday = xmlUtil.getWeekday(f.getWeekday());
		final TemporalAdjuster adjuster;
		switch (f.getWhen()) {
			case AFTER:
				adjuster = next(weekday);
				break;
			case BEFORE:
				adjuster = previous(weekday);
				break;
			case CLOSEST:
				adjuster = closest(weekday);
				break;
			default:
				throw new IllegalArgumentException("Unsupported when adjustment: " + f.getWhen());
		}
		return day.with(adjuster);
	}

	/**
	 * Determines the number of days to move from the XML enumeration.
	 * @param f the enumeration value
	 * @return the number of days
	 */
	private int determineNumberOfDays(FixedWeekdayRelativeToFixed f) {
		if (f.getWhen() == When.CLOSEST) {
			return 0;
		}

		switch (f.getWhich()) {
		case SECOND:
			return 7;
		case THIRD:
			return 14;
		case FOURTH:
			return 21;
		default:
			return 0;
		}
	}

	/**
	 * Returns the closest day-of-week adjuster, which adjusts the date to the
	 * first occurrence of the specified day-of-week closest to the date being
	 * adjusted unless it is already on that day in which case the same object
	 * is returned.
	 * <p>
	 * The ISO calendar system behaves as follows:<br>
	 * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return
	 * 2011-01-17 (two days later).<br>
	 * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return
	 * 2011-01-12 (three days earlier).<br>
	 * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return
	 * 2011-01-15 (same as input).
	 * <p>
	 * The behavior is suitable for use with most calendar systems. It uses the
	 * {@code DAY_OF_WEEK} field and the {@code DAYS} unit, and assumes a seven
	 * day week.
	 *
	 * @param dayOfWeek
	 *            the day-of-week to check for or move the date to, not null
	 * @return the closest day-of-week adjuster, not null
	 */
	public static TemporalAdjuster closest(DayOfWeek dayOfWeek) {
		return (temporal) -> {
			Temporal previous = temporal.with(previousOrSame(dayOfWeek));
			Temporal next = temporal.with(nextOrSame(dayOfWeek));
			int previousDays = Days.between(temporal, previous).abs().getAmount();
			int nextDays = Days.between(temporal, next).abs().getAmount();
			return (previousDays <= nextDays ? previous : next);
		};
	}
}

