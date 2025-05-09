error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3733.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3733.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3733.java
text:
```scala
i@@f (DatabaseDescriptor.getEncryptionOptions() != null && DatabaseDescriptor.getEncryptionOptions().internode_encryption == EncryptionOptions.InternodeEncryption.all)

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

package org.apache.cassandra.net;

import java.io.IOError;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ServerSocketChannel;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.google.common.base.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.concurrent.DebuggableThreadPoolExecutor;
import org.apache.cassandra.concurrent.StageManager;
import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.config.EncryptionOptions;
import org.apache.cassandra.gms.Gossiper;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.locator.ILatencySubscriber;
import org.apache.cassandra.net.io.SerializerType;
import org.apache.cassandra.net.sink.SinkManager;
import org.apache.cassandra.security.SSLFactory;
import org.apache.cassandra.security.streaming.SSLFileStreamTask;
import org.apache.cassandra.service.ReadCallback;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.streaming.FileStreamTask;
import org.apache.cassandra.streaming.StreamHeader;
import org.apache.cassandra.utils.*;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public final class MessagingService implements MessagingServiceMBean
{
    public static final int VERSION_07 = 1;
    public static final int version_ = 2;
    //TODO: make this parameter dynamic somehow.  Not sure if config is appropriate.
    private SerializerType serializerType_ = SerializerType.BINARY;

    /** we preface every message with this number so the recipient can validate the sender is sane */
    private static final int PROTOCOL_MAGIC = 0xCA552DFA;

    /* This records all the results mapped by message Id */
    private final ExpiringMap<String, Pair<InetAddress, IMessageCallback>> callbacks;

    /* Lookup table for registering message handlers based on the verb. */
    private final Map<StorageService.Verb, IVerbHandler> verbHandlers_;

    /* Thread pool to handle messaging write activities */
    private final ExecutorService streamExecutor_;

    private final NonBlockingHashMap<InetAddress, OutboundTcpConnectionPool> connectionManagers_ = new NonBlockingHashMap<InetAddress, OutboundTcpConnectionPool>();

    private static final Logger logger_ = LoggerFactory.getLogger(MessagingService.class);
    private static final int LOG_DROPPED_INTERVAL_IN_MS = 5000;

    private SocketThread socketThread;
    private final SimpleCondition listenGate;
    private final Map<StorageService.Verb, AtomicInteger> droppedMessages = new EnumMap<StorageService.Verb, AtomicInteger>(StorageService.Verb.class);
    private final List<ILatencySubscriber> subscribers = new ArrayList<ILatencySubscriber>();

    {
        for (StorageService.Verb verb : StorageService.Verb.values())
            droppedMessages.put(verb, new AtomicInteger());
    }

    private static class MSHandle
    {
        public static final MessagingService instance = new MessagingService();
    }
    public static MessagingService instance()
    {
        return MSHandle.instance;
    }

    private MessagingService()
    {
        listenGate = new SimpleCondition();
        verbHandlers_ = new EnumMap<StorageService.Verb, IVerbHandler>(StorageService.Verb.class);
        streamExecutor_ = new DebuggableThreadPoolExecutor("Streaming", DatabaseDescriptor.getCompactionThreadPriority());
        Runnable logDropped = new Runnable()
        {
            public void run()
            {
                logDroppedMessages();
            }
        };
        StorageService.scheduledTasks.scheduleWithFixedDelay(logDropped, LOG_DROPPED_INTERVAL_IN_MS, LOG_DROPPED_INTERVAL_IN_MS, TimeUnit.MILLISECONDS);

        Function<Pair<String, Pair<InetAddress, IMessageCallback>>, ?> timeoutReporter = new Function<Pair<String, Pair<InetAddress, IMessageCallback>>, Object>()
        {
            public Object apply(Pair<String, Pair<InetAddress, IMessageCallback>> pair)
            {
                Pair<InetAddress, IMessageCallback> expiredValue = pair.right;
                maybeAddLatency(expiredValue.right, expiredValue.left, (double) DatabaseDescriptor.getRpcTimeout());
                return null;
            }
        };
        callbacks = new ExpiringMap<String, Pair<InetAddress, IMessageCallback>>((long) (1.1 * DatabaseDescriptor.getRpcTimeout()), timeoutReporter);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try
        {
            mbs.registerMBean(this, new ObjectName("org.apache.cassandra.net:type=MessagingService"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Track latency information for the dynamic snitch
     * @param cb: the callback associated with this message -- this lets us know if it's a message type we're interested in
     * @param address: the host that replied to the message
     * @param latency
     */
    public void maybeAddLatency(IMessageCallback cb, InetAddress address, double latency)
    {
        if (cb instanceof ReadCallback || cb instanceof AsyncResult)
            addLatency(address, latency);
    }

    public void addLatency(InetAddress address, double latency)
    {
        for (ILatencySubscriber subscriber : subscribers)
            subscriber.receiveTiming(address, latency);
    }

    public static byte[] hash(String type, byte data[])
    {
        byte result[];
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(type);
            result = messageDigest.digest(data);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }

    /** called from gossiper when it notices a node is not responding. */
    public void convict(InetAddress ep)
    {
        logger_.debug("Resetting pool for " + ep);
        getConnectionPool(ep).reset();
    }

    /**
     * Listen on the specified port.
     * @param localEp InetAddress whose port to listen on.
     */
    public void listen(InetAddress localEp) throws IOException, ConfigurationException
    {
        socketThread = new SocketThread(getServerSocket(localEp), "ACCEPT-" + localEp);
        socketThread.start();
        listenGate.signalAll();
    }

    private ServerSocket getServerSocket(InetAddress localEp) throws IOException, ConfigurationException
    {
        final ServerSocket ss;
        if (DatabaseDescriptor.getEncryptionOptions().internode_encryption == EncryptionOptions.InternodeEncryption.all)
        {
            ss = SSLFactory.getServerSocket(DatabaseDescriptor.getEncryptionOptions(), localEp, DatabaseDescriptor.getStoragePort());
            // setReuseAddress happens in the factory.
            logger_.info("Starting Encrypted Messaging Service on port {}", DatabaseDescriptor.getStoragePort());
        }
        else
        {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ss = serverChannel.socket();
            ss.setReuseAddress(true);
            InetSocketAddress address = new InetSocketAddress(localEp, DatabaseDescriptor.getStoragePort());
            try
            {
                ss.bind(address);
            }
            catch (BindException e)
            {
                if (e.getMessage().contains("in use"))
                    throw new ConfigurationException(address + " is in use by another process.  Change listen_address:storage_port in cassandra.yaml to values that do not conflict with other services");
                else if (e.getMessage().contains("Cannot assign requested address"))
                    throw new ConfigurationException("Unable to bind to address " + address + ". Set listen_address in cassandra.yaml to an interface you can bind to, e.g., your private IP address on EC2");
                else
                    throw e;
            }
            logger_.info("Starting Messaging Service on port {}", DatabaseDescriptor.getStoragePort());
        }
        return ss;
    }

    public void waitUntilListening()
    {
        try
        {
            listenGate.await();
        }
        catch (InterruptedException ie)
        {
            logger_.debug("await interrupted");
        }
    }

    public OutboundTcpConnectionPool getConnectionPool(InetAddress to)
    {
        OutboundTcpConnectionPool cp = connectionManagers_.get(to);
        if (cp == null)
        {
            connectionManagers_.putIfAbsent(to, new OutboundTcpConnectionPool(to));
            cp = connectionManagers_.get(to);
        }
        return cp;
    }

    public OutboundTcpConnection getConnection(InetAddress to, Message msg)
    {
        return getConnectionPool(to).getConnection(msg);
    }

    /**
     * Register a verb and the corresponding verb handler with the
     * Messaging Service.
     * @param verb
     * @param verbHandler handler for the specified verb
     */
    public void registerVerbHandlers(StorageService.Verb verb, IVerbHandler verbHandler)
    {
    	assert !verbHandlers_.containsKey(verb);
    	verbHandlers_.put(verb, verbHandler);
    }

    /**
     * This method returns the verb handler associated with the registered
     * verb. If no handler has been registered then null is returned.
     * @param type for which the verb handler is sought
     * @return a reference to IVerbHandler which is the handler for the specified verb
     */
    public IVerbHandler getVerbHandler(StorageService.Verb type)
    {
        return verbHandlers_.get(type);
    }

    private void addCallback(IMessageCallback cb, String messageId, InetAddress to)
    {
        Pair<InetAddress, IMessageCallback> previous = callbacks.put(messageId, new Pair<InetAddress, IMessageCallback>(to, cb));
        assert previous == null;
    }
    
    private static AtomicInteger idGen = new AtomicInteger(0);
    // TODO make these integers to avoid unnecessary int -> string -> int conversions
    private static String nextId()
    {
        return Integer.toString(idGen.incrementAndGet());
    }

    /**
     * Send a message to a given endpoint. This method specifies a callback
     * which is invoked with the actual response.
     * @param message message to be sent.
     * @param to endpoint to which the message needs to be sent
     * @param cb callback interface which is used to pass the responses or
     *           suggest that a timeout occurred to the invoker of the send().
     *           suggest that a timeout occurred to the invoker of the send().
     * @return an reference to message id used to match with the result
     */
    public String sendRR(Message message, InetAddress to, IMessageCallback cb)
    {
        String id = nextId();
        addCallback(cb, id, to);
        sendOneWay(message, id, to);
        return id;
    }

    public void sendOneWay(Message message, InetAddress to)
    {
        sendOneWay(message, nextId(), to);
    }

    public void sendReply(Message message, String id, InetAddress to)
    {
        sendOneWay(message, id, to);
    }

    /**
     * Send a message to a given endpoint. similar to sendRR(Message, InetAddress, IAsyncCallback)
     * @param producer pro
     * @param to endpoing to which the message needs to be sent
     * @param cb callback that processes responses.
     * @return a reference to the message id use to match with the result.
     */
    public String sendRR(MessageProducer producer, InetAddress to, IAsyncCallback cb)
    {
        try
        {
            return sendRR(producer.getMessage(Gossiper.instance.getVersion(to)), to, cb);
        }
        catch (IOException ex)
        {
            // happened during message creation.
            throw new IOError(ex);
        }
    }

    /**
     * Send a message to a given endpoint. This method adheres to the fire and forget
     * style messaging.
     * @param message messages to be sent.
     * @param to endpoint to which the message needs to be sent
     */
    private void sendOneWay(Message message, String id, InetAddress to)
    {
        if (logger_.isDebugEnabled())
            logger_.debug(FBUtilities.getLocalAddress() + " sending " + message.getVerb() + " to " + id + "@" + to);

        // do local deliveries
        if ( message.getFrom().equals(to) )
        {
            receive(message, id);
            return;
        }

        // message sinks are a testing hook
        Message processedMessage = SinkManager.processClientMessage(message, id, to);
        if (processedMessage == null)
        {
            return;
        }

        // get pooled connection (really, connection queue)
        OutboundTcpConnection connection = getConnection(to, message);

        // pack message with header in a bytebuffer
        byte[] data;
        try
        {
            DataOutputBuffer buffer = new DataOutputBuffer();
            buffer.writeUTF(id);
            Message.serializer().serialize(message, buffer, message.getVersion());
            data = buffer.getData();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        assert data.length > 0;
        ByteBuffer buffer = packIt(data , false, message.getVersion());

        // write it
        connection.write(buffer);
    }

    public IAsyncResult sendRR(Message message, InetAddress to)
    {
        IAsyncResult iar = new AsyncResult();
        sendRR(message, to, iar);
        return iar;
    }

    /**
     * Stream a file from source to destination. This is highly optimized
     * to not hold any of the contents of the file in memory.
     * @param header Header contains file to stream and other metadata.
     * @param to endpoint to which we need to stream the file.
    */

    public void stream(StreamHeader header, InetAddress to)
    {
        /* Streaming asynchronously on streamExector_ threads. */
        if (DatabaseDescriptor.getEncryptionOptions().internode_encryption == EncryptionOptions.InternodeEncryption.all)
            streamExecutor_.execute(new SSLFileStreamTask(header, to));
        else
            streamExecutor_.execute(new FileStreamTask(header, to));
    }

    public void register(ILatencySubscriber subcriber)
    {
        subscribers.add(subcriber);
    }

    /** blocks until the processing pools are empty and done. */
    public void waitFor() throws InterruptedException
    {
        while (!streamExecutor_.isTerminated())
            streamExecutor_.awaitTermination(5, TimeUnit.SECONDS);
    }

    public void shutdown()
    {
        logger_.info("Shutting down MessageService...");

        try
        {
            socketThread.close();
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }

        streamExecutor_.shutdownNow();
        callbacks.shutdown();

        logger_.info("Shutdown complete (no further commands will be processed)");
    }

    public void receive(Message message, String id)
    {
        message = SinkManager.processServerMessage(message, id);
        if (message == null)
            return;

        Runnable runnable = new MessageDeliveryTask(message, id);
        ExecutorService stage = StageManager.getStage(message.getMessageType());
        assert stage != null : "No stage for message type " + message.getMessageType();
        stage.execute(runnable);
    }

    public Pair<InetAddress, IMessageCallback> removeRegisteredCallback(String messageId)
    {
        return callbacks.remove(messageId);
    }

    public long getRegisteredCallbackAge(String messageId)
    {
        return callbacks.getAge(messageId);
    }

    public static void validateMagic(int magic) throws IOException
    {
        if (magic != PROTOCOL_MAGIC)
            throw new IOException("invalid protocol header");
    }

    public static int getBits(int x, int p, int n)
    {
        return x >>> (p + 1) - n & ~(-1 << n);
    }
        
    public ByteBuffer packIt(byte[] bytes, boolean compress, int version)
    {
        /*
             Setting up the protocol header. This is 4 bytes long
             represented as an integer. The first 2 bits indicate
             the serializer type. The 3rd bit indicates if compression
             is turned on or off. It is turned off by default. The 4th
             bit indicates if we are in streaming mode. It is turned off
             by default. The 5th-8th bits are reserved for future use.
             The next 8 bits indicate a version number. Remaining 15 bits
             are not used currently.
        */
        int header = 0;
        // Setting up the serializer bit
        header |= serializerType_.ordinal();
        // set compression bit.
        if (compress)
            header |= 4;
        // Setting up the version bit
        header |= (version << 8);

        ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + 4 + bytes.length);
        buffer.putInt(PROTOCOL_MAGIC);
        buffer.putInt(header);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }

    public ByteBuffer constructStreamHeader(StreamHeader streamHeader, boolean compress, int version)
    {
        /*
        Setting up the protocol header. This is 4 bytes long
        represented as an integer. The first 2 bits indicate
        the serializer type. The 3rd bit indicates if compression
        is turned on or off. It is turned off by default. The 4th
        bit indicates if we are in streaming mode. It is turned off
        by default. The following 4 bits are reserved for future use.
        The next 8 bits indicate a version number. Remaining 15 bits
        are not used currently.
        */
        int header = 0;
        // Setting up the serializer bit
        header |= serializerType_.ordinal();
        // set compression bit.
        if ( compress )
            header |= 4;
        // set streaming bit
        header |= 8;
        // Setting up the version bit
        header |= (version << 8);
        /* Finished the protocol header setup */

        /* Adding the StreamHeader which contains the session Id along
         * with the pendingfile info for the stream.
         * | Session Id | Pending File Size | Pending File | Bool more files |
         * | No. of Pending files | Pending Files ... |
         */
        byte[] bytes;
        try
        {
            DataOutputBuffer buffer = new DataOutputBuffer();
            StreamHeader.serializer().serialize(streamHeader, buffer, version);
            bytes = buffer.getData();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        assert bytes.length > 0;

        ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + 4 + bytes.length);
        buffer.putInt(PROTOCOL_MAGIC);
        buffer.putInt(header);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }

    public int incrementDroppedMessages(StorageService.Verb verb)
    {
        return droppedMessages.get(verb).incrementAndGet();
    }

    private void logDroppedMessages()
    {
        boolean logTpstats = false;
        for (Map.Entry<StorageService.Verb, AtomicInteger> entry : droppedMessages.entrySet())
        {
            AtomicInteger dropped = entry.getValue();
            if (dropped.get() > 0)
            {
                logTpstats = true;
                logger_.warn("Dropped {} {} messages in the last {}ms",
                             new Object[] {dropped, entry.getKey(), LOG_DROPPED_INTERVAL_IN_MS});
            }
            dropped.set(0);
        }

        if (logTpstats)
            StatusLogger.log();
    }

    private static class SocketThread extends Thread
    {
        private final ServerSocket server;

        SocketThread(ServerSocket server, String name)
        {
            super(name);
            this.server = server;
        }

        public void run()
        {
            while (true)
            {
                try
                {
                    Socket socket = server.accept();
                    new IncomingTcpConnection(socket).start();
                }
                catch (AsynchronousCloseException e)
                {
                    // this happens when another thread calls close().
                    logger_.info("MessagingService shutting down server thread.");
                    break;
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        void close() throws IOException
        {
            server.close();
        }
    }

    public Map<String, Integer> getCommandPendingTasks()
    {
        Map<String, Integer> pendingTasks = new HashMap<String, Integer>();
        for (Map.Entry<InetAddress, OutboundTcpConnectionPool> entry : connectionManagers_.entrySet())
            pendingTasks.put(entry.getKey().getHostAddress(), entry.getValue().cmdCon.getPendingMessages());
        return pendingTasks;
    }

    public Map<String, Long> getCommandCompletedTasks()
    {
        Map<String, Long> completedTasks = new HashMap<String, Long>();
        for (Map.Entry<InetAddress, OutboundTcpConnectionPool> entry : connectionManagers_.entrySet())
            completedTasks.put(entry.getKey().getHostAddress(), entry.getValue().cmdCon.getCompletedMesssages());
        return completedTasks;
    }

    public Map<String, Integer> getResponsePendingTasks()
    {
        Map<String, Integer> pendingTasks = new HashMap<String, Integer>();
        for (Map.Entry<InetAddress, OutboundTcpConnectionPool> entry : connectionManagers_.entrySet())
            pendingTasks.put(entry.getKey().getHostAddress(), entry.getValue().ackCon.getPendingMessages());
        return pendingTasks;
    }

    public Map<String, Long> getResponseCompletedTasks()
    {
        Map<String, Long> completedTasks = new HashMap<String, Long>();
        for (Map.Entry<InetAddress, OutboundTcpConnectionPool> entry : connectionManagers_.entrySet())
            completedTasks.put(entry.getKey().getHostAddress(), entry.getValue().ackCon.getCompletedMesssages());
        return completedTasks;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3733.java