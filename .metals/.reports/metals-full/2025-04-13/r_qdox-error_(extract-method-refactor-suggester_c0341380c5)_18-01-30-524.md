error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1792.java
text:
```scala
a@@ssertEquals(2f, new MutableFloat("2.0").floatValue(), 0.0001f);

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
package org.apache.commons.lang3.mutable;

import junit.framework.TestCase;

/**
 * JUnit tests.
 * 
 * @version $Id$
 * @see MutableFloat
 */
public class MutableFloatTest extends TestCase {

    public MutableFloatTest(String testName) {
        super(testName);
    }

    // ----------------------------------------------------------------
    public void testConstructors() {
        assertEquals(0f, new MutableFloat().floatValue(), 0.0001f);
        
        assertEquals(1f, new MutableFloat(1f).floatValue(), 0.0001f);
        
        assertEquals(2f, new MutableFloat(new Float(2f)).floatValue(), 0.0001f);
        assertEquals(3f, new MutableFloat(new MutableFloat(3f)).floatValue(), 0.0001f);

        assertEquals(2f, new MutableDouble("2.0").floatValue(), 0.0001f);

        try {
            new MutableFloat((Number)null);
            fail();
        } catch (NullPointerException ex) {}
    }

    public void testGetSet() {
        final MutableFloat mutNum = new MutableFloat(0f);
        assertEquals(0f, new MutableFloat().floatValue(), 0.0001f);
        assertEquals(new Float(0), new MutableFloat().getValue());
        
        mutNum.setValue(1);
        assertEquals(1f, mutNum.floatValue(), 0.0001f);
        assertEquals(new Float(1f), mutNum.getValue());
        
        mutNum.setValue(new Float(2f));
        assertEquals(2f, mutNum.floatValue(), 0.0001f);
        assertEquals(new Float(2f), mutNum.getValue());
        
        mutNum.setValue(new MutableFloat(3f));
        assertEquals(3f, mutNum.floatValue(), 0.0001f);
        assertEquals(new Float(3f), mutNum.getValue());
        try {
            mutNum.setValue(null);
            fail();
        } catch (NullPointerException ex) {}
    }

    public void testNanInfinite() {
        MutableFloat mutNum = new MutableFloat(Float.NaN);
        assertEquals(true, mutNum.isNaN());
        
        mutNum = new MutableFloat(Float.POSITIVE_INFINITY);
        assertEquals(true, mutNum.isInfinite());
        
        mutNum = new MutableFloat(Float.NEGATIVE_INFINITY);
        assertEquals(true, mutNum.isInfinite());
    }

    public void testEquals() {
        final MutableFloat mutNumA = new MutableFloat(0f);
        final MutableFloat mutNumB = new MutableFloat(0f);
        final MutableFloat mutNumC = new MutableFloat(1f);

        assertEquals(true, mutNumA.equals(mutNumA));
        assertEquals(true, mutNumA.equals(mutNumB));
        assertEquals(true, mutNumB.equals(mutNumA));
        assertEquals(true, mutNumB.equals(mutNumB));
        assertEquals(false, mutNumA.equals(mutNumC));
        assertEquals(false, mutNumB.equals(mutNumC));
        assertEquals(true, mutNumC.equals(mutNumC));
        assertEquals(false, mutNumA.equals(null));
        assertEquals(false, mutNumA.equals(new Float(0f)));
        assertEquals(false, mutNumA.equals("0"));
    }

    public void testHashCode() {
        final MutableFloat mutNumA = new MutableFloat(0f);
        final MutableFloat mutNumB = new MutableFloat(0f);
        final MutableFloat mutNumC = new MutableFloat(1f);

        assertEquals(true, mutNumA.hashCode() == mutNumA.hashCode());
        assertEquals(true, mutNumA.hashCode() == mutNumB.hashCode());
        assertEquals(false, mutNumA.hashCode() == mutNumC.hashCode());
        assertEquals(true, mutNumA.hashCode() == new Float(0f).hashCode());
    }

    public void testCompareTo() {
        final MutableFloat mutNum = new MutableFloat(0f);

        assertEquals(0, mutNum.compareTo(new MutableFloat(0f)));
        assertEquals(+1, mutNum.compareTo(new MutableFloat(-1f)));
        assertEquals(-1, mutNum.compareTo(new MutableFloat(1f)));
        try {
            mutNum.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
    }

    public void testPrimitiveValues() {
        MutableFloat mutNum = new MutableFloat(1.7F);
        
        assertEquals( 1, mutNum.intValue() );
        assertEquals( 1.7, mutNum.doubleValue(), 0.00001 );
        assertEquals( (byte) 1, mutNum.byteValue() );
        assertEquals( (short) 1, mutNum.shortValue() );
        assertEquals( 1, mutNum.intValue() );
        assertEquals( 1L, mutNum.longValue() );
    }

    public void testToFloat() {
        assertEquals(new Float(0f), new MutableFloat(0f).toFloat());
        assertEquals(new Float(12.3f), new MutableFloat(12.3f).toFloat());
    }

    public void testIncrement() {
        MutableFloat mutNum = new MutableFloat(1);
        mutNum.increment();
        
        assertEquals(2, mutNum.intValue());
        assertEquals(2L, mutNum.longValue());
    }

    public void testDecrement() {
        MutableFloat mutNum = new MutableFloat(1);
        mutNum.decrement();
        
        assertEquals(0, mutNum.intValue());
        assertEquals(0L, mutNum.longValue());
    }

    public void testAddValuePrimitive() {
        MutableFloat mutNum = new MutableFloat(1);
        mutNum.add(1.1f);
        
        assertEquals(2.1f, mutNum.floatValue(), 0.01f);
    }

    public void testAddValueObject() {
        MutableFloat mutNum = new MutableFloat(1);
        mutNum.add(new Float(1.1f));
        
        assertEquals(2.1f, mutNum.floatValue(), 0.01f);
    }

    public void testSubtractValuePrimitive() {
        MutableFloat mutNum = new MutableFloat(1);
        mutNum.subtract(0.9f);
        
        assertEquals(0.1f, mutNum.floatValue(), 0.01f);
    }

    public void testSubtractValueObject() {
        MutableFloat mutNum = new MutableFloat(1);
        mutNum.subtract(new Float(0.9f));
        
        assertEquals(0.1f, mutNum.floatValue(), 0.01f);
    }

    public void testToString() {
        assertEquals("0.0", new MutableFloat(0f).toString());
        assertEquals("10.0", new MutableFloat(10f).toString());
        assertEquals("-123.0", new MutableFloat(-123f).toString());
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1792.java