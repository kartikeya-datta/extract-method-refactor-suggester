error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/833.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/833.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/833.java
text:
```scala
S@@et s = (Set) (MAP.get(f.parent));

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.types.resources;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Helper class for ResourceCollections to return Iterators
 * that fail on changes to the object.
 * @since Ant 1.7
 */
/*package-private*/ class FailFast implements Iterator {
    private static final WeakHashMap MAP = new WeakHashMap();

    /**
     * Invalidate any in-use Iterators from the specified Object.
     * @param o the parent Object.
     */
    static synchronized void invalidate(Object o) {
        Set s = (Set) (MAP.get(o));
        if (s != null) {
            s.clear();
        }
    }

    private static synchronized void add(FailFast f) {
        Set s = (Set) (MAP.get(f.parent));
        if (s == null) {
            s = new HashSet();
            MAP.put(f.parent, s);
        }
        s.add(f);
    }

    private static synchronized void remove(FailFast f) {
        Set s = (Set) (MAP.get(f.parent));
        if (s != null) {
            s.remove(f);
        }
    }

    private static synchronized void failFast(FailFast f) {
        Set s = (Set) (map.get(f.parent));
        if (!s.contains(f)) {
            throw new ConcurrentModificationException();
        }
    }

    private Object parent;
    private Iterator wrapped;

    /**
     * Construct a new FailFast Iterator wrapping the specified Iterator
     * and dependent upon the specified parent Object.
     * @param o the parent Object.
     * @param i the wrapped Iterator.
     */
    FailFast(Object o, Iterator i) {
        if (o == null) {
            throw new IllegalArgumentException("parent object is null");
        }
        if (i == null) {
            throw new IllegalArgumentException("cannot wrap null iterator");
        }
        parent = o;
        if (i.hasNext()) {
            wrapped = i;
            add(this);
        }
    }

    /**
     * Fulfill the Iterator contract.
     * @return true if there are more elements.
     */
    public boolean hasNext() {
        if (wrapped == null) {
            return false;
        }
        failFast(this);
        return wrapped.hasNext();
    }

    /**
     * Fulfill the Iterator contract.
     * @return the next element.
     * @throws NoSuchElementException if no more elements.
     */
    public Object next() {
        if (wrapped == null || !wrapped.hasNext()) {
            throw new NoSuchElementException();
        }
        failFast(this);
        try {
            return wrapped.next();
        } finally {
            if (!wrapped.hasNext()) {
                wrapped = null;
                remove(this);
            }
        }
    }

    /**
     * Fulfill the Iterator contract.
     * @throws UnsupportedOperationException always.
     */
    public void remove() {
        throw new UnsupportedOperationException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/833.java