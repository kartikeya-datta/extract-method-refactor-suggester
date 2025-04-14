error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12623.java
text:
```scala
n@@ew DoubleRange(nonComparableNumber);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.lang.math;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test cases for the {@link DoubleRange} class.
 *
 * @author Stephen Colebourne
 * @version $Id$
 */
public final class DoubleRangeTest extends AbstractRangeTest {

    public DoubleRangeTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DoubleRangeTest.class);
        suite.setName("DoubleRange Tests");
        return suite;
    }
    
    @Override
    public void setUp() {
        super.setUp();
        tenToTwenty = new DoubleRange(double10, double20);
        otherRange = new NumberRange(ten, twenty);
    }

    @Override
    protected Range createRange(Integer integer1, Integer integer2) {
        return new DoubleRange(integer1, integer2);
    }
    @Override
    protected Range createRange(Integer integer) {
        return new NumberRange(integer);
    }
    
    //--------------------------------------------------------------------------

    public void testConstructor1a() {
        DoubleRange nr = new DoubleRange(8d);
        assertEquals(double8, nr.getMinimumNumber());
        assertEquals(double8, nr.getMaximumNumber());
        
        try {
            new DoubleRange(Double.NaN);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testConstructor1b() {
        DoubleRange nr = new DoubleRange(double8);
        assertSame(double8, nr.getMinimumNumber());
        assertSame(double8, nr.getMaximumNumber());
        
        Range r = new DoubleRange(nonComparableNumber);
        
        try {
            new DoubleRange(null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new DoubleRange(new Double(Double.NaN));
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testConstructor2a() {
        DoubleRange nr = new DoubleRange(8d, 10d);
        assertEquals(double8, nr.getMinimumNumber());
        assertEquals(double10, nr.getMaximumNumber());
        
        nr = new DoubleRange(10d, 8d);
        assertEquals(double8, nr.getMinimumNumber());
        assertEquals(double10, nr.getMaximumNumber());
        
        try {
            new DoubleRange(Double.NaN, 8d);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testConstructor2b() {
        DoubleRange nr = new DoubleRange(double8, double10);
        assertSame(double8, nr.getMinimumNumber());
        assertSame(double10, nr.getMaximumNumber());
        
        nr = new DoubleRange(double10, double8);
        assertSame(double8, nr.getMinimumNumber());
        assertSame(double10, nr.getMaximumNumber());
        
        nr = new DoubleRange(double8, double10);
        assertSame(double8, nr.getMinimumNumber());
        assertEquals(double10, nr.getMaximumNumber());
        
        // not null
        try {
            new DoubleRange(double8, null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new DoubleRange(null, double8);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new DoubleRange(null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            new DoubleRange(new Double(Double.NaN), double10);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //--------------------------------------------------------------------------

    public void testContainsNumber() {
        assertEquals(false, tenToTwenty.containsNumber(null));
        assertEquals(true, tenToTwenty.containsNumber(nonComparableNumber));
        
        assertEquals(false, tenToTwenty.containsNumber(five));
        assertEquals(true, tenToTwenty.containsNumber(ten));
        assertEquals(true, tenToTwenty.containsNumber(fifteen));
        assertEquals(true, tenToTwenty.containsNumber(twenty));
        assertEquals(false, tenToTwenty.containsNumber(twentyFive));
        
        assertEquals(false, tenToTwenty.containsNumber(long8));
        assertEquals(true, tenToTwenty.containsNumber(long10));
        assertEquals(true, tenToTwenty.containsNumber(long12));
        assertEquals(true, tenToTwenty.containsNumber(long20));
        assertEquals(false, tenToTwenty.containsNumber(long21));
        
        assertEquals(false, tenToTwenty.containsNumber(double8));
        assertEquals(true, tenToTwenty.containsNumber(double10));
        assertEquals(true, tenToTwenty.containsNumber(double12));
        assertEquals(true, tenToTwenty.containsNumber(double20));
        assertEquals(false, tenToTwenty.containsNumber(double21));
        
        assertEquals(false, tenToTwenty.containsNumber(float8));
        assertEquals(true, tenToTwenty.containsNumber(float10));
        assertEquals(true, tenToTwenty.containsNumber(float12));
        assertEquals(true, tenToTwenty.containsNumber(float20));
        assertEquals(false, tenToTwenty.containsNumber(float21));
    }

    @Override
    public void testToString() {
        String str = tenToTwenty.toString();
        assertEquals("Range[10.0,20.0]", str);
        assertSame(str, tenToTwenty.toString());
        assertEquals("Range[-20.0,-10.0]", createRange(new Integer(-20), new Integer(-10)).toString());
    }
    
    //--------------------------------------------------------------------------
    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12623.java