error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7325.java
text:
```scala
t@@his(predicate, trueClosure, NOPClosure.nopClosure());

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
package org.apache.commons.collections4.functors;

import java.io.Serializable;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;

/**
 * Closure implementation acts as an if statement calling one or other closure
 * based on a predicate.
 *
 * @since 3.0
 * @version $Id$
 */
public class IfClosure<E> implements Closure<E>, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = 3518477308466486130L;

    /** The test */
    private final Predicate<? super E> iPredicate;
    /** The closure to use if true */
    private final Closure<? super E> iTrueClosure;
    /** The closure to use if false */
    private final Closure<? super E> iFalseClosure;

    /**
     * Factory method that performs validation.
     * <p>
     * This factory creates a closure that performs no action when
     * the predicate is false.
     *
     * @param <E> the type that the closure acts on
     * @param predicate  predicate to switch on
     * @param trueClosure  closure used if true
     * @return the <code>if</code> closure
     * @throws IllegalArgumentException if either argument is null
     * @since 3.2
     */
    public static <E> Closure<E> ifClosure(final Predicate<? super E> predicate, final Closure<? super E> trueClosure) {
        return IfClosure.<E>ifClosure(predicate, trueClosure, NOPClosure.<E>nopClosure());
    }

    /**
     * Factory method that performs validation.
     *
     * @param <E> the type that the closure acts on
     * @param predicate  predicate to switch on
     * @param trueClosure  closure used if true
     * @param falseClosure  closure used if false
     * @return the <code>if</code> closure
     * @throws IllegalArgumentException if any argument is null
     */
    public static <E> Closure<E> ifClosure(final Predicate<? super E> predicate,
                                           final Closure<? super E> trueClosure,
                                           final Closure<? super E> falseClosure) {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate must not be null");
        }
        if (trueClosure == null || falseClosure == null) {
            throw new IllegalArgumentException("Closures must not be null");
        }
        return new IfClosure<E>(predicate, trueClosure, falseClosure);
    }

    /**
     * Constructor that performs no validation.
     * Use <code>ifClosure</code> if you want that.
     * <p>
     * This constructor creates a closure that performs no action when
     * the predicate is false.
     *
     * @param predicate  predicate to switch on, not null
     * @param trueClosure  closure used if true, not null
     * @since 3.2
     */
    public IfClosure(final Predicate<? super E> predicate, final Closure<? super E> trueClosure) {
        this(predicate, trueClosure, NOPClosure.INSTANCE);
    }

    /**
     * Constructor that performs no validation.
     * Use <code>ifClosure</code> if you want that.
     *
     * @param predicate  predicate to switch on, not null
     * @param trueClosure  closure used if true, not null
     * @param falseClosure  closure used if false, not null
     */
    public IfClosure(final Predicate<? super E> predicate, final Closure<? super E> trueClosure,
                     final Closure<? super E> falseClosure) {
        super();
        iPredicate = predicate;
        iTrueClosure = trueClosure;
        iFalseClosure = falseClosure;
    }

    /**
     * Executes the true or false closure according to the result of the predicate.
     *
     * @param input  the input object
     */
    public void execute(final E input) {
        if (iPredicate.evaluate(input)) {
            iTrueClosure.execute(input);
        } else {
            iFalseClosure.execute(input);
        }
    }

    /**
     * Gets the predicate.
     *
     * @return the predicate
     * @since 3.1
     */
    public Predicate<? super E> getPredicate() {
        return iPredicate;
    }

    /**
     * Gets the closure called when true.
     *
     * @return the closure
     * @since 3.1
     */
    public Closure<? super E> getTrueClosure() {
        return iTrueClosure;
    }

    /**
     * Gets the closure called when false.
     *
     * @return the closure
     * @since 3.1
     */
    public Closure<? super E> getFalseClosure() {
        return iFalseClosure;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7325.java