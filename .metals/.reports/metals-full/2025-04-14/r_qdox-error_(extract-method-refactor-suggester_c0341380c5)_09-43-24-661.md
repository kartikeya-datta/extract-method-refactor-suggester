error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6903.java
text:
```scala
public C@@heckGroup(MarkupContainer parent,String id)

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
package wicket.markup.html.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import wicket.MarkupContainer;
import wicket.WicketRuntimeException;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.util.convert.ConversionException;

/**
 * Component used to connect instances of Check components into a group.
 * Instances of Check have to be in the component hierarchy somewhere below the
 * group component. The model of the CheckGroup component has to be an instance
 * of java.util.Collection. The model collection of the group is filled with
 * model objects of all selected Check components.
 * 
 * ie
 * 
 * <code>
 * <span wicket:id="checkboxgroup">
 *   ...
 *   <input type="radio" wicket:id="checkbox1">choice 1</input>
 *   ...
 *   <input type="radio" wicket:id="checkbox2">choice 2</input>
 *   ...
 * </span>
 * </code>
 * 
 * @see wicket.markup.html.form.Check
 * @see wicket.markup.html.form.CheckGroupSelector
 * 
 * <p>
 * Note: This component does not support cookie persistence
 * 
 * @author Igor Vaynberg (ivaynberg@users.sf.net)
 * 
 */
public class CheckGroup<V> extends FormComponent<Collection<V>> implements IOnChangeListener
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that will create a default model collection
	 * 
	 * @param id
	 *            component id
	 */
	public CheckGroup(MarkupContainer<?> parent,String id)
	{
		super(parent,id);
		setRenderBodyOnly(true);
	}

	/**
	 * Constructor that wraps the provided collection with the
	 * wicket.model.Model object
	 * 
	 * @param id
	 *            component id
	 * @param collection
	 *            collection to be used as the model
	 * 
	 */
	public CheckGroup(MarkupContainer parent,String id, Collection<V> collection)
	{
		this(parent,id, new Model<Collection<V>>(collection));
	}

	/**
	 * @see WebMarkupContainer#WebMarkupContainer(String, IModel)
	 */
	public CheckGroup(MarkupContainer parent,String id, IModel<Collection<V>> model)
	{
		super(parent,id, model);
		setRenderBodyOnly(true);
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#convertValue(String[])
	 */
	@Override
	protected Collection<V> convertValue(String[] paths) throws ConversionException
	{
		List<V> collection = new ArrayList<V>();

		/*
		 * if the input is null we do not need to do anything since the model
		 * collection has already been cleared
		 */

		if (paths != null && paths.length > 0)
		{
			for (int i = 0; i < paths.length; i++)
			{
				String path = paths[i];

				if (path != null)
				{
					/*
					 * checkbox component path sans group path = relative path
					 * from group to checkbox since we know the checkbox is
					 * child of group
					 */

					path = path.substring(getPath().length() + 1);

					// retrieve the selected checkbox component
					Check<V> checkbox = (Check)get(path);

					if (checkbox == null)
					{
						throw new WicketRuntimeException(
								"submitted http post value ["
										+ paths.toString()
										+ "] for CheckGroup component ["
										+ getPath()
										+ "] contains an illegal relative path "
										+ "element ["
										+ path
										+ "] which does not point to a Check component. Due to this the CheckGroup component cannot resolve the selected Check component pointed to by the illegal value. A possible reason is that componment hierarchy changed between rendering and form submission.");
					}

					// assign the value of the group's model
					collection.add(checkbox.getModelObject());
				}
			}
		}
		return collection;
	}

	/**
	 * @see FormComponent#updateModel()
	 */
	@Override
	public void updateModel()
	{
		Collection<V> collection = (Collection<V>)getModelObject();
		if (collection == null)
		{
			collection = (Collection<V>)getConvertedInput();
			setModelObject(collection);
		}
		else
		{
			modelChanging();
			collection.clear();
			collection.addAll((Collection)getConvertedInput());
			modelChanged();
		}
	}

	/**
	 * Check group does not support persistence through cookies
	 * 
	 * @see wicket.markup.html.form.FormComponent#supportsPersistence()
	 */
	@Override
	protected final boolean supportsPersistence()
	{
		return false;
	}

	/**
	 * Called when a selection changes.
	 */
	public final void onSelectionChanged()
	{
		convert();
		updateModel();
		onSelectionChanged((Collection)getModelObject());
	}

	/**
	 * Template method that can be overriden by clients that implement
	 * IOnChangeListener to be notified by onChange events of a select element.
	 * This method does nothing by default.
	 * <p>
	 * Called when a {@link Check} is clicked in a {@link CheckGroup} that wants
	 * to be notified of this event. This method is to be implemented by clients
	 * that want to be notified of selection events.
	 * 
	 * @param newSelection
	 *            The new selection of the {@link CheckGroup}. NOTE this is the
	 *            same as you would get by calling getModelObject() if the new
	 *            selection were current
	 */
	protected void onSelectionChanged(final Collection newSelection)
	{
	}

	/**
	 * This method should be overridden to return true if it is desirable to
	 * have on-selection-changed notifiaction.
	 * 
	 * @return true if component should receive on-selection-changed
	 *         notifications, false otherwise
	 */
	protected boolean wantOnSelectionChangedNotifications()
	{
		return false;
	}
	
	/**
	 * @see wicket.MarkupContainer#isStateless()
	 */
	@Override
	protected boolean isStateless()
	{
		if(wantOnSelectionChangedNotifications())
		{
			return false;
		}
		return super.isStateless();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6903.java