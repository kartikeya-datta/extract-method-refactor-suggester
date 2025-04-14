error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10225.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10225.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10225.java
text:
```scala
t@@ag.put("id", component.getMarkupId());

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
package org.apache.wicket.extensions.markup.html.repeater.data.table.filter;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * A form with filter-related special functionality for its form components.
 * 
 * @author igor
 * 
 */
public class FilterForm extends Form
{
	private static final long serialVersionUID = 1L;

	private final HiddenField hidden;
	private final IFilterStateLocator locator;

	/**
	 * @param id
	 *            component id
	 * @param locator
	 *            filter state locator
	 */
	public FilterForm(String id, IFilterStateLocator locator)
	{
		super(id, new FilterStateModel(locator));

		this.locator = locator;

		// add hidden field used for managing current focus
		hidden = new HiddenField("focus-tracker", new Model());
		
		hidden.add(new AbstractBehavior()
		{
			private static final long serialVersionUID = 1L;

			public void onComponentTag(Component component, ComponentTag tag)
			{
				tag.put("id", getFocusTrackerFieldCssId());
				super.onComponentTag(component, tag);
			}
		});
		add(hidden);
		
		// add javascript to restore focus to a filter component
		add(new WebMarkupContainer("focus-restore")
		{
			private static final long serialVersionUID = 1L;

			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
			{
				AppendingStringBuffer script = new AppendingStringBuffer("<script>_filter_focus_restore('").append(
						getFocusTrackerFieldCssId()).append("');</script>");
				replaceComponentTagBody(markupStream, openTag, script);
			}
		});
	}

	/**
	 * @return css id of the hidden form input that keeps track of the focused
	 *         input field
	 */
	public final String getFocusTrackerFieldCssId()
	{
		return hidden.getPageRelativePath();
	}


	/**
	 * @return IFilterStateLocator passed to this form
	 */
	public final IFilterStateLocator getStateLocator()
	{
		return locator;
	}

	/**
	 * Adds behavior to the form component to allow this form to keep track of
	 * the component's focus which will be restored after a form submit.
	 * 
	 * @param fc
	 *            form component
	 */
	public final void enableFocusTracking(FormComponent fc)
	{
		fc.add(new AbstractBehavior()
		{
			private static final long serialVersionUID = 1L;

			public void onComponentTag(Component component, ComponentTag tag)
			{
				tag.put("id", component.getPageRelativePath());
				tag.put("onfocus", getFocusTrackingHandler(component));
				super.onComponentTag(component, tag);
			}
		});
	}

	/**
	 * Returns the javascript focus handler necessary to notify the form of
	 * focus tracking changes on the component
	 * 
	 * Useful when components want to participate in focus tracking but want to
	 * add the handler their own way.
	 * 
	 * A unique css id is required on the form component for focus tracking to
	 * work.
	 * 
	 * @param component
	 *            component to 
	 * @return the javascript focus handler necessary to notify the form of
	 *         focus tracking changes on the component
	 */
	public final String getFocusTrackingHandler(Component component)
	{
		return ("_filter_focus(this, '" + getFocusTrackerFieldCssId() + "');");
	}

	/**
	 * Model that uses filter state locator as a passthrough for model objects
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 * 
	 */
	private static class FilterStateModel extends Model
	{
		private static final long serialVersionUID = 1L;

		private IFilterStateLocator locator;

		/**
		 * Constructor
		 * 
		 * @param locator
		 *            IFilterStateLocator implementation used to provide model
		 *            object for this model
		 */
		public FilterStateModel(IFilterStateLocator locator)
		{
			if (locator == null)
			{
				throw new IllegalArgumentException("argument [locator] cannot be null");
			}
			this.locator = locator;
		}

		/**
		 * @see org.apache.wicket.model.IModel#getObject()
		 */
		public Object getObject()
		{
			return locator.getFilterState();
		}

		/**
		 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
		 */
		public void setObject(Object object)
		{
			locator.setFilterState(object);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10225.java