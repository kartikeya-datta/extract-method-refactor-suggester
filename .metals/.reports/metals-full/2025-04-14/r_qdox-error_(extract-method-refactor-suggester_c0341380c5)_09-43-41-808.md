error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17880.java
text:
```scala
public L@@ist<IBehavior> getBehaviorsRawList()

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
package org.apache.wicket;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.util.string.Strings;

/**
 * Simple {@link IRequestableComponent} implementation for testing purposes
 * 
 * @author Matej Knopp
 */
public class MockComponent implements IRequestableComponent
{

	private static final long serialVersionUID = 1L;

	private String markupId;
	private String id;
	private IRequestablePage page;
	private String path;

	/**
	 * Construct.
	 */
	public MockComponent()
	{
	}

	public IRequestableComponent get(String path)
	{
		MockComponent c = new MockComponent();
		if (Strings.isEmpty(getPageRelativePath()))
		{
			c.setPath(path);
		}
		else
		{
			c.setPath(getPageRelativePath() + ":" + path);
		}
		c.setPage(getPage());
		c.setId(Strings.lastPathComponent(path, ':'));
		return c;
	}

	public String getId()
	{
		return id;
	}

	/**
	 * Sets the component id
	 * 
	 * @param id
	 * @return <code>this</code>
	 */
	public MockComponent setId(String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Sets the markup Id
	 * 
	 * @param markupId
	 * @return <code>this</code>
	 */
	public MockComponent setMarkupId(String markupId)
	{
		this.markupId = markupId;
		return this;
	}

	public String getMarkupId(boolean createIfDoesNotExist)
	{
		return markupId;
	}

	public IRequestablePage getPage()
	{
		return page;
	}

	/**
	 * Sets the page instance
	 * 
	 * @param page
	 * @return <code>this</code>
	 */
	public MockComponent setPage(IRequestablePage page)
	{
		this.page = page;
		return this;
	}

	public String getPageRelativePath()
	{
		return path;
	}

	/**
	 * Sets the component path
	 * 
	 * @param path
	 * @return <code>this</code>
	 */
	public MockComponent setPath(String path)
	{
		this.path = path;
		return this;
	}

	public void detach()
	{
	}

	public boolean canCallListenerInterface()
	{
		return true;
	}

	public List<IBehavior> getBehaviors()
	{
		return Collections.emptyList();
	}

	public boolean isEnabledInHierarchy()
	{
		return false;
	}

	public boolean isVisibleInHierarchy()
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17880.java