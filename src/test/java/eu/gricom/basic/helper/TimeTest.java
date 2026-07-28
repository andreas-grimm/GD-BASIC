package eu.gricom.basic.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TimeTest.java
 * <p>
 * Test suite for Time class, testing date/time manipulation and comparison operations.
 */
@DisplayName("Time Test Suite")
class TimeTest {

    private Time timeObject;

    @BeforeEach
    void setUp() {
        timeObject = new Time();
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    @DisplayName("Default constructor should initialize with current time")
    void testDefaultConstructor() {
        Time time = new Time();
        assertNotNull(time, "Time object should be created");

        // Verify that year, month, day are reasonable
        assertTrue(time.getYear() >= 2000, "Year should be >= 2000");
        assertTrue(time.getMonth() >= 1 && time.getMonth() <= 12, "Month should be 1-12");
        assertTrue(time.getDay() >= 1 && time.getDay() <= 31, "Day should be 1-31");
    }

    @Test
    @DisplayName("Constructor with valid date string should parse correctly")
    void testConstructorWithValidDateString() {
        Time time = new Time("2023-06-15");
        assertEquals(2023, time.getYear(), "Year should be parsed correctly");
        assertEquals(6, time.getMonth(), "Month should be parsed correctly");
        assertEquals(15, time.getDay(), "Day should be parsed correctly");
    }

    @Test
    @DisplayName("Constructor with null date string should handle gracefully")
    void testConstructorWithNullDateString() {
        assertDoesNotThrow(() -> new Time(null),
            "Constructor should handle null date string gracefully");
    }

    @Test
    @DisplayName("Constructor with invalid date format should handle gracefully")
    void testConstructorWithInvalidDateFormat() {
        assertDoesNotThrow(() -> new Time("invalid-date"),
            "Constructor should handle invalid date format gracefully");
    }

    // ===== CLEAR TIME TESTS =====

    @Test
    @DisplayName("clearTime should reset all time fields to defaults")
    void testClearTime() {
        try {
            timeObject.parseDate("2023-12-25");
        } catch (Exception e) {
            fail("parseDate should not throw exception for valid date");
        }
        timeObject.clearTime();

        assertEquals(2000, timeObject.getYear(), "Year should be reset to 2000");
        assertEquals(1, timeObject.getMonth(), "Month should be reset to 1");
        assertEquals(1, timeObject.getDay(), "Day should be reset to 1");
    }

    // ===== PARSE DATE TESTS =====

    @Test
    @DisplayName("parseDate should correctly parse valid date string")
    void testParseDateValid() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        assertEquals(2023, timeObject.getYear());
        assertEquals(6, timeObject.getMonth());
        assertEquals(15, timeObject.getDay());
    }

