/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */
package com.sonata.generic.automation.browser;

/**
 * The custom exception was constructed so that the FitNesse test can throw the
 * custom exception when needed to stop the test.
 * 
 * It was created as subclass of a runtime exception, so that when running in
 * JUnit test, it could be stopped when such exception happens.
 * 
 */
@SuppressWarnings("serial")
public class StopTestException extends IllegalArgumentException
{
   /**
    * default constructor
    */
   public StopTestException()
   {
      super();
   }

   /**
    * Throws custom exception message.
    * 
    * @param msg
    *           The exception message that needs to be thrown or displaed.
    */
   public StopTestException(String msg)
   {
      super(msg);
   }
}
