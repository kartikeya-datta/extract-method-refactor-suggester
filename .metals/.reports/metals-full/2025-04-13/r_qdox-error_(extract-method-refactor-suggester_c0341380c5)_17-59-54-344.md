error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18340.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18340.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18340.java
text:
```scala
n@@ew FileRowColContainer(findTestPath("testfiles/xyzxyz"));

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.functions;

import java.io.FileNotFoundException;

import org.apache.jmeter.junit.JMeterTestCase;

/**
 * File data container for CSV (and similar delimited) files Data is accessible
 * via row and column number
 * 
 * @version $Revision$
 */
public class TestFileRowColContainer extends JMeterTestCase {

    public void testNull() throws Exception {
        try {
            new FileRowColContainer("testfiles/xyzxyz");
            fail("Should not find the file");
        } catch (FileNotFoundException e) {
        }
    }

    public void testrowNum() throws Exception {
        FileRowColContainer f = new FileRowColContainer(findTestPath("testfiles/test.csv"));
        assertNotNull(f);
        assertEquals("Expected 4 lines", 4, f.getSize());

        assertEquals(0, f.nextRow());
        assertEquals(1, f.nextRow());
        assertEquals(2, f.nextRow());
        assertEquals(3, f.nextRow());
        assertEquals(0, f.nextRow());

    }

    public void testColumns() throws Exception {
        FileRowColContainer f = new FileRowColContainer(findTestPath("testfiles/test.csv"));
        assertNotNull(f);
        assertTrue("Not empty", f.getSize() > 0);

        int myRow = f.nextRow();
        assertEquals(0, myRow);
        assertEquals("a1", f.getColumn(myRow, 0));
        assertEquals("d1", f.getColumn(myRow, 3));

        try {
            f.getColumn(myRow, 4);
            fail("Expected out of bounds");
        } catch (IndexOutOfBoundsException e) {
        }
        myRow = f.nextRow();
        assertEquals(1, myRow);
        assertEquals("b2", f.getColumn(myRow, 1));
        assertEquals("c2", f.getColumn(myRow, 2));
    }

    public void testColumnsComma() throws Exception {
        FileRowColContainer f = new FileRowColContainer(findTestPath("testfiles/test.csv"), ",");
        assertNotNull(f);
        assertTrue("Not empty", f.getSize() > 0);

        int myRow = f.nextRow();
        assertEquals(0, myRow);
        assertEquals("a1", f.getColumn(myRow, 0));
        assertEquals("d1", f.getColumn(myRow, 3));

        try {
            f.getColumn(myRow, 4);
            fail("Expected out of bounds");
        } catch (IndexOutOfBoundsException e) {
        }
        myRow = f.nextRow();
        assertEquals(1, myRow);
        assertEquals("b2", f.getColumn(myRow, 1));
        assertEquals("c2", f.getColumn(myRow, 2));
    }

    public void testColumnsTab() throws Exception {
        FileRowColContainer f = new FileRowColContainer(findTestPath("testfiles/test.tsv"), "\t");
        assertNotNull(f);
        assertTrue("Not empty", f.getSize() > 0);

        int myRow = f.nextRow();
        assertEquals(0, myRow);
        assertEquals("a1", f.getColumn(myRow, 0));
        assertEquals("d1", f.getColumn(myRow, 3));

        try {
            f.getColumn(myRow, 4);
            fail("Expected out of bounds");
        } catch (IndexOutOfBoundsException e) {
        }
        myRow = f.nextRow();
        assertEquals(1, myRow);
        assertEquals("b2", f.getColumn(myRow, 1));
        assertEquals("c2", f.getColumn(myRow, 2));
    }

    public void testEmptyCols() throws Exception {
        FileRowColContainer f = new FileRowColContainer(findTestPath("testfiles/testempty.csv"));
        assertNotNull(f);
        assertEquals("Expected 4 lines", 4, f.getSize());

        int myRow = f.nextRow();
        assertEquals(0, myRow);
        assertEquals("", f.getColumn(myRow, 0));
        assertEquals("d1", f.getColumn(myRow, 3));

        myRow = f.nextRow();
        assertEquals(1, myRow);
        assertEquals("", f.getColumn(myRow, 1));
        assertEquals("c2", f.getColumn(myRow, 2));

        myRow = f.nextRow();
        assertEquals(2, myRow);
        assertEquals("b3", f.getColumn(myRow, 1));
        assertEquals("", f.getColumn(myRow, 2));

        myRow = f.nextRow();
        assertEquals(3, myRow);
        assertEquals("b4", f.getColumn(myRow, 1));
        assertEquals("c4", f.getColumn(myRow, 2));
        assertEquals("", f.getColumn(myRow, 3));
    }
}
 No newline at end of file
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18340.java