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
import org.jollycar.config.HinduHoliday;
import org.jollycar.config.Holidays;
import org.jollycar.parser.AbstractHolidayParser;

import java.util.Set;

/**
 * <p>HinduHolidayParser class.</p>
 *
 * @author Sven
 * @version $Id: $
 */
public class HinduHolidayParser extends AbstractHolidayParser {

	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (HinduHoliday hh : config.getHinduHoliday()) {
			if (!isValid(hh, year))
				continue;
			switch (hh.getType()) {
			case HOLI:
				// 20 February and ending on 21 March (20th march in leap years)
				// TODO: Calculate with hindu calendar.
				break;
			default:
				throw new IllegalArgumentException("Unknown hindu holiday "
						+ hh.getType());
			}
		}
	}

}
