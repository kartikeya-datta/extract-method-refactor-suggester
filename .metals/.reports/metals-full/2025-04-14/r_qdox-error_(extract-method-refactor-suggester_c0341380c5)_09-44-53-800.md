error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7374.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7374.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7374.java
text:
```scala
r@@eturn get(emptyFilter);

/*
 * Copyright (C) 2002-2003, Simon Nieuviarts
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 */
package org.objectweb.carol.cmi;

import java.rmi.Remote;
import java.util.Collection;
import java.util.Iterator;


public class RoundRobin extends StubLB {
    private ClusterStubData csd;
    private int len;
    private StubData[] sd;
    private double[] load;
    private double minLoad;

    /**
     * Builds a round robin algorithm on a Collection of StubData objects.
     * @param c a Collection of StubData objects.
     */
    public RoundRobin(ClusterStubData csd, Collection c) {
        this.csd = csd;
        len = c.size();
        sd = new StubData[len];
        load = new double[len];
        Iterator it = c.iterator();
        for (int i = 0; i < len; i++) {
            StubData s = (StubData) it.next();
            sd[i] = s;
        }

        /* a random start choice
         * TODO A fairer choice is a number form 0.0 to 1.0, and simulate a load where
         * each stub has just passed this value
         */
        for (int i = 0; i<SecureRandom.getInt(len); i++) {
            load[i] = sd[i].getLoadIncr();
        }
    }

    private synchronized void ensureCapacity(int minCapacity) {
        int old = sd.length;
        if (old >= minCapacity)
            return;
        int l = (old * 3) / 2 + 1;
        if (l < minCapacity)
            l = minCapacity;
        StubData[] nsd = new StubData[l];
        double[] nload = new double[l];
        System.arraycopy(sd, 0, nsd, 0, old);
        System.arraycopy(load, 0, nload, 0, old);
        sd = nsd;
        load = nload;
    }

    /**
     * This method must be called only by the ClusterStubData to ensure integrity
     * between this load balancer and the cluster stub.
     * @see org.objectweb.carol.cmi.lb.StubLB#add(org.objectweb.carol.cmi.StubData)
     */
    synchronized void add(StubData sd) {
        ensureCapacity(len + 1);
        this.sd[len] = sd;
        load[len] = minLoad;
        len++;
    }

    /**
     * This method must be called only by the ClusterStubData to ensure integrity
     * between this load balancer and the cluster stub.
     * @see org.objectweb.carol.cmi.lb.StubLB#remove(org.objectweb.carol.cmi.StubData)
     */
    synchronized void remove(StubData s) {
        for (int i=0; i<len; i++) {
            if (sd[i] == s) {
                len--;
                sd[i] = sd[len];
                sd[len] = null;
                load[i] = load[len];
                return;
            }
        }
    }

    private static StubLBFilter emptyFilter = new StubLBFilter();

    public synchronized Remote get() throws NoMoreStubException {
        return get(null);
    }

    public synchronized Remote get(StubLBFilter f) throws NoMoreStubException {
        double min = Double.MAX_VALUE;
        double minOk = Double.MAX_VALUE;
        int index = -1;
        for (int i=0; i<len; i++) {
            double l = load[i];
            if (l < minOk) {
                if (!f.contains(sd[i].getStub())) {
                    minOk = l;
                    index = i;
                }
                if (l < min) {
                    min = l;
                }
            }
        }

        if (index < 0) {
            throw new NoMoreStubException();
        }

        // to avoid overflow, restart values when the min is relatively high
        if (min >= 100.0) {
            for (int i=0; i<len; i++) {
                load[i] -= min;
            }
            min = 0;
        }

        StubData s = sd[index];
        load[index] += s.getLoadIncr();
        return s.getStub();
    }

    /**
     * @see org.objectweb.carol.cmi.StubLB#remove(java.rmi.Remote)
     */
    public void remove(Remote stub) {
        csd.removeStub(stub);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7374.java