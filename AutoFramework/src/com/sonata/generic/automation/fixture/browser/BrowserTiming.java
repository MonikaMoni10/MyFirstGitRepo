/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.browser;

/**
 * Interface for a browser timing related object for doing pauses and timeouts,
 * among other things.
 */
public interface BrowserTiming
{
   /**
    * <p>
    * Gets the length of the minimum timeout possible, in milliseconds.
    * </p>
    * 
    * <p>
    * NOTE: The minimum timeout should be around 1 millisecond.
    * </p>
    * 
    * @return The length of the minimum timeout possible, in milliseconds.
    */
   int getTimeoutMinimum();

   /**
    * <p>
    * Gets the length of a small timeout, in milliseconds.
    * </p>
    * 
    * <p>
    * NOTE: A small timeout should be around 100 milliseconds (0.1 seconds).
    * </p>
    * 
    * @return The length of a small timeout, in milliseconds.
    */
   int getTimeoutSmall();

   /**
    * <p>
    * Gets the length of a medium timeout, in milliseconds.
    * </p>
    * 
    * <p>
    * NOTE: A medium timeout should be around 1000 milliseconds (1 second).
    * </p>
    * 
    * @return The length of a medium timeout, in milliseconds.
    */
   int getTimeoutMedium();

   /**
    * <p>
    * Gets the length of a large timeout, in milliseconds.
    * </p>
    * 
    * <p>
    * NOTE: A large timeout should be around 3000 milliseconds (3 seconds).
    * </p>
    * 
    * @return The length of a large timeout, in milliseconds.
    */
   int getTimeoutLarge();

   /**
    * <p>
    * Gets the length of a "maximum" timeout, in milliseconds.
    * </p>
    * 
    * <p>
    * NOTE: A "maximum" timeout should be around 5000 milliseconds (5 seconds).
    * </p>
    * 
    * @return The length of a "maximum" timeout, in milliseconds.
    */
   int getTimeoutMaximum();

   /**
    * <p>
    * Gets the length of an "extra maximum" timeout, in milliseconds.
    * </p>
    * 
    * <p>
    * NOTE: An "extra maximum" timeout should be around 30000 milliseconds (30
    * seconds).
    * </p>
    * 
    * @return The length of an "extra maximum" timeout, in milliseconds.
    */
   int getTimeoutExtraMaximum();

   /**
    * <p>
    * Gets the length of a "massive" timeout, in milliseconds, where <b>this
    * timeout should only be used for very time-consuming processes, such as
    * data activation</b>.
    * </p>
    * 
    * <p>
    * NOTE: A "massive" timeout should be around 600000 milliseconds (in other
    * words, 600 seconds or 10 minutes).
    * </p>
    * 
    * @return The length of a "massive" timeout, in milliseconds.
    */
   int getTimeoutMassive();

   /**
    * Pauses (sleeps) the system for the specified number of milliseconds.
    * 
    * @param milliseconds
    *           The number of milliseconds to pause (sleep) before continuing
    *           with the next command.
    * 
    * @return Whether or not the pause was successful.
    */
   boolean doPause(final int milliseconds);

   /**
    * Pauses (sleeps) the system the system for the minimum time possible
    * (usually 1 millisecond, as found in {@link #getTimeoutMinimum()
    * getTimeoutMinimum}).
    * 
    * @return Whether or not the pause was successful.
    */
   boolean doMinimumPause();

   /**
    * Pauses (sleeps) the system the system for a small amount of time (usually
    * 100 milliseconds, as found in {@link #getTimeoutSmall() getTimeoutSmall}).
    * 
    * @return Whether or not the pause was successful.
    */
   boolean doSmallPause();

   /**
    * Pauses (sleeps) the system the system for a medium amount of time (usually
    * 1000 milliseconds, as found in {@link #getTimeoutMedium()
    * getTimeoutMedium}).
    * 
    * @return Whether or not the pause was successful.
    */
   boolean doMediumPause();

   /**
    * Pauses (sleeps) the system the system for a large amount of time (usually
    * 3000 milliseconds, as found in {@link #getTimeoutLarge() getTimeoutLarge}
    * ).
    * 
    * @return Whether or not the pause was successful.
    */
   boolean doLargePause();

   /**
    * Pauses (sleeps) the system the system for a "maximum" amount of time
    * (usually 5000 milliseconds, as found in {@link #getTimeoutMaximum()
    * getTimeoutMaximum} ).
    * 
    * @return Whether or not the pause was successful.
    */
   boolean doMaximumPause();

   /**
    * Pauses (sleeps) the system the system for an "extra maximum" amount of
    * time (usually 30000 milliseconds, as found in
    * {@link #getTimeoutExtraMaximum() getTimeoutExtraMaximum} ).
    * 
    * @return Whether or not the pause was successful.
    */
   boolean doExtraMaximumPause();

   /**
    * <p>
    * Gets the default number of milliseconds to wait before timing out.
    * </p>
    * 
    * <p>
    * NOTE: The default timeout should be around 8000 milliseconds (8 seconds).
    * </p>
    * 
    * @return The default number of milliseconds to wait before timing out.
    */
   int getDefaultTimeout();

   /**
    * <p>
    * Gets the default number of milliseconds to wait between tries.
    * </p>
    * 
    * <p>
    * NOTE: The default interval should be around 50 milliseconds.
    * </p>
    * 
    * @return The default number of milliseconds to wait between tries.
    */
   int getDefaultInterval();
}
