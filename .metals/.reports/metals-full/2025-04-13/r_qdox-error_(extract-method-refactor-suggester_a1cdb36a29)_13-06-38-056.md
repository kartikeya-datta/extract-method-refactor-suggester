error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11361.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11361.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11361.java
text:
```scala
p@@rivate static class MyMockPage extends WebPage

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
package org.apache.wicket;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

import junit.framework.Assert;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.resource.DummyApplication;
import org.apache.wicket.resource.loader.ComponentStringResourceLoader;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.tester.WicketTester;
import org.apache.wicket.util.value.ValueMap;

/**
 * Test cases for the <code>Localizer</code> class.
 * 
 * @author Chris Turner
 */
public class LocalizerTest extends WicketTestCase
{

	private static class MyMockPage extends WebPage<Void>
	{
		private static final long serialVersionUID = 1L;

		DropDownChoice drop1;
		DropDownChoice drop2;

		/**
		 * Construct.
		 */
		public MyMockPage()
		{
			final Form form = new Form("form");
			add(form);

			String[] choices = { "choice1", "choice2" };
			drop1 = new DropDownChoice("drop1", Arrays.asList(choices));
			drop2 = new DropDownChoice("drop2", Arrays.asList(choices));

			form.add(drop1);
			form.add(drop2);
		}
	}

	private IResourceSettings settings;

	protected Localizer localizer;

	/**
	 * Create the test case.
	 * 
	 * @param message
	 *            The test name
	 */
	public LocalizerTest(String message)
	{
		super(message);
	}

	/**
	 * 
	 * @throws Exception
	 */
	protected void setUp() throws Exception
	{
		tester = new WicketTester(new DummyApplication());
		settings = tester.getApplication().getResourceSettings();
		localizer = tester.getApplication().getResourceSettings().getLocalizer();
	}

	/**
	 * 
	 */
	public void testGetStringValidString()
	{
		Assert.assertEquals("Expected string should be returned", "This is a test",
			localizer.getString("test.string", null, null, "DEFAULT"));
	}

	/**
	 * 
	 */
	public void testGetStringMissingStringReturnDefault()
	{
		settings.setUseDefaultOnMissingResource(true);
		Assert.assertEquals("Default string should be returned", "DEFAULT", localizer.getString(
			"unknown.string", null, null, "DEFAULT"));
	}

	/**
	 * 
	 */
	public void testGetStringMissingStringNoDefault()
	{
		settings.setUseDefaultOnMissingResource(true);
		settings.setThrowExceptionOnMissingResource(false);

		Assert.assertEquals("Wrapped key should be returned on no default",
			"[Warning: String resource for 'unknown.string' not found]", localizer.getString(
				"unknown.string", null, null, null));
	}

	/**
	 * 
	 */
	public void testGetStringMissingStringDoNotUseDefault()
	{
		settings.setUseDefaultOnMissingResource(false);
		settings.setThrowExceptionOnMissingResource(false);
		Assert.assertEquals("Wrapped key should be returned on not using default and no exception",
			"[Warning: String resource for 'unknown.string' not found]", localizer.getString(
				"unknown.string", null, null, "DEFAULT"));
	}

	/**
	 * 
	 */
	public void testGetStringMissingStringExceptionThrown()
	{
		settings.setUseDefaultOnMissingResource(false);
		settings.setThrowExceptionOnMissingResource(true);
		try
		{
			localizer.getString("unknown.string", null, null, "DEFAULT");
			Assert.fail("MissingResourceException expected");
		}
		catch (MissingResourceException e)
		{
			// Expected result
		}
	}

	/**
	 * 
	 */
	public void testGetStringPropertySubstitution()
	{
		ValueMap vm = new ValueMap();
		vm.put("user", "John Doe");
		Model model = new Model(vm);
		Assert.assertEquals("Property substitution should occur", "Welcome, John Doe",
			localizer.getString("test.substitute", null, model, null));
	}

	/**
	 * 
	 */
	public void testInComponentConstructor()
	{
		Component myComponent = new MyLabel("myLabel");
	}

	/**
	 * Unit test for bug number [1416582] Resource loading caches wrong.
	 */
	public void testTwoComponents()
	{
		Session.get().setLocale(Locale.ENGLISH);
		MyMockPage page = new MyMockPage();
		Application.get().getResourceSettings().addStringResourceLoader(
			new ComponentStringResourceLoader());

		Localizer localizer = Application.get().getResourceSettings().getLocalizer();
		assertEquals("value 1", localizer.getString("null", page.drop1));
		assertEquals("value 2", localizer.getString("null", page.drop2));

		Session.get().setLocale(new Locale("nl"));
		assertEquals("waarde 1", localizer.getString("null", page.drop1));
		assertEquals("waarde 2", localizer.getString("null", page.drop2));

	}

	/**
	 * 
	 */
	public void testGetStringUseModel()
	{
		HashMap model = new HashMap();
		model.put("user", "juergen");

		Assert.assertEquals("Expected string should be returned", "Welcome, juergen",
			localizer.getString("test.substitute", null, new PropertyModel(model, null),
				"DEFAULT {user}"));

		Assert.assertEquals("Expected string should be returned", "DEFAULT juergen",
			localizer.getString("test.substituteDoesNotExist", null,
				new PropertyModel(model, null), "DEFAULT ${user}"));
	}

	/**
	 * Test label.
	 */
	public static class MyLabel extends Label
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * 
		 * @param id
		 */
		public MyLabel(final String id)
		{
			super(id);

			Localizer localizer = Application.get().getResourceSettings().getLocalizer();

			// should work properly in a component constructor (without parent)
			// as well
			Assert.assertEquals("Expected string should be returned", "This is a test",
				localizer.getString("test.string", this, "DEFAULT"));

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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11361.java