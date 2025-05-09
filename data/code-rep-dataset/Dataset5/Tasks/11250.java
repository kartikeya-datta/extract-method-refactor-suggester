public abstract class ExamplePage extends WicketExamplePage

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.examples.repeater;

import org.apache.wicket.examples.WicketExamplePage;

/**
 * Base class for all pages in the QuickStart application. Any page which subclasses this page can
 * get session properties from QuickStartSession via getQuickStartSession().
 * 
 * @param <T>
 */
public abstract class ExamplePage<T> extends WicketExamplePage<T>
{
	/**
	 * Get downcast session object for easy access by subclasses
	 * 
	 * @return The session
	 */
	public ContactsDatabase getContactsDB()
	{
		return ((RepeaterApplication)getApplication()).getContactsDB();
	}
}
 No newline at end of file