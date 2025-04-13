error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4436.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4436.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 590
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4436.java
text:
```scala
public class SpecialKey extends NaturalKey {

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

p@@ackage org.eclipse.ui.commands;

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
public class SpecialKey extends NonModifierKey {

	public final static SpecialKey ARROW_DOWN = new SpecialKey("ARROW_DOWN"); 
	public final static SpecialKey ARROW_LEFT = new SpecialKey("ARROW_LEFT"); 
	public final static SpecialKey ARROW_RIGHT = new SpecialKey("ARROW_RIGHT"); 
	public final static SpecialKey ARROW_UP = new SpecialKey("ARROW_UP"); 
	public final static SpecialKey END = new SpecialKey("END"); 
	public final static SpecialKey F1 = new SpecialKey("F1"); 
	public final static SpecialKey F10 = new SpecialKey("F10"); 
	public final static SpecialKey F11 = new SpecialKey("F11"); 
	public final static SpecialKey F12 = new SpecialKey("F12"); 
	public final static SpecialKey F2 = new SpecialKey("F2"); 
	public final static SpecialKey F3 = new SpecialKey("F3"); 
	public final static SpecialKey F4 = new SpecialKey("F4"); 
	public final static SpecialKey F5 = new SpecialKey("F5"); 
	public final static SpecialKey F6 = new SpecialKey("F6"); 
	public final static SpecialKey F7 = new SpecialKey("F7"); 
	public final static SpecialKey F8 = new SpecialKey("F8"); 
	public final static SpecialKey F9 = new SpecialKey("F9"); 
	public final static SpecialKey HOME = new SpecialKey("HOME"); 
	public final static SpecialKey INSERT = new SpecialKey("INSERT");	
	public final static SpecialKey PAGE_DOWN = new SpecialKey("PAGE_DOWN"); 
	public final static SpecialKey PAGE_UP = new SpecialKey("PAGE_UP"); 

	private SpecialKey(String name) {
		super(name);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4436.java