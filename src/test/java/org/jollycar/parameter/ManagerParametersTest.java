package org.jollycar.parameter;

import org.jollycar.HolidayManager;
import org.jollycar.ManagerParameter;
import org.jollycar.ManagerParameters;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagerParametersTest {

    @Test
    public void testCreateParameterFromLocale() {
        ManagerParameter params = ManagerParameters.create(Locale.GERMANY);
        HolidayManager manager = HolidayManager.getInstance(params);
        assertEquals(Locale.GERMANY.getCountry().toLowerCase(), manager.getCalendarHierarchy().getId());
        Calendar thirdOfOctober = Calendar.getInstance();
        thirdOfOctober.set(Calendar.MONTH, Calendar.OCTOBER);
        thirdOfOctober.set(Calendar.DAY_OF_MONTH, 3);
        assertTrue(manager.isHoliday(thirdOfOctober), "Oct 3rd should be a holiday in " + Locale.GERMANY);
    }
}
