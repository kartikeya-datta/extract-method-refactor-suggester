error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/513.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/513.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/513.java
text:
```scala
S@@STableReader sstable = SSTableUtils.prepare().write(rows);

/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.apache.cassandra.db;

import java.net.InetAddress;
import java.util.*;

import org.apache.cassandra.Util;

import org.junit.Test;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.io.sstable.SSTableUtils;
import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.utils.FBUtilities;
import static junit.framework.Assert.assertEquals;

public class LongCompactionSpeedTest extends CleanupHelper
{
    public static final String TABLE1 = "Keyspace1";
    public static final InetAddress LOCAL = FBUtilities.getLocalAddress();

    /**
     * Test compaction with a very wide row.
     */
    @Test
    public void testCompactionWide() throws Exception
    {
        testCompaction(2, 1, 200000);
    }

    /**
     * Test compaction with lots of skinny rows.
     */
    @Test
    public void testCompactionSlim() throws Exception
    {
        testCompaction(2, 200000, 1);
    }

    /**
     * Test compaction with lots of small sstables.
     */
    @Test
    public void testCompactionMany() throws Exception
    {
        testCompaction(100, 800, 5);
    }

    protected void testCompaction(int sstableCount, int rowsPerSSTable, int colsPerRow) throws Exception
    {
        CompactionManager.instance.disableAutoCompaction();

        Table table = Table.open(TABLE1);
        ColumnFamilyStore store = table.getColumnFamilyStore("Standard1");

        ArrayList<SSTableReader> sstables = new ArrayList<SSTableReader>();
        for (int k = 0; k < sstableCount; k++)
        {
            SortedMap<String,ColumnFamily> rows = new TreeMap<String,ColumnFamily>();
            for (int j = 0; j < rowsPerSSTable; j++)
            {
                String key = String.valueOf(j);
                IColumn[] cols = new IColumn[colsPerRow];
                for (int i = 0; i < colsPerRow; i++)
                {
                    // last sstable has highest timestamps
                    cols[i] = Util.column(String.valueOf(i), String.valueOf(i), k);
                }
                rows.put(key, SSTableUtils.createCF(Long.MIN_VALUE, Integer.MIN_VALUE, cols));
            }
            SSTableReader sstable = SSTableUtils.writeSSTable(rows);
            sstables.add(sstable);
            store.addSSTable(sstable);
        }

        // give garbage collection a bit of time to catch up
        Thread.sleep(1000);

        long start = System.currentTimeMillis();
        CompactionManager.instance.doCompaction(store, sstables, (int) (System.currentTimeMillis() / 1000) - DatabaseDescriptor.getCFMetaData(TABLE1, "Standard1").getGcGraceSeconds());
        System.out.println(String.format("%s: sstables=%d rowsper=%d colsper=%d: %d ms",
                                         this.getClass().getName(),
                                         sstableCount,
                                         rowsPerSSTable,
                                         colsPerRow,
                                         System.currentTimeMillis() - start));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/513.java