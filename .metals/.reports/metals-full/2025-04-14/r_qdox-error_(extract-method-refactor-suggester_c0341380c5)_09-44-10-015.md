error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/607.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/607.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/607.java
text:
```scala
r@@eturn getPage().urlFor(Link.this, ILinkListener.class);

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.markup.html.link;

import wicket.Component;
import wicket.Page;
import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;

/**
 * Implementation of a hyperlink component. A link can be used with an anchor
 * (&lt;a href...) element or any element that supports the onclick javascript
 * event handler (such as buttons, td elements, etc). When used with an anchor,
 * a href attribute will be generated. When used with any other element, an
 * onclick javascript event handler attribute will be generated.
 * <p>
 * You can use a link like:
 * 
 * <pre>
 * 
 *       add(new Link(&quot;myLink&quot;){
 *      
 *         public void linkClicked(RequestCycle cycle)
 *         {
 *            // do something here...  
 *         }
 *       );
 *  
 * </pre>
 * 
 * and in your HTML file:
 * 
 * <pre>
 * 
 *        &lt;a href=&quot;#&quot; id=&quot;wicket-myLink&quot;&gt;click here&lt;/a&gt;
 *  
 * </pre>
 * 
 * or:
 * 
 * <pre>
 * 
 *        &lt;td id=&quot;wicket-myLink&quot;&gt;my clickable column&lt;/td&gt;
 *  
 * </pre>
 * 
 * </p>
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public abstract class Link extends WebMarkupContainer implements ILinkListener
{

	/**
	 * Simple insertion string to allow disabled links to look like <i>Disabled
	 * link </i>.
	 */
	private String afterDisabledLink;

	/** True if link should automatically enable/disable based on current page. */
	private boolean autoEnable = true;

	/**
	 * Simple insertion string to allow disabled links to look like <i>Disabled
	 * link </i>.
	 */
	private String beforeDisabledLink;

	/** True if this link is enabled. */
	private boolean enabled = true;
	/**
	 * The popup specification. If not-null, a javascript on-click event handler
	 * will be generated that opens a new window using the popup properties.
	 */
	private PopupSettings popupSettings = null;

	/**
	 * @see wicket.Component#Component(String)
	 */
	public Link(final String id)
	{
		super(id);
	}

	/**
	 * @see wicket.Component#Component(String, IModel)
	 */
	public Link(final String id, IModel object)
	{
		super(id, object);
	}

	/**
	 * Gets the insertion string to allow disabled links to look like
	 * <i>Disabled link </i>.
	 * 
	 * @return The insertion string
	 */
	public String getAfterDisabledLink()
	{
		return afterDisabledLink;
	}

	/**
	 * Gets whether link should automatically enable/disable based on current
	 * page.
	 * 
	 * @return Whether this link should automatically enable/disable based on
	 *         current page.
	 */
	public final boolean getAutoEnable()
	{
		return autoEnable;
	}

	/**
	 * Gets the insertion string to allow disabled links to look like
	 * <i>Disabled link </i>.
	 * 
	 * @return The insertion string
	 */
	public String getBeforeDisabledLink()
	{
		return beforeDisabledLink;
	}

	/**
	 * Gets the popup specification. If not-null, a javascript on-click event
	 * handler will be generated that opens a new window using the popup
	 * properties.
	 * 
	 * @return the popup specification.
	 */
	public final PopupSettings getPopupSettings()
	{
		return popupSettings;
	}

	/**
	 * Gets whether this link is enabled.
	 * 
	 * @return whether this link is enabled.
	 */
	public final boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Called when a link is clicked.
	 */
	public abstract void onClick();

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET API. DO NOT ATTEMPT TO OVERRIDE OR
	 * CALL IT.
	 * 
	 * Called when a link is clicked. The implementation of this method is
	 * currently to simply call onClick(), but this may be augmented in the
	 * future.
	 * 
	 * @see ILinkListener
	 */
	public final void onLinkClicked()
	{
		// Since the invocation of onLinkClicked occurred through a URL that
		// would repeat the action if the user refreshed the page, we redirect
		// to our resulting page so this won't happen.
		getRequestCycle().setRedirect(true);

		onClick();
	}

	/**
	 * Sets the insertion string to allow disabled links to look like
	 * <i>Disabled link </i>.
	 * 
	 * @param afterDisabledLink
	 *            The insertion string
	 */
	public void setAfterDisabledLink(final String afterDisabledLink)
	{
		if (afterDisabledLink == null)
		{
			throw new IllegalArgumentException(
					"Value cannot be null.  For no text, specify an empty String instead.");
		}
		this.afterDisabledLink = afterDisabledLink;
	}

	/**
	 * Sets whether this link should automatically enable/disable based on
	 * current page.
	 * 
	 * @param autoEnable
	 *            whether this link should automatically enable/disable based on
	 *            current page.
	 * @return This
	 */
	public final Link setAutoEnable(final boolean autoEnable)
	{
		this.autoEnable = autoEnable;
		return this;
	}

	/**
	 * Sets the insertion string to allow disabled links to look like
	 * <i>Disabled link </i>.
	 * 
	 * @param beforeDisabledLink
	 *            The insertion string
	 */
	public void setBeforeDisabledLink(final String beforeDisabledLink)
	{
		if (beforeDisabledLink == null)
		{
			throw new IllegalArgumentException(
					"Value cannot be null.  For no text, specify an empty String instead.");
		}
		this.beforeDisabledLink = beforeDisabledLink;
	}

	/**
	 * Sets link enabled state.
	 * 
	 * @param enabled
	 *            The enabled to set.
	 * @return This
	 */
	public final Link setEnabled(final boolean enabled)
	{
		// Set enabled state
		this.enabled = enabled;
		return this;
	}

	/**
	 * Sets the popup specification. If not-null, a javascript on-click event
	 * handler will be generated that opens a new window using the popup
	 * properties.
	 * 
	 * @param popupSettings
	 *            the popup specification.
	 * @return This
	 */
	public final Link setPopupSettings(PopupSettings popupSettings)
	{
		this.popupSettings = popupSettings;
		return this;
	}

	/**
	 * @param url
	 *            The url for the link
	 * @return Any onClick JavaScript that should be used
	 */
	protected String getOnClickScript(final String url)
	{
		return null;
	}

	/**
	 * Gets the url to use for this link.
	 * 
	 * @return The URL that this link links to
	 */
	protected String getURL()
	{
		return getRequestCycle().urlFor(Link.this, ILinkListener.class);
	}

	/**
	 * Whether this link refers to the given page.
	 * 
	 * @param page
	 *            A page
	 * @return True if this link goes to the given page
	 */
	protected boolean linksTo(final Page page)
	{
		return false;
	}

	/**
	 * Handles this link's tag.
	 * 
	 * @param tag
	 *            the component tag
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	protected final void onComponentTag(final ComponentTag tag)
	{
		// Default handling for tag
		super.onComponentTag(tag);

		// If we're auto-enabling
		if (autoEnable)
		{
			// the link is enabled if this link doesn't link to the current page
			setEnabled(!linksTo(getPage()));
		}

		// Set href to link to this link's linkClicked method
		String url = getURL();

		// If we're disabled
		if (!enabled)
		{
			// if the tag is an anchor proper
			if (tag.getName().equalsIgnoreCase("a"))
			{
				// Change anchor link to span tag
				tag.setName("span");

				// Remove any href from the old link
				tag.remove("href");

				// if it generates a popupscript, remove the design time JS
				// handler
				if (popupSettings != null)
				{
					tag.remove("onclick");
				}
			}
			else
			{
				// Remove any onclick design time code
				tag.remove("onclick");
			}
		}
		else
		{
			// if the tag is an anchor proper
			if (tag.getName().equalsIgnoreCase("a"))
			{
				// generate the href attribute
				tag.put("href", url.replaceAll("&", "&amp;"));

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
					tag.put("onclick", "location.href='" + url + "';");
				}
			}
		}

		// If the subclass specified javascript, use that
		final String onClickJavaScript = getOnClickScript(url);
		if (onClickJavaScript != null)
		{
			tag.put("onclick", onClickJavaScript);
		}
	}

	/**
	 * Renders this link's body.
	 * 
	 * @param markupStream
	 *            the markup stream
	 * @param openTag
	 *            the open part of this tag
	 * @see wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	protected final void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag)
	{
		// Get disabled component of the same name with "Disabled" appended
		final Component disabledComponent = (Component)get("disabled");

		if (disabledComponent != null)
		{
			// Get enabled container
			final Component enabledComponent = (Component)get("enabled");

			// Set visibility of enabled and disabled children
			enabledComponent.setVisible(enabled);
			disabledComponent.setVisible(!enabled);
		}

		// Set default for before/after link text
		if (beforeDisabledLink == null)
		{
			beforeDisabledLink = getApplicationSettings().getDefaultBeforeDisabledLink();
			afterDisabledLink = getApplicationSettings().getDefaultAfterDisabledLink();
		}

		// Draw anything before the body?
		if (!enabled && beforeDisabledLink != null)
		{
			getResponse().write(beforeDisabledLink);
		}

		// Render the body of the link
		renderComponentTagBody(markupStream, openTag);

		// Draw anything after the body?
		if (!enabled && afterDisabledLink != null)
		{
			getResponse().write(afterDisabledLink);
		}
	}

	static
	{
		// Allow calls through the ILinkListener interface
		RequestCycle.registerRequestListenerInterface(ILinkListener.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/607.java