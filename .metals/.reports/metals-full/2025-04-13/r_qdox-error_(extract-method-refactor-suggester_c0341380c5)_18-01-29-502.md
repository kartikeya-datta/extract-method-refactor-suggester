error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/303.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/303.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/303.java
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

public final class GestureSequence implements Comparable {

	private final static int HASH_INITIAL = 47;
	private final static int HASH_FACTOR = 57;

	public static GestureSequence create() {
		return new GestureSequence(Collections.EMPTY_LIST);
	}

	public static GestureSequence create(GestureStroke gestureStroke)
		throws IllegalArgumentException {
		return new GestureSequence(Collections.singletonList(gestureStroke));
	}

	public static GestureSequence create(GestureStroke[] gestureStrokes)
		throws IllegalArgumentException {
		return new GestureSequence(Arrays.asList(gestureStrokes));
	}
	
	public static GestureSequence create(List gestureStrokes)
		throws IllegalArgumentException {
		return new GestureSequence(gestureStrokes);
	}

	public static GestureSequence parse(String gestureString)
		throws IllegalArgumentException {
		// TODO
		return create();
	}

	public static GestureSequence recognize(Point[] points, int sensitivity) {
		GestureStroke gestureStroke = null;
		List gestureStrokes = new ArrayList();
		int x0 = 0;
		int y0 = 0;

		for (int i = 0; i < points.length; i++) {
			Point point = points[i];

			if (i == 0) {
				x0 = point.getX();
				y0 = point.getY();
				continue;
			}

			int x1 = point.getX();
			int y1 = point.getY();
			int dx = (x1 - x0) / sensitivity;
			int dy = (y1 - y0) / sensitivity;

			if ((dx != 0) || (dy != 0)) {
				if (dx > 0 && !GestureStroke.EAST.equals(gestureStroke)) {
					gestureStrokes.add(gestureStroke = GestureStroke.EAST);
				} else if (dx < 0 && !GestureStroke.WEST.equals(gestureStroke)) {
					gestureStrokes.add(gestureStroke = GestureStroke.WEST);
				} else if (dy > 0 && !GestureStroke.SOUTH.equals(gestureStroke)) {
					gestureStrokes.add(gestureStroke = GestureStroke.SOUTH);
				} else if (dy < 0 && !GestureStroke.NORTH.equals(gestureStroke)) {
					gestureStrokes.add(gestureStroke = GestureStroke.NORTH);
				}

				x0 = x1;
				y0 = y1;
			}
		}

		return GestureSequence.create(gestureStrokes);
	}

	private List gestureStrokes;

	private GestureSequence(List gestureStrokes)
		throws IllegalArgumentException {
		super();
		
		if (gestureStrokes == null)
			throw new IllegalArgumentException();
			
		this.gestureStrokes = Collections.unmodifiableList(new ArrayList(gestureStrokes));
		Iterator iterator = this.gestureStrokes.iterator();
		
		while (iterator.hasNext())
			if (!(iterator.next() instanceof GestureStroke))
				throw new IllegalArgumentException();
	}

	public int compareTo(Object object) {
		return Util.compare(gestureStrokes, ((GestureSequence) object).gestureStrokes);
	}
	
	public boolean equals(Object object) {
		return object instanceof GestureSequence && gestureStrokes.equals(((GestureSequence) object).gestureStrokes);
	}
	
	public List getGestureStrokes() {
		return gestureStrokes;
	}

	public int hashCode() {
		int result = HASH_INITIAL;
		Iterator iterator = gestureStrokes.iterator();
		
		while (iterator.hasNext())
			result = result * HASH_FACTOR + ((GestureStroke) iterator.next()).hashCode();

		return result;
	}

	public boolean isChildOf(GestureSequence gestureSequence, boolean equals) {
		if (gestureSequence == null)
			return false;
		
		return Util.isChildOf(gestureStrokes, gestureSequence.gestureStrokes, equals);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/303.java