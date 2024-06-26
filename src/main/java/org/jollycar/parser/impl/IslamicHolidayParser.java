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
import org.jollycar.config.Holidays;
import org.jollycar.config.IslamicHoliday;
import org.jollycar.parser.AbstractHolidayParser;

import java.time.LocalDate;
import java.util.Set;

/**
 * This parser calculates gregorian dates for the different islamic holidays.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class IslamicHolidayParser extends AbstractHolidayParser {

	/**
	 * Properties prefix for islamic holidays.
	 */
	private static final String PREFIX_PROPERTY_ISLAMIC = "islamic.";

	/*
	 * (non-Javadoc)
	 *
	 * @see org.jollycar.parser.HolidayParser#parse(int, java.util.Set,
	 * org.jollycar.config.Holidays)
	 */
	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (IslamicHoliday i : config.getIslamicHoliday()) {
			if (!isValid(i, year)) {
				continue;
			}
			Set<LocalDate> islamicHolidays;
			switch (i.getType()) {
			case NEWYEAR:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 1, 1);
				break;
			case ASCHURA:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 1, 10);
				break;
			case RAMADAN_END:
				islamicHolidays = calendarUtil.getRelativeIslamicHolidaysInGregorianYear(year, 10, 1, -1);
				break;
			case ID_AL_FITR:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 10, 1);
				break;
			case ID_AL_FITR_2:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 10, 2);
				break;
			case ID_AL_FITR_3:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 10, 3);
				break;
			case ARAFAAT:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 12, 9);
				break;
			case ID_UL_ADHA:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 12, 10);
				break;
			case ID_UL_ADHA_2:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 12, 11);
				break;
			case ID_UL_ADHA_3:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 12, 12);
				break;
			case LAILAT_AL_BARAT:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 8, 15);
				break;
			case LAILAT_AL_MIRAJ:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 7, 27);
				break;
			case LAILAT_AL_QADR:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 9, 27);
				break;
			case MAWLID_AN_NABI:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 3, 12);
				break;
			case RAMADAN:
				islamicHolidays = calendarUtil.getIslamicHolidaysInGregorianYear(year, 9, 1);
				break;
			default:
				throw new IllegalArgumentException("Unknown islamic holiday " + i.getType());
			}
			String propertiesKey = PREFIX_PROPERTY_ISLAMIC + i.getType().name();
			HolidayType type = xmlUtil.getType(i.getLocalizedType());
			for (LocalDate d : islamicHolidays) {
				holidays.add(new Holiday(d, propertiesKey, type));
			}
		}
	}

}
