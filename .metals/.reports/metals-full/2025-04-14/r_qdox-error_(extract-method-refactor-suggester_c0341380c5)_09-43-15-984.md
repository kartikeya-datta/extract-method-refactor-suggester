error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1360.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1360.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1074
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1360.java
text:
```scala
public interface AsynchEventProcessor {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
p@@ackage org.jboss.as.clustering.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jboss.logging.Logger;

/**
 * Utility class that accepts objects into a queue and maintains a separate thread that reads them off the queue and passes them
 * to a registered "processor".
 *
 * @todo find a better home for this than the cluster module
 *
 * @author <a href="mailto://brian.stansberry@jboss.com">Brian Stansberry</a>
 */
class AsynchEventHandler implements Runnable {
    /**
     * Interface implemented by classes able to process the objects placed into an AsynchEventHandler's queue.
     */
    public static interface AsynchEventProcessor {
        void processEvent(Object event);
    }

    private String name;
    /** The LinkedQueue of events to pass to our processor */
    private BlockingQueue<Object> events = new LinkedBlockingQueue<Object>();
    /** Whether we're blocking on the queue */
    private boolean blocking;
    private AsynchEventProcessor processor;
    private boolean stopped = true;
    private Thread handlerThread;
    private final ClusteringImplLogger log;

    /**
     * Create a new AsynchEventHandler.
     *
     * @param processor object to which objects placed in the queue should be handed when dequeued
     * @param name name for this instance. Appended to the processor's class name to create a log category, and used to name to
     *        handler thread
     */
    public AsynchEventHandler(AsynchEventProcessor processor, String name) {
        super();
        this.processor = processor;
        if (name == null)
            name = "AsynchEventHandler";
        this.name = name;
        this.log = Logger.getMessageLogger(ClusteringImplLogger.class, processor.getClass().getName() + "." + name);
    }

    /**
     * Place the given object in the queue.
     *
     * @param event the object to asynchronously pass to the AsynchEventHandler.
     *
     * @throws InterruptedException if the thread is interrupted while blocking on the queue.
     */
    public void queueEvent(Object event) throws InterruptedException {
        if (event != null)
            events.add(event);
    }

    @Override
    public void run() {
        log.debugf("Begin %s Thread", name);
        stopped = false;
        boolean intr = false;
        try {
            while (!stopped) {
                try {
                    blocking = true;
                    Object event = events.take();
                    blocking = false;

                    if (!stopped) {
                        processor.processEvent(event);
                    }
                } catch (InterruptedException e) {
                    intr = true;
                    blocking = false;
                    if (stopped) {
                        log.debugf("%s Thread interrupted", name);
                        break;
                    }
                    log.threadInterrupted(e, name);
                } catch (Throwable t) {
                    log.errorHandlingAsyncEvent(t);
                }
            }
            log.debugf("End %s Thread", name);
        } finally {
            if (intr)
                Thread.currentThread().interrupt();
        }
    }

    /**
     * Starts the handler thread.
     */
    public void start() {
        handlerThread = new Thread(this, name + " Thread");
        handlerThread.start();
    }

    /**
     * Stops the handler thread.
     */
    public void stop() {
        stopped = true;
        if (blocking)
            handlerThread.interrupt(); // it's just waiting on the LinkedQueue

        if (handlerThread.isAlive()) {
            // Give it up to 100ms to finish whatever it's doing
            try {
                handlerThread.join(100);
            } catch (Exception ignored) {
            }
        }

        if (handlerThread.isAlive())
            handlerThread.interrupt(); // kill it
    }

    public boolean isStopped() {
        return stopped;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1360.java