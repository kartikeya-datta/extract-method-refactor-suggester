error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13963.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13963.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13963.java
text:
```scala
I@@terator<?> it = f.valuesIterator();

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
package org.apache.commons.math.stat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.commons.math.TestUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test cases for the {@link Frequency} class.
 *
 * @version $Revision$ $Date$
 */

public final class FrequencyTest extends TestCase {
    private long oneL = 1;
    private long twoL = 2;
    private long threeL = 3;
    private int oneI = 1;
    private int twoI = 2;
    private int threeI=3;
    private double tolerance = 10E-15;
    private Frequency f = null;
    
    public FrequencyTest(String name) {
        super(name);
    }

    @Override
    public void setUp() {  
        f = new Frequency();
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(FrequencyTest.class);
        suite.setName("Frequency Tests");
        return suite;
    }
    
    /** test freq counts */
    public void testCounts() {
        assertEquals("total count",0,f.getSumFreq());
        f.addValue(oneL);
        f.addValue(twoL);
        f.addValue(1);
        f.addValue(oneI);
        assertEquals("one frequency count",3,f.getCount(1));
        assertEquals("two frequency count",1,f.getCount(2));
        assertEquals("three frequency count",0,f.getCount(3));
        assertEquals("total count",4,f.getSumFreq());
        assertEquals("zero cumulative frequency", 0, f.getCumFreq(0));
        assertEquals("one cumulative frequency", 3,  f.getCumFreq(1));
        assertEquals("two cumulative frequency", 4,  f.getCumFreq(2));
        assertEquals("Integer argument cum freq",4, f.getCumFreq(Integer.valueOf(2)));
        assertEquals("five cumulative frequency", 4,  f.getCumFreq(5));
        assertEquals("foo cumulative frequency", 0,  f.getCumFreq("foo"));
        
        f.clear();
        assertEquals("total count",0,f.getSumFreq());
        
        // userguide examples -------------------------------------------------------------------
        f.addValue("one");
        f.addValue("One");
        f.addValue("oNe");
        f.addValue("Z");
        assertEquals("one cumulative frequency", 1 ,  f.getCount("one"));
        assertEquals("Z cumulative pct", 0.5,  f.getCumPct("Z"), tolerance);
        assertEquals("z cumulative pct", 1.0,  f.getCumPct("z"), tolerance);
        assertEquals("Ot cumulative pct", 0.25,  f.getCumPct("Ot"), tolerance);
        f.clear();
        
        f = null;
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(Integer.valueOf(1));
        f.addValue(Long.valueOf(1));
        f.addValue(2);
        f.addValue(Integer.valueOf(-1));
        assertEquals("1 count", 3, f.getCount(1));
        assertEquals("1 count", 3, f.getCount(Integer.valueOf(1)));
        assertEquals("0 cum pct", 0.2, f.getCumPct(0), tolerance);
        assertEquals("1 pct", 0.6, f.getPct(Integer.valueOf(1)), tolerance);
        assertEquals("-2 cum pct", 0, f.getCumPct(-2), tolerance);
        assertEquals("10 cum pct", 1, f.getCumPct(10), tolerance);   
        
        f = null;
        f = new Frequency(String.CASE_INSENSITIVE_ORDER);
        f.addValue("one");
        f.addValue("One");
        f.addValue("oNe");
        f.addValue("Z");
        assertEquals("one count", 3 ,  f.getCount("one"));
        assertEquals("Z cumulative pct -- case insensitive", 1 ,  f.getCumPct("Z"), tolerance);
        assertEquals("z cumulative pct -- case insensitive", 1 ,  f.getCumPct("z"), tolerance);

        f = null;
        f = new Frequency();
        assertEquals(0L, f.getCount('a'));
        assertEquals(0L, f.getCumFreq('b'));
        TestUtils.assertEquals(Double.NaN, f.getPct('a'), 0.0);
        TestUtils.assertEquals(Double.NaN, f.getCumPct('b'), 0.0);
        f.addValue('a');
        f.addValue('b');
        f.addValue('c');
        f.addValue('d');
        assertEquals(1L, f.getCount('a'));
        assertEquals(2L, f.getCumFreq('b'));
        assertEquals(0.25, f.getPct('a'), 0.0);
        assertEquals(0.5, f.getCumPct('b'), 0.0);
        assertEquals(1.0, f.getCumPct('e'), 0.0);
    }     
    
    /** test pcts */
    public void testPcts() {
        f.addValue(oneL);
        f.addValue(twoL);
        f.addValue(oneI);
        f.addValue(twoI);
        f.addValue(threeL);
        f.addValue(threeL);
        f.addValue(3);
        f.addValue(threeI);
        assertEquals("one pct",0.25,f.getPct(1),tolerance);
        assertEquals("two pct",0.25,f.getPct(Long.valueOf(2)),tolerance);
        assertEquals("three pct",0.5,f.getPct(threeL),tolerance);
        assertEquals("five pct",0,f.getPct(5),tolerance);
        assertEquals("foo pct",0,f.getPct("foo"),tolerance);
        assertEquals("one cum pct",0.25,f.getCumPct(1),tolerance);
        assertEquals("two cum pct",0.50,f.getCumPct(Long.valueOf(2)),tolerance);
        assertEquals("Integer argument",0.50,f.getCumPct(Integer.valueOf(2)),tolerance);
        assertEquals("three cum pct",1.0,f.getCumPct(threeL),tolerance);
        assertEquals("five cum pct",1.0,f.getCumPct(5),tolerance);
        assertEquals("zero cum pct",0.0,f.getCumPct(0),tolerance);
        assertEquals("foo cum pct",0,f.getCumPct("foo"),tolerance);
    }
    
    /** test adding incomparable values */
    public void testAdd() {
        char aChar = 'a';
        char bChar = 'b';
        String aString = "a";
        f.addValue(aChar);
        f.addValue(bChar);
        try {
            f.addValue(aString);    
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        try {
            f.addValue(2);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        assertEquals("a pct",0.5,f.getPct(aChar),tolerance);
        assertEquals("b cum pct",1.0,f.getCumPct(bChar),tolerance);
        assertEquals("a string pct",0.0,f.getPct(aString),tolerance);
        assertEquals("a string cum pct",0.0,f.getCumPct(aString),tolerance);
        
        f = new Frequency();
        f.addValue("One");
        try {
            f.addValue(new Integer("One")); 
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    /** test empty table */
    public void testEmptyTable() {
        assertEquals("freq sum, empty table", 0, f.getSumFreq());
        assertEquals("count, empty table", 0, f.getCount(0));
        assertEquals("count, empty table",0, f.getCount(Integer.valueOf(0)));
        assertEquals("cum freq, empty table", 0, f.getCumFreq(0));
        assertEquals("cum freq, empty table", 0, f.getCumFreq("x"));
        assertTrue("pct, empty table", Double.isNaN(f.getPct(0)));
        assertTrue("pct, empty table", Double.isNaN(f.getPct(Integer.valueOf(0))));
        assertTrue("cum pct, empty table", Double.isNaN(f.getCumPct(0)));
        assertTrue("cum pct, empty table", Double.isNaN(f.getCumPct(Integer.valueOf(0))));   
    }
    
    /**
     * Tests toString() 
     */
    public void testToString(){
        f.addValue(oneL);
        f.addValue(twoL);
        f.addValue(oneI);
        f.addValue(twoI);
        
        String s = f.toString();
        //System.out.println(s);
        assertNotNull(s);
        BufferedReader reader = new BufferedReader(new StringReader(s));
        try {
            String line = reader.readLine(); // header line
            assertNotNull(line);
            
            line = reader.readLine(); // one's or two's line
            assertNotNull(line);
                        
            line = reader.readLine(); // one's or two's line
            assertNotNull(line);

            line = reader.readLine(); // no more elements
            assertNull(line);
        } catch(IOException ex){
            fail(ex.getMessage());
        }        
    }
    public void testIntegerValues() {
        Object obj1 = null;
        obj1 = Integer.valueOf(1);
        Integer int1 = Integer.valueOf(1);
        f.addValue(obj1);
        f.addValue(int1);
        f.addValue(2);
        f.addValue(Long.valueOf(2));
        assertEquals("Integer 1 count", 2, f.getCount(1));
        assertEquals("Integer 1 count", 2, f.getCount(Integer.valueOf(1)));
        assertEquals("Integer 1 count", 2, f.getCount(Long.valueOf(1)));
        assertEquals("Integer 1 cumPct", 0.5, f.getCumPct(1), tolerance);
        assertEquals("Integer 1 cumPct", 0.5, f.getCumPct(Long.valueOf(1)), tolerance);
        assertEquals("Integer 1 cumPct", 0.5, f.getCumPct(Integer.valueOf(1)), tolerance);
        Iterator it = f.valuesIterator();
        while (it.hasNext()) {
            assertTrue(it.next() instanceof Long);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13963.java