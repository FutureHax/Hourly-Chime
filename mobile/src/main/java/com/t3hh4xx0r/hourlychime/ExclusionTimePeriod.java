package com.t3hh4xx0r.hourlychime;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Adam Yocum
 */
public class ExclusionTimePeriod {
    private String timeStart;
    private String timeEnd;

    private static Date toDate(String hhmm) {
        final String[] hms = hhmm.split(":");
        final GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hms[0]));
        gc.set(Calendar.MINUTE, Integer.parseInt(hms[1]));
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        Date date = gc.getTime();
        return date;
    }

    public static void main() {


        //Test isInPeriod functionality
        ExclusionTimePeriod isInTest = new ExclusionTimePeriod();
        isInTest.setTimeStart("23:00");
        isInTest.setTimeEnd("10:00");

        System.out.println(toDate("22:59") + " is between " + isInTest.getTimeStart() + " and " + isInTest.getTimeEnd() + " = " + isInTest.isNowInPeriod());
        System.out.println(toDate("09:59") + " is between " + isInTest.getTimeStart() + " and " + isInTest.getTimeEnd() + " = " + isInTest.isNowInPeriod());

        System.out.println(toDate("10:00") + " is between " + isInTest.getTimeStart() + " and " + isInTest.getTimeEnd() + " = " + isInTest.isNowInPeriod());
        System.out.println(toDate("23:00") + " is between " + isInTest.getTimeStart() + " and " + isInTest.getTimeEnd() + " = " + isInTest.isNowInPeriod());

        System.out.println(toDate("10:01") + " is between " + isInTest.getTimeStart() + " and " + isInTest.getTimeEnd() + " = " + isInTest.isNowInPeriod());
        System.out.println(toDate("23:01") + " is between " + isInTest.getTimeStart() + " and " + isInTest.getTimeEnd() + " = " + isInTest.isNowInPeriod());

    }

    /**
     * @return the timeStart
     */
    public String getTimeStart() {
        return timeStart;
    }

    /**
     * @param timeStart the timeStart to set
     */
    public void setTimeStart(String timeStart) {
        if (timeStart.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9])$")) {
            this.timeStart = timeStart;
        } else {
            throw new IllegalArgumentException(timeStart + " is not a valid time, expecting HH:MM format");
        }

    }

    /**
     * @return the timeEnd
     */
    public String getTimeEnd() {
        return timeEnd;
    }

    /**
     * @param timeEnd the timeEnd to set
     */
    public void setTimeEnd(String timeEnd) {
        if (timeEnd.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9])$")) {
            this.timeEnd = timeEnd;
        } else {
            throw new IllegalArgumentException(timeEnd + " is not a valid time, expecting HH:MM format");
        }
    }

    public boolean isNowInPeriod() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        Date now = toDate(fmt.print(new DateTime(new Date())));
        return now.after(toDate(getTimeStart())) && now.before(toDate(getTimeEnd()));
    }

    public boolean isTimeInPeriod(String hhmm) {
        Date now = toDate(hhmm);
        return now.after(toDate(getTimeStart())) && now.before(toDate(getTimeEnd()));
    }
}