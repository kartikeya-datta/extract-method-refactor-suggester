error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8196.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8196.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8196.java
text:
```scala
i@@f(value != null && "".equals(value))

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

import wicket.markup.ComponentTag;
import wicket.model.IModel;
import wicket.util.value.ValueMap;
import wicket.version.undo.Change;

/**
 * A form button.
 * <p>
 * Within a form, you can nest Button components. Note that you don't have to do
 * this to let the form work (a simple &lt;input type="submit".. suffices), but
 * if you want to have different kinds of submit behaviour it might be a good
 * idea to use Buttons.
 * </p>
 * <p>
 * When you add a Wicket Button to a form, and that button is clicked, by
 * default the button's onSubmit method is called first, and after that the
 * form's onSubmit button is called. If you want to change this (e.g. you don't
 * want to call the form's onSubmit method, or you want it called before the
 * button's onSubmit method), you can override Form.delegateSubmit.
 * </p>
 * <p>
 * One other option you should know of is the 'defaultFormProcessing' property
 * of Button components. When you set this to false (default is true), all
 * validation and formupdating is bypassed and the onSubmit method of that
 * button is called directly, and the onSubmit method of the parent form is not
 * called. A common use for this is to create a cancel button.
 * </p>
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public class Button extends FormComponent
{
	/**
	 * Indicates that this button should be called without any validation done.
	 * By default, defaultFormProcessing is enabled.
	 */
	private boolean defaultFormProcessing = true;

	/**
	 * @see wicket.Component#Component(String)
	 */
	public Button(String id)
	{
		super(id);
	}
	
	/**
	 * @see wicket.Component#Component(String, IModel)
	 */
	public Button(final String id, final IModel object)
	{
		super(id, object);
	}
	

	/**
	 * Gets the defaultFormProcessing property. When false (default is true),
	 * all validation and formupdating is bypassed and the onSubmit method of
	 * that button is called directly, and the onSubmit method of the parent
	 * form is not called. A common use for this is to create a cancel button.
	 * 
	 * @return defaultFormProcessing
	 */
	public final boolean getDefaultFormProcessing()
	{
		return defaultFormProcessing;
	}

	/**
	 * Sets the defaultFormProcessing property. When true (default is false),
	 * all validation and formupdating is bypassed and the onSubmit method of
	 * that button is called directly, and the onSubmit method of the parent
	 * form is not called. A common use for this is to create a cancel button.
	 * 
	 * @param defaultFormProcessing
	 *            defaultFormProcessing
	 * @return This
	 */
	public final Button setDefaultFormProcessing(boolean defaultFormProcessing)
	{
		if (this.defaultFormProcessing != defaultFormProcessing)
		{
			addStateChange(new Change()
			{
				boolean formerValue = Button.this.defaultFormProcessing;

				public void undo()
				{
					Button.this.defaultFormProcessing = formerValue;
				}
			});
		}

		this.defaultFormProcessing = defaultFormProcessing;
		return this;
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#updateModel()
	 */
	public void updateModel()
	{
	}

	/**
	 * @return Any onClick JavaScript that should be used
	 */
	protected String getOnClickScript()
	{
		return null;
	}

	/**
	 * Processes the component tag.
	 * 
	 * @param tag
	 *            Tag to modify
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	protected void onComponentTag(final ComponentTag tag)
	{
		// Get tag attributes
		final ValueMap attributes = tag.getAttributes();

		// Default handling for component tag
		super.onComponentTag(tag);

		try
		{
			String value = getValue();
			if(value != null)
			{
				tag.put("value", value);
			}
		} 
		catch(Exception e) 
		{
			// ignore.
		}
		
		// If the subclass specified javascript, use that
		final String onClickJavaScript = getOnClickScript();
		if (onClickJavaScript != null)
		{
			tag.put("onclick", onClickJavaScript);
		}
	}

	/**
	 * Override this method to provide special submit handling in a multi-button
	 * form
	 */
	protected void onSubmit()
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8196.java