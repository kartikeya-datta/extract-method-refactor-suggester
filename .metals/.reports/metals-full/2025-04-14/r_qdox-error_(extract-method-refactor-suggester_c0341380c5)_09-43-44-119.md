error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8871.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8871.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8871.java
text:
```scala
S@@tring url = urlFor(resourceReference);

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.extensions.markup.html.datepicker;

import wicket.AttributeModifier;
import wicket.Component;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebComponent;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.panel.Panel;
import wicket.markup.html.resources.JavaScriptReference;
import wicket.markup.html.resources.StyleSheetReference;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * Datepicker component.
 * <p>
 * Link your datepicker to a textfield like this:
 * </p>
 * <p>
 * (Java)
 * 
 * <pre>
 * TextField dateField = new TextField(&quot;dateField&quot;, Date.class);
 * add(dateField);
 * add(new DatePicker(&quot;dateFieldPicker&quot;, dateField));
 * </pre>
 * 
 * (html)
 * 
 * <pre>
 *     &lt;input type=&quot;text&quot; wicket:id=&quot;dateField&quot; size=&quot;10&quot; /&gt;
 *     &lt;span wicket:id=&quot;dateFieldPicker&quot; /&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * Your target doesn't have to be a text field however, attach to any tag that is
 * supported by JSCalendar.
 * </p>
 * <p>
 * Customize the looks, localization etc of the datepicker by providing a custom
 * {@link wicket.extensions.markup.html.datepicker.DatePickerSettings} object.
 * </p>
 * <p>
 * This component is based on Dynarch's JSCalendar component, which can be found at <a
 * href="http://www.dynarch.com/">the Dynarch site</a>.
 * </p>
 * @see wicket.extensions.markup.html.datepicker.DatePickerSettings
 * @author Eelco Hillenius
 * @author Mihai Bazon (creator of the JSCalendar component)
 */
public class DatePicker extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Attribute modifier that modifies/ adds an attribute with value of the given
	 * component's path.
	 */
	private final static class PathAttributeModifier extends AttributeModifier
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * @param attribute the attribute to modify
		 * @param pathProvider the component that provides the path
		 */
		public PathAttributeModifier(String attribute, final Component pathProvider)
		{
			super(attribute, true, new Model()
			{
				private static final long serialVersionUID = 1L;

				public Object getObject(Component component)
				{
					// do this lazily, so we know for sure we have the whole
					// path including the page etc.
					return pathProvider.getPath();
				}
			});
		}
	}

	/**
	 * Button that triggers the popup.
	 */
	private final static class TriggerButton extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * @param id component id
		 * @param resourceReference button icon reference
		 */
		public TriggerButton(final String id, final wicket.ResourceReference resourceReference)
		{
			super(id);
			add(new PathAttributeModifier("id", this));
			IModel srcReplacement = new Model()
			{
				private static final long serialVersionUID = 1L;

				public Object getObject(Component component)
				{
					String url = getPage().urlFor(resourceReference);
					return url;
				};
			};
			add(new AttributeModifier("src", true, srcReplacement));
		}
	}

	/**
	 * Outputs the Javascript initialization code.
	 */
	private final class InitScript extends WebComponent
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * @param id component id
		 */
		public InitScript(String id)
		{
			super(id);
		}

		/**
		 * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream,
		 *      wicket.markup.ComponentTag)
		 */
		protected void onComponentTagBody(final MarkupStream markupStream,
				final ComponentTag openTag)
		{
			replaceComponentTagBody(markupStream, openTag, getInitScript());
		}
	}

	/** any label component. */
	private final Component label;

	/** the receiving component. */
	private final Component target;

	/** the button that triggers the popup. */
	private TriggerButton triggerButton;

	/** settings for the javascript datepicker component. */
	private final DatePickerSettings settings;

	/**
	 * Construct with a default button and style.
	 * @param id the component id
	 * @param target the receiving component
	 */
	public DatePicker(String id, Component target)
	{
		this(id, target, new DatePickerSettings());
	}

	/**
	 * Construct with a default button and style.
	 * @param id the component id
	 * @param label the label for target component.
	 * @param target the receiving component
	 */
	public DatePicker(String id, Component label, Component target)
	{
		this(id, label, target, new DatePickerSettings());
	}

	/**
	 * Construct.
	 * @param id the component id
	 * @param target the receiving component
	 * @param settings datepicker properties
	 */
	public DatePicker(final String id, final Component target, final DatePickerSettings settings)
	{
		this(id, null, target, settings);
	}

	/**
	 * Construct.
	 * @param id the component id
	 * @param label the label component (may be null)
	 * @param target the receiving component
	 * @param settings datepicker properties
	 */
	public DatePicker(final String id, final Component label, final Component target,
			final DatePickerSettings settings)
	{
		super(id);

		if (settings == null)
		{
			throw new IllegalArgumentException("Settings must be non null when using this constructor");
		}

		this.settings = settings;

		if (target == null)
		{
			throw new IllegalArgumentException("Target must be not null");
		}

		target.add(new PathAttributeModifier("id", target));
		this.target = target;

		if (label != null)
		{
			label.add(new PathAttributeModifier("for", target));
		}
		this.label = label;

		add(triggerButton = new TriggerButton("trigger", settings.getIcon()));
		add(new InitScript("script"));
		add(new JavaScriptReference("calendarMain", DatePicker.class, "calendar.js"));
		add(new JavaScriptReference("calendarSetup", DatePicker.class, "calendar-setup.js"));
		add(new JavaScriptReference("calendarLanguage", new Model()
		{
			private static final long serialVersionUID = 1L;

			public Object getObject(Component component)
			{
				return settings.getLanguage(DatePicker.this.getLocale());
			}
		}));
		add(new StyleSheetReference("calendarStyle", settings.getStyle()));
	}

	/**
	 * Gets the initilization javascript.
	 * @return the initilization javascript
	 */
	private String getInitScript()
	{
		String targetId = target.getPath();
		StringBuffer b = new StringBuffer("\nCalendar.setup(\n{");
		b.append("\n\t\tinputField : \"").append(targetId).append("\",");
		b.append("\n\t\tbutton : \"").append(triggerButton.getPath()).append("\",");
		b.append(settings.toScript(getLocale()));
		b.append("\n});");
		return b.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8871.java