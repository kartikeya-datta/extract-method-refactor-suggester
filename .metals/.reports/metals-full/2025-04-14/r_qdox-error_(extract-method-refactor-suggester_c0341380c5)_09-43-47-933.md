error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5962.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5962.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5962.java
text:
```scala
f@@or (final Boolean returnValue : mockReturnValues) {

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

import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Base class for tests of composite predicates.
 *
 * @since 3.0
 * @version $Id$
 */
public abstract class AbstractCompositePredicateTest<T> extends AbstractMockPredicateTest<T> {

    /**
     * Creates a new <code>TestCompositePredicate</code>.
     *
     * @param testValue the value which the mock predicates should expect to see (may be null).
     */
    protected AbstractCompositePredicateTest(final T testValue) {
        super(testValue);
    }

    /**
     * Creates an instance of the predicate to test.
     *
     * @param predicates the arguments to <code>getInstance</code>.
     *
     * @return a predicate to test.
     */
    protected abstract Predicate<T> getPredicateInstance(final Predicate<? super T> ... predicates);

    /**
     * Creates an instance of the predicate to test.
     *
     * @param predicates the argument to <code>getInstance</code>.
     *
     * @return a predicate to test.
     */
    protected abstract Predicate<T> getPredicateInstance(final Collection<Predicate<T>> predicates);

    /**
     * Creates an instance of the predicate to test.
     *
     * @param mockReturnValues the return values for the mock predicates, or null if that mock is not expected
     *                         to be called
     *
     * @return a predicate to test.
     */
    protected final Predicate<T> getPredicateInstance(final Boolean... mockReturnValues) {
        final List<Predicate<T>> predicates = new ArrayList<Predicate<T>>();
        for (Boolean returnValue : mockReturnValues) {
            predicates.add(createMockPredicate(returnValue));
        }
        return getPredicateInstance(predicates);
    }

    /**
     * Tests whether <code>getInstance</code> with a one element array returns the first element in the array.
     */
    @SuppressWarnings("unchecked")
    public void singleElementArrayToGetInstance() {
        final Predicate<T> predicate = createMockPredicate(null);
        final Predicate<T> allPredicate = getPredicateInstance(predicate);
        Assert.assertSame("expected argument to be returned by getInstance()", predicate, allPredicate);
    }

    /**
     * Tests that passing a singleton collection to <code>getInstance</code> returns the single element in the
     * collection.
     */
    public void singletonCollectionToGetInstance() {
        final Predicate<T> predicate = createMockPredicate(null);
        final Predicate<T> allPredicate = getPredicateInstance(
                Collections.<Predicate<T>>singleton(predicate));
        Assert.assertSame("expected argument to be returned by getInstance()", predicate, allPredicate);
    }

    /**
     * Tests <code>getInstance</code> with a null predicate array.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void nullArrayToGetInstance() {
        getPredicateInstance((Predicate<T>[]) null);
    }

    /**
     * Tests <code>getInstance</code> with a single null element in the predicate array.
     */
    @SuppressWarnings({"unchecked"})
    @Test(expected = IllegalArgumentException.class)
    public final void nullElementInArrayToGetInstance() {
        getPredicateInstance(new Predicate[] { null });
    }

    /**
     * Tests <code>getInstance</code> with two null elements in the predicate array.
     */
    @SuppressWarnings({"unchecked"})
    @Test(expected = IllegalArgumentException.class)
    public final void nullElementsInArrayToGetInstance() {
        getPredicateInstance(new Predicate[] { null, null });
    }


    /**
     * Tests <code>getInstance</code> with a null predicate collection
     */
    @Test(expected = IllegalArgumentException.class)
    public final void nullCollectionToGetInstance() {
        getPredicateInstance((Collection<Predicate<T>>) null);
    }

    /**
     * Tests <code>getInstance</code> with a predicate collection that contains null elements
     */
    @Test(expected = IllegalArgumentException.class)
    public final void nullElementsInCollectionToGetInstance() {
        final Collection<Predicate<T>> coll = new ArrayList<Predicate<T>>();
        coll.add(null);
        coll.add(null);
        getPredicateInstance(coll);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5962.java