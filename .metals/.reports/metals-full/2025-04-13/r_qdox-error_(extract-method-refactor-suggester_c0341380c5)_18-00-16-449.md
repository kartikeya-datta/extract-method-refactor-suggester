error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1762.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1762.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1762.java
text:
```scala
p@@ublic static class WeatherStation implements Serializable

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
package org.apache.wicket.model;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Locale;

import junit.framework.Assert;

import org.apache.wicket.WicketTestCase;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.MockPage;
import org.apache.wicket.resource.loader.BundleStringResourceLoader;

/**
 * Test cases for the <code>StringResourceModel</code> class.
 * 
 * @author Chris Turner
 */
public class StringResourceModelTest extends WicketTestCase
{
	private WebPage page;

	private WeatherStation ws;

	private IModel<WeatherStation> wsModel;

	/**
	 * Create the test case.
	 * 
	 * @param name
	 *            The test name
	 */
	public StringResourceModelTest(String name)
	{
		super(name);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		tester.getApplication()
			.getResourceSettings()
			.getStringResourceLoaders()
			.add(new BundleStringResourceLoader("org.apache.wicket.model.StringResourceModelTest"));
		page = new MockPage();
		ws = new WeatherStation();
		wsModel = new Model<WeatherStation>(ws);
	}


	/**
	 * 
	 * 
	 */
	public void testGetSimpleResource()
	{
		StringResourceModel model = new StringResourceModel("simple.text", page, null);
		Assert.assertEquals("Text should be as expected", "Simple text", model.getString());
		Assert.assertEquals("Text should be as expected", "Simple text", model.getObject());
// Assert.assertEquals("Text should be as expected", "Simple text", model.toString());
	}

	/**
	 * 
	 * 
	 */
	public void testNullResourceKey()
	{
		try
		{
			new StringResourceModel(null, page, null);
			Assert.fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// Expected result
		}
	}

	/**
	 * 
	 * 
	 */
	public void testGetSimpleResourceWithKeySubstitution()
	{
		StringResourceModel model = new StringResourceModel("weather.${currentStatus}", page,
			wsModel);
		Assert.assertEquals("Text should be as expected", "It's sunny, wear sunscreen",
			model.getString());
		ws.setCurrentStatus("raining");
		Assert.assertEquals("Text should be as expected", "It's raining, take an umbrella",
			model.getString());
	}

	/**
	 * 
	 * 
	 */
	public void testGetPropertySubstitutedResource()
	{
		StringResourceModel model = new StringResourceModel("weather.message", page, wsModel);
		Assert.assertEquals(
			"Text should be as expected",
			"Weather station \"Europe's main weather station\" reports that the temperature is 25.7 \u00B0C",
			model.getString());
		ws.setCurrentTemperature(11.5);
		Assert.assertEquals(
			"Text should be as expected",
			"Weather station \"Europe's main weather station\" reports that the temperature is 11.5 \u00B0C",
			model.getString());
	}

	/**
	 * 
	 * 
	 */
	public void testSubstitutedPropertyAndParameterResource()
	{
		StringResourceModel model = new StringResourceModel("weather.mixed", page, wsModel,
			new Object[] { new PropertyModel<Double>(wsModel, "currentTemperature"),
					new PropertyModel<String>(wsModel, "units") });
		MessageFormat format = new MessageFormat(
			"Weather station \"Europe''s main weather station\" reports that the temperature is {0} {1}");

		ws.setCurrentTemperature(25.7);
		String expected = format.format(new Object[] { 25.7, "\u00B0C" });
		Assert.assertEquals("Text should be as expected", expected, model.getString());

		ws.setCurrentTemperature(11.5);
		expected = format.format(new Object[] { 11.5, "\u00B0C" });
		Assert.assertEquals("Text should be as expected", expected, model.getString());
	}

	/**
	 * 
	 * 
	 */
	public void testSubstitutionParametersResource()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(2004, Calendar.OCTOBER, 15, 13, 21);
		MessageFormat format = new MessageFormat(
			"The report for {0,date,medium}, shows the temperature as {2,number,###.##} {3} and the weather to be {1}",
			page.getLocale());
		StringResourceModel model = new StringResourceModel("weather.detail", page, wsModel,
			new Object[] { cal.getTime(), "${currentStatus}",
					new PropertyModel<Double>(wsModel, "currentTemperature"),
					new PropertyModel<String>(wsModel, "units") });
		String expected = format.format(new Object[] { cal.getTime(), "sunny", 25.7, "\u00B0C" });
		Assert.assertEquals("Text should be as expected", expected, model.getString());
		ws.setCurrentStatus("raining");
		ws.setCurrentTemperature(11.568);
		expected = format.format(new Object[] { cal.getTime(), "raining", 11.568, "\u00B0C" });
		Assert.assertEquals("Text should be as expected", expected, model.getString());
	}


	public void testSubstitutionParametersResourceWithSingleQuote() throws Exception
	{
		tester.getSession().setLocale(Locale.ENGLISH);
		StringResourceModel model = new StringResourceModel("with.quote", page, null, new Object[] {
				10, 20 });
		assertEquals("2010.00", model.getString());

	}


	/**
	 * 
	 */
	public void testSetObject()
	{
		try
		{
			StringResourceModel model = new StringResourceModel("simple.text", page, null);
			model.setObject("Some value");
			Assert.fail("UnsupportedOperationException expected");
		}
		catch (Exception e)
		{
			if (!(e instanceof UnsupportedOperationException || e.getCause() instanceof UnsupportedOperationException))
			{
				Assert.fail("UnsupportedOperationException expected");
			}
			// Expected result
		}
	}


	/**
	 * @throws Exception
	 */
	public void testDetachAttachDetachableModel() throws Exception
	{
		IModel<WeatherStation> wsDetachModel = new LoadableDetachableModel<WeatherStation>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected WeatherStation load()
			{
				return new WeatherStation();
			}


		};
		StringResourceModel model = new StringResourceModel("simple.text", page, wsDetachModel);
		model.getObject();
		Assert.assertNotNull(model.getLocalizer());
		model.detach();

	}

	/**
	 * Inner class used for testing.
	 */
	public class WeatherStation implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private final String name = "Europe's main weather station";

		private String currentStatus = "sunny";

		private double currentTemperature = 25.7;

		/**
		 * @return status
		 */
		public String getCurrentStatus()
		{
			return currentStatus;
		}

		/**
		 * @param currentStatus
		 */
		public void setCurrentStatus(String currentStatus)
		{
			this.currentStatus = currentStatus;
		}

		/**
		 * @return current temp
		 */
		public double getCurrentTemperature()
		{
			return currentTemperature;
		}

		/**
		 * @param currentTemperature
		 */
		public void setCurrentTemperature(double currentTemperature)
		{
			this.currentTemperature = currentTemperature;
		}

		/**
		 * @return units
		 */
		public String getUnits()
		{
			return "\u00B0C";
		}

		/**
		 * @return name
		 */
		public String getName()
		{
			return name;
		}
	}

}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1762.java