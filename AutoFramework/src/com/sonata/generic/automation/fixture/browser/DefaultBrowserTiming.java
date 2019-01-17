/*

 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.browser;

import com.sonata.generic.automation.library.TimeDelay;

/**
 * Default implementation of a {@link BrowserTiming} object.
 */
public final class DefaultBrowserTiming implements BrowserTiming
{
   /**
    * {@inheritDoc}
    */
   @Override
   public int getTimeoutMinimum()
   {
      return TimeDelay.getTimeoutMinimum();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getTimeoutSmall()
   {
      return TimeDelay.getTimeoutSmall();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getTimeoutMedium()
   {
      return TimeDelay.getTimeoutMedium();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getTimeoutLarge()
   {
      return TimeDelay.getTimeoutLarge();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getTimeoutMaximum()
   {
      return TimeDelay.getTimeoutMaximum();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getTimeoutExtraMaximum()
   {
      return TimeDelay.getTimeoutExtraMaximum();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getTimeoutMassive()
   {
      return TimeDelay.getTimeoutMassive();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean doPause(final int milliseconds)
   {
      return TimeDelay.doPause(milliseconds);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean doMinimumPause()
   {
      return TimeDelay.doMinimumPause();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean doSmallPause()
   {
      return TimeDelay.doSmallPause();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean doMediumPause()
   {
      return TimeDelay.doMediumPause();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean doLargePause()
   {
      return TimeDelay.doLargePause();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean doMaximumPause()
   {
      return TimeDelay.doMaximumPause();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean doExtraMaximumPause()
   {
      return TimeDelay.doExtraMaximumPause();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getDefaultTimeout()
   {
      return TimeDelay.getDefaultTimeout();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getDefaultInterval()
   {
      return TimeDelay.getDefaultInterval();
   }
}
