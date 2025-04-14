error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2371.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2371.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2371.java
text:
```scala
l@@ogger.debug("Deleted {}", desc);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cassandra.io.sstable;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOError;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.db.commitlog.ReplayPosition;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.io.util.BufferedRandomAccessFile;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.cassandra.utils.EstimatedHistogram;
import org.apache.cassandra.utils.Pair;

/**
 * This class is built on top of the SequenceFile. It stores
 * data on disk in sorted fashion. However the sorting is upto
 * the application. This class expects keys to be handed to it
 * in sorted order.
 *
 * A separate index file is maintained as well, containing the
 * SSTable keys and the offset into the SSTable at which they are found.
 * Every 1/indexInterval key is read into memory when the SSTable is opened.
 *
 * Finally, a bloom filter file is also kept for the keys in each SSTable.
 */
public abstract class SSTable
{
    static final Logger logger = LoggerFactory.getLogger(SSTable.class);

    // TODO: replace with 'Component' objects
    public static final String COMPONENT_DATA = Component.Type.DATA.repr;
    public static final String COMPONENT_INDEX = Component.Type.PRIMARY_INDEX.repr;
    public static final String COMPONENT_FILTER = Component.Type.FILTER.repr;
    public static final String COMPONENT_STATS = Component.Type.STATS.repr;

    public static final String TEMPFILE_MARKER = "tmp";

    public final Descriptor descriptor;
    protected final Set<Component> components;
    public final CFMetaData metadata;
    public final IPartitioner partitioner;

    public final ReplayPosition replayPosition;

    protected final EstimatedHistogram estimatedRowSize;
    protected final EstimatedHistogram estimatedColumnCount;

    protected SSTable(Descriptor descriptor, CFMetaData metadata, ReplayPosition replayPosition, IPartitioner partitioner)
    {
        this(descriptor, new HashSet<Component>(), metadata, replayPosition, partitioner);
    }

    protected SSTable(Descriptor descriptor, Set<Component> components, CFMetaData metadata, ReplayPosition replayPosition, IPartitioner partitioner)
    {
        this(descriptor, components, metadata, replayPosition, partitioner, defaultRowHistogram(), defaultColumnHistogram());
    }

    protected SSTable(Descriptor descriptor, Set<Component> components, CFMetaData metadata, ReplayPosition replayPosition, IPartitioner partitioner, EstimatedHistogram rowSizes, EstimatedHistogram columnCounts)
    {
        assert descriptor != null;
        assert components != null;
        assert metadata != null;
        assert replayPosition != null;
        assert partitioner != null;
        assert rowSizes != null;
        assert columnCounts != null;

        this.descriptor = descriptor;
        Set<Component> dataComponents = new HashSet<Component>(components);
        for (Component component : components)
            assert component.type != Component.Type.COMPACTED_MARKER;
        this.components = Collections.unmodifiableSet(dataComponents);
        this.metadata = metadata;
        this.replayPosition = replayPosition;
        this.partitioner = partitioner;
        estimatedRowSize = rowSizes;
        estimatedColumnCount = columnCounts;
    }

    static EstimatedHistogram defaultColumnHistogram()
    {
        return new EstimatedHistogram(114);
    }

    static EstimatedHistogram defaultRowHistogram()
    {
        return new EstimatedHistogram(150);
    }

    public EstimatedHistogram getEstimatedRowSize()
    {
        return estimatedRowSize;
    }

    public EstimatedHistogram getEstimatedColumnCount()
    {
        return estimatedColumnCount;
    }

    /**
     * We use a ReferenceQueue to manage deleting files that have been compacted
     * and for which no more SSTable references exist.  But this is not guaranteed
     * to run for each such file because of the semantics of the JVM gc.  So,
     * we write a marker to `compactedFilename` when a file is compacted;
     * if such a marker exists on startup, the file should be removed.
     *
     * This method will also remove SSTables that are marked as temporary.
     *
     * @return true if the file was deleted
     */
    public static boolean delete(Descriptor desc, Set<Component> components)
    {
        try
        {
            // remove the DATA component first if it exists
            if (components.contains(Component.DATA))
                FileUtils.deleteWithConfirm(desc.filenameFor(Component.DATA));
            for (Component component : components)
            {
                if (component.equals(Component.DATA) || component.equals(Component.COMPACTED_MARKER))
                    continue;
                FileUtils.deleteWithConfirm(desc.filenameFor(component));
            }
            // remove the COMPACTED_MARKER component last if it exists
            FileUtils.delete(desc.filenameFor(Component.COMPACTED_MARKER));
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
        logger.info("Deleted " + desc);
        return true;
    }

    public String getFilename()
    {
        return descriptor.filenameFor(COMPONENT_DATA);
    }

    public String getColumnFamilyName()
    {
        return descriptor.cfname;
    }

    public String getTableName()
    {
        return descriptor.ksname;
    }

    /**
     * @return A Descriptor,Component pair, or null if not a valid sstable component.
     */
    public static Pair<Descriptor,Component> tryComponentFromFilename(File dir, String name)
    {
        try
        {
            return Component.fromFilename(dir, name);
        }
        catch (Exception e)
        {
            if (!"snapshots".equals(name) && !"backups".equals(name))
                logger.warn("Invalid file '{}' in data directory {}.", name, dir);
            return null;
        }
    }

    /**
     * Discovers existing components for the descriptor. Slow: only intended for use outside the critical path.
     */
    static Set<Component> componentsFor(final Descriptor desc, final boolean liveOnly)
    {
        final Set<Component> components = new HashSet<Component>();
        desc.directory.list(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                Pair<Descriptor,Component> component = tryComponentFromFilename(dir, name);
                if (component != null && component.left.equals(desc) && (!liveOnly || !component.left.temporary))
                    components.add(component.right);
                return false;
            }
        });
        return components;
    }

    /** @return An estimate of the number of keys contained in the given data file. */
    static long estimateRowsFromData(Descriptor desc, BufferedRandomAccessFile dfile) throws IOException
    {
        // collect sizes for the first 1000 keys, or first 100 megabytes of data
        final int SAMPLES_CAP = 1000, BYTES_CAP = (int)Math.min(100000000, dfile.length());
        int keys = 0;
        long dataPosition = 0;
        while (dataPosition < BYTES_CAP && keys < SAMPLES_CAP)
        {
            dfile.seek(dataPosition);
            ByteBufferUtil.skipShortLength(dfile);
            long dataSize = SSTableReader.readRowSize(dfile, desc);
            dataPosition = dfile.getFilePointer() + dataSize;
            keys++;
        }
        dfile.seek(0);
        return dfile.length() / (dataPosition / keys);
    }

    /** @return An estimate of the number of keys contained in the given index file. */
    static long estimateRowsFromIndex(BufferedRandomAccessFile ifile) throws IOException
    {
        // collect sizes for the first 10000 keys, or first 10 megabytes of data
        final int SAMPLES_CAP = 10000, BYTES_CAP = (int)Math.min(10000000, ifile.length());
        int keys = 0;
        while (ifile.getFilePointer() < BYTES_CAP && keys < SAMPLES_CAP)
        {
            ByteBufferUtil.skipShortLength(ifile);
            FileUtils.skipBytesFully(ifile, 8);
            keys++;
        }
        assert keys > 0 && ifile.getFilePointer() > 0 && ifile.length() > 0;
        long estimatedRows = ifile.length() / (ifile.getFilePointer() / keys);
        ifile.seek(0);
        return estimatedRows;
    }

    public static long getTotalBytes(Iterable<SSTableReader> sstables)
    {
        long sum = 0;
        for (SSTableReader sstable : sstables)
        {
            sum += sstable.length();
        }
        return sum;
    }

    public long bytesOnDisk()
    {
        long bytes = 0;
        for (Component component : components)
        {
            bytes += new File(descriptor.filenameFor(component)).length();
        }
        return bytes;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "(" +
               "path='" + getFilename() + '\'' +
               ')';
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2371.java