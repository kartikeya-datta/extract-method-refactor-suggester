error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12910.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12910.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12910.java
text:
```scala
c@@mds[i++] = closure;

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
package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.collections.Closure;

/**
 * Closure implementation that chains the specified closures together.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class ChainedClosure<E> implements Closure<E>, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = -3520677225766901240L;

    /** The closures to call in turn */
    private final Closure<? super E>[] iClosures;

    /**
     * Factory method that performs validation and copies the parameter array.
     * 
     * @param closures  the closures to chain, copied, no nulls
     * @return the <code>chained</code> closure
     * @throws IllegalArgumentException if the closures array is null
     * @throws IllegalArgumentException if any closure in the array is null
     */
    public static <E> Closure<E> getInstance(Closure<? super E>[] closures) {
        FunctorUtils.validate(closures);
        if (closures.length == 0) {
            return NOPClosure.<E>getInstance();
        }
        closures = FunctorUtils.copy(closures);
        return new ChainedClosure<E>(closures);
    }

    /**
     * Create a new Closure that calls each closure in turn, passing the 
     * result into the next closure. The ordering is that of the iterator()
     * method on the collection.
     * 
     * @param closures  a collection of closures to chain
     * @return the <code>chained</code> closure
     * @throws IllegalArgumentException if the closures collection is null
     * @throws IllegalArgumentException if any closure in the collection is null
     */
    @SuppressWarnings("unchecked")
    public static <E> Closure<E> getInstance(Collection<Closure<E>> closures) {
        if (closures == null) {
            throw new IllegalArgumentException("Closure collection must not be null");
        }
        if (closures.size() == 0) {
            return NOPClosure.<E>getInstance();
        }
        // convert to array like this to guarantee iterator() ordering
        Closure<? super E>[] cmds = new Closure[closures.size()];
        int i = 0;
        for (Closure<? super E> closure : closures) {
            cmds[i++] = (Closure<E>) closure;
        }
        FunctorUtils.validate(cmds);
        return new ChainedClosure<E>(cmds);
    }

    /**
     * Factory method that performs validation.
     * 
     * @param closure1  the first closure, not null
     * @param closure2  the second closure, not null
     * @return the <code>chained</code> closure
     * @throws IllegalArgumentException if either closure is null
     */
    @SuppressWarnings("unchecked")
    public static <E> Closure<E> getInstance(Closure<? super E> closure1, Closure<? super E> closure2) {
        if (closure1 == null || closure2 == null) {
            throw new IllegalArgumentException("Closures must not be null");
        }
        Closure<E>[] closures = new Closure[] { closure1, closure2 };
        return new ChainedClosure<E>(closures);
    }

    /**
     * Constructor that performs no validation.
     * Use <code>getInstance</code> if you want that.
     * 
     * @param closures  the closures to chain, not copied, no nulls
     */
    public ChainedClosure(Closure<? super E>[] closures) {
        super();
        iClosures = closures;
    }

    /**
     * Execute a list of closures.
     * 
     * @param input  the input object passed to each closure
     */
    public void execute(E input) {
        for (int i = 0; i < iClosures.length; i++) {
            iClosures[i].execute(input);
        }
    }

    /**
     * Gets the closures, do not modify the array.
     * @return the closures
     * @since Commons Collections 3.1
     */
    public Closure<? super E>[] getClosures() {
        return iClosures;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12910.java