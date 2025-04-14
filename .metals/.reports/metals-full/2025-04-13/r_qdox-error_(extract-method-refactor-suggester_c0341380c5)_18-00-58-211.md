error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8378.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8378.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8378.java
text:
```scala
T@@riple<Object, String, Long> triple2 = Triple.of(null, "bar", Long.valueOf(200L));

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
package org.apache.commons.lang3.tuple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.HashSet;

import org.junit.Test;

/**
 * Test the Triple class.
 *
 * @version $Id$
 */
public class TripleTest {

    @Test
    public void testTripleOf() throws Exception {
        Triple<Integer, String, Boolean> triple = Triple.of(0, "foo", Boolean.TRUE);
        assertTrue(triple instanceof ImmutableTriple<?, ?, ?>);
        assertEquals(0, ((ImmutableTriple<Integer, String, Boolean>) triple).left.intValue());
        assertEquals("foo", ((ImmutableTriple<Integer, String, Boolean>) triple).middle);
        assertEquals(Boolean.TRUE, ((ImmutableTriple<Integer, String, Boolean>) triple).right);
        Triple<Object, String, Long> triple2 = Triple.of(null, "bar", 200L);
        assertTrue(triple2 instanceof ImmutableTriple<?, ?, ?>);
        assertNull(((ImmutableTriple<Object, String, Long>) triple2).left);
        assertEquals("bar", ((ImmutableTriple<Object, String, Long>) triple2).middle);
        assertEquals(new Long(200L), ((ImmutableTriple<Object, String, Long>) triple2).right);
    }

    @Test
    public void testCompatibilityBetweenTriples() throws Exception {
        Triple<Integer, String, Boolean> triple = ImmutableTriple.of(0, "foo", Boolean.TRUE);
        Triple<Integer, String, Boolean> triple2 = MutableTriple.of(0, "foo", Boolean.TRUE);
        assertEquals(triple, triple2);
        assertEquals(triple.hashCode(), triple2.hashCode());
        HashSet<Triple<Integer, String, Boolean>> set = new HashSet<Triple<Integer, String, Boolean>>();
        set.add(triple);
        assertTrue(set.contains(triple2));
    }

    @Test
    public void testComparable1() throws Exception {
        Triple<String, String, String> triple1 = Triple.of("A", "D", "A");
        Triple<String, String, String> triple2 = Triple.of("B", "C", "A");
        assertTrue(triple1.compareTo(triple1) == 0);
        assertTrue(triple1.compareTo(triple2) < 0);
        assertTrue(triple2.compareTo(triple2) == 0);
        assertTrue(triple2.compareTo(triple1) > 0);
    }

    @Test
    public void testComparable2() throws Exception {
        Triple<String, String, String> triple1 = Triple.of("A", "C", "B");
        Triple<String, String, String> triple2 = Triple.of("A", "D", "B");
        assertTrue(triple1.compareTo(triple1) == 0);
        assertTrue(triple1.compareTo(triple2) < 0);
        assertTrue(triple2.compareTo(triple2) == 0);
        assertTrue(triple2.compareTo(triple1) > 0);
    }

    @Test
    public void testComparable3() throws Exception {
        Triple<String, String, String> triple1 = Triple.of("A", "A", "D");
        Triple<String, String, String> triple2 = Triple.of("A", "B", "C");
        assertTrue(triple1.compareTo(triple1) == 0);
        assertTrue(triple1.compareTo(triple2) < 0);
        assertTrue(triple2.compareTo(triple2) == 0);
        assertTrue(triple2.compareTo(triple1) > 0);
    }

    @Test
    public void testComparable4() throws Exception {
        Triple<String, String, String> triple1 = Triple.of("B", "A", "C");
        Triple<String, String, String> triple2 = Triple.of("B", "A", "D");
        assertTrue(triple1.compareTo(triple1) == 0);
        assertTrue(triple1.compareTo(triple2) < 0);
        assertTrue(triple2.compareTo(triple2) == 0);
        assertTrue(triple2.compareTo(triple1) > 0);
    }

    @Test
    public void testToString() throws Exception {
        Triple<String, String, String> triple = Triple.of("Key", "Something", "Value");
        assertEquals("(Key,Something,Value)", triple.toString());
    }

    @Test
    public void testToStringCustom() throws Exception {
        Calendar date = Calendar.getInstance();
        date.set(2011, Calendar.APRIL, 25);
        Triple<String, String, Calendar> triple = Triple.of("DOB", "string", date);
        assertEquals("Test created on " + "04-25-2011", triple.toString("Test created on %3$tm-%3$td-%3$tY"));
    }

    @Test
    public void testFormattable_simple() throws Exception {
        Triple<String, String, String> triple = Triple.of("Key", "Something", "Value");
        assertEquals("(Key,Something,Value)", String.format("%1$s", triple));
    }

    @Test
    public void testFormattable_padded() throws Exception {
        Triple<String, String, String> triple = Triple.of("Key", "Something", "Value");
        assertEquals("         (Key,Something,Value)", String.format("%1$30s", triple));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8378.java