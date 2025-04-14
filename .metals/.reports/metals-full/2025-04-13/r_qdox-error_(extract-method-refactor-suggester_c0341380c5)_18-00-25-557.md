error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/28.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/28.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/28.java
text:
```scala
public F@@eedbackMessagesModel(Page< ? > page, IFeedbackMessageFilter filter)

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
package org.apache.wicket.feedback;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;


/**
 * Model for extracting feedback messages.
 * 
 * @author Eelco Hillenius
 */
public class FeedbackMessagesModel implements IModel<List<FeedbackMessage>>
{
	private static final long serialVersionUID = 1L;

	/** Message filter */
	private IFeedbackMessageFilter filter;

	/** Lazy loaded, temporary list. */
	private transient List<FeedbackMessage> messages;

	/** Comparator used for sorting the messages. */
	private Comparator<FeedbackMessage> sortingComparator;

	/**
	 * Constructor. Creates a model for all feedback messages on the page.
	 * 
	 * @param component
	 *            The component where the page will be get from for which messages will be displayed
	 *            usually the same page as the one feedbackpanel is attached to
	 */
	public FeedbackMessagesModel(Component< ? > component)
	{
		if (component == null)
		{
			throw new IllegalArgumentException("Argument [[page]] cannot be null");
		}
	}

	/**
	 * Constructor. Creates a model for all feedback messages accepted by the given filter.
	 * 
	 * @param filter
	 *            The filter to apply
	 * @param page
	 *            Page for which messages will be displayed - usually the same page as the one
	 *            feedbackpanel is attached to
	 * 
	 */
	public FeedbackMessagesModel(Page page, IFeedbackMessageFilter filter)
	{
		this(page);
		setFilter(filter);
	}

	/**
	 * @return The current message filter
	 */
	public final IFeedbackMessageFilter getFilter()
	{
		return filter;
	}

	/**
	 * @return The current sorting comparator
	 */
	public final Comparator<FeedbackMessage> getSortingComparator()
	{
		return sortingComparator;
	}

	/**
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	public final List<FeedbackMessage> getObject()
	{
		if (messages == null)
		{
			// Get filtered messages from page where component lives
			messages = Session.get().getFeedbackMessages().messages(filter);

			// Sort the list before returning it
			if (sortingComparator != null)
			{
				Collections.sort(messages, sortingComparator);
			}

			// Let subclass do any extra processing it wants to on the messages.
			// It may want to do something special, such as removing a given
			// message under some special condition or perhaps eliminate
			// duplicate messages. It could even add a message under certain
			// conditions.
			messages = processMessages(messages);
		}
		return messages;
	}

	/**
	 * @param filter
	 *            Filter to apply to model
	 * @return this
	 */
	public final FeedbackMessagesModel setFilter(IFeedbackMessageFilter filter)
	{
		this.filter = filter;
		return this;
	}

	/**
	 * Sets the comparator used for sorting the messages.
	 * 
	 * @param sortingComparator
	 *            comparator used for sorting the messages
	 * @return this
	 */
	public final FeedbackMessagesModel setSortingComparator(
		Comparator<FeedbackMessage> sortingComparator)
	{
		if (!(sortingComparator instanceof Serializable))
		{
			throw new IllegalArgumentException("sortingComparator must be serializable");
		}
		this.sortingComparator = sortingComparator;
		return this;
	}

	/**
	 * Override this method to post process to the FeedbackMessage list.
	 * 
	 * @param messages
	 *            List of sorted and filtered FeedbackMessages for further processing
	 * @return The processed FeedbackMessage list
	 */
	protected List<FeedbackMessage> processMessages(final List<FeedbackMessage> messages)
	{
		return messages;
	}

	/**
	 * 
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	public void setObject(List<FeedbackMessage> object)
	{
	}

	/**
	 * 
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	public void detach()
	{
		messages = null;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/28.java