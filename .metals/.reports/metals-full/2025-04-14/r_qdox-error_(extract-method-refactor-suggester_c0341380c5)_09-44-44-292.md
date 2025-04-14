error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16921.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16921.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16921.java
text:
```scala
private final M@@essageConsumer consumer;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.protocol.jms.sampler;

import org.apache.jmeter.protocol.jms.Utils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.jms.*;

/**
 * Receiver of pseudo-synchronous reply messages.
 *
 */
public class Receiver implements Runnable {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private volatile boolean active;

    private final QueueSession session;

    private final QueueReceiver consumer;

    private final QueueConnection conn;

    private final boolean useResMsgIdAsCorrelId;

    private Receiver(QueueConnectionFactory factory, Queue receiveQueue, String principal, String credentials, boolean useResMsgIdAsCorrelId) throws JMSException {
        if (null != principal && null != credentials) {
            log.info("creating receiver WITH authorisation credentials. UseResMsgId="+useResMsgIdAsCorrelId);
            conn = factory.createQueueConnection(principal, credentials);
        }else{
            log.info("creating receiver without authorisation credentials. UseResMsgId="+useResMsgIdAsCorrelId);
            conn = factory.createQueueConnection(); 
        }
        session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createReceiver(receiveQueue);
        this.useResMsgIdAsCorrelId = useResMsgIdAsCorrelId;
        log.debug("Receiver - ctor. Starting connection now");
        conn.start();
        log.info("Receiver - ctor. Connection to messaging system established");
    }

    /**
     * Create a receiver to process responses.
     * 
     * @param factory
     * @param receiveQueue
     * @param principal
     * @param credentials
     * @param useResMsgIdAsCorrelId true if should use JMSMessageId, false if should use JMSCorrelationId
     * @return the Receiver which will process the responses
     * @throws JMSException
     */
    public static Receiver createReceiver(QueueConnectionFactory factory, Queue receiveQueue,
            String principal, String credentials, boolean useResMsgIdAsCorrelId)
            throws JMSException {
        Receiver receiver = new Receiver(factory, receiveQueue, principal, credentials, useResMsgIdAsCorrelId);
        Thread thread = new Thread(receiver);
        thread.start();
        return receiver;
    }

    public void run() {
        active = true;
        Message reply;

        while (active) {
            reply = null;
            try {
                reply = consumer.receive(5000);
                if (reply != null) {
                    String messageKey;
                    final MessageAdmin admin = MessageAdmin.getAdmin();
                    if (useResMsgIdAsCorrelId){
                        messageKey = reply.getJMSMessageID();
                        synchronized (admin) {// synchronize with FixedQueueExecutor
                            admin.putReply(messageKey, reply);                            
                        }
                    } else {
                        messageKey = reply.getJMSCorrelationID();
                        if (messageKey == null) {// JMSMessageID cannot be null
                            log.warn("Received message with correlation id null. Discarding message ...");
                        } else {
                            admin.putReply(messageKey, reply);
                        }
                    }
                }

            } catch (JMSException e1) {
                log.error("Error handling receive",e1);
            }
        }
        Utils.close(consumer, log);
        Utils.close(session, log);
        Utils.close(conn, log);
    }

    public void deactivate() {
        active = false;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16921.java