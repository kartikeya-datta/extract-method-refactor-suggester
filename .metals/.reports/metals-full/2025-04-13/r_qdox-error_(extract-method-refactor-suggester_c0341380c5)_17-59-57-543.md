error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/902.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/902.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/902.java
text:
```scala
private final static S@@tring KEY_STROKE_DELIMITERS = KEY_STROKE_DELIMITER + "\b\t\n\f\r\u001b\u007F"; //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.keys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.ui.internal.util.Util;

/**
 * <p>
 * JAVADOC
 * </p>
 * <p>
 * <em>EXPERIMENTAL</em>
 * </p>
 * 
 * @since 3.0
 */
public final class KeySequence implements Comparable {

	private final static char KEY_STROKE_DELIMITER = ' '; //$NON-NLS-1$
	private final static String KEY_STROKE_DELIMITERS = KEY_STROKE_DELIMITER + "\b\t\r\u001b\u007F"; //$NON-NLS-1$

	/**
	 * JAVADOC
	 * 
	 * @return
	 */		
	public static KeySequence getInstance() {
		return new KeySequence(Collections.EMPTY_LIST);
	}

	/**
	 * JAVADOC
	 * 
	 * @param keyStroke
	 * @return
	 */		
	public static KeySequence getInstance(KeyStroke keyStroke) {
		return new KeySequence(Collections.singletonList(keyStroke));
	}

	/**
	 * JAVADOC
	 * 
	 * @param keyStrokes
	 * @return
	 */		
	public static KeySequence getInstance(KeyStroke[] keyStrokes) {
		return new KeySequence(Arrays.asList(keyStrokes));
	}

	/**
	 * JAVADOC
	 * 
	 * @param keyStrokes
	 * @return
	 */		
	public static KeySequence getInstance(List keyStrokes) {
		return new KeySequence(keyStrokes);
	}

	/**
	 * JAVADOC
	 * 
	 * @param string
	 * @return
	 * @throws ParseException
	 */
	public static KeySequence getInstance(String string)
		throws ParseException {
		if (string == null)
			throw new NullPointerException();

		List keyStrokes = new ArrayList();
		StringTokenizer stringTokenizer = new StringTokenizer(string, KEY_STROKE_DELIMITERS);
				
		while (stringTokenizer.hasMoreTokens())
			keyStrokes.add(KeyStroke.getInstance(stringTokenizer.nextToken()));
			
		return new KeySequence(keyStrokes);
	}

	private List keyStrokes;
	
	private KeySequence(List keyStrokes) {
		super();
		this.keyStrokes = Util.safeCopy(keyStrokes, KeyStroke.class);
	}

	public int compareTo(Object object) {
		return Util.compare(keyStrokes, ((KeySequence) object).keyStrokes);
	}

	public boolean equals(Object object) {
		return object instanceof KeySequence && keyStrokes.equals(((KeySequence) object).keyStrokes);
	}

	/**
	 * JAVADOC
	 * 
	 * @return
	 */
	public List getKeyStrokes() {
		return keyStrokes;
	}

	public int hashCode() {
		return keyStrokes.hashCode();
	}

	/**
	 * JAVADOC
	 * 
	 * @param keySequence
	 * @param equals
	 * @return
	 */
	public boolean isChildOf(KeySequence keySequence, boolean equals) {
		if (keySequence == null)
			throw new NullPointerException();
		
		return Util.isChildOf(keyStrokes, keySequence.keyStrokes, equals);
	}

	public String toString() {
		int i = 0;
		Iterator iterator = keyStrokes.iterator();
		StringBuffer stringBuffer = new StringBuffer();
			
		while (iterator.hasNext()) {
			if (i != 0)
				stringBuffer.append(KEY_STROKE_DELIMITER);
	
			stringBuffer.append(((KeyStroke) iterator.next()).toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/902.java