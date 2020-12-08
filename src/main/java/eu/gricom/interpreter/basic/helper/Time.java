package eu.gricom.interpreter.basic.helper;

/**
Time.java
(c) = 2004,..,2007 by Andreas Grimm, Den Haag, The Netherlands

This software is protected according to Dutch and International Law. Certain components of
this package might have been released under terms of conditions of the General Public License
of the GNU organization. Those modules are named with name and version number under the
following URL: http://www.gricom.nl/public_released

Modules and versions not named on this location are not released and cannot be used without
written consent of the author. General terms and conditions of the given URL apply. This
notice cannot not be used as evidence of public release.

Created in 2003

$Id: Time.java,v 1.2 2009/11/05 11:12:23 Andreas Exp $
$Author: Andreas $
$Log: Time.java,v $
Revision 1.2  2009/11/05 11:12:23  Andreas
checkpoint November 2009
Committed on the Free edition of March Hare Software CVSNT Server.
Upgrade to CVS Suite for more features and support:
http://march-hare.com/cvsnt/

Revision 1.1  2009/04/21 19:18:21  Andreas
Initial check in on new directory
Committed on the Free edition of March Hare Software CVSNT Server.
Upgrade to CVS Suite for more features and support:
http://march-hare.com/cvsnt/

Revision 1.1  2007/11/10 21:10:00  agrimm
Windows Version
Java 1.5
Eclipse 3.x Europa


*/

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author andreas
 */
public class Time {
    private static final int YEAR = 2000;
    private static final int MONTH = 1;
    private static final int DAY = 1;
    private static final int HOUR = 0;
    private static final int MINUTE = 0;
    private static final int SECS = 0;

    private int _iYear = YEAR;
    private int _iMonth = MONTH;
    private int _iDay = DAY;
    private int _iHour = HOUR;
    private int _iMinute = MINUTE;
    private int _iSecs = SECS;
    private GregorianCalendar _oDate = new GregorianCalendar();

    /**
     * Time Constructor, sets the object with a 'now'.
     */
    public Time() {
        now();
    }

    /**
     * Time Constructor, sets the object with a date.
     *
     * @param strDate
     *            initialize the Time object with a date string
     */
    public Time(final String strDate) {
        try {
            parseDate(strDate);
        } catch (Exception eException) {
            System.err.println(eException.getMessage());
        }
    }

    /**
     * Reset all fields of this object.
     */
    public final void clearTime() {
        _iYear = YEAR;
        _iMonth = MONTH;
        _iDay = DAY;
        _iHour = HOUR;
        _iMinute = MINUTE;
        _iSecs = SECS;
        _oDate = new GregorianCalendar();
        _oDate.set(_iYear, _iMonth - 1, _iDay, _iHour, _iMinute, _iSecs);
    }

    /**
     * Parse an entered date + time into the object format.
     * 
     * @param strDate
     *            - Date + time as string
     * @throws Exception
     *             - parsing fails
     */
    public final void parseDateTime(final String strDate) throws Exception {
        // assumption that all dates in ProvJs are formatted according to the
        // same pattern: YYYY-MM-DD HH:mm:ss, where HH is the hour from 00 - 23.
        // If this pattern is broken, the method will return a 'false'.
        parseDate(strDate);

        String strDummy = "";

        // first - isolate the hour
        strDummy = strDate.substring(11, 13);
        if (strDummy != null) {
            _iHour = Integer.parseInt(strDummy);
        } else {
            throw new Exception("[eu.gricom.interpreter.basic.helper.Time] Hour could not been parsed");
        }

        // second - parse the month
        strDummy = strDate.substring(14, 16);
        if (strDummy != null) {
            _iMinute = Integer.parseInt(strDummy);
        } else {
            throw new Exception("[eu.gricom.interpreter.basic.helper.Time] Minute could not been parsed");
        }

        // third - parse the day
        strDummy = strDate.substring(17, 19);
        if (strDummy != null) {
            _iSecs = Integer.parseInt(strDummy);
        } else {
            throw new Exception("[eu.gricom.interpreter.basic.helper.Time] Seconds could not been parsed");
        }

        if ((_iHour < 0) || (_iHour > 23) || (_iMinute < 0) || (_iMinute > 59) || (_iSecs < 0) || (_iSecs > 59)) {
            throw new Exception("[eu.gricom.interpreter.basic.helper.Time] Time out of range");
        }

        _oDate.set(_iYear, _iMonth, _iDay, _iHour, _iMinute, _iSecs);
    }

