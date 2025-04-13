error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12458.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12458.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12458.java
text:
```scala
public v@@oid testHandleCauseChecked() {

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
package org.apache.commons.lang.concurrent;

import java.util.concurrent.ExecutionException;

import junit.framework.TestCase;

/**
 * Test class for {@link ConcurrentUtils}.
 *
 * @version $Id$
 */
public class ConcurrentUtilsTest extends TestCase {
    /**
     * Tests creating a ConcurrentException with a runtime exception as cause.
     */
    public void testConcurrentExceptionCauseUnchecked() {
        try {
            new ConcurrentException(new RuntimeException());
            fail("Could create ConcurrentException with unchecked cause!");
        } catch (IllegalArgumentException iex) {
            // ok
        }
    }

    /**
     * Tests creating a ConcurrentException with an error as cause.
     */
    public void testConcurrentExceptionCauseError() {
        try {
            new ConcurrentException("An error", new Error());
            fail("Could create ConcurrentException with an error cause!");
        } catch (IllegalArgumentException iex) {
            // ok
        }
    }

    /**
     * Tests creating a ConcurrentException with null as cause.
     */
    public void testConcurrentExceptionCauseNull() {
        try {
            new ConcurrentException(null);
            fail("Could create ConcurrentException with null cause!");
        } catch (IllegalArgumentException iex) {
            // ok
        }
    }

    /**
     * Tests extractCause() for a null exception.
     */
    public void testExtractCauseNull() {
        assertNull("Non null result", ConcurrentUtils.extractCause(null));
    }

    /**
     * Tests extractCause() if the cause of the passed in exception is null.
     */
    public void testExtractCauseNullCause() {
        assertNull("Non null result", ConcurrentUtils
                .extractCause(new ExecutionException("Test", null)));
    }

    /**
     * Tests extractCause() if the cause is an error.
     */
    public void testExtractCauseError() {
        Error err = new AssertionError("Test");
        try {
            ConcurrentUtils.extractCause(new ExecutionException(err));
            fail("Error not thrown!");
        } catch (Error e) {
            assertEquals("Wrong error", err, e);
        }
    }

    /**
     * Tests extractCause() if the cause is an unchecked exception.
     */
    public void testExtractCauseUnchecked() {
        RuntimeException rex = new RuntimeException("Test");
        try {
            ConcurrentUtils.extractCause(new ExecutionException(rex));
            fail("Runtime exception not thrown!");
        } catch (RuntimeException r) {
            assertEquals("Wrong exception", rex, r);
        }
    }

    /**
     * Tests extractCause() if the cause is a checked exception.
     */
    public void testExtractCauseChecked() {
        Exception ex = new Exception("Test");
        ConcurrentException cex = ConcurrentUtils
                .extractCause(new ExecutionException(ex));
        assertSame("Wrong cause", ex, cex.getCause());
    }

    /**
     * Tests handleCause() if the cause is an error.
     */
    public void testHandleCauseError() throws ConcurrentException {
        Error err = new AssertionError("Test");
        try {
            ConcurrentUtils.handleCause(new ExecutionException(err));
            fail("Error not thrown!");
        } catch (Error e) {
            assertEquals("Wrong error", err, e);
        }
    }

    /**
     * Tests handleCause() if the cause is an unchecked exception.
     */
    public void testHandleCauseUnchecked() throws ConcurrentException {
        RuntimeException rex = new RuntimeException("Test");
        try {
            ConcurrentUtils.handleCause(new ExecutionException(rex));
            fail("Runtime exception not thrown!");
        } catch (RuntimeException r) {
            assertEquals("Wrong exception", rex, r);
        }
    }

    /**
     * Tests handleCause() if the cause is a checked exception.
     */
    public void testHandleCauseChecked() throws ConcurrentException {
        Exception ex = new Exception("Test");
        try {
            ConcurrentUtils.handleCause(new ExecutionException(ex));
            fail("ConcurrentException not thrown!");
        } catch (ConcurrentException cex) {
            assertEquals("Wrong cause", ex, cex.getCause());
        }
    }

    /**
     * Tests handleCause() for a null parameter or a null cause. In this case
     * the method should do nothing. We can only test that no exception is
     * thrown.
     */
    public void testHandleCauseNull() throws ConcurrentException {
        ConcurrentUtils.handleCause(null);
        ConcurrentUtils.handleCause(new ExecutionException("Test", null));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12458.java