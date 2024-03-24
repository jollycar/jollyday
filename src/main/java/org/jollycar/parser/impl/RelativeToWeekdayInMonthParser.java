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
import org.jollycar.config.RelativeToWeekdayInMonth;
import org.jollycar.config.When;

import java.time.LocalDate;
import java.util.Set;

/**
 * <p>
 * RelativeToWeekdayInMonthParser class.
 * </p>
 * 
 * @author Sven
 * @version $Id: $
 */
public class RelativeToWeekdayInMonthParser extends FixedWeekdayInMonthParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jollycar.parser.impl.FixedWeekdayInMonthParser#parse(int,
	 * java.util.Set, org.jollycar.config.Holidays)
	 */
	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (RelativeToWeekdayInMonth rtfw : config.getRelativeToWeekdayInMonth()) {
			if (!isValid(rtfw, year)) {
				continue;
			}
			LocalDate date = parse(year, rtfw.getFixedWeekday()).plusDays(1);
			int direction = (rtfw.getWhen() == When.BEFORE ? -1 : 1);
			while (date.getDayOfWeek() != xmlUtil.getWeekday(rtfw.getWeekday())) {
				date = date.plusDays(direction);
			}
			HolidayType type = xmlUtil.getType(rtfw.getLocalizedType());
			holidays.add(new Holiday(date, rtfw.getDescriptionPropertiesKey(), type));
		}
	}

}
