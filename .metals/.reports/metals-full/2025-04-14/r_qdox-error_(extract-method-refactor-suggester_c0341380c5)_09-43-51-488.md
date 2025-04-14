error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12152.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12152.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12152.java
text:
```scala
r@@eturn object.getDeclaringClass().getSimpleName() + "." + object.name();

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

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.util.string.Strings;

/**
 * {@link IChoiceRenderer} implementation that makes it easy to work with java 5 enums. This
 * renderer will attempt to lookup strings used for the display value using a localizer of a given
 * component. If the component is not specified, the global instance of localizer will be used for
 * lookups.
 * <p>
 * display value resource key format: {@code <enum.getSimpleClassName()>.<enum.name()>}
 * </p>
 * <p>
 * id value format: {@code <enum.name()>}
 * </p>
 * 
 * @author igor.vaynberg
 * 
 * @param <T>
 */
public class EnumChoiceRenderer<T extends Enum<T>> implements IChoiceRenderer<T>
{

	private static final long serialVersionUID = 1L;

	/**
	 * component used to resolve i18n resources for this renderer.
	 */
	private final Component resourceSource;


	/**
	 * Constructor that creates the choice renderer that will use global instance of localizer to
	 * resolve resource keys.
	 */
	public EnumChoiceRenderer()
	{
		resourceSource = null;
	}

	/**
	 * Constructor
	 * 
	 * @param resourceSource
	 */
	public EnumChoiceRenderer(Component resourceSource)
	{
		this.resourceSource = resourceSource;
	}

	/** {@inheritDoc} */
	public final Object getDisplayValue(T object)
	{
		final String value;

		String key = resourceKey(object);

		if (resourceSource != null)
		{
			value = resourceSource.getString(key);
		}
		else
		{
			value = Application.get().getResourceSettings().getLocalizer().getString(key, null);
		}

		return postprocess(value);
	}

	/**
	 * Translates the {@code object} into resource key that will be used to lookup the value shown
	 * to the user
	 * 
	 * @param object
	 * @return resource key
	 */
	protected String resourceKey(T object)
	{
		return object.getClass().getSimpleName() + "." + object.name();
	}

	/**
	 * Postprocesses the {@code value} after it is retrieved from the localizer. Default
	 * implementation escapes any markup found in the {@code value}.
	 * 
	 * @param value
	 * @return postprocessed value
	 */
	protected CharSequence postprocess(String value)
	{
		return Strings.escapeMarkup(value);
	}

	/** {@inheritDoc} */
	public String getIdValue(T object, int index)
	{
		return object.name();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12152.java