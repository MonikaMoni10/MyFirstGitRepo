/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>TableNavigation</code> class represents a navigation section,
 * consisting of four Buttons, one TextBox, and one Label.
 */
public class TableNavigation extends Widget
{
   /**
    * constructs an instance of the {@link TableNavigation} class.
    * 
    * @param baseLocator
    *           the base locator table adjusted to Xpath from this locator to
    *           the navigation elements
    * @param browser
    *           the browser that will be used to access the control
    */
   public TableNavigation(final String baseLocator, final Browser browser)
   {
      super(baseLocator, browser);
   }

   /**
    * returns a {@link Button} representing navigating to the first item
    * 
    * @return a {@link Button} representing navigating to the first item
    */
   public Button getFirstButton()
   {
      // assuming the locator is already an XPATH
      return new Button(this.getLocator() + "/a[@title='Go to the first page']", this.getBrowser());
   }

   /**
    * returns a {@link Button} representing navigating to the previous item
    * 
    * @return a {@link Button} representing navigating to the previous item
    */
   public Button getPreviousButton()
   {
      // assuming the locator is already an XPATH      
      return new Button(this.getLocator() + "/a[@title='Go to the previous page']", this.getBrowser());
   }

   /**
    * returns a {@link TextBox} representing current page of the table
    * 
    * @return a {@link TextBox} representing current page of the table
    */
   public TextBox getCurrentPageTextBox()
   {
      // assuming the locator is already an XPATH      
      return new TextBox(this.getLocator() + "/span[1]/input", this.getBrowser());
   }

   /**
    * returns a {@link Label} representing total number of pages of the table
    * 
    * @return a {@link Label} representing total number of pages of the table
    */
   public Label getTotalPagesLabel()
   {
      // assuming the locator is already an XPATH      
      return new Label(this.getLocator() + "/span[1]", this.getBrowser());
   }

   /**
    * returns a {@link Button} representing navigating to the next item
    * 
    * @return a {@link Button} representing navigating to the next item
    */
   public Button getNextButton()
   {
      // assuming the locator is already an XPATH      
      return new Button(this.getLocator() + "/a[@title='Go to the next page']", this.getBrowser());
   }

   /**
    * returns a {@link Button} representing navigating to the last item
    * 
    * @return a {@link Button} representing navigating to the last item
    */
   public Button getLastButton()
   {
      // assuming the locator is already an XPATH      
      return new Button(this.getLocator() + "/a[@title='Go to the last page']", this.getBrowser());
   }
   
   /**
    * returns a {@link Label} representing the detailed number of items on current page
    * 
    * @return a {@link Label} representing the detailed number of items on current page
    */
   public Label getPageItems()
   {
      // assuming the locator is already an XPATH      
      return new Label(this.getLocator() + "/span[2]", this.getBrowser());
   }
}
