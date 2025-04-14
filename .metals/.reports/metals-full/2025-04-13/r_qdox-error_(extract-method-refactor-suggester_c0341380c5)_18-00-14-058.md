error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18271.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18271.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[12,1]

error in qdox parser
file content:
```java
offset: 561
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18271.java
text:
```scala
public interface IQueueDequeue {

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

p@@ackage org.eclipse.ecf.core.util;

public interface QueueDequeue {
    /*
     * Dequeue a single event. Returns null if no Events available for dequeue
     * @return Event the Event dequeued. Null if queue is empty.
     */
    Event dequeue();
    /*
     * Dequeue several events in one operation. num events are dequeued.
     * 
     * @return Event[] the Events dequeue. Returns null if there are not
     * sufficient events on queue to support dequeuing num events
     */
    Event[] dequeue(int num);
    /*
     * Dequeue all available Events. @return Event[] the events on this queue.
     * Returns null if there are no events in queue
     */
    Event[] dequeue_all();
    /*
     * Dequeue a single Event. Blocks until an Event is available for dequeue,
     * or until timeout_millis have elapsed. If timeout_millis is -1, dequeue
     * does not timeout. @param timeout_millis the timeout for a dequeue in
     * milliseconds. @return Event removed from queue. Returns null if no events
     * on queue.
     */
    Event blocking_dequeue(int timeout_millis);
    /*
     * Dequeue a multiple Events. Blocks until num Events are available for
     * dequeue, or until timeout_millis have elapsed. If timeout_millis is -1,
     * dequeue does not timeout. @param timeout_millis the timeout for a dequeue
     * in milliseconds. @param num the number of Events to dequeue @return Event []
     * the num Events removed from queue
     */
    Event[] blocking_dequeue(int timeout_millis, int num);
    /*
     * Dequeue all Events currently on queue. Blocks until num Events are
     * available for dequeue, or until timeout_millis have elapsed. If
     * timeout_millis is -1, dequeue does not timeout. @param timeout_millis the
     * timeout for a dequeue in milliseconds. @param num the number of Events to
     * dequeue @return Event [] the num Events removed from queue
     */
    Event[] blocking_dequeue_all(int timeout_millis);
    /*
     * Provide the current size of the queue (the number of Events) currently on
     * the queue. @return size the int size of the queue
     */
    int size();
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18271.java