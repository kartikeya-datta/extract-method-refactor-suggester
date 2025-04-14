error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5489.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5489.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5489.java
text:
```scala
p@@age.add(new Link<Void>(MockPageWithLinkAndComponent.LINK_ID)

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
package org.apache.wicket.ajax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.MockPageWithLinkAndComponent;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Tests that an AbstractAjaxTimerBehavior injects itself into the markup once and only once. Also
 * tests the callback URL to make sure the timer reinjects itself
 * 
 * @author Jim McLaughlin
 */
public class AjaxTimerBehaviorTest extends WicketTestCase
{
	private static final Logger log = LoggerFactory.getLogger(AjaxTimerBehaviorTest.class);

	/**
	 * Tests timer behavior in a component added to an AjaxRequestTarget
	 */
	public void testAddToAjaxUpdate()
	{
		Duration dur = Duration.seconds(20);
		final MyAjaxSelfUpdatingTimerBehavior timer = new MyAjaxSelfUpdatingTimerBehavior(dur);
		final MockPageWithLinkAndComponent page = new MockPageWithLinkAndComponent();

		page.add(new WebComponent(MockPageWithLinkAndComponent.COMPONENT_ID).setOutputMarkupId(true));


		page.add(new AjaxLink<Void>(MockPageWithLinkAndComponent.LINK_ID)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				WebMarkupContainer wmc = new WebMarkupContainer(
					MockPageWithLinkAndComponent.COMPONENT_ID);
				wmc.setOutputMarkupId(true);
				wmc.add(timer);
				page.replace(wmc);
				target.add(wmc);
			}
		});

		tester.startPage(page);
		tester.clickLink(MockPageWithLinkAndComponent.LINK_ID);

		validate(timer, false);

	}


	/**
	 * tests timer behavior in a WebPage.
	 */
	public void testAddToWebPage()
	{
		Duration dur = Duration.seconds(20);
		final MyAjaxSelfUpdatingTimerBehavior timer = new MyAjaxSelfUpdatingTimerBehavior(dur);
		final MockPageWithLinkAndComponent page = new MockPageWithLinkAndComponent();
		Label label = new Label(MockPageWithLinkAndComponent.COMPONENT_ID, "Hello");
		page.add(label);
		page.add(new Link(MockPageWithLinkAndComponent.LINK_ID)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				// do nothing, link is just used to simulate a roundtrip
			}
		});
		label.setOutputMarkupId(true);
		label.add(timer);

		tester.startPage(page);

		validate(timer, true);

		tester.clickLink(MockPageWithLinkAndComponent.LINK_ID);

		validate(timer, true);

	}

	/**
	 * Validates the reponse, then makes sure the timer injects itself again when called.
	 * 
	 * @param timer
	 * @param inBodyOnLoad
	 */
	private void validate(MyAjaxSelfUpdatingTimerBehavior timer, boolean inBodyOnLoad)
	{
		String document = tester.getLastResponseAsString();

		String updateScript = timer.getUpdateScript();

		if (inBodyOnLoad)
		{
			String bodyOnLoadUpdateScript = "Wicket.Event.add(window, \"load\", function(event) { " +
				updateScript + ";});";
			validateTimerScript(document, bodyOnLoadUpdateScript);
		}
		else
		{
			validateTimerScript(document, updateScript);
		}


		tester.executeBehavior(timer);


		if (inBodyOnLoad)
		{
			updateScript = timer.getUpdateScript();
		}

		// Validate the document
		document = tester.getLastResponseAsString();
		validateTimerScript(document, updateScript);
	}

	/**
	 * Checks that the timer javascript is in the document once and only once
	 * 
	 * @param document
	 *            the response from the Application
	 * @param updateScript
	 *            the timer script
	 */
	private void validateTimerScript(String document, String updateScript)
	{
		log.debug(document);
		String quotedRegex;
		quotedRegex = quote(updateScript);
		Pattern pat = Pattern.compile(quotedRegex, Pattern.DOTALL);
		Matcher mat = pat.matcher(document);

		int count = 0;
		while (mat.find())
		{
			++count;
		}
		// make sure there is only one match
		assertEquals("There should be 1 and only 1 script in the markup for this behavior," +
			"but " + count + " were found", 1, count);
	}

	// quick fix for JDK 5 method
	private static final String quote(String s)
	{
		int slashEIndex = s.indexOf("\\E");
		if (slashEIndex == -1)
		{
			return "\\Q" + s + "\\E";
		}

		StringBuilder sb = new StringBuilder(s.length() * 2);
		sb.append("\\Q");
		slashEIndex = 0;
		int current = 0;
		while ((slashEIndex = s.indexOf("\\E", current)) != -1)
		{
			sb.append(s.substring(current, slashEIndex));
			current = slashEIndex + 2;
			sb.append("\\E\\\\E\\Q");
		}
		sb.append(s.substring(current, s.length()));
		sb.append("\\E");
		return sb.toString();
	}

	static class MyAjaxSelfUpdatingTimerBehavior extends AjaxSelfUpdatingTimerBehavior
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final Duration duration;
		String updateScript;

		/**
		 * Construct.
		 * 
		 * @param updateInterval
		 */
		public MyAjaxSelfUpdatingTimerBehavior(Duration updateInterval)
		{
			super(updateInterval);
			duration = updateInterval;
		}

		@Override
		protected void onComponentRendered()
		{
			super.onComponentRendered();
			updateScript = getJsTimeoutCall(duration);
		}

		/**
		 * @return Update script
		 */
		public String getUpdateScript()
		{
			return updateScript;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5489.java