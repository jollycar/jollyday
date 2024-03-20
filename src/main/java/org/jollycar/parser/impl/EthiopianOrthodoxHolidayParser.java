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

import java.time.LocalDate;
import java.util.Set;

import org.jollycar.Holiday;
import org.jollycar.HolidayType;
import org.jollycar.config.EthiopianOrthodoxHoliday;
import org.jollycar.config.Holidays;
import org.jollycar.parser.AbstractHolidayParser;

/**
 * Calculates the ethiopian orthodox holidays.
 * 
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class EthiopianOrthodoxHolidayParser extends AbstractHolidayParser {

	/**
	 * Ethiopian orthodox properties prefix.
	 */
	private static final String PREFIX_PROPERTY_ETHIOPIAN_ORTHODOX = "ethiopian.orthodox.";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jollycar.parser.HolidayParser#parse(int, java.util.Set,
	 * org.jollycar.config.Holidays)
	 */
	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, Holidays config) {
		for (EthiopianOrthodoxHoliday h : config.getEthiopianOrthodoxHoliday()) {
			if (!isValid(h, year)) {
				continue;
			}
			final Set<LocalDate> ethiopianHolidays;
			switch (h.getType()) {
			case TIMKAT:
				ethiopianHolidays = calendarUtil.getEthiopianOrthodoxHolidaysInGregorianYear(year, 5, 10);
				break;
			case ENKUTATASH:
				ethiopianHolidays = calendarUtil.getEthiopianOrthodoxHolidaysInGregorianYear(year, 1, 1);
				break;
			case MESKEL:
				ethiopianHolidays = calendarUtil.getEthiopianOrthodoxHolidaysInGregorianYear(year, 1, 17);
				break;
			default:
				throw new IllegalArgumentException("Unknown ethiopian orthodox holiday type " + h.getType());
			}
			String propertiesKey = PREFIX_PROPERTY_ETHIOPIAN_ORTHODOX + h.getType().name();
			HolidayType type = xmlUtil.getType(h.getLocalizedType());
			for (LocalDate d : ethiopianHolidays) {
				holidays.add(new Holiday(d, propertiesKey, type));
			}
		}
	}

}
