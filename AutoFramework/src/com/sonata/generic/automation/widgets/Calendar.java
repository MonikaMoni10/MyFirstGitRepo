/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.library.TimeDelay;

/**
 * The <code>Calendar</code> class represents a Calendar.
 */
public class Calendar extends Widget
{
   private String[] calendarMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
   private String currentMonthAsText;
   private int currentYear;
   
   /**
    * constructs an instance of the {@link Calendar} class.
    * 
    * @param locator
    *           the base locator of the Calendar.
    * @param browser
    *           the browser that will be used to access the control.
    */
   public Calendar(final String locator, final Browser browser)
   {
      super(locator, browser);
   }

   /**
    * Get from the UI and set the Current Month and Year properties
    */
   private void setCurrentMonthAndYear()
   {
      // locator is assumed already to be an XPATH
      String text = this.getBrowser().getText(this.getLocator() + "/div[1]/div[1]/a[2]");
      String[] monthYear = text.split(" ");
      currentMonthAsText = monthYear[0];
      currentYear = Integer.parseInt(monthYear[1]);     
   }   
   
   /**
    * Move to the previous month in the calendar
    */   
   private void navigateMonthPrevious()
   {
      this.getBrowser().click(this.getLocator() + "/div[1]/div[1]/a[1]");
      TimeDelay.doPause(500);
      setCurrentMonthAndYear();         
   }
   
   /**
    * Move to the next month in the calendar
    */    
   private void navigateMonthNext()
   {
      this.getBrowser().click(this.getLocator() + "/div[1]/div[1]/a[3]");
      TimeDelay.doPause(500);
      setCurrentMonthAndYear();         
   }   
   
   /**
    * Selects the specified Date in the Calendar.
    * 
    * @param year
    *           Year (The value entered by the User) to be selected.
    * @param month
    *           Month (The value entered by the User) to be selected.
    * @param date
    *           Date (The value entered by the User) to be selected.
    * 
    * @return <li><code>true</code> if the date is successfully selected.</li>
    *         <li>
    *         <code>false</code> if the date is not selected.</li>
    * 
    */
   private boolean selectYearMonthAndDate(int year, String month, int date)
   {
      if (year < this.currentYear)
      {
         // Navigate the months before the current month
         while (year != this.currentYear || !month.equalsIgnoreCase(this.currentMonthAsText))
            navigateMonthPrevious();          
         
         if (year == this.currentYear && month.equalsIgnoreCase(this.currentMonthAsText))
            return selectDate(date);
      }
      else if (year > this.currentYear)
      {
         // Navigate the months after the current month
         while (year != this.currentYear || !month.equalsIgnoreCase(this.currentMonthAsText))
            navigateMonthNext();           
         
         if (year == this.currentYear && month.equalsIgnoreCase(this.currentMonthAsText))
            return selectDate(date);
      }
      
      return false;
   }

   /**
    * Selects the specified Month and Day in the Calendar.
    * 
    * @param month
    *           Month (The value entered by the User) to be selected.
    * @param monthText
    *           MonthText (The default value displayed in the Date Picker)to be
    *           selected.
    * @param date
    *           Date (The value entered by the User) to be selected.
    * 
    * @return <li><code>true</code> if the Month and Day is successfully
    *         selected.</li> <li>
    *         <code>false</code> if the Month and Day is not selected.</li>
    * 
    */
   private boolean selectMonthAndDate(int month, String monthText, int date)
   {
      int differenceInMonth = month - convertMonthNameToInt(monthText);

      if ( differenceInMonth < 0)
      {
         // Navigate the months before the current month         
         while (!monthText.equalsIgnoreCase(this.currentMonthAsText))
            navigateMonthPrevious();            
         
         if (monthText.equalsIgnoreCase(this.currentMonthAsText))
            return selectDate(date);
      }
      else
      {
         // Navigate the months after the current month         
         while (!monthText.equalsIgnoreCase(this.currentMonthAsText))
            navigateMonthNext();          
                  
         if (monthText.equalsIgnoreCase(this.currentMonthAsText))  
            return selectDate(date);
      }
      
      return false;
   }

