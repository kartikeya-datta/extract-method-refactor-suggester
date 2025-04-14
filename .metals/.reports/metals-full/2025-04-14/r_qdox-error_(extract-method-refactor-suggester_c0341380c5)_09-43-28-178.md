error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6273.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6273.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6273.java
text:
```scala
n@@ew BigReal(17), new BigReal(FastMath.PI),

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
package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.apache.commons.math.TestUtils;
import org.junit.Test;

public class BigRealTest {

    @Test
    public void testConstructor() {
        assertEquals(1.625, new BigReal(new BigDecimal("1.625")).doubleValue(), 1.0e-15);
        assertEquals(-5.0, new BigReal(new BigInteger("-5")).doubleValue(), 1.0e-15);
        assertEquals(-5.0, new BigReal(new BigInteger("-5"), MathContext.DECIMAL64).doubleValue(), 1.0e-15);
        assertEquals(0.125, new BigReal(new BigInteger("125"), 3).doubleValue(), 1.0e-15);
        assertEquals(0.125, new BigReal(new BigInteger("125"), 3, MathContext.DECIMAL64).doubleValue(), 1.0e-15);
        assertEquals(1.625, new BigReal(new char[] { '1', '.', '6', '2', '5' }).doubleValue(), 1.0e-15);
        assertEquals(1.625, new BigReal(new char[] { 'A', 'A', '1', '.', '6', '2', '5', '9' }, 2, 5).doubleValue(), 1.0e-15);
        assertEquals(1.625, new BigReal(new char[] { 'A', 'A', '1', '.', '6', '2', '5', '9' }, 2, 5, MathContext.DECIMAL64).doubleValue(), 1.0e-15);
        assertEquals(1.625, new BigReal(new char[] { '1', '.', '6', '2', '5' }, MathContext.DECIMAL64).doubleValue(), 1.0e-15);
        assertEquals(1.625, new BigReal(1.625).doubleValue(), 1.0e-15);
        assertEquals(1.625, new BigReal(1.625, MathContext.DECIMAL64).doubleValue(), 1.0e-15);
        assertEquals(-5.0, new BigReal(-5).doubleValue(), 1.0e-15);
        assertEquals(-5.0, new BigReal(-5, MathContext.DECIMAL64).doubleValue(), 1.0e-15);
        assertEquals(-5.0, new BigReal(-5l).doubleValue(), 1.0e-15);
        assertEquals(-5.0, new BigReal(-5l, MathContext.DECIMAL64).doubleValue(), 1.0e-15);
        assertEquals(1.625, new BigReal("1.625").doubleValue(), 1.0e-15);
        assertEquals(1.625, new BigReal("1.625", MathContext.DECIMAL64).doubleValue(), 1.0e-15);
    }

    @Test
    public void testCompareTo() {
        BigReal first = new BigReal(1.0 / 2.0);
        BigReal second = new BigReal(1.0 / 3.0);
        BigReal third = new BigReal(1.0 / 2.0);

        assertEquals(0, first.compareTo(first));
        assertEquals(0, first.compareTo(third));
        assertEquals(1, first.compareTo(second));
        assertEquals(-1, second.compareTo(first));

    }

    public void testAdd() {
        BigReal a = new BigReal("1.2345678");
        BigReal b = new BigReal("8.7654321");
        assertEquals(9.9999999, a.add(b).doubleValue(), 1.0e-15);
    }

    public void testSubtract() {
        BigReal a = new BigReal("1.2345678");
        BigReal b = new BigReal("8.7654321");
        assertEquals( -7.5308643, a.subtract(b).doubleValue(), 1.0e-15);
    }

    public void testDivide() {
        BigReal a = new BigReal("1.0000000000");
        BigReal b = new BigReal("0.0009765625");
        assertEquals(1024.0, a.divide(b).doubleValue(), 1.0e-15);
    }

    public void testMultiply() {
        BigReal a = new BigReal("1024.0");
        BigReal b = new BigReal("0.0009765625");
        assertEquals(1.0, a.multiply(b).doubleValue(), 1.0e-15);
    }

    @Test
    public void testDoubleValue() {
        assertEquals(0.5, new BigReal(0.5).doubleValue(), 1.0e-15);
    }

    @Test
    public void testBigDecimalValue() {
        BigDecimal pi = new BigDecimal("3.1415926535897932384626433832795028841971693993751");
        assertEquals(pi, new BigReal(pi).bigDecimalValue());
        assertEquals(new BigDecimal(0.5), new BigReal(1.0 / 2.0).bigDecimalValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        BigReal zero = new BigReal(0.0);
        BigReal nullReal = null;
        assertTrue(zero.equals(zero));
        assertFalse(zero.equals(nullReal));
        assertFalse(zero.equals(Double.valueOf(0)));
        BigReal zero2 = new BigReal(0.0);
        assertTrue(zero.equals(zero2));
        assertEquals(zero.hashCode(), zero2.hashCode());
        BigReal one = new BigReal(1.0);
        assertFalse((one.equals(zero) || zero.equals(one)));
        assertTrue(one.equals(BigReal.ONE));
    }

    public void testSerial() {
        BigReal[] Reals = {
            new BigReal(3.0), BigReal.ONE, BigReal.ZERO,
            new BigReal(17), new BigReal(Math.PI),
            new BigReal(-2.5)
        };
        for (BigReal Real : Reals) {
            assertEquals(Real, TestUtils.serializeAndRecover(Real));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6273.java