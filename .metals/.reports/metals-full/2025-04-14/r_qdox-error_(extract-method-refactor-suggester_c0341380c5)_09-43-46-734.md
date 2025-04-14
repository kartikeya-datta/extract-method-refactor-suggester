error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4260.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4260.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4260.java
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
import java.util.StringTokenizer;

public final class KeySequence implements Comparable {

	private final static int HASH_INITIAL = 47;
	private final static int HASH_FACTOR = 57;
	private final static String KEY_STROKE_SEPARATOR = " "; //$NON-NLS-1$

	public static KeySequence create() {
		return new KeySequence(Collections.EMPTY_LIST);
	}

	public static KeySequence create(KeyStroke keyStroke)
		throws IllegalArgumentException {
		return new KeySequence(Collections.singletonList(keyStroke));
	}

	public static KeySequence create(KeyStroke[] keyStrokes)
		throws IllegalArgumentException {
		return new KeySequence(Arrays.asList(keyStrokes));
	}
	
	public static KeySequence create(List keyStrokes)
		throws IllegalArgumentException {
		return new KeySequence(keyStrokes);
	}

	public static KeySequence parse(String string)
		throws IllegalArgumentException {
		if (string == null)
			throw new IllegalArgumentException();

		List keyStrokes = new ArrayList();
		StringTokenizer stringTokenizer = new StringTokenizer(string);
				
		while (stringTokenizer.hasMoreTokens())
			keyStrokes.add(KeyStroke.parse(stringTokenizer.nextToken()));
			
		return create(keyStrokes);
	}

	private List keyStrokes;

	private KeySequence(List keyStrokes)
		throws IllegalArgumentException {
		super();
		this.keyStrokes = Collections.unmodifiableList(Util.safeCopy(keyStrokes, KeyStroke.class));
	}

	public int compareTo(Object object) {
		return Util.compare(keyStrokes, ((KeySequence) object).keyStrokes);
	}
	
	public boolean equals(Object object) {
		return object instanceof KeySequence && keyStrokes.equals(((KeySequence) object).keyStrokes);
	}
	
	public List getKeyStrokes() {
		return keyStrokes;
	}

	public int hashCode() {
		int result = HASH_INITIAL;
		Iterator iterator = keyStrokes.iterator();
		
		while (iterator.hasNext())
			result = result * HASH_FACTOR + ((KeyStroke) iterator.next()).hashCode();

		return result;
	}

	public boolean isChildOf(KeySequence keySequence, boolean equals) {
		if (keySequence == null)
			return false;
		
		return Util.isChildOf(keyStrokes, keySequence.keyStrokes, equals);
	}

	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		Iterator iterator = keyStrokes.iterator();
		int i = 0;
		
		while (iterator.hasNext()) {
			if (i != 0)
				stringBuffer.append(KEY_STROKE_SEPARATOR);

			stringBuffer.append(iterator.next());
			i++;
		}

		return stringBuffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4260.java