    /**
     * Parse an entered date into the object format.
     * 
     * @param strDate
     *            - Date as string
     * @throws Exception
     *             - parsing fails
     */
    public final void parseDate(final String strDate) throws Exception {
        // assumption that all dates in ProvJs are formatted according to the
        // same pattern: YYYY-MM-DD.
        // If this pattern is broken, the method will return a 'false'.
        String strDummy = "";

        // first - isolate the year
        strDummy = strDate.substring(0, 4);
        if (strDummy != null) {
            _iYear = Integer.parseInt(strDummy);
        } else {
            throw new Exception("[eu.gricom.interpreter.basic.helper.Time] Year could not been parsed");
        }

        // second - parse the month
        strDummy = strDate.substring(5, 7);
        if (strDummy != null) {
            _iMonth = Integer.parseInt(strDummy);
        } else {
            throw new Exception("[eu.gricom.interpreter.basic.helper.Time] Month could not been parsed");
        }

        // third - parse the day
        strDummy = strDate.substring(8, 10);
        if (strDummy != null) {
            _iDay = Integer.parseInt(strDummy);
        } else {
            throw new Exception("[eu.gricom.interpreter.basic.helper.Time] Day could not been parsed");
        }

        if ((_iYear < 0) || (_iYear > 3000) || (_iMonth < 1) || (_iMonth > 12) || (_iDay < 0) || (_iDay > 31)) {
            throw new Exception("[eu.gricom.interpreter.basic.helper.Time] Date out of range");
        }

        _oDate.set(_iYear, _iMonth, _iDay);
    }

    /**
     * set this time object to the current system time.
     */
    public final void now() {
        Calendar oCalendar = Calendar.getInstance();
        _iDay = oCalendar.get(Calendar.DAY_OF_MONTH);
        _iMonth = oCalendar.get(Calendar.MONTH) + 1;
        _iYear = oCalendar.get(Calendar.YEAR);
        _iHour = oCalendar.get(Calendar.HOUR_OF_DAY);
        _iMinute = oCalendar.get(Calendar.MINUTE);
        _iSecs = oCalendar.get(Calendar.SECOND);
        _oDate.set(_iYear, _iMonth - 1, _iDay, _iHour, _iMinute, _iSecs);
    }

    /**
     * get the date of this object.
     * 
     * @return - date of this string
     */
    public final String getDate() {
        String strTime = "";
        String strDay = "";
        String strMonth = "";

        if (_iMonth < 10) {
            strMonth = "0" + _iMonth;
        } else {
            strMonth += _iMonth;
        }

        if (_iDay < 10) {
            strDay = "0" + _iDay;
        } else {
            strDay += _iDay;
        }

        strTime = _iYear + "-" + strMonth + "-" + strDay;
        return (strTime);
    }

    /**
     * get the date + time of this object.
     * 
     * @return - date and time as string
     */
    public final String getDateTime() {
        String strDateTime = getDate();
        String strHour = "";
        String strMinute = "";
        String strSecs = "";

        if (_iHour < 10) {
            strHour = "0" + _iHour;
        } else {
            strHour += _iHour;
        }

        if (_iMinute < 10) {
            strMinute = "0" + _iMinute;
        } else {
            strMinute += _iMinute;
        }

        if (_iSecs < 10) {
            strSecs = "0" + _iSecs;
        } else {
            strSecs += _iSecs;
        }

        strDateTime += " " + strHour + ":" + strMinute + ":" + strSecs;
        return (strDateTime);
    }

    /**
     * get the year of this date.
     * 
     * @return - year value of this date
     */
    public final int getYear() {
        return (_iYear);
    }

    /**
     * get the month of this date.
     * 
     * @return - month value of date
     */
    public final int getMonth() {
        return (_iMonth);
    }

    /**
     * get the day of this date.
     * 
     * @return - day value of date
     */
    public final int getDay() {
        return (_iDay);
    }

