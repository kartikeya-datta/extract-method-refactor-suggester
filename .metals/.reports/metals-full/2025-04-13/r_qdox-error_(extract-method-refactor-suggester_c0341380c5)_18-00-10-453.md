error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12159.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12159.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12159.java
text:
```scala
protected b@@oolean isLinkEnabled()

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
package org.apache.wicket.markup.html.link;

import org.apache.wicket.Application;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Base class that that contains functionality for rendering disabled links.
 * 
 * @author Matej Knopp
 */
public abstract class AbstractLink extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;

	/** this link's label/body. */
	private IModel<?> bodyModel;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public AbstractLink(String id)
	{
		this(id, null);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            the component id
	 * @param model
	 *            the link's model
	 */
	public AbstractLink(String id, IModel<?> model)
	{
		super(id, model);
	}

	/**
	 * Simple insertion string to allow disabled links to look like <i>Disabled link </i>.
	 */
	private String beforeDisabledLink;

	/**
	 * Simple insertion string to allow disabled links to look like <i>Disabled link </i>.
	 */
	private String afterDisabledLink;


	/**
	 * Sets the insertion string to allow disabled links to look like <i>Disabled link </i>.
	 * 
	 * @param afterDisabledLink
	 *            The insertion string
	 * @return this
	 */
	public AbstractLink setAfterDisabledLink(final String afterDisabledLink)
	{
		if (afterDisabledLink == null)
		{
			throw new IllegalArgumentException(
				"Value cannot be null.  For no text, specify an empty String instead.");
		}
		this.afterDisabledLink = afterDisabledLink;
		return this;
	}

	/**
	 * Gets the insertion string to allow disabled links to look like <i>Disabled link </i>.
	 * 
	 * @return The insertion string
	 */
	public String getAfterDisabledLink()
	{
		return afterDisabledLink;
	}

	/**
	 * Sets the insertion string to allow disabled links to look like <i>Disabled link </i>.
	 * 
	 * @param beforeDisabledLink
	 *            The insertion string
	 * @return this
	 */
	public AbstractLink setBeforeDisabledLink(final String beforeDisabledLink)
	{
		if (beforeDisabledLink == null)
		{
			throw new IllegalArgumentException(
				"Value cannot be null.  For no text, specify an empty String instead.");
		}
		this.beforeDisabledLink = beforeDisabledLink;
		return this;
	}

	/**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		// Set default for before/after link text
		if (beforeDisabledLink == null)
		{
			final Application app = getApplication();
			beforeDisabledLink = app.getMarkupSettings().getDefaultBeforeDisabledLink();
			afterDisabledLink = app.getMarkupSettings().getDefaultAfterDisabledLink();
		}
	}

	/**
	 * Gets the insertion string to allow disabled links to look like <i>Disabled link </i>.
	 * 
	 * @return The insertion string
	 */
	public String getBeforeDisabledLink()
	{
		return beforeDisabledLink;
	}

	/**
	 * Helper methods that both checks whether the link is enabled and whether the action ENABLE is
	 * allowed.
	 * 
	 * @return whether the link should be rendered as enabled
	 */
	protected final boolean isLinkEnabled()
	{
		return isEnabledInHierarchy();
	}

	/**
	 * Renders this link's body.
	 * 
	 * @param markupStream
	 *            the markup stream
	 * @param openTag
	 *            the open part of this tag
	 * @see org.apache.wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		// Draw anything before the body?
		if (!isLinkEnabled() && getBeforeDisabledLink() != null)
		{
			getResponse().write(getBeforeDisabledLink());
		}

		if ((bodyModel != null) && (bodyModel.getObject() != null))
		{
			replaceComponentTagBody(markupStream, openTag,
				getDefaultModelObjectAsString(bodyModel.getObject()));
		}
		else
		{
			// Render the body of the link
			super.onComponentTagBody(markupStream, openTag);
		}
		// Draw anything after the body?
		if (!isLinkEnabled() && getAfterDisabledLink() != null)
		{
			getResponse().write(getAfterDisabledLink());
		}
	}

	/**
	 * Alters the tag so that the link renders as disabled.
	 * 
	 * This method is meant to be called from {@link #onComponentTag(ComponentTag)} method of the
	 * derived class.
	 * 
	 * @param tag
	 */
	protected void disableLink(final ComponentTag tag)
	{
		// if the tag is an anchor proper
		if (tag.getName().equalsIgnoreCase("a") || tag.getName().equalsIgnoreCase("link") ||
			tag.getName().equalsIgnoreCase("area"))
		{
			// Change anchor link to span tag
			tag.setName("span");

			// Remove any href from the old link
			tag.remove("href");

			tag.remove("onclick");
		}
		// if the tag is a button or input
		else if ("button".equalsIgnoreCase(tag.getName()) ||
			"input".equalsIgnoreCase(tag.getName()))
		{
			tag.put("disabled", "disabled");
		}
	}

	/**
	 * @return the link's body model
	 */
	public IModel<?> getBody()
	{
		return bodyModel;
	}

	/**
	 * Sets the link's body model
	 * 
	 * @param bodyModel
	 * @return <code>this</code> for method chaining
	 */
	public AbstractLink setBody(final IModel<?> bodyModel)
	{
		this.bodyModel = wrap(bodyModel);
		return this;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12159.java