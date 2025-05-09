error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12936.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12936.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12936.java
text:
```scala
private i@@nt uuid = -1;

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
package org.apache.wicket.markup.html.form;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

/**
 * Component representing a single radio choice in a org.apache.wicket.markup.html.form.RadioGroup.
 * 
 * Must be attached to an &lt;input type=&quot;radio&quot; ... &gt; markup.
 * <p>
 * STATELESS NOTES: By default this component cannot be used inside a stateless form. If it is
 * desirable to use this inside a stateless form then
 * <ul>
 * <li>
 * override #getValue() and return some stateless value to uniquely identify this radio (eg relative
 * component path from group to this radio)</li>
 * <li>
 * override {@link #getStatelessHint()} and return <code>true</code></li>
 * </ul>
 * </p>
 * 
 * @see org.apache.wicket.markup.html.form.RadioGroup
 * 
 * @author Igor Vaynberg
 * @author Sven Meier (svenmeier)
 * 
 * @param <T>
 *            The model object type
 */
public class Radio<T> extends LabeledWebMarkupContainer
{
	private static final long serialVersionUID = 1L;

	private static final String ATTR_DISABLED = "disabled";

	/**
	 * page-scoped uuid of this check. this property must not be accessed directly, instead
	 * {@link #getValue()} must be used
	 */
	private short uuid = -1;

	private final RadioGroup<T> group;

	/**
	 * @see WebMarkupContainer#WebMarkupContainer(String)
	 */
	public Radio(final String id)
	{
		this(id, null, null);
	}

	/**
	 * @param id
	 * @param model
	 * @see WebMarkupContainer#WebMarkupContainer(String, IModel)
	 */
	public Radio(final String id, final IModel<T> model)
	{
		this(id, model, null);
	}

	/**
	 * @param id
	 * @param group
	 *            parent {@link RadioGroup}
	 * @see WebMarkupContainer#WebMarkupContainer(String)
	 */
	public Radio(final String id, final RadioGroup<T> group)
	{
		this(id, null, group);
	}

	/**
	 * @param id
	 * @param model
	 * @param group
	 *            parent {@link RadioGroup}
	 * @see WebMarkupContainer#WebMarkupContainer(String, IModel)
	 */
	public Radio(final String id, final IModel<T> model, final RadioGroup<T> group)
	{
		super(id, model);
		this.group = group;
		setOutputMarkupId(true);
	}

	/**
	 * Form submission value used for this radio component. This string will appear as the value of
	 * the <code>value</code> html attribute for the <code>input</code> tag.
	 * 
	 * @return form submission value
	 */
	public String getValue()
	{
		if (uuid < 0)
		{
			uuid = getPage().getAutoIndex();
		}
		return "radio" + uuid;
	}

	/**
	 * 
	 * @return The associated radio group Component
	 */
	@SuppressWarnings("unchecked")
	protected RadioGroup<T> getGroup()
	{
		RadioGroup<T> group = this.group;
		if (group == null)
		{
			group = findParent(RadioGroup.class);
			if (group == null)
			{
				throw new WicketRuntimeException(
					"Radio component [" +
						getPath() +
						"] cannot find its parent RadioGroup. All Radio components must be a child of or below in the hierarchy of a RadioGroup component.");
			}
		}
		return group;
	}

	/**
	 * @see Component#onComponentTag(ComponentTag)
	 * @param tag
	 *            the abstraction representing html tag of this component
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag)
	{
		// Default handling for component tag
		super.onComponentTag(tag);

		// must be attached to <input type="radio" .../> tag
		checkComponentTag(tag, "input");
		checkComponentTagAttribute(tag, "type", "radio");

		final String value = getValue();

		RadioGroup<?> group = getGroup();

		// assign name and value
		tag.put("name", group.getInputName());
		tag.put("value", value);

		// compare the model objects of the group and self, if the same add the
		// checked attribute, first check if there was a raw input on the group.
		if (group.hasRawInput())
		{
			String rawInput = group.getRawInput();
			if (rawInput != null && rawInput.equals(value))
			{
				tag.put("checked", "checked");
			}
		}
		else if (group.getModelComparator().compare(group, getDefaultModelObject()))
		{
			tag.put("checked", "checked");
		}

		if (group.wantOnSelectionChangedNotifications())
		{
			// url that points to this components IOnChangeListener method
			CharSequence url = group.urlFor(IOnChangeListener.INTERFACE);

			Form<?> form = group.findParent(Form.class);
			if (form != null)
			{
				tag.put("onclick", form.getJsForInterfaceUrl(url));
			}
			else
			{
				// NOTE: do not encode the url as that would give invalid JavaScript
				tag.put("onclick", "window.location.href='" + url +
					(url.toString().indexOf('?') > -1 ? "&amp;" : "?") + group.getInputName() +
					"=' + this.value;");
			}
		}

		if (!isEnabledInHierarchy())
		{
			tag.put(ATTR_DISABLED, ATTR_DISABLED);
		}

		// put group id into the class so we can easily identify all radios belonging to the group
		final String marker = "wicket-" + getGroup().getMarkupId();
		String clazz = tag.getAttribute("class");
		if (Strings.isEmpty(clazz))
		{
			clazz = marker;
		}
		else
		{
			clazz = clazz + " " + marker;
		}
		tag.put("class", clazz);

	}

	/**
	 * The value will be made available to the validator property by means of ${label}. It does not
	 * have any specific meaning to Radio itself.
	 * 
	 * @param labelModel
	 * @return this for chaining
	 */
	public Radio<T> setLabel(IModel<String> labelModel)
	{
		setLabelInternal(labelModel);
		return this;
	}

	/**
	 * Gets model
	 * 
	 * @return model
	 */
	@SuppressWarnings("unchecked")
	public final IModel<T> getModel()
	{
		return (IModel<T>)getDefaultModel();
	}

	/**
	 * Sets model
	 * 
	 * @param model
	 */
	public final void setModel(IModel<T> model)
	{
		setDefaultModel(model);
	}

	/**
	 * Gets model object
	 * 
	 * @return model object
	 */
	@SuppressWarnings("unchecked")
	public final T getModelObject()
	{
		return (T)getDefaultModelObject();
	}

	/**
	 * Sets model object
	 * 
	 * @param object
	 */
	public final void setModelObject(T object)
	{
		setDefaultModelObject(object);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean getStatelessHint()
	{
		// because we keep uuid this component cannot be stateless
		return false;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12936.java