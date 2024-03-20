package org.jollycar.configuration.impl;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DefaultConfigurationProviderContentTest {

	private static final Set<String> KEYS_DEFAULT_CONFIG = new HashSet<>(Arrays.asList("manager.impl",
			"manager.impl.jp","configuration.datasource.impl","parser.impl.org.jollycar.config.Fixed",
			"parser.impl.org.jollycar.config.FixedWeekdayInMonth", "parser.impl.org.jollycar.config.IslamicHoliday",
			"parser.impl.org.jollycar.config.ChristianHoliday", "parser.impl.org.jollycar.config.RelativeToFixed",
			"parser.impl.org.jollycar.config.RelativeToWeekdayInMonth",
			"parser.impl.org.jollycar.config.FixedWeekdayBetweenFixed",
			"parser.impl.org.jollycar.config.FixedWeekdayRelativeToFixed",
			"parser.impl.org.jollycar.config.EthiopianOrthodoxHoliday",
			"parser.impl.org.jollycar.config.RelativeToEasterSunday"));

	DefaultConfigurationProvider configurationProvider = new DefaultConfigurationProvider();

	@Test
	public void testPutConfiguration() {
		Properties p = configurationProvider.getProperties();
		assertFalse(p.isEmpty(), "Properties shouldn't be empty.");
		assertEquals(KEYS_DEFAULT_CONFIG, p.keySet(), "Default properties are not as expected.");
	}

}
