error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3003.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3003.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3003.java
text:
```scala
private static final S@@tring emailPattern = "[\\w\\.-\\\\+]+@[\\w\\.-]+";

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
package org.apache.wicket.extensions.markup.html.basic;

import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * This implementation adds link render strategies for email addresses and urls.
 * 
 * @see SmartLinkLabel
 * @see SmartLinkMultiLineLabel
 * 
 * @author Gerolf Seitz
 */
public class DefaultLinkParser extends LinkParser
{
	/** Email address pattern */
	private static final String emailPattern = "[\\w\\.-]+@[\\w\\.-]+";

	/** URL pattern */
	private static final String urlPattern = "([a-zA-Z]+://[\\w\\.\\-\\:\\/~]+)[\\w\\.:\\-/?&=%]*";

	/**
	 * Email address render strategy.<br/>
	 * Renders &lt;a href="mailto:{EMAIL}"&gt;{EMAIL}&lt;/a&gt;
	 */
	public static final ILinkRenderStrategy EMAIL_RENDER_STRATEGY = new ILinkRenderStrategy()
	{
		public String buildLink(String linkTarget)
		{
			return "<a href=\"mailto:" + linkTarget + "\">" + linkTarget + "</a>";
		}
	};

	/**
	 * Email address render strategy. Similar to <code>EMAIL_RENDER_STRATEGY</code>, but encrypts
	 * the email address with html entities.
	 */
	public static final ILinkRenderStrategy ENCRYPTED_EMAIL_RENDER_STRATEGY = new ILinkRenderStrategy()
	{
		public String buildLink(String linkTarget)
		{
			AppendingStringBuffer cryptedEmail = new AppendingStringBuffer(64);
			for (int i = 0; i < linkTarget.length(); i++)
			{
				cryptedEmail.append("&#");
				cryptedEmail.append(Integer.toString(linkTarget.charAt(i)));
				cryptedEmail.append(";");
			}

			AppendingStringBuffer result = new AppendingStringBuffer(256);
			result.append("<a href=\"mailto:");
			result.append(cryptedEmail.toString());
			result.append("\">");
			result.append(cryptedEmail.toString());
			result.append("</a>");

			return result.toString();
		}
	};

	/**
	 * Url render strategy.<br/>
	 * Renders &lt;a href="{URL}"&gt;{URL}&lt;/a&gt;
	 */
	public static final ILinkRenderStrategy URL_RENDER_STRATEGY = new ILinkRenderStrategy()
	{
		public String buildLink(String linkTarget)
		{
			return "<a href=\"" +
				linkTarget +
				"\">" +
				(linkTarget.indexOf('?') == -1 ? linkTarget : linkTarget.substring(0,
					linkTarget.indexOf('?'))) + "</a>";
		}
	};

	/**
	 * Default constructor.
	 */
	public DefaultLinkParser()
	{
		addLinkRenderStrategy(emailPattern, EMAIL_RENDER_STRATEGY);
		addLinkRenderStrategy(urlPattern, URL_RENDER_STRATEGY);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3003.java