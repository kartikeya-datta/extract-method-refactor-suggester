error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2735.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2735.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[5,1]

error in qdox parser
file content:
```java
offset: 97
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2735.java
text:
```scala
private static class Entry {

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

p@@ackage org.xbill.DNS;

/**
 * DNS Name Compression object.
 * @see Name
 *
 * @author Brian Wellington
 */

class Compression {

private class Entry {
	Name name;
	int pos;
	Entry next;
}

private static final int TABLE_SIZE = 17;
private Entry [] table;
private boolean verbose = Options.check("verbosecompression");

/**
 * Creates a new Compression object.
 */
public
Compression() {
	table = new Entry[TABLE_SIZE];
}

/** Adds a compression entry mapping a name to a position.  */
public void
add(int pos, Name name) {
	int row = (name.hashCode() & 0x7FFFFFFF) % TABLE_SIZE;
	Entry entry = new Entry();
	entry.name = name;
	entry.pos = pos;
	entry.next = table[row];
	table[row] = entry;
	if (verbose)
		System.err.println("Adding " + name + " at " + pos);
}

/**
 * Retrieves the position of the given name, if it has been previously
 * included in the message.
 */
public int
get(Name name) {
	int row = (name.hashCode() & 0x7FFFFFFF) % TABLE_SIZE;
	int pos = -1;
	for (Entry entry = table[row]; entry != null; entry = entry.next) {
		if (entry.name.equals(name))
			pos = entry.pos;
	}
	if (verbose)
		System.err.println("Looking for " + name + ", found " + pos);
	return pos;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2735.java