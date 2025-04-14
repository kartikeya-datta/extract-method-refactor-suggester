error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6388.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6388.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6388.java
text:
```scala
r@@eturn "return Wicket.$$(this)&&Wicket.$$('" + getForm().getMarkupId() + "')";

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
package org.apache.wicket.ajax.form;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * Ajax event behavior that submits a form via ajax when the event it is attached to is invoked.
 * <p>
 * The form must have an id attribute in the markup or have MarkupIdSetter added.
 * 
 * @see AjaxEventBehavior
 * 
 * @since 1.2
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public abstract class AjaxFormSubmitBehavior extends AjaxEventBehavior
{
	private static final long serialVersionUID = 1L;

	private Form<?> form;

	/**
	 * Constructor. This constructor can only be used when the component this behavior is attached
	 * to is inside a form.
	 * 
	 * @param event
	 *            javascript event this behavior is attached to, like onclick
	 */
	public AjaxFormSubmitBehavior(String event)
	{
		this(null, event);
	}

	/**
	 * Construct.
	 * 
	 * @param form
	 *            form that will be submitted
	 * @param event
	 *            javascript event this behavior is attached to, like onclick
	 */
	public AjaxFormSubmitBehavior(Form<?> form, String event)
	{
		super(event);
		this.form = form;

		if (form != null)
		{
			form.setOutputMarkupId(true);
		}
	}

	/**
	 * 
	 * @return Form that will be submitted by this behavior
	 */
	protected Form<?> getForm()
	{
		if (form == null)
		{
			// try to find form in the hierarchy of owning component
			Component component = getComponent();
			form = component.findParent(Form.class);
			if (form == null)
			{
				throw new IllegalStateException(
					"form was not specified in the constructor and cannot "
						+ "be found in the hierarchy of the component this behavior "
						+ "is attached to");
			}
		}
		return form;
	}

	/**
	 * 
	 * @see org.apache.wicket.ajax.AjaxEventBehavior#getEventHandler()
	 */
	@Override
	protected CharSequence getEventHandler()
	{
		final String formId = getForm().getMarkupId();
		final CharSequence url = getCallbackUrl();

		AppendingStringBuffer call = new AppendingStringBuffer("wicketSubmitFormById('").append(
			formId).append("', '").append(url).append("', ");

		if (getComponent() instanceof IFormSubmittingComponent)
		{
			call.append("'")
				.append(((IFormSubmittingComponent)getComponent()).getInputName())
				.append("' ");
		}
		else
		{
			call.append("null");
		}

		return generateCallbackScript(call) + ";";
	}

	/**
	 * 
	 * @see org.apache.wicket.ajax.AjaxEventBehavior#onEvent(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onEvent(AjaxRequestTarget target)
	{
		getForm().getRootForm().onFormSubmitted();
		if (!getForm().isSubmitted())
		{ // only process the form submission if the form was actually submitted -> needs to be
			// enabled and visible
			return;
		}
		if (!getForm().hasError())
		{
			onSubmit(target);
		}
		if (form.findParent(Page.class) != null)
		{
			/*
			 * there can be cases when a form is replaced with another component in the onsubmit()
			 * handler of this behavior. in that case form no longer has a page and so calling
			 * .hasError on it will cause an exception, thus the check above.
			 */
			if (getForm().hasError())
			{
				onError(target);
			}
		}
	}

	/**
	 * Listener method that is invoked after the form has been submitted and processed without
	 * errors
	 * 
	 * @param target
	 */
	protected abstract void onSubmit(AjaxRequestTarget target);

	/**
	 * Listener method invoked when the form has been processed and errors occurred
	 * 
	 * @param target
	 * 
	 */
	protected abstract void onError(AjaxRequestTarget target);

	/**
	 * 
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#getPreconditionScript()
	 */
	@Override
	protected CharSequence getPreconditionScript()
	{
		return "return Wicket.$$(this)&amp;&amp;Wicket.$$('" + getForm().getMarkupId() + "')";
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6388.java