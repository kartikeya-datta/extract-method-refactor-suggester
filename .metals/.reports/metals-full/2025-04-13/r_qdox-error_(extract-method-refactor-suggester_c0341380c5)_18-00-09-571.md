error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7673.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7673.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7673.java
text:
```scala
S@@tringBuilder builder = new StringBuilder(this.getClass().getSimpleName());

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
package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Abstract Pair (or 2-element Tuple).
 *
 * @since Lang 3.0
 * @version $Id$
 */
public abstract class Pair<L, R> implements Serializable, Map.Entry<L, R> {
    /** Serialization version */
    private static final long serialVersionUID = 4954918890077093841L;

    /**
     * Get the "left" element of the pair.
     * @return L
     */
    public abstract L getLeftElement();

    /**
     * Get the "right" element of the pair.
     * @return R
     */
    public abstract R getRightElement();

    /**
     * Return {@link #getLeftElement()} as a {@link java.util.Map.Entry}'s key.
     * @return L
     */
    public final L getKey() {
        return getLeftElement();
    }

    /**
     * Return {@link #getRightElement()} as a {@link java.util.Map.Entry}'s value.
     * @return R
     */
    public R getValue() {
        return getRightElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Pair<?, ?> == false) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return ObjectUtils.equals(getLeftElement(), other.getLeftElement())
                && ObjectUtils.equals(getRightElement(), other.getRightElement());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // TODO should the hashCodeBuilder be seeded per concrete type?
        return new HashCodeBuilder().append(getLeftElement()).append(getRightElement())
                .toHashCode();
    }

    /**
     * Returns a String representation of the Pair in the form: (L,R)
     * @return a string for this object
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(ClassUtils.getShortClassName(this, null));
        builder.append("(");
        builder.append(getLeftElement());
        builder.append(",");
        builder.append(getRightElement());
        builder.append(")");
        return builder.toString();
    }

    /**
     * Static fluent creation method for a {@link Pair}<L, R>:
     * <code>Pair.of(left, right)</code>
     * @param <L> the left generic type
     * @param <R> the right generic type
     * @param left the left value
     * @param right the right value
     * @return ImmutablePair<L, R>(left, right)
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new ImmutablePair<L, R>(left, right);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7673.java