    /**
     * get the Gregorian day of the year.
     * 
     * @return - number of days passed for this time object in this year
     */
    public final int getDayOfYear() {
        return (_oDate.get(GregorianCalendar.DAY_OF_YEAR));
    }

    /**
     * Verifies whether the date in this object is valid.
     * 
     * @return - true, if date is valid
     */
    public final boolean isValid() {
        if ((_iYear < 1900) || (_iYear > 2100)) {
            return (false);
        }
        if ((_iMonth < 1) || (_iMonth > 12)) {
            return (false);
        }
        if ((_iDay < 1) || (_iDay > 31)) {
            return (false);
        }
        if (((_iMonth == 4) || (_iMonth == 6) || (_iMonth == 9) || (_iMonth == 11)) && (_iDay > 30)) {
            return (false);
        }
        if ((_iMonth == 2) && (_iDay > 29)) {
            return (false);
        }
        if ((_iHour < 0) || (_iHour > 23) || (_iMinute < 0) || (_iMinute > 59) || (_iSecs < 0) || (_iSecs > 59)) {
            return (false);
        }
        return (true);
    }

    /**
     * Returns the next date / day.
     * 
     * @return - returns the next day as string
     */
    public final String getNextDay() {
        Time oTempTime = new Time(getDateTime());
        oTempTime.addDays(1);

        return (oTempTime.getDateTime());
    }

    /**
     * Adds a number of days to this time object.
     * 
     * @param iDays
     *            - number of days to add
     */
    public final void addDays(final int iDays) {

        int iMaxDays = getDaysInMonth(_iMonth, _iYear);

        _iDay += iDays;
        // calculate a balance of months and days from the input parameter
        while (_iDay > iMaxDays) {
            _iDay -= iMaxDays;
            _iMonth++;
            if (_iMonth > 12) {
                _iMonth -= 12;
                _iYear++;
            }
            iMaxDays = getDaysInMonth(_iMonth, _iYear);
        }

        _oDate.set(_iYear, _iMonth - 1, _iDay, _iHour, _iMinute, _iSecs);
    }

    /**
     * Adds a number of months to this time object.
     * 
     * @param iInputMonths
     *            - number of months to add
     */
    public final void addMonths(final int iInputMonths) {
        int iMonth = _iMonth + iInputMonths;

        while (iMonth > 12) {
            _iYear++;
            iMonth--;
        }

        // now normailze the day again ...
        int iMaxDays = getDaysInMonth(iMonth, _iYear);
        while (_iDay > iMaxDays) {
            iMonth++;
            _iDay -= iMaxDays;
            iMaxDays = getDaysInMonth(iMonth, _iYear);
        }

        _oDate.set(_iYear, iMonth - 1, _iDay, _iHour, _iMinute, _iSecs);
        _iMonth = iMonth;
    }

    /**
     * Deducts a number of days from this time object.
     * 
     * @param iDays
     *            - number of days to deduct
     */
    public final void substractDays(final int iDays) {
        int iDay = _iDay - iDays;

        while (iDay < 1) {
            _iMonth--;

            if (_iMonth < 1) {
                _iYear--;
                _iMonth += 12;
            }

            iDay += getDaysInMonth(_iMonth, _iYear);
        }
        _oDate.set(_iYear, _iMonth - 1, iDay, _iHour, _iMinute, _iSecs);
        _iDay = iDay;
    }

    /**
     * Compares whether this time object represents a time prior than the one to
     * compare to.
     * 
     * @param oTime
     *            - object to compare to
     * @return - true, if this time is nether greater than or matches the compared
     *         time
     */
    public final boolean lessThan(final Time oTime) {
        if (greaterThan(oTime)) {
            return (false);
        }

        if (matches(oTime)) {
            return (false);
        }

        return (true);
    }

    /**
     * Matching of this time object with another one.
     * 
     * @param oTime
     *            - object to compare to
     * @return - true, if objects represent the same time
     */
    public final boolean matches(final Time oTime) {
        if ((oTime._iYear == _iYear) && (oTime._iMonth == _iMonth) && (oTime._iDay == _iDay) && (oTime._iHour == _iHour)
                && (oTime._iMinute == _iMinute) && (oTime._iSecs == _iSecs)) {
            return (true);
        }
        return (false);
    }

