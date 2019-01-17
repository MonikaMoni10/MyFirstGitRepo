/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.library;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateHelper class to implement the methods that help working with dates.
 */
public final class DateHelper
{
   /**
    * The constructor was made private so it can never be instantiated.
    */
   private DateHelper()
   {
   }

   /**
    * Get the date from the given string in the format.
    * 
    * @param dateStr
    *           the string to be parsed as date
    * @param format
    *           the format string like "MM/DD/YYYY"
    * @return return the date object.
    */
   public static Date getDate(final String dateStr, final String format)
   {
      DateFormat dateFormat = new SimpleDateFormat(format);
      Date date = null;
      try
      {
         date = dateFormat.parse(dateStr);
      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }
      return date;
   }

   /**
    * Get today's date based on a desired format.
    * 
    * @param format
    *           the format string like "M/D/YYYY" or "M/D/YY"
    * 
    * @return string which represents the today's date.
    */
   public static String getTodaysDate(final String format)
   {
      Calendar todayCalendar = Calendar.getInstance();

      SimpleDateFormat dateFormat = new SimpleDateFormat(format);

      String today = dateFormat.format(todayCalendar.getTime());

      return today;
   }

   /**
    * we suppose format is m/dd/yyyy and delimiter is "/".
    * 
    * @return string which represents the today's date.
    */
   public static String getTodaysDate()
   {
      String format = "M/d/yyyy";

      Calendar todayCalendar = Calendar.getInstance();

      SimpleDateFormat dateFormat = new SimpleDateFormat(format);

      String today = dateFormat.format(todayCalendar.getTime());

      return today;

   }

   /**
    * we suppose delimiter is "/".
    * 
    * @return string which represents the today's year.
    */
   public static String getTodaysYear()
   {
      String delimiter = "/";
      String[] temp = getTodaysDate().split(delimiter);

      return temp[2];

   }

   /**
    * we suppose delimiter is "/".
    * 
    * @return string which represents today's month.
    */
   public static String getTodaysMonth()
   {

      String delimiter = "/";
      String[] temp = getTodaysDate().split(delimiter);

      return temp[0];
   }

   /**
    * we suppose delimiter is "/".
    * 
    * @return string which represents today's day.
    */
   public static String getTodaysDay()
   {

      String delimiter = "/";
      String[] temp = getTodaysDate().split(delimiter);

      return temp[1];
   }

   /**
    * Get tomorrow's date based on a desired format.
    * 
    * @param format
    *           the format string like "M/D/YYYY" or "M/D/YY"
    * 
    * @return string which represents tomorrow's date.
    */
   public static String getTomorrowsDate(final String format)
   {
      Calendar yesterdayCalendar = Calendar.getInstance();
      yesterdayCalendar.add(Calendar.DATE, 1);

      SimpleDateFormat dateFormat = new SimpleDateFormat(format);
      String yesterday = dateFormat.format(yesterdayCalendar.getTime());

      return yesterday;
   }

   /**
    * Get yesterday's date based on a desired format.
    * 
    * @param format
    *           the format string like "M/D/YYYY" or "M/D/YY"
    * 
    * @return string which represents the yesterday's date.
    */
   public static String getYesterdaysDate(final String format)
   {
      Calendar yesterdayCalendar = Calendar.getInstance();
      yesterdayCalendar.add(Calendar.DATE, -1);

      SimpleDateFormat dateFormat = new SimpleDateFormat(format);
      String yesterday = dateFormat.format(yesterdayCalendar.getTime());

      return yesterday;
   }
}
