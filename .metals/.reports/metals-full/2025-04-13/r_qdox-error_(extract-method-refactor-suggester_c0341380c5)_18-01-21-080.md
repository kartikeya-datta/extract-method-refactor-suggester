error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14589.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14589.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14589.java
text:
```scala
public static <@@E> Closure<E> chainedClosure(final Collection<? extends Closure<? super E>> closures) {

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
import java.util.Collection;

import org.apache.commons.collections4.Closure;

/**
 * Closure implementation that chains the specified closures together.
 *
 * @since 3.0
 * @version $Id$
 */
public class ChainedClosure<E> implements Closure<E>, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = -3520677225766901240L;

    /** The closures to call in turn */
    private final Closure<? super E>[] iClosures;

    /**
     * Factory method that performs validation and copies the parameter array.
     *
     * @param <E> the type that the closure acts on
     * @param closures  the closures to chain, copied, no nulls
     * @return the <code>chained</code> closure
     * @throws IllegalArgumentException if the closures array is null
     * @throws IllegalArgumentException if any closure in the array is null
     */
    public static <E> Closure<E> chainedClosure(final Closure<? super E>... closures) {
        FunctorUtils.validate(closures);
        if (closures.length == 0) {
            return NOPClosure.<E>nopClosure();
        }
        return new ChainedClosure<E>(closures);
    }

    /**
     * Create a new Closure that calls each closure in turn, passing the
     * result into the next closure. The ordering is that of the iterator()
     * method on the collection.
     *
     * @param <E> the type that the closure acts on
     * @param closures  a collection of closures to chain
     * @return the <code>chained</code> closure
     * @throws IllegalArgumentException if the closures collection is null
     * @throws IllegalArgumentException if any closure in the collection is null
     */
    @SuppressWarnings("unchecked")
    public static <E> Closure<E> chainedClosure(final Collection<Closure<E>> closures) {
        if (closures == null) {
            throw new IllegalArgumentException("Closure collection must not be null");
        }
        if (closures.size() == 0) {
            return NOPClosure.<E>nopClosure();
        }
        // convert to array like this to guarantee iterator() ordering
        final Closure<? super E>[] cmds = new Closure[closures.size()];
        int i = 0;
        for (final Closure<? super E> closure : closures) {
            cmds[i++] = closure;
        }
        FunctorUtils.validate(cmds);
        return new ChainedClosure<E>(false, cmds);
    }

    /**
     * Hidden constructor for the use by the static factory methods.
     *
     * @param clone  if {@code true} the input argument will be cloned
     * @param closures  the closures to chain, no nulls
     */
    private ChainedClosure(final boolean clone, final Closure<? super E>... closures) {
        super();
        iClosures = clone ? FunctorUtils.copy(closures) : closures;
    }

    /**
     * Constructor that performs no validation.
     * Use <code>chainedClosure</code> if you want that.
     *
     * @param closures  the closures to chain, copied, no nulls
     */
    public ChainedClosure(final Closure<? super E>... closures) {
        this(true, closures);
    }

    /**
     * Execute a list of closures.
     *
     * @param input  the input object passed to each closure
     */
    public void execute(final E input) {
        for (final Closure<? super E> iClosure : iClosures) {
            iClosure.execute(input);
        }
    }

    /**
     * Gets the closures.
     *
     * @return a copy of the closures
     * @since 3.1
     */
    public Closure<? super E>[] getClosures() {
        return FunctorUtils.<E>copy(iClosures);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14589.java