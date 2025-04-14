error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13883.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13883.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13883.java
text:
```scala
r@@eturn Integer.valueOf(++initializeCalls);

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
package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BackgroundInitializerTest {
    /**
     * Helper method for checking whether the initialize() method was correctly
     * called. start() must already have been invoked.
     *
     * @param init the initializer to test
     */
    private void checkInitialize(BackgroundInitializerTestImpl init) {
        try {
            Integer result = init.get();
            assertEquals("Wrong result", 1, result.intValue());
            assertEquals("Wrong number of invocations", 1, init.initializeCalls);
            assertNotNull("No future", init.getFuture());
        } catch (ConcurrentException cex) {
            fail("Unexpected exception: " + cex);
        }
    }

    /**
     * Tests whether initialize() is invoked.
     */
    @Test
    public void testInitialize() {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        init.start();
        checkInitialize(init);
    }

    /**
     * Tries to obtain the executor before start(). It should not have been
     * initialized yet.
     */
    @Test
    public void testGetActiveExecutorBeforeStart() {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        assertNull("Got an executor", init.getActiveExecutor());
    }

    /**
     * Tests whether an external executor is correctly detected.
     */
    @Test
    public void testGetActiveExecutorExternal() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl(
                    exec);
            init.start();
            assertSame("Wrong executor", exec, init.getActiveExecutor());
            checkInitialize(init);
        } finally {
            exec.shutdown();
        }
    }

    /**
     * Tests getActiveExecutor() for a temporary executor.
     */
    @Test
    public void testGetActiveExecutorTemp() {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        init.start();
        assertNotNull("No active executor", init.getActiveExecutor());
        checkInitialize(init);
    }

    /**
     * Tests the execution of the background task if a temporary executor has to
     * be created.
     */
    @Test
    public void testInitializeTempExecutor() {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        assertTrue("Wrong result of start()", init.start());
        checkInitialize(init);
        assertTrue("Executor not shutdown", init.getActiveExecutor()
                .isShutdown());
    }

    /**
     * Tests whether an external executor can be set using the
     * setExternalExecutor() method.
     */
    @Test
    public void testSetExternalExecutor() throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
            init.setExternalExecutor(exec);
            assertEquals("Wrong executor service", exec, init
                    .getExternalExecutor());
            assertTrue("Wrong result of start()", init.start());
            assertSame("Wrong active executor", exec, init.getActiveExecutor());
            checkInitialize(init);
            assertFalse("Executor was shutdown", exec.isShutdown());
        } finally {
            exec.shutdown();
        }
    }

    /**
     * Tests that setting an executor after start() causes an exception.
     */
    @Test
    public void testSetExternalExecutorAfterStart() throws ConcurrentException {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        init.start();
        try {
            init.setExternalExecutor(Executors.newSingleThreadExecutor());
            fail("Could set executor after start()!");
        } catch (IllegalStateException istex) {
            init.get();
        }
    }

    /**
     * Tests invoking start() multiple times. Only the first invocation should
     * have an effect.
     */
    @Test
    public void testStartMultipleTimes() {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        assertTrue("Wrong result for start()", init.start());
        for (int i = 0; i < 10; i++) {
            assertFalse("Could start again", init.start());
        }
        checkInitialize(init);
    }

    /**
     * Tests calling get() before start(). This should cause an exception.
     */
    @Test(expected=IllegalStateException.class)
    public void testGetBeforeStart() throws ConcurrentException {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        init.get();
    }

    /**
     * Tests the get() method if background processing causes a runtime
     * exception.
     */
    @Test
    public void testGetRuntimeException() throws Exception {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        RuntimeException rex = new RuntimeException();
        init.ex = rex;
        init.start();
        try {
            init.get();
            fail("Exception not thrown!");
        } catch (Exception ex) {
            assertEquals("Runtime exception not thrown", rex, ex);
        }
    }

    /**
     * Tests the get() method if background processing causes a checked
     * exception.
     */
    @Test
    public void testGetCheckedException() throws Exception {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        Exception ex = new Exception();
        init.ex = ex;
        init.start();
        try {
            init.get();
            fail("Exception not thrown!");
        } catch (ConcurrentException cex) {
            assertEquals("Exception not thrown", ex, cex.getCause());
        }
    }

    /**
     * Tests the get() method if waiting for the initialization is interrupted.
     */
    @Test
    public void testGetInterruptedException() throws Exception {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        final BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl(
                exec);
        final CountDownLatch latch1 = new CountDownLatch(1);
        init.shouldSleep = true;
        init.start();
        final AtomicReference<InterruptedException> iex = new AtomicReference<InterruptedException>();
        Thread getThread = new Thread() {
            @Override
            public void run() {
                try {
                    init.get();
                } catch (ConcurrentException cex) {
                    if (cex.getCause() instanceof InterruptedException) {
                        iex.set((InterruptedException) cex.getCause());
                    }
                } finally {
                    assertTrue("Thread not interrupted", isInterrupted());
                    latch1.countDown();
                }
            }
        };
        getThread.start();
        getThread.interrupt();
        latch1.await();
        exec.shutdownNow();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        assertNotNull("No interrupted exception", iex.get());
    }

    /**
     * Tests isStarted() before start() was called.
     */
    @Test
    public void testIsStartedFalse() {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        assertFalse("Already started", init.isStarted());
    }

    /**
     * Tests isStarted() after start().
     */
    @Test
    public void testIsStartedTrue() {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        init.start();
        assertTrue("Not started", init.isStarted());
    }

    /**
     * Tests isStarted() after the background task has finished.
     */
    @Test
    public void testIsStartedAfterGet() {
        BackgroundInitializerTestImpl init = new BackgroundInitializerTestImpl();
        init.start();
        checkInitialize(init);
        assertTrue("Not started", init.isStarted());
    }

    /**
     * A concrete implementation of BackgroundInitializer. It also overloads
     * some methods that simplify testing.
     */
    private static class BackgroundInitializerTestImpl extends
            BackgroundInitializer<Integer> {
        /** An exception to be thrown by initialize(). */
        Exception ex;

        /** A flag whether the background task should sleep a while. */
        boolean shouldSleep;

        /** The number of invocations of initialize(). */
        volatile int initializeCalls;

        public BackgroundInitializerTestImpl() {
            super();
        }

        public BackgroundInitializerTestImpl(ExecutorService exec) {
            super(exec);
        }

        /**
         * Records this invocation. Optionally throws an exception or sleeps a
         * while.
         */
        @Override
        protected Integer initialize() throws Exception {
            if (ex != null) {
                throw ex;
            }
            if (shouldSleep) {
                Thread.sleep(60000L);
            }
            return ++initializeCalls;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13883.java