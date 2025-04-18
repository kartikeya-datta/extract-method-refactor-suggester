tester.getRequest().getPostParameters().setParameterValue(textfield.getInputName(),

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
package org.apache.wicket.markup.html.form.validation;

import org.apache.wicket.WicketTestCase;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class HomePageTest extends WicketTestCase
{

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		tester.startPage(HomePage.class);
		tester.assertRenderedPage(HomePage.class);
	}

	/**
	 * 
	 */
	public void testWithoutBorder()
	{
		tester.executeAjaxEvent("form:submit", "onclick");
		assertEquals("Expected one error message",
			tester.getMessages(FeedbackMessage.ERROR).size(), 2);
		assertTrue(((HomePage)tester.getLastRenderedPage()).hitOnError);
		assertFalse(((HomePage)tester.getLastRenderedPage()).hitOnSubmit);
	}

	/**
	 * 
	 */
	public void testWithoutBorder2()
	{
		FormTester formTester = tester.newFormTester("form");
		formTester.setValue("textfield1", "testxxx");
		tester.executeAjaxEvent("form:submit", "onclick");
		tester.assertNoErrorMessage();
		assertFalse(((HomePage)tester.getLastRenderedPage()).hitOnError);
		assertTrue(((HomePage)tester.getLastRenderedPage()).hitOnSubmit);
	}

	/**
	 * 
	 */
	public void testWithBorder()
	{
		tester.executeAjaxEvent("border:form2:submit", "onclick");
		assertEquals("Expected one error message",
			tester.getMessages(FeedbackMessage.ERROR).size(), 2);
		assertTrue(((MyBorder)tester.getLastRenderedPage().get("border")).hitOnError);
		assertFalse(((MyBorder)tester.getLastRenderedPage().get("border")).hitOnSubmit);
	}

	/**
	 * 
	 */
	public void testWithBorder2()
	{
		TextField textfield = (TextField)tester.getLastRenderedPage().get(
			"border:form2:border_body:textfield1");
		tester.getRequest().getPostRequestParameters().setParameterValue(textfield.getInputName(),
			"abcde");
		tester.executeAjaxEvent("border:form2:submit", "onclick");
		tester.assertNoErrorMessage();
		assertFalse(((MyBorder)tester.getLastRenderedPage().get("border")).hitOnError);
		assertTrue(((MyBorder)tester.getLastRenderedPage().get("border")).hitOnSubmit);
	}

	/**
	 * wicket-2202
	 */
	public void testWithPanelAjax()
	{
		tester.executeAjaxEvent("form3:submit", "onclick");

		HomePage page = (HomePage)tester.getLastRenderedPage();
		assertFalse((page.getFormSubmitted() & HomePage.AJAX) == HomePage.AJAX);
	}

	/**
	 * wicket-2202
	 */
	public void testWithPanelForm()
	{
		tester.clickLink("form3:submit2");
		HomePage page = (HomePage)tester.getLastRenderedPage();
		assertFalse((page.getFormSubmitted() & HomePage.NORMAL) == HomePage.NORMAL);
	}

	public void test_2589() throws Exception
	{
		tester = new WicketTester();
		tester.startPage(HomePage1.class);
		tester.assertRenderedPage(HomePage1.class);
		tester.assertResultPage(getClass(), "HomePage1_ExpectedResult.html");
	}
}