   /**
    * Selects the specified Day in the Calendar.
    * 
    * @param date
    *           Date (The value entered by the User) to be selected.
    * 
    * @return <li><code>true</code> if the Day is successfully selected.</li>
    *         <li>
    *         <code>false</code> if the Day is not selected.</li>
    * 
    */
   private boolean selectDate(int day)
   {
      int indexValue = 1;
      int totalRowsOfDates = 6;
      int totalColumnsOfDates = 7;
      
      // before trying to select ensure that gave a little time as sometimes there
      // are timing issues without
      TimeDelay.doMediumPause();
      
      // Set the row index to start looking for the date
      if (day > 25)
      {
         indexValue = 4;
      }
      
      for (; indexValue <= totalRowsOfDates; indexValue++)
      {
         for (int columnIndex = 1; columnIndex <= totalColumnsOfDates; columnIndex++)
         {
            String locator = this.getLocator() + "/div/table[1]/tbody/tr[" + indexValue + "]/td[" + columnIndex + "]/a";
            
            if (!this.getBrowser().exists(locator))
               throw new IllegalArgumentException("Calendar day locator '" + locator + " not found.");
            
            String text = this.getBrowser().getText(locator);
            if (text.equalsIgnoreCase(Integer.toString(day)))
            {
               return this.getBrowser().click(locator);
            }
         }
      }
      
      return false;
   }

   /**
    * Gets the user input Month as a string.
    * 
    * @param month
    *           Month to be changed as String.
    * 
    * @return a {@link String} representing the text value for a Month.
    */
   private String getMonthAsText(int month)
   {
      if (month < 1 || month > 12)
         throw new IllegalArgumentException("Month value " + month + " is invalid.");  
      
      return calendarMonths[month - 1];
   }

   /**
    * Gets the Currently set Month as a Integer value.
    * 
    * @param month
    *           the month (as a string) to be converted
    *           
    * @return an integer value of specified Month Text.
    */
   private int convertMonthNameToInt(String month)
   {
      for (int i = 0; i < calendarMonths.length; i++)
      {
         if (month.equalsIgnoreCase(calendarMonths[i]))
            return i + 1;
      }
      
      // Should not come to this
      return 0;
   }

   /**
    * Determine if a given year is a leap year.
    * 
    * @param year
    *           the year to be examined
    * 
    * @return <li><code>True </code>if year is a leap year.</li> <li>
    *         <code>False</code> if year is not a leap year.</li>
    */   
   private boolean isLeapYear(int year)
   {
      if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0))
         return true;
      
      return false;
   }
   
   /**
    * Selects the specified Date in the Calendar.
    * 
    * <p>
    * Note:The format <b>date</b> should be "MM/DD/YYYY".
    * </p>
    * 
    * @param inputDate
    *           the string to be parsed as date.
    * 
    * @return <li><code>True </code>if Date is selected successfully.</li> <li>
    *         <code>False</code> if Date is not selected.</li>
    */
   public boolean setCalendarDate(String inputDate)
   {
      String[] dateArray = inputDate.split("/");
                 
      int month = Integer.parseInt(dateArray[0]);
      int date = Integer.parseInt(dateArray[1]);       
      int year = Integer.parseInt(dateArray[2]);
      
      // If Month is not an appropriate value then throw an exception
      if (month < 0 || month > 12) 
         throw new IllegalArgumentException("The month value of " + month + " is invalid.");       
      
      // If the Day is not an appropriate value then throw an exception
      if (date > 31 || date < 0)
         throw new IllegalArgumentException("The day value of " + date + " is invalid.");  

      // If the Year is not an appropriate value then throw an exception
      if (year < 1900 || year > 2400)
         throw new IllegalArgumentException("The year value of " + year + " is invalid.");
    
      
      String monthText = getMonthAsText(month);
      
      // Handle February and the leap year condition, throwing exception if date is invalid
      if (monthText.equals("February"))
      {
         if (date > 29)
            throw new IllegalArgumentException("The Year is Leap year, Feb have the date only till 29");   
            
         if (isLeapYear(year) && date == 29)
            throw new IllegalArgumentException("The Year is Not Leap year, Feb have the date only till 28");            
      }
 
      // If date value given for the months that only have 30 days then throw an exception
      if ((monthText.equals("April") || monthText.equals("June") || monthText.equals("September") || monthText.equals("November"))
            && date > 30)
      {
         throw new IllegalArgumentException(monthText + " only has 30 days.");
      }
      
      // Get the Current Month and Year value as per the UI to help dictate how to navigate to find what is needed
      setCurrentMonthAndYear();
      
      // If the year is different from the currently set year and month then we need to select year, month, and date
      if (year != this.currentYear)
         return selectYearMonthAndDate(year, monthText, date);  
      
      // At this point the year is the assumed the same as current, but if the months is different then need to select the month and date 
      if (!monthText.equalsIgnoreCase(this.currentMonthAsText))
         return selectMonthAndDate(month, monthText, date);
      
      // At this point the year and month are the same as the currently set year and month, so only select the date
      return selectDate(date);
   }
}
