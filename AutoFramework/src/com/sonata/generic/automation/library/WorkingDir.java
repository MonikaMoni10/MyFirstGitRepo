package com.sonata.generic.automation.library;

import java.io.File;

/**
 * Class to return current working directly when running FitNesse test.
 * 
 * @author wanku01
 * 
 */
public final class WorkingDir
{

   /**
    * The constructor was made private so it can never be instantiated.
    */
   private WorkingDir()
   {
   }

   /**
    * Get current working directory.
    * 
    * @return String the working directory.
    */
   public static String getWorkingDir()
   {
      File directory = new File(".");
      try
      {
         return directory.getCanonicalPath();
      }
      catch (Exception e)
      {
         System.out.println("Exceptione is =" + e.getMessage());
      }
      return null;
   }
}
