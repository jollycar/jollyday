package org.jollycar.tests;

import org.jollycar.tests.base.AbstractCountryTestBase;
import org.junit.jupiter.api.Test;

public class HolidaySETest extends AbstractCountryTestBase {

    private static final String ISO_CODE = "se";
    private static final int YEAR = 2016;

    @Test
    public void testManagerSEStructure() throws Exception {
        validateCalendarData(ISO_CODE, YEAR);
    }

}
