error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8791.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8791.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8791.java
text:
```scala
I@@SourceLocation getSourceLocation();

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.bridge;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
  * Wrap message with any associated throwable or source location.
  */
public interface IMessage {
	/** no messages */
	public static final IMessage[] RA_IMessage = new IMessage[0];

	// int values must sync with KINDS order below
	public static final Kind INFO = new Kind("info", 10);
	public static final Kind DEBUG = new Kind("debug", 20);
	public static final Kind WARNING = new Kind("warning", 30);
	public static final Kind ERROR = new Kind("error", 40);
	public static final Kind FAIL = new Kind("fail", 50);
	public static final Kind ABORT = new Kind("abort", 60);
	// XXX prefer another Kind to act as selector for "any",
	// but can't prohibit creating messages with it.
	//public static final Kind ANY = new Kind("any-selector", 0);

	/** list of Kind in precedence order. 0 is less than 
	 * IMessage.Kind.COMPARATOR.compareTo(KINDS.get(i), KINDS.get(i + 1))
	 */
	public static final List KINDS =
		Collections.unmodifiableList(
			Arrays.asList(
				new Kind[] { INFO, DEBUG, WARNING, ERROR, FAIL, ABORT }));

	/** @return non-null String with simple message */
	String getMessage();

	/** @return the kind of this message */
	Kind getKind();

	/** @return true if this is an error */
	boolean isError();

	/** @return true if this is a warning */
	boolean isWarning();

	/** @return true if this is an internal debug message */
	boolean isDebug();

	/** @return true if this is information for the user  */
	boolean isInfo();

	/** @return true if the process is aborting  */
	boolean isAbort(); // XXX ambiguous

	/** @return true if something failed   */
	boolean isFailed();

	/** @return Throwable associated with this message, or null if none */
	Throwable getThrown();

	/** @return source location associated with this message, or null if none */
	ISourceLocation getISourceLocation();

	public static final class Kind implements Comparable {
		public static final Comparator COMPARATOR = new Comparator() {
			public int compare(Object o1, Object o2) {
				Kind one = (Kind) o1;
				Kind two = (Kind) o2;
				if (null == one) {
					return (null == two ? 0 : -1);
				} else if (null == two) {
					return 1;
				} else if (one == two) {
					return 0;
				} else {
					return (one.precedence - two.precedence);
				}
			}
		};
        
        /**
         * @param kind the Kind floor
         * @return false if kind is null or this
         *         has less precedence than kind,
         *         true otherwise.
         */
		public boolean isSameOrLessThan(Kind kind) {
            return (0 >= COMPARATOR.compare(this, kind));
		}
        
		public int compareTo(Object other) {
			return COMPARATOR.compare(this, other);
		}

		private final int precedence;
		private final String name;

		private Kind(String name, int precedence) {
			this.name = name;
			this.precedence = precedence;
		}
		public String toString() {
			return name;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8791.java