/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The <code>BrowserSpecParser</code> class provides the ability to parse a
 * FitNesse-style browser specification into the critical bits.
 */
final class BrowserSpecParser
{
   private static final BrowserSpecParser SINGLETON = new BrowserSpecParser();

   /** Hide the constructor */
   private BrowserSpecParser()
   {
   }

   /**
    * returns an instance of the parser
    * 
    * @return an instance of the browser
    */
   static BrowserSpecParser getInst()
   {
      return SINGLETON;
   }

   /**
    * parses the specification, expecting only the permitted keys. This aims to
    * be extremely forgiving since it is in place to support FitNesse use.
    * <p>
    * The specification is expected to follow one of the following styles:
    * <ul>
    * <li>browser is chrome, server is bcraccqa04 and port is 8080</li>
    * <li>browser=chrome,server=bcraccqa04,port=8080</li>
    * </ul>
    * 
    * @param specification
    *           the specification to parse
    * @param permitted
    *           the permitted keys, in lower case
    * @return a map containing the parsed information, keyed by the lower case
    *         value of the setting
    * @throws IllegalArgumentException
    *            if the specification is invalid
    */
   Map<String, String> parse(String specification, Set<String> permitted)
   {
      final Map<String, String> result = new HashMap<String, String>();
      final String[] settingPairs = specification.split("(,\\s*)|(\\s+and\\s+)");
      for (String pair : settingPairs)
      {
         String[] each = pair.split("(\\s*=\\s*)|(\\s+is\\s+)");
         final String key = each[0].toLowerCase(Locale.ENGLISH);
         if (permitted.contains(key))
         {
            result.put(key, each[1]);
         }
         else
         {
            throw new IllegalArgumentException(formExceptionMessageOnInvalidSetting(each[0], specification, permitted));
         }
      }
      return result;
   }

   /**
    * form a good meaningful exception message for this event
    * 
    * @param setting
    *           the setting that was invalid
    * @param specification
    *           the specification
    * @param permitted
    *           the list of permitted settings
    * 
    * @return a String containing all the relevant information in an easy to
    *         read form
    */
   private String formExceptionMessageOnInvalidSetting(final String setting, final String specification,
         final Set<String> permitted)
   {
      String[] asStrings = permitted.toArray(new String[permitted.size()]);
      StringBuilder sb = new StringBuilder();
      sb.append("The setting '").append(setting).append("' in the specification '").append(specification)
            .append("' is not valid.  Valid values are: ");
      for (int i = 0; i < asStrings.length; ++i)
      {
         if (i > 0 && i == asStrings.length - 1)
            sb.append(" and ");
         else if (i != 0)
            sb.append(", ");
         sb.append(asStrings[i]);
      }
      final String string = sb.toString();
      return string;
   }
}
