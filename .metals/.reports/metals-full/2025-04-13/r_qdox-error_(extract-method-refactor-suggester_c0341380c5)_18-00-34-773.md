error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14217.java
text:
```scala
r@@eturn String.valueOf(childIdCounter);

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
package org.apache.wicket.markup.repeater;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;


/**
 * <p>
 * A repeater view that renders all of its children, using its body markup, in
 * the order they were added.
 * 
 * </p>
 * Example:
 * <p>
 * <u>Java:</u>
 * 
 * <pre>
 * RepeatingView view = new RepeatingView(&quot;repeater&quot;);
 * view.add(new Label(&quot;1&quot;, &quot;hello&quot;));
 * view.add(new Label(&quot;2&quot;, &quot;goodbye&quot;));
 * view.add(new Label(&quot;3&quot;, &quot;good morning&quot;));
 * </pre>
 * 
 * </p>
 * <p>
 * <u>Markup:</u>
 * 
 * <pre>
 *  &lt;ul&gt;&lt;li wicket:id=&quot;repeater&quot;&gt;&lt;/li&gt;&lt;/ul&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * <u>Yields:</u>
 * 
 * <pre>
 *  &lt;ul&gt;&lt;li&gt;hello&lt;/li&gt;&lt;li&gt;goodbye&lt;/li&gt;&lt;li&gt;good morning&lt;/li&gt;&lt;/ul&gt;
 * </pre>
 * 
 * To expand a bit: the repeater itself produces no markup, instead every direct
 * child inherits the entire markup of the repeater. In the example above
 * reeaters's markup is:
 * 
 * <pre>
 *  &lt;li wicket:id=&quot;repeater&quot;&gt;&lt;/li&gt;
 * </pre>
 * 
 * and so this is the markup that is available to the direct children - the
 * Label components. So as each label renders it produces a line of the output
 * that has the <code>li</code>tag.
 * 
 * 
 * @author Igor Vaynberg ( ivaynberg )
 * 
 */
public class RepeatingView extends AbstractRepeater
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Counter used for generating unique child component ids. */
	private long childIdCounter = 0;

	/** @see Component#Component(String) */
	public RepeatingView(String id)
	{
		super(id);
	}

	/** @see Component#Component(String, IModel) */
	public RepeatingView(String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * Generates a unique id string. This makes it easy to add items to be
	 * rendered w/out having to worry about generating unique id strings in your
	 * code.
	 * 
	 * @return unique child id
	 */
	public String newChildId()
	{
		childIdCounter++;

		if (childIdCounter == Long.MAX_VALUE)
		{
			// mmm yeah...like this will ever happen
			throw new RuntimeException("generateChildId() out of space.");
		}

		// We prepend the id's with the text 'id' so they will generate valid
		// markup id's if needed.
		return "id"+String.valueOf(childIdCounter);
	}

	/**
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#renderIterator()
	 */
	protected Iterator renderIterator()
	{
		return iterator();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14217.java