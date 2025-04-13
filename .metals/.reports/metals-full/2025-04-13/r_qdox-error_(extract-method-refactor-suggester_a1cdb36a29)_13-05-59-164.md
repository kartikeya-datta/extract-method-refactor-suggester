error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1699.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1699.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1699.java
text:
```scala
S@@STableReader sstable = SSTableWriter.renameAndOpen(pendingFile.getDescriptor());

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

package org.apache.cassandra.streaming;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.db.Table;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.io.sstable.SSTableWriter;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.service.StorageService;

/**
 * This is the callback handler that is invoked on the receiving node when a file changes status from RECEIVE to either
 * FileStatus.STREAM (needs to be restreamed) or FileStatus.DELETE (successfully completed).
*/
class FileStatusHandler
{
    private static Logger logger = LoggerFactory.getLogger(FileStatusHandler.class);

    public void onStatusChange(InetAddress host, PendingFile pendingFile, FileStatus streamStatus) throws IOException
    {
        if (FileStatus.Action.STREAM == streamStatus.getAction())
        {
            // file needs to be restreamed
            logger.warn("Streaming of file " + pendingFile + " from " + host + " failed: requesting a retry.");
            MessagingService.instance.sendOneWay(streamStatus.makeStreamStatusMessage(), host);
            return;
        }
        assert FileStatus.Action.DELETE == streamStatus.getAction() :
            "Unknown stream action: " + streamStatus.getAction();

        // file was successfully streamed: if it was the last component of an sstable, assume that the rest
        // have already arrived
        if (pendingFile.getFilename().endsWith("-Data.db"))
        {
            // last component triggers add: see TODO in SSTable.getAllComponents()
            String tableName = pendingFile.getDescriptor().ksname;
            File file = new File(pendingFile.getFilename());
            String fileName = file.getName();
            String [] temp = fileName.split("-");

            try
            {
                SSTableReader sstable = SSTableWriter.renameAndOpen(pendingFile.getFilename());
                Table.open(tableName).getColumnFamilyStore(temp[0]).addSSTable(sstable);
                logger.info("Streaming added " + sstable.getFilename());
            }
            catch (IOException e)
            {
                throw new RuntimeException("Not able to add streamed file " + pendingFile.getFilename(), e);
            }
        }

        // send a StreamStatus message telling the source node it can delete this file
        if (logger.isDebugEnabled())
            logger.debug("Sending a streaming finished message for " + pendingFile + " to " + host);
        MessagingService.instance.sendOneWay(streamStatus.makeStreamStatusMessage(), host);

        // if all files have been received from this host, remove from bootstrap sources
        if (StreamInManager.isDone(host) && StorageService.instance.isBootstrapMode())
        {
            StorageService.instance.removeBootstrapSource(host, pendingFile.getDescriptor().ksname);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1699.java