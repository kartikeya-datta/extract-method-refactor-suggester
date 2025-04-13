error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16034.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16034.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16034.java
text:
```scala
i@@f (popupPageMap != null && popupPageMap.getName() != null)

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

import org.apache.wicket.IPageMap;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

/**
 * A simple anchor link (&lt;a href="http://url"&gt;) pointing to any URL. Usually this is used for
 * links to destinations outside of Wicket.
 * 
 * @author Juergen Donnerstag
 */
public class ExternalLink extends AbstractLink
{
	private static final long serialVersionUID = 1L;

	/** this links' label. */
	private final IModel label;

	private boolean contextRelative = false;

	/**
	 * The popup specification. If not-null, a javascript on-click event handler will be generated
	 * that opens a new window using the popup properties.
	 */
	private PopupSettings popupSettings = null;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param href
	 *            the href attribute to set
	 * @param label
	 *            the label (body)
	 */
	public ExternalLink(final String id, final String href, final String label)
	{
		super(id);

		setModel(href != null ? new Model(href) : null);
		this.label = (label != null ? new Model(label) : null);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The name of this component
	 * @param href
	 *            the href attribute to set
	 */
	public ExternalLink(final String id, final String href)
	{
		this(id, href, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param href
	 *            the href attribute to set
	 * @param label
	 *            the label (body)
	 */
	public ExternalLink(final String id, final IModel href, final IModel label)
	{
		super(id);

		setModel(wrap(href));
		this.label = wrap(label);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The name of this component
	 * @param href
	 *            the href attribute to set
	 */
	public ExternalLink(final String id, final IModel href)
	{
		this(id, href, null);
	}

	/**
	 * Gets the popup specification. If not-null, a javascript on-click event handler will be
	 * generated that opens a new window using the popup properties.
	 * 
	 * @return the popup specification.
	 */
	public final PopupSettings getPopupSettings()
	{
		return popupSettings;
	}

	/**
	 * Sets the popup specification. If not-null, a javascript on-click event handler will be
	 * generated that opens a new window using the popup properties.
	 * 
	 * @param popupSettings
	 *            the popup specification.
	 * @return This
	 */
	public final ExternalLink setPopupSettings(final PopupSettings popupSettings)
	{
		this.popupSettings = popupSettings;
		return this;
	}

	/**
	 * Processes the component tag.
	 * 
	 * @param tag
	 *            Tag to modify
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);

		if (isLinkEnabled() == false)
		{
			disableLink(tag);
		}
		else if (getModel() != null)
		{
			Object hrefValue = getModelObject();
			if (hrefValue != null)
			{
				String url = hrefValue.toString();

				if (contextRelative)
				{
					if (url.length() > 0 && url.charAt(0) == '/')
					{
						url = url.substring(1);
					}
					url = RequestCycle.get().getRequest().getRelativePathPrefixToContextRoot() +
							url;
				}

				// if the tag is an anchor proper
				if (tag.getName().equalsIgnoreCase("a") || tag.getName().equalsIgnoreCase("link") ||
						tag.getName().equalsIgnoreCase("area"))
				{
					// generate the href attribute
					tag.put("href", Strings.replaceAll(url, "&", "&amp;"));

					// Add any popup script
					if (popupSettings != null)
					{
						// NOTE: don't encode to HTML as that is not valid
						// JavaScript
						tag.put("onclick", popupSettings.getPopupJavaScript());
					}
				}
				else
				{
					// generate a popup script by asking popup settings for one
					if (popupSettings != null)
					{
						popupSettings.setTarget("'" + url + "'");
						String popupScript = popupSettings.getPopupJavaScript();
						tag.put("onclick", popupScript);
					}
					else
					{
						// or generate an onclick JS handler directly
						tag.put("onclick", "window.location.href='" + url + "';");
					}
				}
			}

			if (popupSettings != null)
			{
				IPageMap popupPageMap = popupSettings.getPageMap(this);
				if (popupPageMap != null)
				{
					tag.put("target", popupPageMap.getName());
				}
			}
		}
	}

	/**
	 * Handle the container's body.
	 * 
	 * @param markupStream
	 *            The markup stream
	 * @param openTag
	 *            The open tag for the body
	 * @see org.apache.wicket.Component#onComponentTagBody(org.apache.wicket.markup.MarkupStream,
	 *      org.apache.wicket.markup.ComponentTag)
	 */
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		// Draw anything before the body?
		if (!isLinkEnabled() && getBeforeDisabledLink() != null)
		{
			getResponse().write(getBeforeDisabledLink());
		}


		if ((label != null) && (label.getObject() != null))
		{
			replaceComponentTagBody(markupStream, openTag,
					getModelObjectAsString(label.getObject()));
		}
		else
		{
			renderComponentTagBody(markupStream, openTag);
		}

		// Draw anything after the body?
		if (!isLinkEnabled() && getAfterDisabledLink() != null)
		{
			getResponse().write(getAfterDisabledLink());
		}

	}

	/**
	 * @return True if this link is automatically prepended with ../ to make it relative to the
	 *         context root.
	 */
	public boolean isContextRelative()
	{
		return contextRelative;
	}

	/**
	 * Set to true if this link should be automatically prepended with ../ to make it relative to
	 * the context root.
	 * 
	 * @param contextRelative
	 * @return This for chaining
	 */
	public ExternalLink setContextRelative(boolean contextRelative)
	{
		this.contextRelative = contextRelative;
		return this;
	}


	/**
	 * @return label attribute
	 */
	public IModel getLabel()
	{
		return label;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16034.java