error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1525.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1525.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1053
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1525.java
text:
```scala
public class JMSServices {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

p@@ackage org.jboss.as.messaging.jms;

import static org.jboss.as.messaging.jms.CommonAttributes.AUTO_GROUP;
import static org.jboss.as.messaging.jms.CommonAttributes.BLOCK_ON_ACK;
import static org.jboss.as.messaging.jms.CommonAttributes.BLOCK_ON_DURABLE_SEND;
import static org.jboss.as.messaging.jms.CommonAttributes.BLOCK_ON_NON_DURABLE_SEND;
import static org.jboss.as.messaging.jms.CommonAttributes.CACHE_LARGE_MESSAGE_CLIENT;
import static org.jboss.as.messaging.jms.CommonAttributes.CALL_TIMEOUT;
import static org.jboss.as.messaging.jms.CommonAttributes.CLIENT_FAILURE_CHECK_PERIOD;
import static org.jboss.as.messaging.jms.CommonAttributes.CLIENT_ID;
import static org.jboss.as.messaging.jms.CommonAttributes.CONFIRMATION_WINDOW_SIZE;
import static org.jboss.as.messaging.jms.CommonAttributes.CONNECTION_TTL;
import static org.jboss.as.messaging.jms.CommonAttributes.CONNECTOR;
import static org.jboss.as.messaging.jms.CommonAttributes.CONSUMER_MAX_RATE;
import static org.jboss.as.messaging.jms.CommonAttributes.CONSUMER_WINDOW_SIZE;
import static org.jboss.as.messaging.jms.CommonAttributes.DISCOVERY_GROUP_NAME;
import static org.jboss.as.messaging.jms.CommonAttributes.DISCOVERY_INITIAL_WAIT_TIMEOUT;
import static org.jboss.as.messaging.jms.CommonAttributes.DUPS_OK_BATCH_SIZE;
import static org.jboss.as.messaging.jms.CommonAttributes.ENTRIES;
import static org.jboss.as.messaging.jms.CommonAttributes.FAILOVER_ON_INITIAL_CONNECTION;
import static org.jboss.as.messaging.jms.CommonAttributes.FAILOVER_ON_SERVER_SHUTDOWN;
import static org.jboss.as.messaging.jms.CommonAttributes.GROUP_ID;
import static org.jboss.as.messaging.jms.CommonAttributes.MAX_RETRY_INTERVAL;
import static org.jboss.as.messaging.jms.CommonAttributes.MIN_LARGE_MESSAGE_SIZE;
import static org.jboss.as.messaging.jms.CommonAttributes.PRE_ACK;
import static org.jboss.as.messaging.jms.CommonAttributes.PRODUCER_MAX_RATE;
import static org.jboss.as.messaging.jms.CommonAttributes.PRODUCER_WINDOW_SIZE;
import static org.jboss.as.messaging.jms.CommonAttributes.RECONNECT_ATTEMPTS;
import static org.jboss.as.messaging.jms.CommonAttributes.RETRY_INTERVAL;
import static org.jboss.as.messaging.jms.CommonAttributes.RETRY_INTERVAL_MULTIPLIER;
import static org.jboss.as.messaging.jms.CommonAttributes.SCHEDULED_THREAD_POOL_MAX_SIZE;
import static org.jboss.as.messaging.jms.CommonAttributes.THREAD_POOL_MAX_SIZE;
import static org.jboss.as.messaging.jms.CommonAttributes.TRANSACTION_BATCH_SIZE;
import static org.jboss.as.messaging.jms.CommonAttributes.USE_GLOBAL_POOLS;

import org.jboss.as.messaging.MessagingServices;
import org.jboss.dmr.ModelType;
import org.jboss.msc.service.ServiceName;

/**
 * @author Emanuel Muckenhuber
 */
class JMSServices {

    public static final ServiceName JMS = MessagingServices.JBOSS_MESSAGING.append("jms");
    public static final ServiceName JMS_MANAGER = JMS.append("manager");
    public static final ServiceName JMS_QUEUE_BASE = JMS.append("queue");
    public static final ServiceName JMS_TOPIC_BASE = JMS.append("topic");
    public static final ServiceName JMS_CF_BASE = JMS.append("connection-factory");

    static NodeAttribute[] CONNECTION_FACTORY_ATTRS = new NodeAttribute[] {
        //Do these 2 most frequently used ones out of alphabetical order
        new NodeAttribute(CONNECTOR, ModelType.OBJECT, false),   //<------
        new NodeAttribute(ENTRIES, ModelType.LIST, ModelType.STRING, false),

        new NodeAttribute(AUTO_GROUP, ModelType.BOOLEAN, false),
        new NodeAttribute(BLOCK_ON_ACK, ModelType.BOOLEAN, false),
        new NodeAttribute(BLOCK_ON_DURABLE_SEND, ModelType.BOOLEAN, false),
        new NodeAttribute(BLOCK_ON_NON_DURABLE_SEND, ModelType.BOOLEAN, false),
        new NodeAttribute(CACHE_LARGE_MESSAGE_CLIENT, ModelType.BOOLEAN, false),
        new NodeAttribute(CALL_TIMEOUT, ModelType.LONG, false),
        new NodeAttribute(CLIENT_FAILURE_CHECK_PERIOD, ModelType.LONG, false),
        new NodeAttribute(CLIENT_ID, ModelType.STRING, false),
        new NodeAttribute(CONFIRMATION_WINDOW_SIZE, ModelType.INT, false),
        new NodeAttribute(CONNECTION_TTL, ModelType.LONG, false),
        new NodeAttribute(CONSUMER_MAX_RATE, ModelType.INT, false),
        new NodeAttribute(CONSUMER_WINDOW_SIZE, ModelType.INT, false),
        new NodeAttribute(DISCOVERY_GROUP_NAME, ModelType.STRING, false),
        new NodeAttribute(DISCOVERY_INITIAL_WAIT_TIMEOUT, ModelType.LONG, false),
        new NodeAttribute(DUPS_OK_BATCH_SIZE, ModelType.INT, false),
        new NodeAttribute(FAILOVER_ON_INITIAL_CONNECTION, ModelType.BOOLEAN, false),
        new NodeAttribute(FAILOVER_ON_SERVER_SHUTDOWN, ModelType.BOOLEAN, false),
        new NodeAttribute(GROUP_ID, ModelType.STRING, false),
        new NodeAttribute(MAX_RETRY_INTERVAL, ModelType.LONG, false),
        new NodeAttribute(MIN_LARGE_MESSAGE_SIZE, ModelType.LONG, false),
        new NodeAttribute(PRE_ACK, ModelType.BOOLEAN, false),
        new NodeAttribute(PRODUCER_MAX_RATE, ModelType.INT, false),
        new NodeAttribute(PRODUCER_WINDOW_SIZE, ModelType.INT, false),
        new NodeAttribute(RECONNECT_ATTEMPTS, ModelType.INT, false),
        new NodeAttribute(RETRY_INTERVAL, ModelType.LONG, false),
        new NodeAttribute(RETRY_INTERVAL_MULTIPLIER, ModelType.BIG_DECIMAL, false),
        new NodeAttribute(SCHEDULED_THREAD_POOL_MAX_SIZE, ModelType.INT, false),
        new NodeAttribute(THREAD_POOL_MAX_SIZE, ModelType.INT, false),
        new NodeAttribute(TRANSACTION_BATCH_SIZE, ModelType.INT, false),
        new NodeAttribute(USE_GLOBAL_POOLS, ModelType.BOOLEAN, false)};

    static class NodeAttribute {
        private final String name;
        private final ModelType type;
        private final ModelType valueType;
        private final boolean required;

        NodeAttribute(String name, ModelType type, boolean required) {
            this(name, type, null, required);
        }


        NodeAttribute(String name, ModelType type, ModelType valueType, boolean required) {
            this.name = name;
            this.type = type;
            this.required = required;
            this.valueType = valueType;
        }

        String getName() {
            return name;
        }

        ModelType getType() {
            return type;
        }

        ModelType getValueType() {
            return valueType;
        }

        boolean isRequired() {
            return required;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1525.java