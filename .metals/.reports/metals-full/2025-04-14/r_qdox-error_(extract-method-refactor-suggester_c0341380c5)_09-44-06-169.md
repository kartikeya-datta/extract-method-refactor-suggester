error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12630.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12630.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12630.java
text:
```scala
q@@ueueCount = Utils.getInt(storm_conf.get(Config.WORKER_RECEIVER_THREAD_COUNT), 1);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package backtype.storm.messaging.netty;

import backtype.storm.Config;
import backtype.storm.messaging.IConnection;
import backtype.storm.messaging.TaskMessage;
import backtype.storm.utils.Utils;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

class Server implements IConnection {
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);
    @SuppressWarnings("rawtypes")
    Map storm_conf;
    int port;
    
    // Create multiple queues for incoming messages. The size equals the number of receiver threads.
    // For message which is sent to same task, it will be stored in the same queue to preserve the message order.
    private LinkedBlockingQueue<ArrayList<TaskMessage>>[] message_queue;
    
    volatile ChannelGroup allChannels = new DefaultChannelGroup("storm-server");
    final ChannelFactory factory;
    final ServerBootstrap bootstrap;
    
    private int queueCount;
    HashMap<Integer, Integer> taskToQueueId = null;
    int roundRobinQueueId;
	
    boolean closing = false;
    List<TaskMessage> closeMessage = Arrays.asList(new TaskMessage(-1, null));
    
    
    @SuppressWarnings("rawtypes")
    Server(Map storm_conf, int port) {
        this.storm_conf = storm_conf;
        this.port = port;
        
        queueCount = Utils.getInt(storm_conf.get("worker.receiver.thread.count"), 1);
        roundRobinQueueId = 0;
        taskToQueueId = new HashMap<Integer, Integer>();
    
        message_queue = new LinkedBlockingQueue[queueCount];
        for (int i = 0; i < queueCount; i++) {
            message_queue[i] = new LinkedBlockingQueue<ArrayList<TaskMessage>>();
        }
        
        // Configure the server.
        int buffer_size = Utils.getInt(storm_conf.get(Config.STORM_MESSAGING_NETTY_BUFFER_SIZE));
        int maxWorkers = Utils.getInt(storm_conf.get(Config.STORM_MESSAGING_NETTY_SERVER_WORKER_THREADS));

        ThreadFactory bossFactory = new NettyRenameThreadFactory(name() + "-boss");
        ThreadFactory workerFactory = new NettyRenameThreadFactory(name() + "-worker");
        
        if (maxWorkers > 0) {
            factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(bossFactory), 
                Executors.newCachedThreadPool(workerFactory), maxWorkers);
        } else {
            factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(bossFactory), 
                Executors.newCachedThreadPool(workerFactory));
        }
        
        LOG.info("Create Netty Server " + name() + ", buffer_size: " + buffer_size + ", maxWorkers: " + maxWorkers);
        
        bootstrap = new ServerBootstrap(factory);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.receiveBufferSize", buffer_size);
        bootstrap.setOption("child.keepAlive", true);

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new StormServerPipelineFactory(this));

        // Bind and start to accept incoming connections.
        Channel channel = bootstrap.bind(new InetSocketAddress(port));
        allChannels.add(channel);
    }
    
    private ArrayList<TaskMessage>[] groupMessages(List<TaskMessage> msgs) {
      ArrayList<TaskMessage> messageGroups[] = new ArrayList[queueCount];
      
      for (int i = 0; i < msgs.size(); i++) {
        TaskMessage message = msgs.get(i);
        int task = message.task();
        
        if (task == -1) {
          closing = true;
          return null;
        }
        
        Integer queueId = getMessageQueueId(task);
        
        if (null == messageGroups[queueId]) {
          messageGroups[queueId] = new ArrayList<TaskMessage>();
        }
        messageGroups[queueId].add(message);
      }
      return messageGroups;
    }
    
    private Integer getMessageQueueId(int task) {
      // try to construct the map from taskId -> queueId in round robin manner.
      
      Integer queueId = taskToQueueId.get(task);
      if (null == queueId) {
        synchronized(taskToQueueId) {
          //assgin task to queue in round-robin manner
          if (null == taskToQueueId.get(task)) {
            queueId = roundRobinQueueId++;
            
            taskToQueueId.put(task, queueId);
            if (roundRobinQueueId == queueCount) {
              roundRobinQueueId = 0;
            }
          }
        }
      }
      return queueId;
    }

    /**
     * enqueue a received message 
     * @param message
     * @throws InterruptedException
     */
    protected void enqueue(List<TaskMessage> msgs) throws InterruptedException {
      
      if (null == msgs || msgs.size() == 0 || closing) {
        return;
      }
      
      ArrayList<TaskMessage> messageGroups[] = groupMessages(msgs);
      
      if (null == messageGroups || closing) {
        return;
      }
      
      for (int receiverId = 0; receiverId < messageGroups.length; receiverId++) {
        ArrayList<TaskMessage> msgGroup = messageGroups[receiverId];
        if (null != msgGroup) {
          message_queue[receiverId].put(msgGroup);
        }
      }
   }
    
    /**
     * fetch a message from message queue synchronously (flags != 1) or asynchronously (flags==1)
     */
    public Iterator<TaskMessage> recv(int flags)  {
      if (queueCount > 1) {
        throw new RuntimeException("Use recv(int flags, int clientId) instead, as we have worker.receiver.thread.count=" + queueCount + " receive threads, clientId should be 0 <= clientId < " + queueCount);
      }
      return recv(flags, 0);
    }
    
    public Iterator<TaskMessage> recv(int flags, int receiverId)  {
      if (closing) {
        return closeMessage.iterator();
      }
      
      ArrayList<TaskMessage> ret = null; 
      int queueId = receiverId % queueCount;
      if ((flags & 0x01) == 0x01) { 
            //non-blocking
            ret = message_queue[queueId].poll();
        } else {
            try {
                ArrayList<TaskMessage> request = message_queue[queueId].take();
                LOG.debug("request to be processed: {}", request);
                ret = request;
            } catch (InterruptedException e) {
                LOG.info("exception within msg receiving", e);
                ret = null;
            }
        }
      
      if (null != ret) {
        return ret.iterator();
      }
      return null;
    }

    /**
     * register a newly created channel
     * @param channel
     */
    protected void addChannel(Channel channel) {
        allChannels.add(channel);
    }
    
    /**
     * close a channel
     * @param channel
     */
    protected void closeChannel(Channel channel) {
        channel.close().awaitUninterruptibly();
        allChannels.remove(channel);
    }

    /**
     * close all channels, and release resources
     */
    public synchronized void close() {
        if (allChannels != null) {
            allChannels.close().awaitUninterruptibly();
            factory.releaseExternalResources();
            allChannels = null;
        }
    }

    public void send(int task, byte[] message) {
        throw new RuntimeException("Server connection should not send any messages");
    }
    
    public void send(Iterator<TaskMessage> msgs) {
      throw new RuntimeException("Server connection should not send any messages");
    }
	
    public String name() {
      return "Netty-server-localhost-" + port;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12630.java