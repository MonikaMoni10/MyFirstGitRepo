/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

/**
 * The <code>Properties</code> interface acts as a base interface for the
 * several sorts of properties. There is generally one subclass for each sort of
 * {@link WebPage}.
 * 
 * They do not necessarily follow the same hierarchy as the <code>WebPage</code>
 * hierarchy, since a subclass may provide a property for itself. For example
 * although a <code>WebPage</code> requires a sign-in validation element all
 * setup UIs must have a navigation set of buttons and so all setup UIs already
 * have such an element declared (once they know what the navigation elements
 * are called).
 * 
 */
interface Properties
{
}
