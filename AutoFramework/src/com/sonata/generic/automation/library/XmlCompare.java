/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.library;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.xml.sax.SAXException;

/**
 * XML compare class to implement the methods that interact with xml format
 * Files.
 * 
 */
public final class XmlCompare
{
   /**
    * The constructor was made private so it can never be instantiated.
    */
   private XmlCompare()
   {
   }

   /**
    * Compares the two xml files.
    * 
    * @param expectedFile
    *           Path of expected xml file
    * @param actualFile
    *           Path of actual xml file
    * 
    *           NOTE: current similar will have same result as identical
    * 
    * @return <li><code>True </code>if xml files Compared as identical</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public static boolean compareTwoXMLFile(final String expectedFile, final String actualFile)
   {
      FileReader fr1 = null;
      FileReader fr2 = null;

      try
      {
         fr1 = new FileReader(expectedFile);
         fr2 = new FileReader(actualFile);
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace();
         return false;
      }

      try
      {
         Diff diff = new Diff(fr1, fr2);
         DetailedDiff detailDiff = new DetailedDiff(diff);
         //System.out.println("Similar? " + detailDiff.similar());
         //System.out.println("Identical? " + detailDiff.identical());
         List<?> differences = detailDiff.getAllDifferences();
         for (Object object : differences)
         {
            Difference difference = (Difference)object;
            //System.out.println(difference);
         }
         return detailDiff.identical();
      }
      catch (SAXException e)
      {
         e.printStackTrace();
         return false;
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return false;
      }

   }

}
