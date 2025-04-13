error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1044.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1044.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1044.java
text:
```scala
a@@ssertEquals("equal", 0, c.compare(equalFile1, equalFile2));

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
package org.apache.commons.io.comparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Test case for {@link CompositeFileComparator}.
 */
public class CompositeFileComparatorTest extends ComparatorAbstractTestCase {

    /**
     * Construct a new test case with the specified name.
     *
     * @param name Name of the test
     */
    public CompositeFileComparatorTest(String name) {
        super(name);
    }

    /** @see junit.framework.TestCase#setUp() */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        comparator = new CompositeFileComparator(SizeFileComparator.SIZE_COMPARATOR,
                                                 ExtensionFileComparator.EXTENSION_COMPARATOR);
        reverse = new ReverseComparator(comparator);
        File dir = getTestDirectory();
        lessFile   = new File(dir, "xyz.txt");
        equalFile1 = new File(dir, "foo.txt");
        equalFile2 = new File(dir, "bar.txt");
        moreFile   = new File(dir, "foo.xyz");
        createFile(lessFile,   32);
        createFile(equalFile1, 48);
        createFile(equalFile2, 48);
        createFile(moreFile,   48);
    }

    /**
     * Test Constructor with null Iterable
     */
    public void testConstructorIterable() {
        List<Comparator<File>> list = new ArrayList<Comparator<File>>();
        list.add(SizeFileComparator.SIZE_COMPARATOR);
        list.add(ExtensionFileComparator.EXTENSION_COMPARATOR);
        Comparator<File> c = new CompositeFileComparator(list);

        assertTrue("equal", c.compare(equalFile1, equalFile2) == 0);
        assertTrue("less",  c.compare(lessFile, moreFile) < 0);
        assertTrue("more",  c.compare(moreFile, lessFile) > 0);
    }

    /**
     * Test Constructor with null Iterable
     */
    public void testConstructorIterableNull() {
        Comparator<File> c = new CompositeFileComparator((Iterable<Comparator<File>>)null);
        assertEquals("less,more", 0, c.compare(lessFile, moreFile));
        assertEquals("more,less", 0, c.compare(moreFile, lessFile));
        assertEquals("toString", "CompositeFileComparator{}", c.toString());
    }

    /**
     * Test Constructor with null array
     */
    public void testConstructorArrayNull() {
        Comparator<File> c = new CompositeFileComparator((Comparator<File>[])null);
        assertEquals("less,more", 0, c.compare(lessFile, moreFile));
        assertEquals("more,less", 0, c.compare(moreFile, lessFile));
        assertEquals("toString", "CompositeFileComparator{}", c.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1044.java