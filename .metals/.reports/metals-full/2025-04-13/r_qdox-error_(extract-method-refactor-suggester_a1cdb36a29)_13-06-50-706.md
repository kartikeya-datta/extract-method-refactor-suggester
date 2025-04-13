error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14466.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14466.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14466.java
text:
```scala
S@@tringBuilder modelStringBuffer = new StringBuilder();

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
package org.apache.wicket.extensions.markup.html.form.palette.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;


/**
 * Component to keep track of selections on the html side. Also used for encoding and decoding those
 * selections between html and java.
 * 
 * @param <T>
 *            Type of the palette
 * 
 * @author Igor Vaynberg ( ivaynberg )
 */
public class Recorder<T> extends HiddenField<Object>
{
	private static final long serialVersionUID = 1L;

	private static final String[] EMPTY_IDS = new String[0];

	/** conveniently maintained array of selected ids */
	private String[] ids;

	/** parent palette object */
	private final Palette<T> palette;

	private boolean attached = false;

	/**
	 * @return parent Palette object
	 */
	public Palette<T> getPalette()
	{
		return palette;
	}

	/**
	 * @param id
	 *            component id
	 * @param palette
	 *            parent palette object
	 */
	public Recorder(String id, Palette<T> palette)
	{
		super(id);
		this.palette = palette;
		setDefaultModel(new Model<Serializable>());
		setOutputMarkupId(true);
	}

	/**
	 * 
	 * @see org.apache.wicket.markup.html.form.AbstractTextComponent#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		if (!getForm().hasError())
		{
			initIds();
		}
		else if (ids == null)
		{
			ids = EMPTY_IDS;
		}
		attached = true;
	}

	/**
	 * Synchronize ids collection from the palette's model
	 */
	private void initIds()
	{
		// construct the model string based on selection collection
		IChoiceRenderer<T> renderer = getPalette().getChoiceRenderer();
		StringBuffer modelStringBuffer = new StringBuffer();
		Collection<T> modelCollection = getPalette().getModelCollection();
		if (modelCollection == null)
		{
			throw new WicketRuntimeException(
				"Expected getPalette().getModelCollection() to return a non-null value."
					+ " Please make sure you have model object assigned to the palette");
		}
		Iterator<T> selection = modelCollection.iterator();

		int i = 0;
		while (selection.hasNext())
		{
			modelStringBuffer.append(renderer.getIdValue(selection.next(), i++));
			if (selection.hasNext())
			{
				modelStringBuffer.append(",");
			}
		}

		// set model and update ids array
		String modelString = modelStringBuffer.toString();
		setDefaultModel(new Model<String>(modelString));
		updateIds(modelString);
	}


	@Override
	protected void onValid()
	{
		super.onValid();
		if (attached)
			updateIds();
	}

	/**
	 * @return iterator over selected choices
	 */
	@SuppressWarnings("unchecked")
	public Iterator<T> getSelectedChoices()
	{
		IChoiceRenderer<T> renderer = getPalette().getChoiceRenderer();
		if (ids.length == 0)
		{
			return Collections.EMPTY_LIST.iterator();
		}

		List<T> selected = new ArrayList<T>(ids.length);
		for (int i = 0; i < ids.length; i++)
		{
			Iterator<? extends T> it = getPalette().getChoices().iterator();
			while (it.hasNext())
			{
				final T choice = it.next();
				if (renderer.getIdValue(choice, 0).equals(ids[i]))
				{
					selected.add(choice);
					break;
				}
			}
		}
		return selected.iterator();
	}

	/**
	 * @return iterator over unselected choices
	 */
	public Iterator<T> getUnselectedChoices()
	{
		IChoiceRenderer<T> renderer = getPalette().getChoiceRenderer();
		Collection<? extends T> choices = getPalette().getChoices();

		if (choices.size() - ids.length == 0)
		{
			return Collections.<T> emptyList().iterator();
		}

		List<T> unselected = new ArrayList<T>(Math.max(1, choices.size() - ids.length));
		Iterator<? extends T> it = choices.iterator();
		while (it.hasNext())
		{
			final T choice = it.next();
			final String choiceId = renderer.getIdValue(choice, 0);
			boolean selected = false;
			for (int i = 0; i < ids.length; i++)
			{
				if (ids[i].equals(choiceId))
				{
					selected = true;
					break;
				}
			}
			if (!selected)
			{
				unselected.add(choice);
			}
		}
		return unselected.iterator();
	}


	@Override
	protected void onInvalid()
	{
		super.onInvalid();
		if (attached)
			updateIds();
	}

	private void updateIds()
	{
		updateIds(getValue());
	}

	private void updateIds(String value)
	{
		if (Strings.isEmpty(value))
		{
			ids = EMPTY_IDS;
		}
		else
		{
			ids = value.split(",");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14466.java