error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13213.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13213.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13213.java
text:
```scala
n@@ew Locale("th", "TH"), new Locale("ru"), new Locale("ko", "KR") });

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
package org.apache.wicket.examples.forminput;

import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.SharedResources;
import org.apache.wicket.examples.WicketExampleApplication;
import org.apache.wicket.markup.html.image.resource.DefaultButtonImageResource;
import org.apache.wicket.protocol.http.WebSession;


/**
 * Application class for form input example.
 * 
 * @author Eelco Hillenius
 */
public class FormInputApplication extends WicketExampleApplication
{
	/** Relevant locales wrapped in a list. */
	public static final List LOCALES = Arrays.asList(new Locale[] { Locale.ENGLISH,
			new Locale("nl", "NL"), Locale.GERMAN, Locale.SIMPLIFIED_CHINESE, Locale.JAPANESE,
			new Locale("pt", "BR"), new Locale("fa", "IR"), new Locale("da", "DK"),
			new Locale("th", "TH"), new Locale("ru") });

	/**
	 * Constructor.
	 */
	public FormInputApplication()
	{
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	public Class getHomePage()
	{
		return FormInput.class;
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.Request,
	 *      org.apache.wicket.Response)
	 */
	@Override
	public Session newSession(Request request, Response response)
	{
		WebSession session = new WebSession(request);
		Locale locale = session.getLocale();
		if (!LOCALES.contains(locale))
		{
			session.setLocale(Locale.ENGLISH);
		}
		return session;
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	protected void init()
	{
		getResourceSettings().setThrowExceptionOnMissingResource(false);

		// Chinese buttons
		Font font = new Font("SimSun", Font.BOLD, 16);
		DefaultButtonImageResource imgSave = new DefaultButtonImageResource("\u4FDD\u5B58");
		imgSave.setFont(font);
		DefaultButtonImageResource imgReset = new DefaultButtonImageResource("\u91CD\u7F6E");
		imgReset.setFont(font);
		SharedResources sharedResources = getSharedResources();
		sharedResources.add("save", Locale.SIMPLIFIED_CHINESE, imgSave);
		sharedResources.add("reset", Locale.SIMPLIFIED_CHINESE, imgReset);

		// Japanese buttons
		Font fontJa = new Font("Serif", Font.BOLD, 16);
		DefaultButtonImageResource imgSaveJa = new DefaultButtonImageResource("\u4fdd\u5b58");
		imgSaveJa.setFont(fontJa);
		DefaultButtonImageResource imgResetJa = new DefaultButtonImageResource(
				"\u30ea\u30bb\u30c3\u30c8");
		imgResetJa.setFont(fontJa);
		sharedResources.add("save", Locale.JAPANESE, imgSaveJa);
		sharedResources.add("reset", Locale.JAPANESE, imgResetJa);

		// Persian buttons
		Font fontFa = new Font("Serif", Font.BOLD, 16);
		Locale farsi = new Locale("fa", "IR");
		DefaultButtonImageResource imgSaveFa = new DefaultButtonImageResource(
				"\u0630\u062e\u064a\u0631\u0647");
		imgSaveFa.setFont(fontFa);
		DefaultButtonImageResource imgResetFa = new DefaultButtonImageResource(
				"\u0628\u0627\u0632\u0646\u0634\u0627\u0646\u064a");
		imgResetFa.setFont(fontFa);
		getSharedResources().add("save", farsi, imgSaveFa);
		getSharedResources().add("reset", farsi, imgResetFa);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13213.java