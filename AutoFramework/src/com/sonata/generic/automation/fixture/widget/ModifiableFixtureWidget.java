/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

/**
 * Interface for a {@link FixtureWidget} that can be modified in terms of
 * setting up parent-child relationships between widgets on a form. This is only
 * meant to be used by the fixture configuration parser in order to be able to
 * create the fixture widget before setting up parent-child relationships.
 */
public interface ModifiableFixtureWidget extends FixtureWidget
{
   /**
    * Sets the parent {@link FixtureWidget} of this fixture widget.
    * 
    * @param parent
    *           The parent {@link FixtureWidget} of this fixture widget.
    */
   void setParent(FixtureWidget parent);

   /**
    * Adds a child {@link FixtureWidget} to this fixture widget.
    * 
    * @param childWidgetName
    *           The descriptive widget name of the child to add.
    * @param child
    *           The child {@link FixtureWidget} to add.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or the widget name is empty, or
    *            another child fixture widget by that name has already been
    *            added.
    */
   void addChild(String childWidgetName, FixtureWidget child);
}
