error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5963.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5963.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5963.java
text:
```scala
f@@or (final Predicate<? super T> predicate : mockPredicatesToVerify) {

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

import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.replay;
import org.junit.Before;
import org.junit.After;
import org.apache.commons.collections.Predicate;
import org.easymock.EasyMock;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for tests of predicates which delegate to other predicates when evaluating an object.  This class
 * provides methods to create and verify mock predicates to which to delegate.
 *
 * @since 3.0
 * @version $Id$
 */
public abstract class AbstractMockPredicateTest<T> {
    /**
     * Mock predicates created by a single test case which need to be verified after the test completes.
     */
    private List<Predicate<? super T>> mockPredicatesToVerify;
    
    /**
     * The value to pass to mocks.
     */
    private final T testValue;

    /**
     * Creates a new <code>PredicateTestBase</code>.
     *
     * @param testValue the value to pass to mock predicates.
     */
    protected AbstractMockPredicateTest(final T testValue) {
        this.testValue = testValue;
    }

    /**
     * Creates the list of predicates to verify.
     */
    @Before
    public final void createVerifyList()
    {
        mockPredicatesToVerify = new ArrayList<Predicate<? super T>>();
    }

    /**
     * Verifies all the mock predicates created for the test.
     */
    @After
    public final void verifyPredicates()
    {
        for (Predicate<? super T> predicate : mockPredicatesToVerify) {
            verify(predicate);
        }
    }

    /**
     * Gets the value which will be passed to the mock predicates.
     *
     * @return the test value.
     */
    protected final T getTestValue() {
        return testValue;
    }

    /**
     * Creates a single mock predicate.
     *
     * @param returnValue the return value for the mock predicate, or null if the mock is not expected to be called.
     *
     * @return a single mock predicate.
     */
    @SuppressWarnings({"unchecked", "boxing"})
    protected final Predicate<T> createMockPredicate(final Boolean returnValue) {
        final Predicate<T> mockPredicate = EasyMock.createMock(Predicate.class);
        if (returnValue != null) {
            EasyMock.expect(mockPredicate.evaluate(testValue)).andReturn(returnValue);
        }
        replay(mockPredicate);
        mockPredicatesToVerify.add(mockPredicate);

        return mockPredicate;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5963.java