error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3446.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3446.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3446.java
text:
```scala
l@@ong endPosition = 0, startPosition = -1;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.db;

import java.io.DataOutput;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.io.sstable.IndexHelper;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.io.util.IIterableColumns;
import org.apache.cassandra.utils.BloomFilter;


/**
 * Help to create an index for a column family based on size of columns
 */
public class ColumnIndexer
{
	/**
	 * Given a column family this, function creates an in-memory structure that represents the
	 * column index for the column family, and subsequently writes it to disk.
	 * @param columns Column family to create index for
	 * @param dos data output stream
	 * @throws IOException
	 */
    public static void serialize(IIterableColumns columns, DataOutput dos)
    {
        try
        {
            serializeInternal(columns, dos);
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
    }

    public static void serializeInternal(IIterableColumns columns, DataOutput dos) throws IOException
    {
        int columnCount = columns.getEstimatedColumnCount();

        BloomFilter bf = BloomFilter.getFilter(columnCount, 4);

        if (columnCount == 0)
        {
            writeEmptyHeader(dos, bf);
            return;
        }

        // update bloom filter and create a list of IndexInfo objects marking the first and last column
        // in each block of ColumnIndexSize
        List<IndexHelper.IndexInfo> indexList = new ArrayList<IndexHelper.IndexInfo>();
        int endPosition = 0, startPosition = -1;
        int indexSizeInBytes = 0;
        IColumn lastColumn = null, firstColumn = null;
        for (IColumn column : columns)
        {
            bf.add(column.name());

            if (firstColumn == null)
            {
                firstColumn = column;
                startPosition = endPosition;
            }
            endPosition += column.serializedSize();
            /* if we hit the column index size that we have to index after, go ahead and index it. */
            if (endPosition - startPosition >= DatabaseDescriptor.getColumnIndexSize())
            {
                IndexHelper.IndexInfo cIndexInfo = new IndexHelper.IndexInfo(firstColumn.name(), column.name(), startPosition, endPosition - startPosition);
                indexList.add(cIndexInfo);
                indexSizeInBytes += cIndexInfo.serializedSize();
                firstColumn = null;
            }

            lastColumn = column;
        }

        // all columns were GC'd after all
        if (lastColumn == null)
        {
            writeEmptyHeader(dos, bf);
            return;
        }

        // the last column may have fallen on an index boundary already.  if not, index it explicitly.
        if (indexList.isEmpty() || columns.getComparator().compare(indexList.get(indexList.size() - 1).lastName, lastColumn.name()) != 0)
        {
            IndexHelper.IndexInfo cIndexInfo = new IndexHelper.IndexInfo(firstColumn.name(), lastColumn.name(), startPosition, endPosition - startPosition);
            indexList.add(cIndexInfo);
            indexSizeInBytes += cIndexInfo.serializedSize();
        }

        /* Write out the bloom filter. */
        writeBloomFilter(dos, bf);

        // write the index.  we should always have at least one computed index block, but we only write it out if there is more than that.
        assert indexSizeInBytes > 0;
        if (indexList.size() > 1)
        {
            dos.writeInt(indexSizeInBytes);
            for (IndexHelper.IndexInfo cIndexInfo : indexList)
            {
                cIndexInfo.serialize(dos);
            }
        }
        else
        {
            dos.writeInt(0);
        }
	}

    private static void writeEmptyHeader(DataOutput dos, BloomFilter bf)
            throws IOException
    {
        writeBloomFilter(dos, bf);
        dos.writeInt(0);
    }

    private static void writeBloomFilter(DataOutput dos, BloomFilter bf) throws IOException
    {
        DataOutputBuffer bufOut = new DataOutputBuffer();
        BloomFilter.serializer().serialize(bf, bufOut);
        dos.writeInt(bufOut.getLength());
        dos.write(bufOut.getData(), 0, bufOut.getLength());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3446.java