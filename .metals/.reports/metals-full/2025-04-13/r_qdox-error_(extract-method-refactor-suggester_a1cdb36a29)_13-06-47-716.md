error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14486.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14486.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14486.java
text:
```scala
S@@tringBuilder script = new StringBuilder("var w = window.open(" + target + ", '").append(

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

import org.apache.wicket.IClusterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A popup specification can be used as a property of the {@link Link}classes to specify that the
 * link should be rendered with an onClick javascript event handler that opens a new window with the
 * links' URL.
 * <p>
 * You can 'or' display flags together like this:
 * 
 * <pre>
 * new PopupSettings(PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS);
 * </pre>
 * 
 * </p>
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public class PopupSettings implements IClusterable
{
	/** The log. */
	private static final Logger log = LoggerFactory.getLogger(PopupSettings.class);

	private static final long serialVersionUID = 1L;

	/** Flag to include location bar */
	public static final int LOCATION_BAR = 1;

	/** Flag to include menu bar */
	public static final int MENU_BAR = 2;

	/** Flag to make popup resizable */
	public static final int RESIZABLE = 4;

	/** Flag to include scrollbars */
	public static final int SCROLLBARS = 8;

	/** Flag to include status bar */
	public static final int STATUS_BAR = 16;

	/** Flag to include location bar */
	public static final int TOOL_BAR = 32;

	/** Display flags */
	private int displayFlags;

	/** Height of popup window. */
	private int height = -1;

	/** Left position of popup window. */
	private int left = -1;

	/**
	 * The target to put in JavaScript. This implementation simply refers to the href element, but
	 * clients may want to override this (e.g. when the HTML element is not an anchor).
	 */
	private String target = "href";

	/** Top position of popup window. */
	private int top = -1;

	/** Width of popup window. */
	private int width = -1;

	/**
	 * The logical name of the window. This can be anything you want, although you should use
	 * alphanumeric characters only (no spaces or punctuation). If you have a window already open
	 * and call window.open a second time using the same windowName, the first window will be reused
	 * rather than opening a second window.
	 */
	private String windowName = null;

	/**
	 * Construct. If you are not using these popup settings with an external link - in which case we
	 * don't need to know about a page map - you should use one of the constructors with a
	 * {@link PageMap} argument. Typically, you should put any popup in a separate page map as
	 * Wicket holds references to a limited number of pages/ versions only. If you don't put your
	 * popup in a separate page map, the user might get page expired exceptions when getting back to
	 * the main window again.
	 */
	public PopupSettings()
	{
	}

	/**
	 * Construct.
	 * 
	 * @param displayFlags
	 *            Display flags
	 */
	public PopupSettings(final int displayFlags)
	{
		this(null, displayFlags);
	}

	/**
	 * Construct.
	 * 
	 * @param windowName
	 */
	public PopupSettings(String windowName)
	{
		this(windowName, 0);
	}

	/**
	 * Construct.
	 * 
	 * @param windowName
	 *            The pagemap where this popup must be in. Typically, you should put any popup in a
	 *            separate page map as Wicket holds references to a limited number of pages/
	 *            versions only. If you don't put your popup in a separate page map, the user might
	 *            get page expired exceptions when getting back to the main window again.
	 * @param displayFlags
	 *            Display flags
	 */
	public PopupSettings(String windowName, final int displayFlags)
	{
		this.displayFlags = displayFlags;
		this.windowName = windowName;
	}

	/**
	 * Get the onClick javascript event handler.
	 * 
	 * @return the onClick javascript event handler
	 */
	public String getPopupJavaScript()
	{
		String windowTitle = windowName;

		if (windowTitle == null)
		{
			windowTitle = "";
		}
		else
		{
			// Fix for IE bug.
			windowTitle = windowTitle.replaceAll("\\W", "_");
		}

		StringBuffer script = new StringBuffer("var w = window.open(" + target + ", '").append(
			windowTitle).append("', '");

		script.append("scrollbars=").append(flagToString(SCROLLBARS));
		script.append(",location=").append(flagToString(LOCATION_BAR));
		script.append(",menuBar=").append(flagToString(MENU_BAR));
		script.append(",resizable=").append(flagToString(RESIZABLE));
		script.append(",status=").append(flagToString(STATUS_BAR));
		script.append(",toolbar=").append(flagToString(TOOL_BAR));

		if (width != -1)
		{
			script.append(",width=").append(width);
		}

		if (height != -1)
		{
			script.append(",height=").append(height);
		}

		if (left != -1)
		{
			script.append(",left=").append(left);
		}

		if (top != -1)
		{
			script.append(",top=").append(top);
		}

		script.append("'); if(w.blur) w.focus();").append(" return false;");

		return script.toString();
	}

	/**
	 * Sets the popup window height.
	 * 
	 * @param popupHeight
	 *            the popup window height.
	 * @return This
	 */
	public PopupSettings setHeight(int popupHeight)
	{
		height = popupHeight;
		return this;
	}

	/**
	 * Sets the left position of the popup window.
	 * 
	 * @param popupPositionLeft
	 *            the left position of the popup window.
	 * @return This
	 */
	public PopupSettings setLeft(int popupPositionLeft)
	{
		left = popupPositionLeft;
		return this;
	}

	/**
	 * Sets the target of the link. The default implementation simply refers to the href element,
	 * but clients may want to override this (e.g. when the HTML element is not an anchor) by
	 * setting the target explicitly.
	 * 
	 * @param target
	 *            the target of the link
	 */
	public void setTarget(String target)
	{
		this.target = target;
	}

	/**
	 * Sets the top position of the popup window.
	 * 
	 * @param popupPositionTop
	 *            the top position of the popup window.
	 * @return This
	 */
	public PopupSettings setTop(int popupPositionTop)
	{
		top = popupPositionTop;
		return this;
	}

	/**
	 * Sets the popup window width.
	 * 
	 * @param popupWidth
	 *            the popup window width.
	 * @return This
	 */
	public PopupSettings setWidth(int popupWidth)
	{
		width = popupWidth;
		return this;
	}

	/**
	 * Sets the window name. The logical name of the window. This can be anything you want, although
	 * you should use alphanumeric characters only (no spaces or punctuation). If you have a window
	 * already open and call window.open a second time using the same windowName, the first window
	 * will be reused rather than opening a second window
	 * 
	 * @param popupWindowName
	 *            window name.
	 * @return This
	 */
	public PopupSettings setWindowName(String popupWindowName)
	{
		if (popupWindowName != null)
		{
			windowName = popupWindowName;
		}
		return this;
	}

	/**
	 * @param flag
	 *            The flag to test
	 * @return Yes or no depending on whether the flag is set
	 */
	private String flagToString(final int flag)
	{
		return (displayFlags & flag) != 0 ? "yes" : "no";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14486.java