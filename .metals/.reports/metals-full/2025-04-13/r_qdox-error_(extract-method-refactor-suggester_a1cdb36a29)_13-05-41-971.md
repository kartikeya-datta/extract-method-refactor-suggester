error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18273.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18273.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[12,1]

error in qdox parser
file content:
```java
offset: 583
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18273.java
text:
```scala
public class SimpleQueueImpl implements ISimpleQueue {

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

p@@ackage org.eclipse.ecf.core.util;

import java.util.List;
import java.util.LinkedList;


public class SimpleQueueImpl implements SimpleQueue {
    List list;
    boolean stopped;

    public SimpleQueueImpl() {
        list = new LinkedList();
        stopped = false;
    }

    public synchronized boolean enqueue(Object obj) {
        if (isStopped() || obj == null) {
            return false;
        }
        // Add item to the list
        list.add(obj);
        // Notify waiting thread. Dequeue should only be read by one thread, so
        // only need
        // notify() rather than notifyAll().
        notify();
        return true;
    }

    public synchronized Object dequeue() {
        Object val = peekQueue();
        if (val != null) {
            removeHead();
        }
        return val;
    }

    public synchronized Object peekQueue() {
        while (isEmpty()) {
            if (stopped) {
                return null;
            } else {
                try {
                    wait();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return list.get(0);
    }

    public synchronized Object peekQueue(long waitMS) {
        if (waitMS == 0)
            return peekQueue();
        if (stopped) {
            return null;
        } else {
            try {
                wait(waitMS);
            } catch (Exception e) {
                return null;
            }
        }
        if (isEmpty())
            return null;
        else
            return list.get(0);
    }

    public synchronized Object removeHead() {
        if (list.isEmpty())
            return null;
        return list.remove(0);
    }

    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    public synchronized void stop() {
        stopped = true;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    public synchronized int size() {
        return list.size();
    }
    public synchronized Object[] flush() {
        Object[] out = list.toArray();
        list.clear();
        close();
        return out;
    }

    public synchronized void close() {
        stop();
        notifyAll();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("SimpleQueueImpl[");
        sb.append(list).append("]");
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18273.java