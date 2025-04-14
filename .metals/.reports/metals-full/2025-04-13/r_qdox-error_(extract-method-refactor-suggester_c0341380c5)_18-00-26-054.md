error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11043.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11043.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11043.java
text:
```scala
protected v@@oid onSelectionChanged(final T newSelection)

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
package wicket.markup.html.form;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.WicketRuntimeException;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;
import wicket.util.convert.ConversionException;

/**
 * Component used to connect instances of Radio components into a group.
 * Instances of Radio have to be in the component hierarchy somewhere below the
 * group component. The model object of the gorup is set to the model object of
 * the selected radio component or null if none selected.
 * 
 * ie
 * 
 * <pre>
 *      &lt;span wicket:id=&quot;radiochoicegroup&quot;&gt;
 *        ...
 *        &lt;input type=&quot;radio&quot; wicket:id=&quot;singleradiochoice1&quot;&gt;choice 1&lt;/input&gt;
 *        ...
 *        &lt;input type=&quot;radio&quot; wicket:id=&quot;singleradiochoice2&quot;&gt;choice 2&lt;/input&gt;
 *        ...
 *      &lt;/span&gt;
 * </pre>
 * 
 * @param <T>
 *            The type
 * 
 * @author Igor Vaynberg (ivaynberg@users.sf.net)
 * @author Sven Meier (svenmeier)
 * 
 */
public class RadioGroup<T> extends FormComponent<T> implements IOnChangeListener
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see WebMarkupContainer#WebMarkupContainer(MarkupContainer,String)
	 */
	public RadioGroup(MarkupContainer parent, String id)
	{
		super(parent, id);
		setRenderBodyOnly(true);
	}

	/**
	 * @see WebMarkupContainer#WebMarkupContainer(MarkupContainer,String,
	 *      IModel)
	 */
	public RadioGroup(MarkupContainer parent, String id, IModel<T> model)
	{
		super(parent, id, model);
		setRenderBodyOnly(true);
	}

	protected boolean wantOnSelectionChangedNotifications()
	{
		return false;
	}

	/**
	 * @see wicket.MarkupContainer#getStatelessHint()
	 */
	@Override
	protected boolean getStatelessHint()
	{
		if (wantOnSelectionChangedNotifications())
		{
			return false;
		}
		return super.getStatelessHint();
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#convertValue(String[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected T convertValue(String[] input) throws ConversionException
	{
		if (input != null && input.length > 0)
		{
			final String value = input[0];

			// retrieve the selected single radio choice component
			Radio<T> choice = (Radio)visitChildren(new Component.IVisitor()
			{

				public Object component(Component component)
				{
					if (component instanceof Radio)
					{
						final Radio radio = (Radio)component;
						if (radio.getValue().equals(value) && radio.getGroup() == RadioGroup.this)
						{
							return radio;
						}
					}
					return CONTINUE_TRAVERSAL;
				}

			});
			if (choice == null)
			{
				throw new WicketRuntimeException(
						"submitted http post value ["
								+ value
								+ "] for RadioGroup component ["
								+ getPath()
								+ "] is illegal because it does not contain relative path to a Radio componnet. "
								+ "Due to this the RadioGroup component cannot resolve the selected Radio component pointed to by the illegal value. A possible reason is that componment hierarchy changed between rendering and form submission.");
			}


			// assign the value of the group's model
			return choice.getModelObject();
		}
		return null;
	}


	/**
	 * Called when a selection changes.
	 */
	public final void onSelectionChanged()
	{
		convert();
		updateModel();
		onSelectionChanged(getModelObject());
	}

	/**
	 * Template method that can be overriden by clients that implement
	 * IOnChangeListener to be notified by onChange events of a select element.
	 * This method does nothing by default.
	 * <p>
	 * Called when a option is selected of a dropdown list that wants to be
	 * notified of this event. This method is to be implemented by clients that
	 * want to be notified of selection events.
	 * 
	 * @param newSelection
	 *            The newly selected object of the backing model NOTE this is
	 *            the same as you would get by calling getModelObject() if the
	 *            new selection were current
	 */
	protected void onSelectionChanged(final Object newSelection)
	{
	}

	/**
	 * Radio group does not support persistence through cookies
	 * 
	 * @see wicket.markup.html.form.FormComponent#supportsPersistence()
	 */
	@Override
	protected final boolean supportsPersistence()
	{
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11043.java