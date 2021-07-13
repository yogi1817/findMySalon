package com.spj.salon.utils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author Yogesh Sharma
 */
public class DateUtils {

    /**
     * NoOne should be able to instantiate
     */
    private DateUtils() {
        throw new IllegalStateException("UtilityClass");
    }

    /**
     * Return todays date
     *
     * @return
     */
    public static Date getTodaysDate(TimeZone timeZone) {
        SimpleDateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        gmtDateFormat.setTimeZone(timeZone);

        Date date = DateUtils.getFormattedDateAsDate(gmtDateFormat.format(new Date()), "yyyy-MM-dd");
        return Calendar.getInstance().getTime();
    }

    public static LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }

    /**
     * Get hours as an integer from 0 to 23
     *
     * @param time
     * @return
     */
    public static Date getHoursAndMinutes(String time, TimeZone timeZone) {
        //String time = "20:30:44 PM"; // this is your input string
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        sdf.setTimeZone(timeZone);

        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            System.err.println("Couldn't parse string! " + e.getMessage());
        }
        return null;
    }

    public static Date getFormattedDate(String calendarDateString, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            return df.parse(calendarDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFormattedDateInString(Date calendarDate, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(calendarDate);
    }

    public static String getTimeZoneFormattedDateInString(Date calendarDate, String dateFormat, TimeZone timeZone) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        df.setTimeZone(timeZone);

        return df.format(calendarDate);
    }

    /**
     * This method will tell what day its is today.
     *
     * @return
     */
    public static String getTodaysDay(TimeZone timeZone) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
        simpleDateformat.setTimeZone(timeZone);

        return simpleDateformat.format(new Date()).toUpperCase();
    }

    public static boolean isTodayDate(Date date, TimeZone timeZone) {
        return date != null &&
                org.apache.commons.lang3.time.DateUtils.isSameDay(getTodaysDate(timeZone), date);
    }

    public static Date getNowTimePlus60Mins1970(TimeZone timeZone) {
        //String time = "20:30:44 PM"; // this is your input string
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        try {
            Date date = sdf.parse(getTimeZoneFormattedDateInString(new Date(), "hh:mm aa", timeZone));

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);

            gc.add(Calendar.HOUR, +1);
            return gc.getTime();
        } catch (ParseException e) {
            System.err.println("Couldn't parse string! " + e.getMessage());
        }
        return null;
    }

    public static Date getNowTime1970Format(TimeZone timeZone) {
        //String time = "20:30:44 PM"; // this is your input string
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        try {
            Date date = sdf.parse(getTimeZoneFormattedDateInString(new Date(), "hh:mm aa", timeZone));

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);

            return gc.getTime();
        } catch (ParseException e) {
            System.err.println("Couldn't parse string! " + e.getMessage());
        }
        return null;
    }

    //Ex todays Cal close time is 10PM and current time is 8:59 PM then return false
    //Ex todays Cal close time is 10PM and current time is 9:01 PM then return true
    public static boolean isTodaysCalenderCloseTimeInOneHour(Date closeTime, TimeZone timeZone) {
        if (closeTime.after(getNowTimePlus60Mins1970(timeZone)))
            return false;

        return true;
    }

    public static Date getFormattedDateAsDate(String calendarDateString, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            return df.parse(calendarDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
