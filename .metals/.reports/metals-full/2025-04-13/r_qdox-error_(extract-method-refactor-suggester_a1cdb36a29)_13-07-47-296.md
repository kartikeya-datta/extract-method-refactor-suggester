error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7082.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7082.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7082.java
text:
```scala
C@@opyright (c) 2003 IBM Corporation and others.

/************************************************************************
Copyright (c) 2002 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Path implements Comparable {

	final static int MAXIMUM_PATH_ITEMS = 16;

	private final static int HASH_INITIAL = 87;
	private final static int HASH_FACTOR = 97;
	
	static Path create() {
		return new Path(Collections.EMPTY_LIST);
	}

	static Path create(String pathItem)
		throws IllegalArgumentException {
		return new Path(Collections.singletonList(pathItem));
	}

	static Path create(String[] pathItems)
		throws IllegalArgumentException {
		return new Path(Arrays.asList(pathItems));
	}

	static Path create(List pathItems)
		throws IllegalArgumentException {
		return new Path(pathItems);
	}

	private List pathItems;

	private Path(List pathItems)
		throws IllegalArgumentException {
		super();
		
		if (pathItems == null)
			throw new IllegalArgumentException();
		
		this.pathItems = Collections.unmodifiableList(new ArrayList(pathItems));

		if (this.pathItems.size() >= MAXIMUM_PATH_ITEMS)
			throw new IllegalArgumentException();
		
		Iterator iterator = this.pathItems.iterator();
		
		while (iterator.hasNext())
			if (!(iterator.next() instanceof String))
				throw new IllegalArgumentException();
	}

	public int compareTo(Object object) {
		return Util.compare(pathItems, ((Path) object).pathItems);
	}
	
	public boolean equals(Object object) {
		return object instanceof Path && pathItems.equals(((Path) object).pathItems);
	}

	public List getPathItems() {
		return pathItems;
	}

	public int hashCode() {
		int result = HASH_INITIAL;
		Iterator iterator = pathItems.iterator();
		
		while (iterator.hasNext())
			result = result * HASH_FACTOR + iterator.next().hashCode();

		return result;
	}

	public boolean isChildOf(Path path, boolean equals) {
		if (path == null)
			return false;

		return Util.isChildOf(pathItems, path.pathItems, equals);
	}

	public int match(Path path)
		throws IllegalArgumentException {
		if (path == null)
			throw new IllegalArgumentException();
			
		if (path.isChildOf(this, true)) 
			return path.pathItems.size() - pathItems.size();
		else 
			return -1;
	}

	public String toString() {
		return pathItems.toString();	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7082.java