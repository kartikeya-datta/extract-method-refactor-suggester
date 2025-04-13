error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13392.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13392.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13392.java
text:
```scala
t@@ester.assertErrorMessages(new String[] { "'foo' ist kein g\u00FCltiger Wert f\u00FCr 'Integer'." });

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
package org.apache.wicket.markup.html.form;

import java.util.Locale;

import org.apache.wicket.WicketTestCase;

/**
 * Test case for checking localized error messages.
 */
public class LocalizedErrorMessageTest extends WicketTestCase
{
	/**
	 * Test for checking if changing the session's locale to another language actually causes the
	 * feedback messages to be altered as well. Testcase for WICKET-891.
	 */
	public void testWICKET_891()
	{
		tester.setupRequestAndResponse();

		tester.getWicketSession().setLocale(new Locale("nl"));

		LocalizedMessagePage page = new LocalizedMessagePage();
		tester.startPage(page);
		tester.processRequestCycle();
		tester.setupRequestAndResponse();

		tester.getServletRequest().setRequestToComponent(page.form);
		tester.getServletRequest().setParameter(page.integerField.getInputName(), "foo");

		page.form.onFormSubmitted();

		tester.assertErrorMessages(new String[] { "'foo' in veld 'integer' moet een geheel getal zijn. " });
		tester.getWicketSession().setLocale(new Locale("us"));

		tester.getWicketSession().cleanupFeedbackMessages();

		tester.setupRequestAndResponse();

		page = new LocalizedMessagePage();
		tester.startPage(page);
		tester.processRequestCycle();
		tester.setupRequestAndResponse();

		tester.getServletRequest().setRequestToComponent(page.form);
		tester.getServletRequest().setParameter(page.integerField.getInputName(), "foo");

		page.form.onFormSubmitted();

		tester.assertErrorMessages(new String[] { "'foo' is not a valid Integer." });
	}

	/**
	 * WicketTester.assertErrorMessages returns FeedbackMessages in iso-8859-1 encoding only. Hence
	 * assertErrorMessage will fail for special characters in languages like e.g. German. Testcase
	 * for WICKET-1972.
	 * 
	 */
	public void testWICKET_1927()
	{
		tester.getApplication().getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		tester.setupRequestAndResponse();

		tester.getWicketSession().setLocale(new Locale("de"));

		LocalizedMessagePage page = new LocalizedMessagePage();
		tester.startPage(page);
		tester.processRequestCycle();
		tester.setupRequestAndResponse();

		tester.getServletRequest().setRequestToComponent(page.form);
		tester.getServletRequest().setParameter(page.integerField.getInputName(), "foo");

		page.form.onFormSubmitted();

		tester.assertErrorMessages(new String[] { "'foo' ist kein gültiger Wert für 'Integer'." });
		tester.getWicketSession().setLocale(new Locale("pl"));

		tester.getWicketSession().cleanupFeedbackMessages();

		tester.setupRequestAndResponse();

		page = new LocalizedMessagePage();
		tester.startPage(page);
		tester.processRequestCycle();
		tester.setupRequestAndResponse();

		tester.getServletRequest().setRequestToComponent(page.form);
		tester.getServletRequest().setParameter(page.integerField.getInputName(), "foo");

		page.form.onFormSubmitted();

		tester.assertErrorMessages(new String[] { "'foo' nie jest w\u0142a\u015Bciwym Integer." });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13392.java