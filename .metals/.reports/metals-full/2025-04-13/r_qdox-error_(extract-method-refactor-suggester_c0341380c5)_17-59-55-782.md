error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10066.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10066.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10066.java
text:
```scala
r@@eturn wrapped.nextInt(n);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math3.random;

/**
 * Any {@link RandomGenerator} implementation can be thread-safe if it
 * is used through an instance of this class.
 * This is achieved by enclosing calls to the methods of the actual
 * generator inside the overridden {@code synchronized} methods of this
 * class.
 *
 * @since 3.1
 * @version $Id$
 */
public class SynchronizedRandomGenerator implements RandomGenerator {
    /** Object to which all calls will be delegated. */
    private final RandomGenerator wrapped;

    /**
     * Creates a synchronized wrapper for the given {@code RandomGenerator}
     * instance.
     *
     * @param rng Generator whose methods will be called through
     * their corresponding overridden synchronized version.
     * To ensure thread-safety, the wrapped generator <em>must</em>
     * not be used directly.
     */
    public SynchronizedRandomGenerator(RandomGenerator rng) {
        wrapped = rng;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setSeed(int seed) {
        wrapped.setSeed(seed);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setSeed(int[] seed) {
        wrapped.setSeed(seed);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setSeed(long seed) {
        wrapped.setSeed(seed);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void nextBytes(byte[] bytes) {
        wrapped.nextBytes(bytes);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int nextInt() {
        return wrapped.nextInt();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int nextInt(int n) {
        return wrapped.nextInt();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long nextLong() {
        return wrapped.nextLong();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean nextBoolean() {
        return wrapped.nextBoolean();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized float nextFloat() {
        return wrapped.nextFloat();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized double nextDouble() {
        return wrapped.nextDouble();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized double nextGaussian() {
        return wrapped.nextGaussian();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10066.java