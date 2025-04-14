error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4736.java
text:
```scala
.@@compile("ConstantInitializer@-?\\d+ \\[ object = " + VALUE

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
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@code ConstantInitializer}.
 *
 * @version $Id$
 */
public class ConstantInitializerTest {
    /** Constant for the object managed by the initializer. */
    private static final Integer VALUE = 42;

    /** The initializer to be tested. */
    private ConstantInitializer<Integer> init;

    @Before
    public void setUp() throws Exception {
        init = new ConstantInitializer<Integer>(VALUE);
    }

    /**
     * Helper method for testing equals() and hashCode().
     *
     * @param obj the object to compare with the test instance
     * @param expected the expected result
     */
    private void checkEquals(final Object obj, final boolean expected) {
        assertTrue("Wrong result of equals", expected == init.equals(obj));
        if (obj != null) {
            assertTrue("Not symmetric", expected == obj.equals(init));
            if (expected) {
                assertEquals("Different hash codes", init.hashCode(),
                        obj.hashCode());
            }
        }
    }

    /**
     * Tests whether the correct object is returned.
     */
    @Test
    public void testGetObject() {
        assertEquals("Wrong object", VALUE, init.getObject());
    }

    /**
     * Tests whether get() returns the correct object.
     */
    @Test
    public void testGet() throws ConcurrentException {
        assertEquals("Wrong object", VALUE, init.get());
    }

    /**
     * Tests equals() if the expected result is true.
     */
    @Test
    public void testEqualsTrue() {
        checkEquals(init, true);
        ConstantInitializer<Integer> init2 = new ConstantInitializer<Integer>(
                Integer.valueOf(VALUE.intValue()));
        checkEquals(init2, true);
        init = new ConstantInitializer<Integer>(null);
        init2 = new ConstantInitializer<Integer>(null);
        checkEquals(init2, true);
    }

    /**
     * Tests equals() if the expected result is false.
     */
    @Test
    public void testEqualsFalse() {
        ConstantInitializer<Integer> init2 = new ConstantInitializer<Integer>(
                null);
        checkEquals(init2, false);
        init2 = new ConstantInitializer<Integer>(VALUE + 1);
        checkEquals(init2, false);
    }

    /**
     * Tests equals() with objects of other classes.
     */
    @Test
    public void testEqualsWithOtherObjects() {
        checkEquals(null, false);
        checkEquals(this, false);
        checkEquals(new ConstantInitializer<String>("Test"), false);
    }

    /**
     * Tests the string representation.
     */
    @Test
    public void testToString() {
        final String s = init.toString();
        final Pattern pattern = Pattern
                .compile("ConstantInitializer@\\d+ \\[ object = " + VALUE
                        + " \\]");
        assertTrue("Wrong string: " + s, pattern.matcher(s).matches());
    }

    /**
     * Tests the string representation if the managed object is null.
     */
    @Test
    public void testToStringNull() {
        final String s = new ConstantInitializer<Object>(null).toString();
        assertTrue("Object not found: " + s, s.indexOf("object = null") > 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4736.java