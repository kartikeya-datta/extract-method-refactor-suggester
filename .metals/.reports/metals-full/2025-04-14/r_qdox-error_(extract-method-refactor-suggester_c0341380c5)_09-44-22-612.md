error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14326.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14326.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14326.java
text:
```scala
c@@ancel.setDefaultFormProcessing(true);

/*
 * $Id$
 * $Revision$
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
package wicket.extensions.markup.html.beanedit;

import java.io.Serializable;

import wicket.AttributeModifier;
import wicket.IFeedback;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.StringResourceModel;

/**
 * Panel with a form for generic bean displaying/ editing.
 *
 * @author Eelco Hillenius
 */
public class BeanFormPanel extends Panel
{
	/**
	 * Construct.
	 * @param id component id
	 * @param bean JavaBean to be edited or displayed
	 */
	public BeanFormPanel(String id, Serializable bean)
	{
		this(id, new BeanModel(bean), null);
	}

	/**
	 * Construct.
	 * @param id component id
	 * @param beanModel model with the JavaBean to be edited or displayed
	 */
	public BeanFormPanel(String id, BeanModel beanModel)
	{
		this(id, beanModel, null);
	}

	/**
	 * Construct.
	 * @param id component id
	 * @param bean JavaBean to be edited or displayed
	 * @param feedback feeback receiver
	 */
	public BeanFormPanel(String id, Serializable bean, IFeedback feedback)
	{
		this(id, new BeanModel(bean));
	}

	/**
	 * Construct.
	 * @param id component id
	 * @param beanModel model with the JavaBean to be edited or displayed
	 * @param feedback feeback receiver
	 */
	public BeanFormPanel(String id, BeanModel beanModel, IFeedback feedback)
	{
		super(id, beanModel);
		setRenderBodyOnly(true);
		add(new BeanForm("form", beanModel, feedback));
	}

	/**
	 * Creates a new instance of the bean panel.
	 * @param panelId component id
	 * @param beanModel model with the JavaBean to be edited or displayed
	 * @return a new instance of bean panel
	 */
	protected AbstractBeanPanel newBeanPanel(String panelId, BeanModel beanModel)
	{
		return new BeanPanel(panelId, beanModel);
	}

	/**
	 * Called whenever the user pushed the cancel button.
	 */
	protected void onCancel()
	{
	}

	/**
	 * Called whenever the user pushed the save button.
	 */
	protected void onSave()
	{
	}

	/**
	 * Gets the label model for the cancel button.
	 * @return the label model for the cancel button
	 */
	protected IModel getLabelModelForCancelButton()
	{
		return new StringResourceModel("wicket.ext.BeanFormPanel.cancel", this, null);
	}

	/**
	 * Gets the label model for the save button.
	 * @return the label model for the save button
	 */
	protected IModel getLabelModelForSaveButton()
	{
		return new StringResourceModel("wicket.ext.BeanFormPanel.save", this, null);
	}

	/**
	 * Gets the css class model for the cancel button.
	 * @return the css class model for the cancel button
	 */
	protected IModel getCSSClassModelForCancelButton()
	{
		return new Model("wicketExtCancelButton");
	}

	/**
	 * Gets the css class model for the save button.
	 * @return the css class model for the save button
	 */
	protected IModel getCSSClassModelForSaveButton()
	{
		return new Model("wicketExtSaveButton");
	}

	/**
	 * Form for editing the bean.
	 */
	private final class BeanForm extends Form
	{
		/**
		 * Construct.
		 * @param id component id
		 * @param beanModel model with the JavaBean to be edited or displayed
		 * @param feedback feeback receiver
		 */
		public BeanForm(String id, BeanModel beanModel, IFeedback feedback)
		{
			super(id, beanModel, feedback);
			add(newBeanPanel("beanPanel", beanModel));

			Button cancel = new Button("cancel")
			{
				protected void onSubmit()
				{
					onCancel();
				}
			};
			cancel.add(new AttributeModifier("value", true, getLabelModelForCancelButton()));
			cancel.add(new AttributeModifier("class", true, getCSSClassModelForCancelButton()));
			cancel.setImmediate(true);
			add(cancel);

			Button save = new Button("save")
			{
				protected void onSubmit()
				{
					onSave();
				}
			};
			save.add(new AttributeModifier("value", true, getLabelModelForSaveButton()));
			save.add(new AttributeModifier("class", true, getCSSClassModelForSaveButton()));
			add(save);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14326.java