    @Test
    @DisplayName("parseDate should parse date with leading zeros")
    void testParseDateWithLeadingZeros() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-01-05"));
        assertEquals(2023, timeObject.getYear());
        assertEquals(1, timeObject.getMonth());
        assertEquals(5, timeObject.getDay());
    }

    @Test
    @DisplayName("parseDate should reject year out of range (too low)")
    void testParseDateYearTooLow() {
        assertThrows(Exception.class, () -> timeObject.parseDate("-100-06-15"),
            "Should reject negative year");
    }

    @Test
    @DisplayName("parseDate should reject year out of range (too high)")
    void testParseDateYearTooHigh() {
        assertThrows(Exception.class, () -> timeObject.parseDate("3500-06-15"),
            "Should reject year above acceptable range");
    }

    @Test
    @DisplayName("parseDate should reject invalid month (too low)")
    void testParseDateMonthTooLow() {
        assertThrows(Exception.class, () -> timeObject.parseDate("2023-00-15"),
            "Should reject month 0");
    }

    @Test
    @DisplayName("parseDate should reject invalid month (too high)")
    void testParseDateMonthTooHigh() {
        assertThrows(Exception.class, () -> timeObject.parseDate("2023-13-15"),
            "Should reject month 13");
    }

    @Test
    @DisplayName("parseDate should reject invalid day (too low)")
    void testParseDateDayTooLow() {
        assertThrows(Exception.class, () -> timeObject.parseDate("2023-06-00"),
            "Should reject day 0");
    }

    @Test
    @DisplayName("parseDate should reject invalid day (too high)")
    void testParseDateDayTooHigh() {
        assertThrows(Exception.class, () -> timeObject.parseDate("2023-06-32"),
            "Should reject day 32");
    }

    @Test
    @DisplayName("parseDate should handle February 29th in leap year")
    void testParseDateLeapYearFebruary() {
        assertDoesNotThrow(() -> timeObject.parseDate("2024-02-29"),
            "Should accept Feb 29 in leap year");
    }

    @Test
    @DisplayName("parseDate should accept valid dates in all months")
    void testParseDateAllMonths() {
        String[] validDates = {
            "2023-01-31", "2023-02-28", "2023-03-31", "2023-04-30",
            "2023-05-31", "2023-06-30", "2023-07-31", "2023-08-31",
            "2023-09-30", "2023-10-31", "2023-11-30", "2023-12-31"
        };

        for (String date : validDates) {
            assertDoesNotThrow(() -> timeObject.parseDate(date),
                "Should accept valid date: " + date);
        }
    }

    @Test
    @DisplayName("parseDate should reject invalid dates for specific months")
    void testParseDateInvalidDaysInMonths() {
        assertThrows(Exception.class, () -> timeObject.parseDate("2023-04-31"),
            "Should reject April 31st");
        assertThrows(Exception.class, () -> timeObject.parseDate("2023-06-31"),
            "Should reject June 31st");
        assertThrows(Exception.class, () -> timeObject.parseDate("2023-09-31"),
            "Should reject September 31st");
    }

    // ===== PARSE DATE TIME TESTS =====

    @Test
    @DisplayName("parseDateTime should correctly parse valid date-time string")
    void testParseDateTimeValid() {
        assertDoesNotThrow(() -> timeObject.parseDateTime("2023-06-15 14:30:45"));
        assertEquals(2023, timeObject.getYear());
        assertEquals(6, timeObject.getMonth());
        assertEquals(15, timeObject.getDay());
    }

    @Test
    @DisplayName("parseDateTime should reject invalid hour (too high)")
    void testParseDateTimeHourTooHigh() {
        assertThrows(Exception.class, () -> timeObject.parseDateTime("2023-06-15 25:30:45"),
            "Should reject hour 25");
    }

    @Test
    @DisplayName("parseDateTime should reject invalid minute (too high)")
    void testParseDateTimeMinuteTooHigh() {
        assertThrows(Exception.class, () -> timeObject.parseDateTime("2023-06-15 14:60:45"),
            "Should reject minute 60");
    }

    @Test
    @DisplayName("parseDateTime should reject invalid second (too high)")
    void testParseDateTimeSecondTooHigh() {
        assertThrows(Exception.class, () -> timeObject.parseDateTime("2023-06-15 14:30:60"),
            "Should reject second 60");
    }

    @Test
    @DisplayName("parseDateTime should accept midnight")
    void testParseDateTimeMidnight() {
        assertDoesNotThrow(() -> timeObject.parseDateTime("2023-06-15 00:00:00"),
            "Should accept midnight");
    }

    @Test
    @DisplayName("parseDateTime should accept end of day")
    void testParseDateTimeEndOfDay() {
        assertDoesNotThrow(() -> timeObject.parseDateTime("2023-06-15 23:59:59"),
            "Should accept 23:59:59");
    }

    // ===== NOW TESTS =====

    @Test
    @DisplayName("now method should set current time")
    void testNowMethod() {
        try {
            timeObject.parseDate("2000-01-01");
        } catch (Exception e) {
            fail("parseDate should not throw exception for valid date");
        }
        timeObject.now();

        // Year should be current (at least 2020)
        assertTrue(timeObject.getYear() >= 2020, "now() should set current year");
        // Month should be valid
        assertTrue(timeObject.getMonth() >= 1 && timeObject.getMonth() <= 12);
        // Day should be valid
        assertTrue(timeObject.getDay() >= 1 && timeObject.getDay() <= 31);
    }

    // ===== GET DATE TESTS =====

    @Test
    @DisplayName("getDate should return properly formatted date string")
    void testGetDateFormatted() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-05"));
        String result = timeObject.getDate();

        assertEquals("2023-06-05", result, "Date should be formatted as YYYY-MM-DD");
    }

    @Test
    @DisplayName("getDate should use leading zeros for single-digit month")
    void testGetDateLeadingZeroMonth() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-01-15"));
        String result = timeObject.getDate();

        assertTrue(result.contains("-01-"), "Month should have leading zero");
    }

    @Test
    @DisplayName("getDate should use leading zeros for single-digit day")
    void testGetDateLeadingZeroDay() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-05"));
        String result = timeObject.getDate();

        assertTrue(result.contains("-05"), "Day should have leading zero");
    }

    // ===== GET DATE TIME TESTS =====

    @Test
    @DisplayName("getDateTime should return date and time formatted correctly")
    void testGetDateTimeFormatted() {
        assertDoesNotThrow(() -> timeObject.parseDateTime("2023-06-15 14:30:45"));
        String result = timeObject.getDateTime();

        assertTrue(result.contains("2023-06-15"), "Should contain date");
        assertTrue(result.contains("14:30:45"), "Should contain time");
    }

    @Test
    @DisplayName("getDateTime should include leading zeros for time")
    void testGetDateTimeLeadingZeros() {
        assertDoesNotThrow(() -> timeObject.parseDateTime("2023-06-15 09:05:03"));
        String result = timeObject.getDateTime();

        assertTrue(result.contains("09:05:03"), "Time should have leading zeros");
    }

    // ===== GET COMPONENTS TESTS =====

    @Test
    @DisplayName("getYear should return correct year")
    void testGetYear() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        assertEquals(2023, timeObject.getYear());
    }

    @Test
    @DisplayName("getMonth should return correct month")
    void testGetMonth() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        assertEquals(6, timeObject.getMonth());
    }

    @Test
    @DisplayName("getDay should return correct day")
    void testGetDay() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        assertEquals(15, timeObject.getDay());
    }

    @Test
    @DisplayName("getDayOfYear should return day number in year")
    void testGetDayOfYear() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-01-01"));
        assertEquals(1, timeObject.getDayOfYear(), "Jan 1 should be day 1 of year");

        assertDoesNotThrow(() -> timeObject.parseDate("2023-12-31"));
        assertEquals(365, timeObject.getDayOfYear(), "Dec 31 in non-leap year should be day 365");
    }

    // ===== IS VALID TESTS =====

    @Test
    @DisplayName("isValid should return true for valid date")
    void testIsValidTrue() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        assertTrue(timeObject.isValid(), "Valid date should pass isValid()");
    }

    @Test
    @DisplayName("parseDate should reject year out of range (too high)")
    void testIsValidInvalidYear() {
        assertThrows(Exception.class, () -> timeObject.parseDate("3500-06-15"),
            "Should reject year above acceptable range (>3000)");
    }

    // ===== GET NEXT DAY TESTS =====

    @Test
    @DisplayName("getNextDay should return next calendar day")
    void testGetNextDay() {
        assertDoesNotThrow(() -> timeObject.parseDateTime("2023-06-15 14:30:45"));
        String nextDay = timeObject.getNextDay();

        assertTrue(nextDay.contains("2023-06-16"), "Next day should be June 16");
    }

    @Test
    @DisplayName("getNextDay should handle month boundary")
    void testGetNextDayMonthBoundary() {
        assertDoesNotThrow(() -> timeObject.parseDateTime("2023-06-30 23:59:59"));
        String nextDay = timeObject.getNextDay();

        assertTrue(nextDay.contains("2023-07-01"), "Next day after month end should be next month");
    }

    @Test
    @DisplayName("getNextDay should handle year boundary")
    void testGetNextDayYearBoundary() {
        assertDoesNotThrow(() -> timeObject.parseDateTime("2023-12-31 23:59:59"));
        String nextDay = timeObject.getNextDay();

        assertTrue(nextDay.contains("2024-01-01"), "Next day after year end should be new year");
    }

    // ===== ADD DAYS TESTS =====

    @Test
    @DisplayName("addDays should add days within same month")
    void testAddDaysSameMonth() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        timeObject.addDays(5);

        assertEquals(20, timeObject.getDay(), "Should add 5 days to day 15");
        assertEquals(6, timeObject.getMonth(), "Month should remain same");
        assertEquals(2023, timeObject.getYear(), "Year should remain same");
    }

    @Test
    @DisplayName("addDays should handle month overflow")
    void testAddDaysMonthOverflow() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-25"));
        timeObject.addDays(10);

        assertEquals(5, timeObject.getDay(), "Should wrap to next month");
        assertEquals(7, timeObject.getMonth(), "Should advance to July");
        assertEquals(2023, timeObject.getYear());
    }

    @Test
    @DisplayName("addDays should handle year overflow")
    void testAddDaysYearOverflow() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-12-20"));
        timeObject.addDays(20);

        assertEquals(9, timeObject.getDay(), "Should wrap to next year");
        assertEquals(1, timeObject.getMonth(), "Should advance to January");
        assertEquals(2024, timeObject.getYear(), "Should advance to next year");
    }

    @Test
    @DisplayName("addDays with zero should not change date")
    void testAddDaysZero() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        timeObject.addDays(0);

        assertEquals(15, timeObject.getDay());
        assertEquals(6, timeObject.getMonth());
    }

    @Test
    @DisplayName("addDays should handle large numbers")
    void testAddDaysLarge() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-01-01"));
        timeObject.addDays(365);

        assertEquals(1, timeObject.getDay());
        assertEquals(1, timeObject.getMonth());
        assertEquals(2024, timeObject.getYear(), "Should advance one year");
    }

    // ===== ADD MONTHS TESTS =====

    @Test
    @DisplayName("addMonths should add months within same year")
    void testAddMonthsSameYear() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        timeObject.addMonths(3);

        assertEquals(15, timeObject.getDay());
        assertEquals(9, timeObject.getMonth(), "Should advance 3 months");
        assertEquals(2023, timeObject.getYear());
    }

    @Test
    @DisplayName("addMonths should handle year overflow")
    void testAddMonthsYearOverflow() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-10-15"));
        timeObject.addMonths(5);

        assertEquals(15, timeObject.getDay());
        assertEquals(3, timeObject.getMonth(), "Should wrap to March");
        assertEquals(2024, timeObject.getYear(), "Should advance to next year");
    }

    @Test
    @DisplayName("addMonths should handle day normalization (e.g., Jan 31 + 1 month)")
    void testAddMonthsDayNormalization() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-01-31"));
        timeObject.addMonths(1);

        // When adding 1 month to Jan 31, it should adjust to Feb's max day (28)
        assertEquals(2, timeObject.getMonth(), "Should be February");
    }

    // ===== SUBSTRACT DAYS TESTS =====

    @Test
    @DisplayName("substractDays should subtract days within same month")
    void testSubstractDaysSameMonth() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        timeObject.substractDays(5);

        assertEquals(10, timeObject.getDay(), "Should subtract 5 days");
        assertEquals(6, timeObject.getMonth());
        assertEquals(2023, timeObject.getYear());
    }

    @Test
    @DisplayName("substractDays should handle month underflow")
    void testSubstractDaysMonthUnderflow() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-05"));
        timeObject.substractDays(10);

        assertEquals(26, timeObject.getDay(), "Should wrap to previous month");
        assertEquals(5, timeObject.getMonth(), "Should go back to May");
        assertEquals(2023, timeObject.getYear());
    }

    @Test
    @DisplayName("substractDays should handle year underflow")
    void testSubstractDaysYearUnderflow() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-01-05"));
        timeObject.substractDays(10);

        assertEquals(26, timeObject.getDay(), "Should wrap to previous year");
        assertEquals(12, timeObject.getMonth(), "Should go back to December");
        assertEquals(2022, timeObject.getYear(), "Should go back to previous year");
    }

    @Test
    @DisplayName("substractDays with zero should not change date")
    void testSubstractDaysZero() {
        assertDoesNotThrow(() -> timeObject.parseDate("2023-06-15"));
        timeObject.substractDays(0);

        assertEquals(15, timeObject.getDay());
        assertEquals(6, timeObject.getMonth());
    }

    // ===== COMPARISON TESTS =====

    @Test
    @DisplayName("matches should return true for identical times")
    void testMatchesTrue() {
        assertDoesNotThrow(() -> {
            timeObject.parseDateTime("2023-06-15 14:30:45");
            Time otherTime = new Time("2023-06-15 14:30:45");
            assertTrue(timeObject.matches(otherTime), "Identical times should match");
        });
    }

    @Test
    @DisplayName("matches should return false for different times")
    void testMatchesFalse() {
        assertDoesNotThrow(() -> {
            timeObject.parseDate("2023-06-15");
            Time otherTime = new Time("2023-06-16");
            assertFalse(timeObject.matches(otherTime), "Different times should not match");
        });
    }

    @Test
    @DisplayName("lessThan should return true when this time is earlier")
    void testLessThanTrue() {
        assertDoesNotThrow(() -> {
            timeObject.parseDate("2023-06-15");
            Time otherTime = new Time("2023-06-20");
            assertTrue(timeObject.lessThan(otherTime), "Earlier time should be less than later time");
        });
    }

    @Test
    @DisplayName("lessThan should return false when this time is equal")
    void testLessThanFalseEqual() {
        assertDoesNotThrow(() -> {
            timeObject.parseDateTime("2023-06-15 14:30:45");
            Time otherTime = new Time("2023-06-15 14:30:45");
            assertFalse(timeObject.lessThan(otherTime), "Equal times should not be less than");
        });
    }

    @Test
    @DisplayName("lessThan should return false when this time is later")
    void testLessThanFalseLater() {
        assertDoesNotThrow(() -> {
            timeObject.parseDate("2023-06-20");
            Time otherTime = new Time("2023-06-15");
            assertFalse(timeObject.lessThan(otherTime), "Later time should not be less than earlier time");
        });
    }

    @Test
    @DisplayName("greaterThan should return true when this time is later")
    void testGreaterThanTrue() {
        assertDoesNotThrow(() -> {
            timeObject.parseDate("2023-06-20");
            Time otherTime = new Time("2023-06-15");
            assertTrue(timeObject.greaterThan(otherTime), "Later time should be greater than earlier time");
        });
    }

    @Test
    @DisplayName("greaterThan should return false when times are equal")
    void testGreaterThanFalseEqual() {
        assertDoesNotThrow(() -> {
            timeObject.parseDateTime("2023-06-15 14:30:45");
            Time otherTime = new Time("2023-06-15 14:30:45");
            assertFalse(timeObject.greaterThan(otherTime), "Equal times should not be greater than");
        });
    }

    @Test
    @DisplayName("greaterThan should return false when this time is earlier")
    void testGreaterThanFalseEarlier() {
        assertDoesNotThrow(() -> {
            timeObject.parseDate("2023-06-15");
            Time otherTime = new Time("2023-06-20");
            assertFalse(timeObject.greaterThan(otherTime), "Earlier time should not be greater than later time");
        });
    }

    // ===== DIFF IN DAYS TESTS =====

    @Test
    @DisplayName("diffInDays should return 0 for identical dates")
    void testDiffInDaysZero() {
        assertDoesNotThrow(() -> {
            timeObject.parseDate("2023-06-15");
            Time otherTime = new Time("2023-06-15");
            assertEquals(0, timeObject.diffInDays(otherTime), "Identical dates should have 0 difference");
        });
    }

    @Test
    @DisplayName("diffInDays should return positive for earlier date")
    void testDiffInDaysPositive() {
        assertDoesNotThrow(() -> {
            timeObject.parseDate("2023-06-15");
            Time otherTime = new Time("2023-06-10");
            int diff = timeObject.diffInDays(otherTime);
            assertTrue(diff > 0, "Difference should be positive when comparing later vs earlier date");
        });
    }

    @Test
    @DisplayName("diffInDays should work for dates in different years")
    void testDiffInDaysDifferentYears() {
        assertDoesNotThrow(() -> {
            timeObject.parseDate("2024-01-01");
            Time otherTime = new Time("2023-01-01");
            int diff = timeObject.diffInDays(otherTime);
            assertTrue(diff > 0, "Should calculate difference across year boundary");
        });
    }
}
