error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8215.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8215.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8215.java
text:
```scala
final C@@SVFormat format = CSVFormat.defaults().withIgnoreSurroundingSpaces(false).build();

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

package org.apache.commons.csv.perf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests performance.
 * 
 * To run this test, use: mvn test -Dtest=PeformanceTest
 * 
 * @version $Id$
 */
public class PerformanceTest {

    private final int max = 10;

    private static final File BIG_FILE = new File(System.getProperty("java.io.tmpdir"), "worldcitiespop.txt");

    @BeforeClass
    public static void setUpClass() throws FileNotFoundException, IOException {
        if (BIG_FILE.exists()) {
            System.out.println(String.format("Found test fixture %s: %,d bytes.", BIG_FILE, BIG_FILE.length()));
            return;
        }
        System.out.println("Decompressing test fixture " + BIG_FILE + "...");
        final InputStream input = new GZIPInputStream(new FileInputStream("src/test/resources/perf/worldcitiespop.txt.gz"));
        final OutputStream output = new FileOutputStream(BIG_FILE);
        IOUtils.copy(input, output);
        System.out.println(String.format("Decompressed test fixture %s: %,d bytes.", BIG_FILE, BIG_FILE.length()));
    }

    private BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new FileReader(BIG_FILE));
    }

    private long parse(final Reader in, boolean traverseColumns) throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        long recordCount = 0;
        for (final CSVRecord record : format.parse(in)) {
            recordCount++;
            if (traverseColumns) {
                for (String value : record) {
                    // do nothing for now
                }
            }
        }
        return recordCount;
    }

    private void println(final String s) {
        System.out.println(s);
    }

    private long readAll(final BufferedReader in) throws IOException {
        long count = 0;
        while (in.readLine() != null) {
            count++;
        }
        return count;
    }

    public long testParseBigFile(boolean traverseColumns) throws Exception {
        final long startMillis = System.currentTimeMillis();
        final long count = this.parse(this.getBufferedReader(), traverseColumns);
        final long totalMillis = System.currentTimeMillis() - startMillis;
        this.println(String.format("File parsed in %,d milliseconds with Commons CSV: %,d lines.", totalMillis, count));
        return totalMillis;
    }

    @Test
    public void testParseBigFileRepeat() throws Exception {
        long bestTime = Long.MAX_VALUE;
        for (int i = 0; i < this.max; i++) {
            bestTime = Math.min(this.testParseBigFile(false), bestTime);
        }
        this.println(String.format("Best time out of %,d is %,d milliseconds.", this.max, bestTime));
    }

    @Test
    public void testReadBigFile() throws Exception {
        long bestTime = Long.MAX_VALUE;
        for (int i = 0; i < this.max; i++) {
            final BufferedReader in = this.getBufferedReader();
            final long startMillis = System.currentTimeMillis();
            long count = 0;
            try {
                count = this.readAll(in);
            } finally {
                in.close();
            }
            final long totalMillis = System.currentTimeMillis() - startMillis;
            bestTime = Math.min(totalMillis, bestTime);
            this.println(String.format("File read in %,d milliseconds: %,d lines.", totalMillis, count));
        }
        this.println(String.format("Best time out of %,d is %,d milliseconds.", this.max, bestTime));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8215.java