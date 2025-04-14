error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8286.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8286.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8286.java
text:
```scala
final C@@omponentTag tag = nextComponentTag();

/*
 * $Id: HtmlProblemFinder.java 4594 2006-02-21 18:20:55 +0000 (Tue, 21 Feb 2006)
 * jdonnerstag $ $Revision$ $Date: 2006-02-21 18:20:55 +0000 (Tue, 21 Feb
 * 2006) $
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
package wicket.markup.parser.filter;

import java.text.ParseException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.markup.ComponentTag;
import wicket.markup.MarkupElement;
import wicket.markup.parser.AbstractMarkupFilter;

/**
 * This is a markup inline filter which by default is not added to the list of
 * markup filter. It can be added by means of subclassing
 * Application.newMarkupParser() like
 * 
 * <pre>
 *        public class MyApplication extends Application
 *        {
 *            ...
 *            public IMarkupFilter[] getAdditionalMarkupHandler()
 *            {
 *                return new IMarkupFilter[] { new HtmlProblemFinder(HtmlProblemFinder.ERR_THROW_EXCEPTION) };
 *            }
 * </pre>
 * 
 * The purpose of the filter is to find possible HTML issues and to log a
 * warning.
 * 
 * @author Juergen Donnerstag
 */
public final class HtmlProblemFinder extends AbstractMarkupFilter
{
	/** Logging */
	private static final Log log = LogFactory.getLog(HtmlProblemFinder.class);

	/** Ignore the issue detected */
	public static final int ERR_INGORE = 3;

	/** Log a warning on the issue detected */
	public static final int ERR_LOG_WARN = 2;

	/** Log an error on the issue detected */
	public static final int ERR_LOG_ERROR = 1;

	/** Throw an exception on the issue detected */
	public static final int ERR_THROW_EXCEPTION = 0;

	/** Default behavior in case of a potential HTML issue detected */
	private final int problemEscalation;

	/**
	 * Construct.
	 * 
	 * @param problemEscalation
	 *            How to escalate the issue found.
	 */
	public HtmlProblemFinder(final int problemEscalation)
	{
		super(null);

		this.problemEscalation = problemEscalation;
	}

	/**
	 * Get the next MarkupElement from the parent MarkupFilter and handle it if
	 * the specific filter criteria are met. Depending on the filter, it may
	 * return the MarkupElement unchanged, modified or it remove by asking the
	 * parent handler for the next tag.
	 * 
	 * @see wicket.markup.parser.IMarkupFilter#nextTag()
	 * @return Return the next eligible MarkupElement
	 */
	public MarkupElement nextTag() throws ParseException
	{
		// Get the next tag. If null, no more tags are available
		final ComponentTag tag = (ComponentTag)getParent().nextTag();
		if (tag == null)
		{
			return tag;
		}

		// Make sure some typical and may be tricky problems are detected and
		// logged.
		if ("img".equals(tag.getName()) && (tag.isOpen() || tag.isOpenClose()))
		{
			String src = tag.getAttributes().getString("src");
			if ((src != null) && (src.trim().length() == 0))
			{
				escalateWarning("Attribute 'src' should not be empty. Location: ", tag);
			}
		}

		// Some people are using a dot "wicket.xxx" instead of a colon
		// "wicket:xxx"
		Iterator iter = tag.getAttributes().keySet().iterator();
		while (iter.hasNext())
		{
			String key = (String)iter.next();
			if (key != null)
			{
				key = key.toLowerCase();
				if (key.startsWith("wicket."))
				{
					escalateWarning(
							"You probably want 'wicket:xxx' rather than 'wicket.xxx'. Location: ",
							tag);
				}
			}
		}

		return tag;
	}

	/**
	 * Handle the issue. Depending the setting either log a warning, an error,
	 * throw an exception or ignore it.
	 * 
	 * @param msg
	 *            The message
	 * @param tag
	 *            The current tag
	 * @throws ParseException
	 */
	private void escalateWarning(final String msg, final ComponentTag tag) throws ParseException
	{
		if (problemEscalation == ERR_LOG_WARN)
		{
			log.warn(msg + tag.toUserDebugString());
		}
		else if (problemEscalation == ERR_LOG_ERROR)
		{
			log.error(msg + tag.toUserDebugString());
		}
		else if (problemEscalation == ERR_INGORE)
		{
			// no action required
		}
		else
		// if (problemEscalation == ERR_THROW_EXCEPTION)
		{
			throw new ParseException(msg + tag.toUserDebugString(), tag.getPos());
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8286.java