    /**
     * Defines whether another time object represents a time greater than this one.
     * 
     * @param oTime
     *            - time object to compare to
     * @return true, if the compared time is later than this one
     */
    public final boolean greaterThan(final Time oTime) {
        if (oTime._iYear < _iYear) {
            return (true);
        }

        if ((oTime._iYear == _iYear) && (oTime._iMonth < _iMonth)) {
            return (true);
        }

        if ((oTime._iYear == _iYear) && (oTime._iMonth == _iMonth) && (oTime._iDay < _iDay)) {
            return (true);
        }

        if ((oTime._iYear == _iYear) && (oTime._iMonth == _iMonth) && (oTime._iDay == _iDay)
                && (oTime._iHour < _iHour)) {
            return (true);
        }

        if ((oTime._iYear == _iYear) && (oTime._iMonth == _iMonth) && (oTime._iDay == _iDay) && (oTime._iHour == _iHour)
                && (oTime._iMinute < _iMinute)) {
            return (true);
        }

        if ((oTime._iYear == _iYear) && (oTime._iMonth == _iMonth) && (oTime._iDay == _iDay) && (oTime._iHour == _iHour)
                && (oTime._iMinute == _iMinute) && (oTime._iSecs < _iSecs)) {
            return (true);
        }

        return (false);
    }

    /**
     * Difference between to dates in days
     * 
     * This method uses the date represented by this time object and compares it
     * with another time object.
     * 
     * @param oTime
     *            - the time object to compare to
     * @return - difference in days
     */
    public final int diffInDays(final Time oTime) {
        GregorianCalendar oGregorianCalendar = new GregorianCalendar();
        int iDifference = 0;
        Time oLowerTime = null;
        Time oHigherTime = null;

        if (matches(oTime)) {
            return (0);
        }

        if (greaterThan(oTime)) {
            oLowerTime = oTime;
            oHigherTime = this;
        } else {
            oLowerTime = this;
            oHigherTime = oTime;
        }

        iDifference = getDaysUntilEndOfYear(oLowerTime);

        int iWorkYear = oLowerTime._iYear + 1;
        while (iWorkYear < (oHigherTime._iYear - 1)) {
            if (oGregorianCalendar.isLeapYear(iWorkYear)) {
                iDifference += 366;
            } else {
                iDifference += 365;
            }
        }

        return (iDifference);
    }

    /**
     * Get the number of days in a month.
     * 
     * @param iMonth
     *            - the month requested (1 - 12)
     * @param iYear
     *            - the year requested
     * @return - number of days in month
     */
    private int getDaysInMonth(final int iMonth, final int iYear) {
        GregorianCalendar oGregorianCalendar = new GregorianCalendar();

        int iMaxDays = 31;

        // check whether the month is February ...
        if (iMonth == 2) {
            // now that is February ... let's see how many days February has ...
            if (oGregorianCalendar.isLeapYear(iYear)) {
                iMaxDays = 29;
            } else {
                iMaxDays = 28;
            }
        }

        if ((iMonth == 4) || (iMonth == 6) || (iMonth == 9) || (iMonth == 11)) {
            iMaxDays = 30;
        }
        return (iMaxDays);
    }

    /**
     * Days until end of year.
     * 
     * @param oTime
     *            - time object
     * @return - number of days until the end of the year
     */
    private int getDaysUntilEndOfYear(final Time oTime) {
        int iDays = 0;
        int iWorkMonth = oTime._iMonth + 1;

        while (iWorkMonth < 13) {
            iDays += getDaysInMonth(iWorkMonth, oTime._iYear);
        }

        iDays += getDaysUntilEndOfMonth(oTime);
        return (iDays);
    }

    /**
     * Days until end of month.
     * 
     * @param oTime
     *            - time object
     * @return - number of days until the end of the month
     */
    private int getDaysUntilEndOfMonth(final Time oTime) {
        int iDays = getDaysInMonth(oTime._iMonth, oTime._iYear) - oTime._iDay;
        return (iDays);
    }
}
