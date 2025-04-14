error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14483.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14483.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14483.java
text:
```scala
final S@@erviceController<?> service = context.getServiceRegistry().getService(JMS_MANAGER);

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

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.messaging.MessagingSubsystemElement;
import org.jboss.as.model.AbstractSubsystemElement;
import org.jboss.as.model.AbstractSubsystemUpdate;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * The JMS subsystem configuration.
 *
 * @author Emanuel Muckenhuber
 */
public class JMSSubsystemElement extends AbstractSubsystemElement<JMSSubsystemElement> {

    private static final long serialVersionUID = 3225118788089921849L;

    public static final ServiceName JMS = MessagingSubsystemElement.JBOSS_MESSAGING.append("jms");
    public static final ServiceName JMS_MANAGER = JMS.append("manager");
    public static final ServiceName JMS_QUEUE_BASE = JMS.append("queue");
    public static final ServiceName JMS_TOPIC_BASE = JMS.append("topic");
    public static final ServiceName JMS_CF_BASE = JMS.append("connection-factory");

    private final NavigableMap<String, JMSQueueElement> queues = new TreeMap<String, JMSQueueElement>();
    private final NavigableMap<String, JMSTopicElement> topics = new TreeMap<String, JMSTopicElement>();
    private final NavigableMap<String, ConnectionFactoryElement> connectionFactories = new TreeMap<String, ConnectionFactoryElement>();

    protected JMSSubsystemElement() {
        super(Namespace.CURRENT.getUriString());
    }

    /** {@inheritDoc} */
    protected void getUpdates(List<? super AbstractSubsystemUpdate<JMSSubsystemElement, ?>> list) {
        for(final ConnectionFactoryElement cf : connectionFactories.values()) {
            list.add(new ConnectionFactoryAdd(cf));
        }
        for(final JMSQueueElement queue : queues.values()) {
            list.add(JMSQueueAdd.create(queue));
        }
        for(final JMSTopicElement topic : topics.values()) {
            list.add(JMSTopicAdd.create(topic));
        }
    }

    /** {@inheritDoc} */
    protected boolean isEmpty() {
        return queues.isEmpty() && topics.isEmpty() && connectionFactories.isEmpty();
    }

    /** {@inheritDoc} */
    protected JMSSubsystemAdd getAdd() {
        return new JMSSubsystemAdd();
    }

    /** {@inheritDoc} */
    protected <P> void applyRemove(UpdateContext context, UpdateResultHandler<? super Void, P> resultHandler, P param) {
        final ServiceController<?> service = context.getServiceContainer().getService(JMS_MANAGER);
        if(service == null) {
            resultHandler.handleSuccess(null, param);
        } else {
            service.addListener(new UpdateResultHandler.ServiceRemoveListener<P>(resultHandler, param));
        }
    }

    boolean addConnectionFactory(final ConnectionFactoryElement cf) {
        if(connectionFactories.containsKey(cf.getName())) {
            return false;
        }
        connectionFactories.put(cf.getName(), cf);
        return true;
    }

    public ConnectionFactoryElement getConnectionFactory(final String name) {
        return connectionFactories.get(name);
    }

    boolean removeConnectionFactory(final String name) {
        return connectionFactories.remove(name) != null;
    }

    JMSQueueElement addQueue(final String name) {
        if(queues.containsKey(name)) {
            return null;
        }
        final JMSQueueElement queue = new JMSQueueElement(name);
        queues.put(name, queue);
        return queue;
    }

    public JMSQueueElement getQueue(final String name) {
        return queues.get(name);
    }

    boolean removeQueue(final String name) {
        return queues.remove(name) != null;
    }

    JMSTopicElement addTopic(final String name) {
        if(topics.containsKey(name)) {
            return null;
        }
        final JMSTopicElement topic = new JMSTopicElement(name);
        topics.put(name, topic);
        return topic;
    }

    public JMSTopicElement getTopic(final String name) {
        return topics.get(name);
    }

    boolean removeTopic(final String name) {
        return topics.remove(name) != null;
    }

    /** {@inheritDoc} */
    protected Class<JMSSubsystemElement> getElementClass() {
        return JMSSubsystemElement.class;
    }

    /** {@inheritDoc} */
    public void writeContent(XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        if(! connectionFactories.isEmpty()) {
            for(final ConnectionFactoryElement cf : connectionFactories.values()) {
                streamWriter.writeStartElement(Element.CONNECTION_FACTORY.getLocalName());
                cf.writeContent(streamWriter);
            }
        }
        if(! queues.isEmpty()) {
            for(final JMSQueueElement queue : queues.values()) {
                streamWriter.writeStartElement(Element.QUEUE.getLocalName());
                queue.writeContent(streamWriter);
            }
        }
        if(! topics.isEmpty()) {
            for(final JMSTopicElement topic : topics.values()) {
                streamWriter.writeStartElement(Element.TOPIC.getLocalName());
                topic.writeContent(streamWriter);
            }
        }
        streamWriter.writeEndElement();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14483.java