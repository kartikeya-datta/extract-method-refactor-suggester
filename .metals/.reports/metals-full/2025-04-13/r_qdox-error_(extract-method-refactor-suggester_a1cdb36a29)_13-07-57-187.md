error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3594.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3594.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3594.java
text:
```scala
T@@RANSACTION_BATH_SIZE(CommonAttributes.TRANSACTION_BATCH_SIZE),

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

package org.jboss.as.messaging.jms;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emanuel Muckenhuber
 */
enum Element {
    UNKNOWN(null),

    AUTO_GROUP(CommonAttributes.AUTO_GROUP),
    BLOCK_ON_ACK(CommonAttributes.BLOCK_ON_ACK),
    BLOCK_ON_DURABLE_SEND(CommonAttributes.BLOCK_ON_DURABLE_SEND),
    BLOCK_ON_NON_DURABLE_SEND(CommonAttributes.BLOCK_ON_NON_DURABLE_SEND),
    CACHE_LARGE_MESSAGE_CLIENT(CommonAttributes.CACHE_LARGE_MESSAGE_CLIENT),
    CALL_TIMEOUT(CommonAttributes.CALL_TIMEOUT),
    CLIENT_FAILURE_CHECK_PERIOD(CommonAttributes.CLIENT_FAILURE_CHECK_PERIOD),
    CLIENT_ID(CommonAttributes.CLIENT_ID),
    CONNECTION_FACTORY(CommonAttributes.CONNECTION_FACTORY),
    CONNECTOR_REF(CommonAttributes.CONNECTOR_REF),
    CONNECTORS(CommonAttributes.CONNECTORS),
    CONNECTION_TTL(CommonAttributes.CONNECTION_TTL),
    CONFIRMATION_WINDOW_SIZE(CommonAttributes.CONFIRMATION_WINDOW_SIZE),
    CONSUMER_MAX_RATE(CommonAttributes.CONSUMER_MAX_RATE),
    CONSUMER_WINDOW_SIZE(CommonAttributes.CONSUMER_WINDOW_SIZE),
    DISCOVERY_INITIAL_WAIT_TIMEOUT(CommonAttributes.DISCOVERY_INITIAL_WAIT_TIMEOUT),
    DISCOVERY_GROUP_REF(CommonAttributes.DISCOVERY_GROUP_REF),
    DUPS_OK_BATCH_SIZE(CommonAttributes.DUPS_OK_BATCH_SIZE),
    DURABLE(CommonAttributes.DURABLE),
    ENTRIES(CommonAttributes.ENTRIES),
    ENTRY(CommonAttributes.ENTRY),
    FAILOVER_ON_INITIAL_CONNECTION(CommonAttributes.FAILOVER_ON_INITIAL_CONNECTION),
    FAILOVER_ON_SERVER_SHUTDOWN(CommonAttributes.FAILOVER_ON_SERVER_SHUTDOWN),
    GROUP_ID(CommonAttributes.GROUP_ID),
    LOAD_BALANCING_CLASS_NAME(CommonAttributes.LOAD_BALANCING_CLASS_NAME),
    MAX_RETRY_INTERVAL(CommonAttributes.MAX_RETRY_INTERVAL),
    MIN_LARGE_MESSAGE_SIZE(CommonAttributes.MIN_LARGE_MESSAGE_SIZE),
    PRE_ACK(CommonAttributes.PRE_ACK),
    PRODUCER_WINDOW_SIZE(CommonAttributes.PRODUCER_WINDOW_SIZE),
    PRODUCER_MAX_RATE(CommonAttributes.PRODUCER_MAX_RATE),
    QUEUE(CommonAttributes.QUEUE),
    RECONNECT_ATTEMPTS(CommonAttributes.RECONNECT_ATTEMPTS),
    RETRY_INTERVAL(CommonAttributes.RETRY_INTERVAL),
    RETRY_INTERVAL_MULTIPLIER(CommonAttributes.RETRY_INTERVAL_MULTIPLIER),
    SELECTOR(CommonAttributes.SELECTOR),
    SCHEDULED_THREAD_POOL_MAX_SIZE(CommonAttributes.SCHEDULED_THREAD_POOL_MAX_SIZE),
    THREAD_POOL_MAX_SIZE(CommonAttributes.THREAD_POOL_MAX_SIZE),
    TOPIC(CommonAttributes.TOPIC),
    TRANSACTION_BATH_SIZE(CommonAttributes.TRANSACTION_BATH_SIZE),
    USE_GLOBAL_POOLS(CommonAttributes.USE_GLOBAL_POOLS),

    ;

    private final String name;

    Element(final String name) {
       this.name = name;
    }

    /**
     * Get the local name of this element.
     *
     * @return the local name
     */
    public String getLocalName() {
       return name;
    }

    private static final Map<String, Element> MAP;

    static {
       final Map<String, Element> map = new HashMap<String, Element>();
       for (Element element : values()) {
          final String name = element.getLocalName();
          if (name != null) map.put(name, element);
       }
       MAP = map;
    }

    public static Element forName(String localName) {
       final Element element = MAP.get(localName);
       return element == null ? UNKNOWN : element;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3594.java