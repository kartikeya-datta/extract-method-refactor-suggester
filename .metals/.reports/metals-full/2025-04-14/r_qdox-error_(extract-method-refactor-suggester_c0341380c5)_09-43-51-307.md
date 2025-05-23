error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1637.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1637.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1637.java
text:
```scala
b@@yte[] body = message.getMessageBody();

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

package org.apache.cassandra.dht;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.Table;
import org.apache.cassandra.io.DataInputBuffer;
import org.apache.cassandra.net.EndPoint;
import org.apache.cassandra.net.IVerbHandler;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.net.io.StreamContextManager;
import org.apache.cassandra.service.StreamManager;
import org.apache.cassandra.utils.BloomFilter;
import org.apache.cassandra.utils.LogUtil;
import org.apache.log4j.Logger;

/**
 * This verb handler handles the BootstrapMetadataMessage that is sent
 * by the leader to the nodes that are responsible for handing off data. 
*/
public class BootstrapMetadataVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger(BootstrapMetadataVerbHandler.class);
    
    public void doVerb(Message message)
    {
        logger_.debug("Received a BootstrapMetadataMessage from " + message.getFrom());
        byte[] body = (byte[])message.getMessageBody()[0];
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(body, body.length);
        try
        {
            BootstrapMetadataMessage bsMetadataMessage = BootstrapMetadataMessage.serializer().deserialize(bufIn);
            BootstrapMetadata[] bsMetadata = bsMetadataMessage.bsMetadata_;
            
            /*
             * This is for debugging purposes. Remove later.
            */
            for ( BootstrapMetadata bsmd : bsMetadata )
            {
                logger_.debug(bsmd.toString());                                      
            }
            
            for ( BootstrapMetadata bsmd : bsMetadata )
            {
                long startTime = System.currentTimeMillis();
                doTransfer(bsmd.target_, bsmd.ranges_);     
                logger_.debug("Time taken to boostrap " + 
                        bsmd.target_ + 
                        " is " + 
                        (System.currentTimeMillis() - startTime) +
                        " msecs.");
            }
        }
        catch ( IOException ex )
        {
            logger_.info(LogUtil.throwableToString(ex));
        }
    }
    
    /*
     * This method needs to figure out the files on disk
     * locally for each range and then stream them using
     * the Bootstrap protocol to the target endpoint.
    */
    private void doTransfer(EndPoint target, List<Range> ranges) throws IOException
    {
        if ( ranges.size() == 0 )
        {
            logger_.debug("No ranges to give scram ...");
            return;
        }
        
        /* Just for debugging process - remove later */            
        for ( Range range : ranges )
        {
            StringBuilder sb = new StringBuilder("");                
            sb.append(range.toString());
            sb.append(" ");            
            logger_.debug("Beginning transfer process to " + target + " for ranges " + sb.toString());                
        }
      
        /*
         * (1) First we dump all the memtables to disk.
         * (2) Run a version of compaction which will basically
         *     put the keys in the range specified into a directory
         *     named as per the endpoint it is destined for inside the
         *     bootstrap directory.
         * (3) Handoff the data.
        */
        List<String> tables = DatabaseDescriptor.getTables();
        for ( String tName : tables )
        {
            Table table = Table.open(tName);
            logger_.debug("Flushing memtables ...");
            table.flush(false);
            logger_.debug("Forcing compaction ...");
            /* Get the counting bloom filter for each endpoint and the list of files that need to be streamed */
            List<String> fileList = new ArrayList<String>();
            boolean bVal = table.forceCompaction(ranges, target, fileList);                
            doHandoff(target, fileList);
        }
    }

    /**
     * Stream the files in the bootstrap directory over to the
     * node being bootstrapped.
    */
    private void doHandoff(EndPoint target, List<String> fileList) throws IOException
    {
        List<File> filesList = new ArrayList<File>();
        for(String file : fileList)
        {
            filesList.add(new File(file));
        }
        File[] files = filesList.toArray(new File[0]);
        StreamContextManager.StreamContext[] streamContexts = new StreamContextManager.StreamContext[files.length];
        int i = 0;
        for ( File file : files )
        {
            streamContexts[i] = new StreamContextManager.StreamContext(file.getAbsolutePath(), file.length());
            logger_.debug("Stream context metadata " + streamContexts[i]);
            ++i;
        }
        
        if ( files.length > 0 )
        {
            /* Set up the stream manager with the files that need to streamed */
            StreamManager.instance(target).addFilesToStream(streamContexts);
            /* Send the bootstrap initiate message */
            BootstrapInitiateMessage biMessage = new BootstrapInitiateMessage(streamContexts);
            Message message = BootstrapInitiateMessage.makeBootstrapInitiateMessage(biMessage);
            logger_.debug("Sending a bootstrap initiate message to " + target + " ...");
            MessagingService.getMessagingInstance().sendOneWay(message, target);                
            logger_.debug("Waiting for transfer to " + target + " to complete");
            StreamManager.instance(target).waitForStreamCompletion();
            logger_.debug("Done with transfer to " + target);  
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1637.java