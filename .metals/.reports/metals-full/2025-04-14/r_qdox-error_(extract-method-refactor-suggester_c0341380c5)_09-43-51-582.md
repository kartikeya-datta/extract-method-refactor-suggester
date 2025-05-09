error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18031.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18031.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18031.java
text:
```scala
r@@eturn "YAHOO.wicket." + getJavascriptId();

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
package org.apache.wicket.extensions.yui.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.extensions.yui.YuiLib;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.string.JavascriptUtils;


/**
 * Abstract calendar component based on the YUI (Yahoo User Interface library)
 * javascript widget.
 * <p>
 * Although this component by itself is fully functional, it doesn't do much
 * other than just displaying the calendar. Hence, this class is abstract.
 * </p>
 * <p>
 * An easy way to build upon this component is to override
 * {@link #appendToInit(String, String, String, StringBuffer)} and add event
 * handlers etc. in the YUI widget's initialization function.
 * </p>
 * See <a href="http://developer.yahoo.com/yui/calendar/">YUI's calendar
 * documentation</a> for more info.
 * 
 * @author eelcohillenius
 * 
 * @see CalendarPopup
 */
// TODO provide localization strings (base them on the messages of
// JsDatePicker?)
public abstract class AbstractCalendar extends WebComponent
{
	/**
	 * Format to be used when configuring YUI calendar. Can be used when using
	 * the &quot;selected&quot; property.
	 */
	public static final DateFormat FORMAT_DATE = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * For specifying which page (month/year) to show in the calendar, use this
	 * format for the date. This is to be used together with the property
	 * &quot;pagedate&quot;
	 */
	public static final DateFormat FORMAT_PAGEDATE = new SimpleDateFormat("MM/yyyy");
	private static final long serialVersionUID = 1L;

	/**
	 * Construct. Contributes packaged dependencies.
	 * 
	 * @param id
	 *            The component id
	 */
	public AbstractCalendar(String id)
	{
		this(id, true);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            The component id
	 * @param contributeDependencies
	 *            Whether to contribute the packaged dependencies. Pass false in
	 *            case you want to include the dependencies manually in your own
	 *            page, e.g. when you want to keep them in your web application
	 *            dir. To contribute yourself (in case you want to pass false),
	 *            your page header should look like:
	 * 
	 * <pre>
	 * 	 &lt;script type=&quot;text/javascript&quot; src=&quot;yahoo.js&quot;&gt;&lt;/script&gt; 
	 * 	 &lt;script type=&quot;text/javascript&quot; src=&quot;dom.js&quot;&gt;&lt;/script&gt; 
	 * 	 &lt;script type=&quot;text/javascript&quot; src=&quot;event.js&quot;&gt;&lt;/script&gt; 
	 * 	 &lt;script type=&quot;text/javascript&quot; src=&quot;calendar.js&quot;&gt;&lt;/script&gt; 
	 * 	 &lt;link rel=&quot;stylesheet&quot; type=&quot;text/css&quot; href=&quot;calendar.css&quot; /&gt; 
	 * </pre>
	 */
	public AbstractCalendar(String id, boolean contributeDependencies)
	{

		super(id);
		setOutputMarkupId(true);
		if (contributeDependencies)
		{
			contributeDependencies();
		}

		add(new StringHeaderContributor(new LoadableDetachableModel()
		{

			private static final long serialVersionUID = 1L;

			protected Object load()
			{

				// not pretty to look at, but cheaper than using a template
				String markupId = AbstractCalendar.this.getMarkupId();
				String javascriptId = getJavascriptId();
				String javascriptWidgetId = getJavascriptWidgetId();
				StringBuffer b = new StringBuffer();
				b.append(JavascriptUtils.SCRIPT_OPEN_TAG);
				// initialize wicket namespace and register the init function
				// for the YUI widget
				b.append("YAHOO.namespace(\"wicket\");\nfunction init");
				b.append(javascriptId);
				b.append("() {\n");

				// instantiate the calendar object
				b.append("  ");
				b.append(javascriptWidgetId);
				b.append(" = new YAHOO.widget.Calendar(\"");
				b.append(javascriptId);
				b.append("\",\"");
				b.append(markupId);

				Properties p = new Properties();
				configureWidgetProperties(p);
				b.append("\", { ");
				for (Iterator i = p.entrySet().iterator(); i.hasNext();)
				{
					Entry entry = (Entry)i.next();
					b.append(entry.getKey());
					Object value = entry.getValue();
					if (value instanceof CharSequence)
					{
						b.append(":\"");
						b.append(value);
						b.append("\"");
					}
					else if (value instanceof CharSequence[])
					{
						b.append(":[");
						CharSequence[] valueArray = (CharSequence[])value;
						for (int j = 0; j < valueArray.length; j++)
						{
							CharSequence tmpValue = valueArray[j];
							b.append("\"");
							b.append(tmpValue);
							b.append("\"");
							if (j < valueArray.length - 1)
							{
								b.append(",");
							}
						}
						b.append("]");
					}
					else
					{
						b.append(":");
						b.append(value);
					}
					// TODO handle arrays
					if (i.hasNext())
					{
						b.append(",");
					}
				}

				b.append(" });\n");

				// append the javascript we want for our init function; call
				// this in an overridable method so that clients can add their
				// stuff without needing a big ass API
				appendToInit(markupId, javascriptId, javascriptWidgetId, b);

				// trigger rendering
				b.append("  ");
				b.append(javascriptWidgetId);
				b.append(".render();\n");

				b.append("}\n");
				// register the function for execution when the page is loaded
				b.append("YAHOO.util.Event.addListener(window, \"load\", init");
				b.append(javascriptId);
				b.append(");");
				b.append(JavascriptUtils.SCRIPT_CLOSE_TAG);
				return b;
			}
		}));
	}

	/**
	 * Gets the id of the javascript widget. Note that this is the
	 * non-namespaced id, so depending on what you want to do with it, you may
	 * need to prepend 'YAHOO.wicket.' to it. Or you can call
	 * {@link #getJavascriptWidgetId()}.
	 * 
	 * @return The javascript id
	 * @see #getJavascriptWidgetId()
	 */
	public final String getJavascriptId()
	{
		return getMarkupId() + "Js";
	}

	/**
	 * The name spaced id of the widget.
	 * 
	 * @return The widget id
	 * @see #getJavascriptId()
	 */
	public final String getJavascriptWidgetId()
	{
		return "YAHOO.org.apache.wicket." + getJavascriptId();
	}

	/**
	 * add header contributions for packaged resources.
	 */
	private void contributeDependencies()
	{
		add(HeaderContributor.forJavaScript(YuiLib.class, "yahoo.js"));
		add(HeaderContributor.forJavaScript(YuiLib.class, "event.js"));
		add(HeaderContributor.forJavaScript(YuiLib.class, "dom.js"));
		add(HeaderContributor.forJavaScript(AbstractCalendar.class, "calendar.js"));
		add(HeaderContributor.forCss(AbstractCalendar.class, "assets/calendar.css"));
	}

	/**
	 * Append javascript to the initialization function for the YUI widget. Can
	 * be used by subclasses to conveniently extend configuration without having
	 * to write a separate contribution.
	 * 
	 * @param markupId
	 *            The markup id of the calendar component
	 * @param javascriptId
	 *            the non-name spaced javascript id of the widget
	 * @param javascriptWidgetId
	 *            the name space id of the widget
	 * @param b
	 *            the buffer to append the script to
	 */
	protected void appendToInit(String markupId, String javascriptId, String javascriptWidgetId,
			StringBuffer b)
	{
	}

	/**
	 * Gives overriding classes the option of adding (or even changing/
	 * removing) configuration properties for the javascript widget. See <a
	 * href="http://developer.yahoo.com/yui/calendar/">the widget's
	 * documentation</a> for the available options. If you want to override/
	 * remove properties, you obviously should call
	 * {@link super#setWidgetProperties(Properties)} first.
	 * 
	 * @param widgetProperties
	 *            the current widget properties
	 */
	protected void configureWidgetProperties(Map widgetProperties)
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18031.java