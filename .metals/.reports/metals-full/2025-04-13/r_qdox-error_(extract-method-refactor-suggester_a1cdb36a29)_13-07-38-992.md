error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10406.java
text:
```scala
public static final b@@oolean enable = true;

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
package wicket.markup.parser.filter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import wicket.Application;
import wicket.markup.ComponentTag;
import wicket.markup.ContainerInfo;
import wicket.markup.MarkupElement;
import wicket.markup.parser.AbstractMarkupFilter;
import wicket.settings.IResourceSettings;

/**
 * THIS IS EXPERIMENTAL ONLY AND DISABLED BY DEFAULT
 * <p>
 * This is a markup inline filter. It identifies wicket:message attributes and
 * replaces the attributes referenced. E.g. wicket:message="value=key" would
 * replace or add the attribute "value" with the message associated with "key".
 * 
 * @author Juergen Donnerstag
 */
public final class WicketMessageTagHandler extends AbstractMarkupFilter
{
	/** Logging */
	// private final static Log log = LogFactory.getLog(WicketMessageTagHandler.class);

	/** TODO Post 1.2: General: Namespace should not be a constant */
	private final static String WICKET_MESSAGE_ATTRIBUTE_NAME = "wicket:message";

	/**
	 * globally enable wicket:message; If accepted by user, we should use an
	 * apps setting
	 */
	public static boolean enable = false;

	/**
	 * The MarkupContainer requesting the information incl. class, locale and
	 * style
	 */
	private final ContainerInfo containerInfo;

	/** temporary storage unomdified while the object instance exists */
	private final List searchStack;

	/**
	 * The application settings required. Note: you can rely on
	 * Application.get().getResourceSettings() as reading the markup happens in
	 * another thread due to ModificationWatcher.
	 */
	private IResourceSettings settings;

	/**
	 * Construct.
	 * 
	 * @param containerInfo
	 *            The container requesting the current markup incl class, style
	 *            and locale
	 */
	public WicketMessageTagHandler(final ContainerInfo containerInfo)
	{
		this.containerInfo = containerInfo;
		this.settings = Application.get().getResourceSettings();

		this.searchStack = new ArrayList();
		searchStack.add(containerInfo.getContainerClass());
	}

	/**
	 * 
	 * @see wicket.markup.parser.IMarkupFilter#nextTag()
	 * @return The next tag to be processed. Null, if not more tags are
	 *         available
	 */
	public final MarkupElement nextTag() throws ParseException
	{
		// Get the next tag from the next MarkupFilter in the chain
		// If null, no more tags are available
		final ComponentTag tag = (ComponentTag)getParent().nextTag();
		if (tag == null)
		{
			return tag;
		}

		final String wicketMessageAttribute = tag.getAttributes().getString(
				WICKET_MESSAGE_ATTRIBUTE_NAME);
		if ((wicketMessageAttribute != null) && (wicketMessageAttribute.trim().length() > 0))
		{
			if (this.containerInfo == null)
			{
				throw new ParseException(
						"Found "
								+ WICKET_MESSAGE_ATTRIBUTE_NAME
								+ " but the message can not be resolved, because the associated Page is not known."
								+ " This might be caused by using the wrong MarkupParser constructor",
						tag.getPos());
			}

			StringTokenizer attrTokenizer = new StringTokenizer(wicketMessageAttribute, ",");
			while (attrTokenizer.hasMoreTokens())
			{
				String text = attrTokenizer.nextToken().trim();
				if (text == null)
				{
					text = wicketMessageAttribute;
				}

				StringTokenizer valueTokenizer = new StringTokenizer(text, "=");
				if (valueTokenizer.countTokens() != 2)
				{
					throw new ParseException("Wrong format of wicket:message attribute value. "
							+ text + "; Must be: key=value[, key=value]", tag.getPos());
				}

				String attrName = valueTokenizer.nextToken();
				String messageKey = valueTokenizer.nextToken();
				if ((attrName == null) || (attrName.trim().length() == 0) || (messageKey == null)
 (messageKey.trim().length() == 0))
				{
					throw new ParseException("Wrong format of wicket:message attribute value. "
							+ text + "; Must be: key=value[, key=value]", tag.getPos());
				}

				String value = settings.getLocalizer().getString(messageKey, null, searchStack,
						containerInfo.getLocale(), containerInfo.getStyle());

				if (value != null && (value.length() > 0))
				{
					tag.getAttributes().put(attrName, value);
					tag.setModified(true);
				}
				else if (tag.getAttributes().get(attrName) == null)
				{
					tag.getAttributes().put(attrName, value);
					tag.setModified(true);
				}
				else
				{
					// Do not modify the existing value
				}
			}
		}

		return tag;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10406.java