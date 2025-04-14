error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/37.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/37.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/37.java
text:
```scala
D@@escriptor remotedesc = remote.desc;

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

import java.io.*;
import java.net.InetAddress;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.db.ColumnFamilyStore;
import org.apache.cassandra.db.Table;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.net.IVerbHandler;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.FBUtilities;

public class StreamInitiateVerbHandler implements IVerbHandler
{
    private static Logger logger = LoggerFactory.getLogger(StreamInitiateVerbHandler.class);

    /*
     * Here we handle the StreamInitiateMessage. Here we get the
     * array of StreamContexts. We get file names for the column
     * families associated with the files and replace them with the
     * file names as obtained from the column family store on the
     * receiving end.
    */
    public void doVerb(Message message)
    {
        byte[] body = message.getMessageBody();
        ByteArrayInputStream bufIn = new ByteArrayInputStream(body);
        if (logger.isDebugEnabled())
            logger.debug(String.format("StreamInitiateVerbeHandler.doVerb %s %s %s", message.getVerb(), message.getMessageId(), message.getMessageType()));

        try
        {
            StreamInitiateMessage biMsg = StreamInitiateMessage.serializer().deserialize(new DataInputStream(bufIn));
            PendingFile[] pendingFiles = biMsg.getStreamContext();

            if (pendingFiles.length == 0)
            {
                if (logger.isDebugEnabled())
                    logger.debug("no data needed from " + message.getFrom());
                if (StorageService.instance.isBootstrapMode())
                    StorageService.instance.removeBootstrapSource(message.getFrom(), new String(message.getHeader(StreamOut.TABLE_NAME)));
                return;
            }

            /*
             * For each of the remote files in the incoming message
             * generate a local pendingFile and store it in the StreamInManager.
             */
            for (Map.Entry<PendingFile, PendingFile> pendingFile : getContextMapping(pendingFiles).entrySet())
            {
                PendingFile remoteFile = pendingFile.getKey();
                PendingFile localFile = pendingFile.getValue();

                FileStatus streamStatus = new FileStatus(remoteFile.getFilename());

                if (logger.isDebugEnabled())
                  logger.debug("Preparing to receive stream from " + message.getFrom() + ": " + remoteFile + " -> " + localFile);
                addStreamContext(message.getFrom(), localFile, streamStatus);
            }

            StreamInManager.registerFileStatusHandler(message.getFrom(), new FileStatusHandler());
            if (logger.isDebugEnabled())
              logger.debug("Sending a stream initiate done message ...");
            Message doneMessage = new Message(FBUtilities.getLocalAddress(), "", StorageService.Verb.STREAM_INITIATE_DONE, new byte[0] );
            MessagingService.instance.sendOneWay(doneMessage, message.getFrom());
        }
        catch (IOException ex)
        {
            throw new IOError(ex);
        }
    }

    /**
     * Translates remote files to local files by creating a local sstable
     * per remote sstable.
     */
    public LinkedHashMap<PendingFile, PendingFile> getContextMapping(PendingFile[] remoteFiles) throws IOException
    {
        /* Create a local sstable for each remote sstable */
        LinkedHashMap<PendingFile, PendingFile> mapping = new LinkedHashMap<PendingFile, PendingFile>();
        for (PendingFile remote : remoteFiles)
        {
            Descriptor remotedesc = remote.getDescriptor();

            // new local sstable
            Table table = Table.open(remotedesc.ksname);
            ColumnFamilyStore cfStore = table.getColumnFamilyStore(remotedesc.cfname);

            Descriptor localdesc = Descriptor.fromFilename(cfStore.getFlushPath());

            // add a local file for this component
            mapping.put(remote, new PendingFile(localdesc, remote));
        }

        return mapping;
    }

    private void addStreamContext(InetAddress host, PendingFile pendingFile, FileStatus streamStatus)
    {
        if (logger.isDebugEnabled())
          logger.debug("Adding stream context " + pendingFile + " for " + host + " ...");
        StreamInManager.addStreamContext(host, pendingFile, streamStatus);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/37.java