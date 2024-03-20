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
package org.jollycar.parser.impl;

import java.time.LocalDate;
import java.util.Set;

import org.jollycar.Holiday;
import org.jollycar.config.HolidayType;
import org.jollycar.config.Holidays;
import org.jollycar.config.RelativeToEasterSunday;
import org.jollycar.parser.AbstractHolidayParser;

/**
 * This parser creates holidays relative to easter sunday.
 * 
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class RelativeToEasterSundayParser extends AbstractHolidayParser {

	/**
	 * Properties prefix for christian holidays names.
	 */
	private static final String PREFIX_PROPERTY_CHRISTIAN = "christian.";

	/**
	 * {@inheritDoc}
	 * 
	 * Parses relative to easter sunday holidays.
	 */
	@Override
	public void parse(int year, Set<Holiday> holidays, Holidays config) {
		for (RelativeToEasterSunday ch : config.getRelativeToEasterSunday()) {
			if (!isValid(ch, year)) {
				continue;
			}
			LocalDate easterSunday = getEasterSunday(year, ch.getChronology()).plusDays(ch.getDays());
			String propertiesKey = PREFIX_PROPERTY_CHRISTIAN + ch.getDescriptionPropertiesKey();
			addChristianHoliday(easterSunday, propertiesKey, ch.getLocalizedType(), holidays);
		}
	}

	/**
	 * Adds the given day to the list of holidays.
	 * 
	 * @param day
	 *            a {@link LocalDate} object.
	 * @param propertiesKey
	 *            a {@link java.lang.String} object.
	 * @param holidayType
	 *            a {@link org.jollycar.config.HolidayType} object.
	 * @param holidays
	 *            a {@link java.util.Set} object.
	 */
	protected void addChristianHoliday(LocalDate day, String propertiesKey, HolidayType holidayType,
			Set<Holiday> holidays) {
		org.jollycar.HolidayType type = xmlUtil.getType(holidayType);
		Holiday h = new Holiday(day, propertiesKey, type);
		holidays.add(h);
	}

}
