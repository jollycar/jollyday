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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import org.jollycar.Holiday;
import org.jollycar.HolidayType;
import org.jollycar.config.Holidays;
import org.jollycar.config.RelativeToFixed;
import org.jollycar.config.When;
import org.jollycar.parser.AbstractHolidayParser;

/**
 * The Class RelativeToFixedParser.
 * 
 * @author tboven
 * @version $Id: $
 */
public class RelativeToFixedParser extends AbstractHolidayParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jollycar.parser.HolidayParser#parse(int, java.util.Set,
	 * org.jollycar.config.Holidays)
	 */
	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (RelativeToFixed rf : config.getRelativeToFixed()) {
			if (!isValid(rf, year)) {
				continue;
			}
			LocalDate fixed = calendarUtil.create(year, rf.getDate());
			if (rf.getWeekday() != null) {
				// if weekday is set -> move to weekday
				DayOfWeek day = xmlUtil.getWeekday(rf.getWeekday());
				int direction = (rf.getWhen() == When.BEFORE ? -1 : 1);
				// don't test start day
				fixed = fixed.plusDays(direction);
				while (fixed.getDayOfWeek() != day){
					fixed = fixed.plusDays(direction);
				}
			} else if (rf.getDays() != null) {
				// if number of days set -> move number of days
				fixed = fixed.plusDays(rf.getWhen() == When.BEFORE ? -rf.getDays() : rf.getDays());
			}
			HolidayType type = xmlUtil.getType(rf.getLocalizedType());
			holidays.add(new Holiday(fixed, rf.getDescriptionPropertiesKey(), type));
		}
	}

}
