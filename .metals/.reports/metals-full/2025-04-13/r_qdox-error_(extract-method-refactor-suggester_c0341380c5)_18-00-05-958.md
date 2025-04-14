error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12353.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12353.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12353.java
text:
```scala
static C@@harSequence smartLink(final CharSequence text)

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.extensions.markup.html.basic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.basic.Label;
import wicket.model.IModel;

/**
 * If you have email addresses or web URLs in the data that you are displaying,
 * then you can automatically display those pieces of data as hyperlinks, you
 * will not have to take any action to convert that data.
 * <p>
 * Email addresses will be wrapped with a &lt;a
 * href="mailto:xxx"&gt;xxx&lt;/a&gt; tag, where "xxx" is the email address that
 * was detected.
 * <p>
 * Web URLs will be wrapped with a &lt;a href="xxx"&gt;xxx&lt;/a&gt; tag, where
 * "xxx" is the URL that was detected (it can be any valid URL type, http://,
 * https://, ftp://, etc...)
 * 
 * @author Juergen Donnerstag
 */
public final class SmartLinkLabel extends Label
{
	private static final long serialVersionUID = 1L;

	/** Email address pattern */
	private static final Pattern emailPattern = Pattern.compile("[\\w\\.-]+@[\\w\\.-]+",
			Pattern.DOTALL);

	private static final String emailReplacePattern = "<a href=\"mailto:$0\">$0</a>";

	/** URL pattern */
	private static final Pattern urlPattern = Pattern.compile(
			"([a-zA-Z]+://[\\w\\.\\-\\:\\/]+)[\\w\\.:\\-/?&=%]*", Pattern.DOTALL);

	private static final String urlReplacePattern = "<a href=\"$0\">$1</a>";

	/**
	 * @see Label#Label(String, String)
	 */
	public SmartLinkLabel(String name, String label)
	{
		super(name, label);
	}

	/**
	 * @see Label#Label(String, IModel)
	 */
	public SmartLinkLabel(String name, IModel model)
	{
		super(name, model);
	}

	/**
	 * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream,
	 *      wicket.markup.ComponentTag)
	 */
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		replaceComponentTagBody(markupStream, openTag, smartLink(getModelObjectAsString()));
	}

	/**
	 * Replace all email and URL addresses
	 * 
	 * @param text
	 *            Text to be modified
	 * @return Modified Text
	 */
	static String smartLink(final String text)
	{
		if (text == null)
		{
			return text;
		}

		Matcher matcher = emailPattern.matcher(text);
		String work = matcher.replaceAll(emailReplacePattern);

		matcher = urlPattern.matcher(work);
		work = matcher.replaceAll(urlReplacePattern);

		return work;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12353.java