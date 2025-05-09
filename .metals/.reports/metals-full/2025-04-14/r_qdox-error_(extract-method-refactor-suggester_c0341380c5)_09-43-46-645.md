error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8377.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8377.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8377.java
text:
```scala
a@@ssertTrue("Wrong daemon flag", flag == t.isDaemon());

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ThreadFactory;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@code BasicThreadFactory}.
 *
 * @version $Id$
 */
public class BasicThreadFactoryTest {
    /** Constant for the test naming pattern. */
    private static final String PATTERN = "testThread-%d";

    /** The builder for creating a thread factory. */
    private BasicThreadFactory.Builder builder;

    @Before
    public void setUp() throws Exception {
        builder = new BasicThreadFactory.Builder();
    }

    /**
     * Tests the default options of a thread factory.
     *
     * @param factory the factory to be checked
     */
    private void checkFactoryDefaults(BasicThreadFactory factory) {
        assertNull("Got a naming pattern", factory.getNamingPattern());
        assertNull("Got an exception handler", factory
                .getUncaughtExceptionHandler());
        assertNull("Got a priority", factory.getPriority());
        assertNull("Got a daemon flag", factory.getDaemonFlag());
        assertNotNull("No wrapped factory", factory.getWrappedFactory());
    }

    /**
     * Tests the default values used by the builder.
     */
    @Test
    public void testBuildDefaults() {
        BasicThreadFactory factory = builder.build();
        checkFactoryDefaults(factory);
    }

    /**
     * Tries to set a null naming pattern.
     */
    @Test(expected = NullPointerException.class)
    public void testBuildNamingPatternNull() {
        builder.namingPattern(null);
    }

    /**
     * Tries to set a null wrapped factory.
     */
    @Test(expected = NullPointerException.class)
    public void testBuildWrappedFactoryNull() {
        builder.wrappedFactory(null);
    }

    /**
     * Tries to set a null exception handler.
     */
    @Test(expected = NullPointerException.class)
    public void testBuildUncaughtExceptionHandlerNull() {
        builder.uncaughtExceptionHandler(null);
    }

    /**
     * Tests the reset() method of the builder.
     */
    @Test
    public void testBuilderReset() {
        ThreadFactory wrappedFactory = EasyMock.createMock(ThreadFactory.class);
        Thread.UncaughtExceptionHandler exHandler = EasyMock
                .createMock(Thread.UncaughtExceptionHandler.class);
        EasyMock.replay(wrappedFactory, exHandler);
        builder.namingPattern(PATTERN).daemon(true).priority(
                Thread.MAX_PRIORITY).uncaughtExceptionHandler(exHandler)
                .wrappedFactory(wrappedFactory);
        builder.reset();
        BasicThreadFactory factory = builder.build();
        checkFactoryDefaults(factory);
        assertNotSame("Wrapped factory not reset", wrappedFactory, factory
                .getWrappedFactory());
        EasyMock.verify(wrappedFactory, exHandler);
    }

    /**
     * Tests whether reset() is automatically called after build().
     */
    @Test
    public void testBuilderResetAfterBuild() {
        builder.wrappedFactory(EasyMock.createNiceMock(ThreadFactory.class))
                .namingPattern(PATTERN).daemon(true).build();
        checkFactoryDefaults(builder.build());
    }

    /**
     * Tests whether the naming pattern is applied to new threads.
     */
    @Test
    public void testNewThreadNamingPattern() {
        ThreadFactory wrapped = EasyMock.createMock(ThreadFactory.class);
        Runnable r = EasyMock.createMock(Runnable.class);
        final int count = 12;
        for (int i = 0; i < count; i++) {
            EasyMock.expect(wrapped.newThread(r)).andReturn(new Thread());
        }
        EasyMock.replay(wrapped, r);
        BasicThreadFactory factory = builder.wrappedFactory(wrapped)
                .namingPattern(PATTERN).build();
        for (int i = 0; i < count; i++) {
            Thread t = factory.newThread(r);
            assertEquals("Wrong thread name", String.format(PATTERN, Long
                    .valueOf(i + 1)), t.getName());
            assertEquals("Wrong thread count", i + 1, factory.getThreadCount());
        }
        EasyMock.verify(wrapped, r);
    }

    /**
     * Tests whether the thread name is not modified if no naming pattern is
     * set.
     */
    @Test
    public void testNewThreadNoNamingPattern() {
        ThreadFactory wrapped = EasyMock.createMock(ThreadFactory.class);
        Runnable r = EasyMock.createMock(Runnable.class);
        final String name = "unchangedThreadName";
        Thread t = new Thread(name);
        EasyMock.expect(wrapped.newThread(r)).andReturn(t);
        EasyMock.replay(wrapped, r);
        BasicThreadFactory factory = builder.wrappedFactory(wrapped).build();
        assertSame("Wrong thread", t, factory.newThread(r));
        assertEquals("Name was changed", name, t.getName());
        EasyMock.verify(wrapped, r);
    }

    /**
     * Helper method for testing whether the daemon flag is taken into account.
     *
     * @param flag the value of the flag
     */
    private void checkDaemonFlag(boolean flag) {
        ThreadFactory wrapped = EasyMock.createMock(ThreadFactory.class);
        Runnable r = EasyMock.createMock(Runnable.class);
        Thread t = new Thread();
        EasyMock.expect(wrapped.newThread(r)).andReturn(t);
        EasyMock.replay(wrapped, r);
        BasicThreadFactory factory = builder.wrappedFactory(wrapped).daemon(
                flag).build();
        assertSame("Wrong thread", t, factory.newThread(r));
        assertEquals("Wrong daemon flag", flag, t.isDaemon());
        EasyMock.verify(wrapped, r);
    }

    /**
     * Tests whether daemon threads can be created.
     */
    @Test
    public void testNewThreadDaemonTrue() {
        checkDaemonFlag(true);
    }

    /**
     * Tests whether the daemon status of new threads can be turned off.
     */
    @Test
    public void testNewThreadDaemonFalse() {
        checkDaemonFlag(false);
    }

    /**
     * Tests whether the daemon flag is not touched on newly created threads if
     * it is not specified.
     */
    @Test
    public void testNewThreadNoDaemonFlag() {
        ThreadFactory wrapped = EasyMock.createMock(ThreadFactory.class);
        Runnable r1 = EasyMock.createMock(Runnable.class);
        Runnable r2 = EasyMock.createMock(Runnable.class);
        Thread t1 = new Thread();
        Thread t2 = new Thread();
        t1.setDaemon(true);
        EasyMock.expect(wrapped.newThread(r1)).andReturn(t1);
        EasyMock.expect(wrapped.newThread(r2)).andReturn(t2);
        EasyMock.replay(wrapped, r1, r2);
        BasicThreadFactory factory = builder.wrappedFactory(wrapped).build();
        assertSame("Wrong thread 1", t1, factory.newThread(r1));
        assertTrue("No daemon thread", t1.isDaemon());
        assertSame("Wrong thread 2", t2, factory.newThread(r2));
        assertFalse("A daemon thread", t2.isDaemon());
        EasyMock.verify(wrapped, r1, r2);
    }

    /**
     * Tests whether the priority is set on newly created threads.
     */
    @Test
    public void testNewThreadPriority() {
        ThreadFactory wrapped = EasyMock.createMock(ThreadFactory.class);
        Runnable r = EasyMock.createMock(Runnable.class);
        Thread t = new Thread();
        EasyMock.expect(wrapped.newThread(r)).andReturn(t);
        EasyMock.replay(wrapped, r);
        final int priority = Thread.NORM_PRIORITY + 1;
        BasicThreadFactory factory = builder.wrappedFactory(wrapped).priority(
                priority).build();
        assertSame("Wrong thread", t, factory.newThread(r));
        assertEquals("Wrong priority", priority, t.getPriority());
        EasyMock.verify(wrapped, r);
    }

    /**
     * Tests whether the original priority is not changed if no priority is
     * specified.
     */
    @Test
    public void testNewThreadNoPriority() {
        ThreadFactory wrapped = EasyMock.createMock(ThreadFactory.class);
        Runnable r = EasyMock.createMock(Runnable.class);
        final int orgPriority = Thread.NORM_PRIORITY + 1;
        Thread t = new Thread();
        t.setPriority(orgPriority);
        EasyMock.expect(wrapped.newThread(r)).andReturn(t);
        EasyMock.replay(wrapped, r);
        BasicThreadFactory factory = builder.wrappedFactory(wrapped).build();
        assertSame("Wrong thread", t, factory.newThread(r));
        assertEquals("Wrong priority", orgPriority, t.getPriority());
        EasyMock.verify(wrapped, r);
    }

    /**
     * Tests whether the exception handler is set if one is provided.
     */
    @Test
    public void testNewThreadExHandler() {
        ThreadFactory wrapped = EasyMock.createMock(ThreadFactory.class);
        Runnable r = EasyMock.createMock(Runnable.class);
        Thread.UncaughtExceptionHandler handler = EasyMock
                .createMock(Thread.UncaughtExceptionHandler.class);
        Thread t = new Thread();
        EasyMock.expect(wrapped.newThread(r)).andReturn(t);
        EasyMock.replay(wrapped, r, handler);
        BasicThreadFactory factory = builder.wrappedFactory(wrapped)
                .uncaughtExceptionHandler(handler).build();
        assertSame("Wrong thread", t, factory.newThread(r));
        assertEquals("Wrong exception handler", handler, t
                .getUncaughtExceptionHandler());
        EasyMock.verify(wrapped, r, handler);
    }

    /**
     * Tests whether the original exception hander is not touched if none is
     * specified.
     */
    @Test
    public void testNewThreadNoExHandler() {
        ThreadFactory wrapped = EasyMock.createMock(ThreadFactory.class);
        Runnable r = EasyMock.createMock(Runnable.class);
        Thread.UncaughtExceptionHandler handler = EasyMock
                .createMock(Thread.UncaughtExceptionHandler.class);
        Thread t = new Thread();
        t.setUncaughtExceptionHandler(handler);
        EasyMock.expect(wrapped.newThread(r)).andReturn(t);
        EasyMock.replay(wrapped, r, handler);
        BasicThreadFactory factory = builder.wrappedFactory(wrapped).build();
        assertSame("Wrong thread", t, factory.newThread(r));
        assertEquals("Wrong exception handler", handler, t
                .getUncaughtExceptionHandler());
        EasyMock.verify(wrapped, r, handler);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8377.java