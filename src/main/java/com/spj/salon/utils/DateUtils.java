package com.spj.salon.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Yogesh Sharma
 */
public class DateUtils {

    /**
     * NoOne should be able to instintiate
     */
    private DateUtils() {
        throw new IllegalStateException("UtilityClass");
    }

    /**
     * Return todays date
     *
     * @return
     */
    public static Date getTodaysDate() {
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
    public static Date getHoursAndMinutes(String time) {
        //String time = "20:30:44 PM"; // this is your input string
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            System.err.println("Couldn't parse string! " + e.getMessage());
        }
        return null;
    }

    public static Date getFormattedDate(String calendayDateString, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            return df.parse(calendayDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFormattedDateInString(Date calendayDate, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(calendayDate);
    }

    /**
     * This method will tell what day its is today.
     *
     * @return
     */
    public static String getTodaysDay() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
        return simpleDateformat.format(new Date());
    }

    public static boolean isTodayDate(Date date) {
        return date != null &&
                org.apache.commons.lang3.time.DateUtils.isSameDay(getTodaysDate(), date);
    }

    public static Date getNowTimePlus60Mins1970() {
        //String time = "20:30:44 PM"; // this is your input string
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        try {
            Date date = sdf.parse(getFormattedDateInString(new Date(), "hh:mm aa"));

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);

            gc.add(Calendar.HOUR, +1);
            return gc.getTime();
        } catch (ParseException e) {
            System.err.println("Couldn't parse string! " + e.getMessage());
        }
        return null;
    }

    public static Date getNowTime1970Format() {
        //String time = "20:30:44 PM"; // this is your input string
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        try {
            Date date = sdf.parse(getFormattedDateInString(new Date(), "hh:mm aa"));

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);

            return gc.getTime();
        } catch (ParseException e) {
            System.err.println("Couldn't parse string! " + e.getMessage());
        }
        return null;
    }
}
