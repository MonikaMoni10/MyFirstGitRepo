package com.sonata.generic.automation.library;

/**
 * TimeDelay class.
 * 
 * Creates minimum, small, medium, large, maximum and massive time delay.
 * 
 */
public final class TimeDelay
{

   /**
    * The constructor was made private so it can never be instantiated.
    */
   private TimeDelay()
   {
   }

   //Time Out Constants.
   private static final int TIMEOUT_MINIMUM       = 1;
   private static final int TIMEOUT_SMALL         = 100;
   private static final int TIMEOUT_MEDIUM        = 1000;
   private static final int TIMEOUT_LARGE         = 3000;
   private static final int TIMEOUT_MAXIMUM       = 5000;
   private static final int TIMEOUT_EXTRA_MAXIMUM = 30000;
   private static final int TIMEOUT_MASSIVE       = 600000;
   private static final int DEFAULT_TIMEOUT       = 8000;
   private static final int DEFAULT_INTERVAL      = 50;

   /**
    * Minimum timeout getter.
    * 
    * @return <code>An integer representing a minimum time (1 millisecond)
    *         </code>
    */
   public static int getTimeoutMinimum()
   {
      return TIMEOUT_MINIMUM;
   }

   /**
    * Small timeout getter.
    * 
    * @return <code>An integer representing a small time (100 millisecond)
    *         </code>
    */
   public static int getTimeoutSmall()
   {
      return TIMEOUT_SMALL;
   }

   /**
    * Medium timeout getter.
    * 
    * @return <code>An integer representing a medium time (1000 millisecond)
    *         </code>
    */
   public static int getTimeoutMedium()
   {
      return TIMEOUT_MEDIUM;
   }

   /**
    * Large timeout getter.
    * 
    * @return <code>An integer representing a large time (3000 millisecond)
    *         </code>
    */
   public static int getTimeoutLarge()
   {
      return TIMEOUT_LARGE;
   }

   /**
    * Maximum timeout getter.
    * 
    * @return <code>An integer representing a maximum time (5000 millisecond)</code>
    */
   public static int getTimeoutMaximum()
   {
      return TIMEOUT_MAXIMUM;
   }

   /**
    * Extra Maximum timeout getter.
    * 
    * @return <code>An integer representing an extra maximum time (30000 millisecond)</code>
    */
   public static int getTimeoutExtraMaximum()
   {
      return TIMEOUT_EXTRA_MAXIMUM;
   }

   /**
    * This timeout should only be used for very time consuming processes, such
    * as Data Activation.
    * 
    * @return <code>An integer representing a maximum time (600000 millisecond)</code>
    */
   public static int getTimeoutMassive()
   {
      return TIMEOUT_MASSIVE;
   }

   /**
    * Sleeps the system for the amount of time passed to the function.
    * 
    * @param time
    *           Number of milliseconds to sleep before continuing the next
    *           command.
    * @return <li><code>true</code> if the sleep was successful.</li> <li>
    *         <code>false</code> if the sleep was not successful.</li>
    */
   public static boolean doPause(final int time)
   {
      try
      {
         Thread.sleep(time);
         return true;
      }
      catch (InterruptedException e)
      {
         return false;
      }
   }

   /**
    * Sleeps the system for 1 millisecond.
    * 
    * @return <li><code>True</code> if the sleep was successful.</li> <li>
    *         <code>False</code> if the sleep was not successful.</li>
    */
   public static boolean doMinimumPause()
   {
      return doPause(getTimeoutMinimum());
   }

   /**
    * Sleeps the system for 100 milliseconds.
    * 
    * @return <li><code>True</code> if the sleep was successful.</li> <li>
    *         <code>False</code> if the sleep was not successful.</li>
    */
   public static boolean doSmallPause()
   {
      return doPause(getTimeoutSmall());
   }

   /**
    * Sleeps the system for 1000 milliseconds.
    * 
    * @return <li><code>True</code> if the sleep was successful.</li> <li>
    *         <code>False</code> if the sleep was not successful.</li>
    */
   public static boolean doMediumPause()
   {
      return doPause(getTimeoutMedium());
   }

   /**
    * Sleeps the system for 3000 milliseconds.
    * 
    * @return <li><code>True</code> if the sleep was successful.</li> <li>
    *         <code>False</code> if the sleep was not successful.</li>
    */
   public static boolean doLargePause()
   {
      return doPause(getTimeoutLarge());
   }

   /**
    * Sleeps the system for 5000 milliseconds.
    * 
    * @return <li><code>True</code> if the sleep was successful.</li> <li>
    *         <code>False</code> if the sleep was not successful.</li>
    */
   public static boolean doMaximumPause()
   {
      return doPause(getTimeoutMaximum());
   }

   /**
    * Sleeps the system for 30000 milliseconds.
    * 
    * @return <li><code>True</code> if the sleep was successful.</li> <li>
    *         <code>False</code> if the sleep was not successful.</li>
    */
   public static boolean doExtraMaximumPause()
   {
      return doPause(getTimeoutExtraMaximum());
   }

   /**
    * @return the defaultTimeout
    */
   public static int getDefaultTimeout()
   {
      return DEFAULT_TIMEOUT;
   }

   /**
    * @return the defaultInterval
    */
   public static int getDefaultInterval()
   {
      return DEFAULT_INTERVAL;
   }
}
