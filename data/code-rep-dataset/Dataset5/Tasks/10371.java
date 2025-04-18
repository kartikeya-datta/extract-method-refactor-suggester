executeListener(page.get("link"), "TestPage_ExpectedResult-2.html");

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
package org.apache.wicket.markup.html.header.testing3;

import org.apache.wicket.WicketTestCase;

/**
 * 
 * @author Juergen Donnerstag
 */
public class HeaderTest extends WicketTestCase
{
	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public HeaderTest(String name)
	{
		super(name);
	}

	/**
	 * Replace a Panel which has a body onLoad modifier
	 * 
	 * @throws Exception
	 */
	public void test_1() throws Exception
	{
		executeTest(TestPage.class, "TestPage_ExpectedResult-1.html");
		TestPage page = (TestPage)tester.getLastRenderedPage();

		executedListener(TestPage.class, page.get("link"), "TestPage_ExpectedResult-2.html");
	}
}