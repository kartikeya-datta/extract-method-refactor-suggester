error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7842.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7842.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7842.java
text:
```scala
t@@agValue = Boolean.valueOf((String)value).booleanValue();

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
package wicket.markup.html.form;

import java.io.Serializable;

import wicket.WicketRuntimeException;
import wicket.markup.ComponentTag;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;

/**
 * HTML checkbox input component.
 *
 * @author Jonathan Locke
 */
public final class CheckBox extends FormComponent
{
	/** Serial Version ID. */
	private static final long serialVersionUID = 7559827519977114184L;

	/**
	 * Constructor that uses the provided {@link IModel} as its model. All components have
	 * names. A component's name cannot be null.
	 * @param name The non-null name of this component
	 * @param model the model
	 * @throws wicket.WicketRuntimeException Thrown if the component has been given a null name.
	 */
	public CheckBox(String name, IModel model)
	{
		super(name, model);
	}

	/**
	 * Constructor that uses the provided instance of {@link IModel} as a dynamic model.
	 * This model will be wrapped in an instance of {@link wicket.model.PropertyModel}using the
	 * provided expression. Thus, using this constructor is a short-hand for:
	 * 
	 * <pre>
	 * new MyComponent(name, new PropertyModel(myIModel, expression));
	 * </pre>
	 * 
	 * All components have names. A component's name cannot be null.
	 * @param name The non-null name of this component
	 * @param model the instance of {@link IModel} from which the model object will be
	 *            used as the subject for the given expression
	 * @param expression the OGNL expression that works on the given object
	 * @throws wicket.WicketRuntimeException Thrown if the component has been given a null name.
	 */
	public CheckBox(String name, IModel model, String expression)
	{
		super(name);
		// construct model without formatting as we want the model object as-is
		setModel(new PropertyModel(model, expression, false));
	}

	/**
	 * Constructor that uses the provided object as a simple model. This object will be
	 * wrapped in an instance of {@link wicket.model.Model}. All components have names. A component's
	 * name cannot be null.
	 * @param name The non-null name of this component
	 * @param object the object that will be used as a simple model
	 * @throws wicket.WicketRuntimeException Thrown if the component has been given a null name.
	 */
	public CheckBox(String name, Serializable object)
	{
		super(name, object);
	}

	/**
	 * Constructor that uses the provided object as a dynamic model. This object will be
	 * wrapped in an instance of {@link wicket.model.Model} that will be wrapped in an instance of
	 * {@link wicket.model.PropertyModel} using the provided expression. Thus, using this constructor
	 * is a short-hand for:
	 * 
	 * <pre>
	 * new MyComponent(name, new PropertyModel(new Model(object), expression));
	 * </pre>
	 * 
	 * All components have names. A component's name cannot be null.
	 * @param name The non-null name of this component
	 * @param object the object that will be used as the subject for the given expression
	 * @param expression the OGNL expression that works on the given object
	 * @throws wicket.WicketRuntimeException Thrown if the component has been given a null name.
	 */
	public CheckBox(String name, Serializable object, String expression)
	{
		super(name, object, expression);
		// construct model without formatting as we want the model object as-is
		setModel(new PropertyModel(new Model(object), expression, false));
	}

	/**
	 * @see FormComponent#supportsPersistence()
	 */
	public boolean supportsPersistence()
	{
		return true;
	}

	/**
	 * @see FormComponent#setValue(java.lang.String)
	 */
	public void setValue(String value)
	{
		setModelObject(Boolean.valueOf(value));
	}

	/**
	 * Updates this components' model from the request.
	 * @see wicket.markup.html.form.FormComponent#updateModel()
	 */
	public void updateModel()
	{
		setModelObject(getRequestBoolean());
	}

	/**
	 * Processes the component tag.
	 * @param tag Tag to modify
	 * @see wicket.Component#handleComponentTag(ComponentTag)
	 */
	protected void handleComponentTag(final ComponentTag tag)
	{
		checkTag(tag, "input");
		checkAttribute(tag, "type", "checkbox");
		super.handleComponentTag(tag);

		Object value = getModelObject();

		if (value != null)
		{
			final boolean tagValue;
			if(value instanceof String) // probably was formatted or straight from request
			{
				tagValue = Boolean.parseBoolean((String)value);
			}
			else if(value instanceof Boolean)
			{
				tagValue = ((Boolean)value).booleanValue();
			}
			else
			{
				throw new WicketRuntimeException(
						"Model objects for checkboxes have to be of type Boolean");
			}
			if(tagValue) tag.put("checked", "checked");
			else tag.remove("checked"); // in case the was a design time attrib
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7842.java