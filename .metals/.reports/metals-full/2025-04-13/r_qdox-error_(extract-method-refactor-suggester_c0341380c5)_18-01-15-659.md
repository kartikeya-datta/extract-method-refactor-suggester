error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4724.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4724.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4724.java
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

package org.eclipse.ui.internal.commands.gestures;

import java.util.Arrays;

import org.eclipse.ui.internal.commands.*;

public final class Gesture implements Comparable {

	private final static int HASH_INITIAL = 27;
	private final static int HASH_FACTOR = 37;

	public static Gesture create(int data, int pen, Point[] points)
		throws IllegalArgumentException {
		return new Gesture(data, pen, points);
	}

	private int data;
	private int pen;
	private Point[] points;

	private Gesture(int data, int pen, Point[] points)
		throws IllegalArgumentException {
		super();
		this.data = data;
		this.pen = pen;

		if (points == null)
			throw new IllegalArgumentException();
		
		points = (Point[]) points.clone();

		for (int i = 0; i < points.length; i++)
			if (points[i] == null)
				throw new IllegalArgumentException();
	
		this.points = points;
	}

	public int compareTo(Object object) {
		Gesture gesture = (Gesture) object;
		int compareTo = data - gesture.data;

		if (compareTo == 0) {
			compareTo = pen - gesture.pen;

			if (compareTo == 0)
				compareTo = org.eclipse.ui.internal.commands.Util.compare(points, gesture.points);
		}

		return compareTo;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Gesture))
			return false;

		Gesture gesture = (Gesture) object;
		return data == gesture.data && pen == gesture.pen && Arrays.equals(points, gesture.points);
	}

	public int getData() {
		return data;
	}

	public int getPen() {
		return pen;
	}

	public Point[] getPoints() {
		return (Point[]) points.clone();
	}

	public int hashCode() {
		int result = HASH_INITIAL;
		result = result * HASH_FACTOR + data;
		result = result * HASH_FACTOR + pen;

		for (int i = 0; i < points.length; i++)
			result = result * HASH_FACTOR + points[i].hashCode();
		
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4724.java