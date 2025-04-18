error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12418.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12418.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12418.java
text:
```scala
i@@f (handoffExecutor != null) {

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

package org.jboss.as.threads;

import org.jboss.as.model.AbstractModelUpdate;
import org.jboss.as.model.PropertiesElement;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.BatchServiceBuilder;
import org.jboss.msc.service.Location;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamException;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class BoundedQueueThreadPoolExecutorElement extends AbstractExecutorElement<BoundedQueueThreadPoolExecutorElement> {

    private static final long serialVersionUID = -6314205265652284301L;

    private String handoffExecutor;
    private boolean blocking;
    private boolean allowCoreTimeout;
    private ScaledCount queueLength;
    private ScaledCount coreThreads;

    public BoundedQueueThreadPoolExecutorElement(final Location location, final String name) {
        super(location, name);
    }

    public BoundedQueueThreadPoolExecutorElement(final XMLExtendedStreamReader reader) throws XMLStreamException {
        super(reader);
        // Attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i ++) {
            if (reader.getAttributeNamespace(i) != null) {
                throw unexpectedAttribute(reader, i);
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ALLOW_CORE_TIMEOUT: {
                    allowCoreTimeout = Boolean.parseBoolean(reader.getAttributeValue(i));
                    break;
                }
                case BLOCKING: {
                    blocking = Boolean.parseBoolean(reader.getAttributeValue(i));
                    break;
                }
                case NAME: break;
                default: throw unexpectedAttribute(reader, i);
            }
        }
        // Elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case THREADS_1_0: {
                    switch (Element.forName(reader.getLocalName())) {
                        case CORE_THREADS: {
                            coreThreads = readScaledCountElement(reader);
                            break;
                        }
                        case MAX_THREADS: {
                            setMaxThreads(readScaledCountElement(reader));
                            break;
                        }
                        case KEEPALIVE_TIME: {
                            setKeepaliveTime(readTimeSpecElement(reader));
                            break;
                        }
                        case THREAD_FACTORY: {
                            setThreadFactory(readStringAttributeElement(reader, "name"));
                            break;
                        }
                        case PROPERTIES: {
                            setProperties(new PropertiesElement(reader));
                            break;
                        }
                        case QUEUE_LENGTH: {
                            queueLength = readScaledCountElement(reader);
                            break;
                        }
                        case HANDOFF_EXECUTOR: {
                            handoffExecutor = readStringAttributeElement(reader, "handoff-executor");
                            break;
                        }
                        default: throw unexpectedElement(reader);
                    }
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }

    }

    public void activate(final ServiceActivatorContext context) {final BatchBuilder batchBuilder = context.getBatchBuilder();
        final ScaledCount coreThreads = this.coreThreads;
        int coreThreadsValue = coreThreads != null ? coreThreads.getScaledCount() : 0;
        final ScaledCount maxThreads = getMaxThreads();
        int maxThreadsValue = maxThreads != null ? maxThreads.getScaledCount() : Integer.MAX_VALUE;
        final ScaledCount queueLength = this.queueLength;
        int queueLengthValue = queueLength != null ? queueLength.getScaledCount() : Integer.MAX_VALUE;

        TimeSpec keepAlive = getKeepaliveTime();
        if(keepAlive == null)
            keepAlive = TimeSpec.DEFAULT_KEEPALIVE;
        final BoundedQueueThreadPoolService service = new BoundedQueueThreadPoolService(coreThreadsValue, maxThreadsValue, queueLengthValue, blocking, keepAlive, allowCoreTimeout);
        final ServiceName serviceName = JBOSS_THREAD_EXECUTOR.append(getName());
        final BatchServiceBuilder<ExecutorService> serviceBuilder = batchBuilder.addService(serviceName, service);
        final String threadFactory = getThreadFactory();
        final ServiceName threadFactoryName;
        if (threadFactory == null) {
            threadFactoryName = serviceName.append("thread-factory");
            batchBuilder.addService(threadFactoryName, new ThreadFactoryService());
        } else {
            threadFactoryName = JBOSS_THREAD_FACTORY.append(threadFactory);
        }
        serviceBuilder.addDependency(threadFactoryName, ThreadFactory.class, service.getThreadFactoryInjector());
        final String handoffExecutor = this.handoffExecutor;
        if (handoffExecutor == null) {
            final ServiceName handoffExecutorName = JBOSS_THREAD_EXECUTOR.append(handoffExecutor);
            serviceBuilder.addDependency(handoffExecutorName, Executor.class, service.getHandoffExecutorInjector());
        }
    }

    public long elementHash() {
        long hash = super.elementHash();
        hash = Long.rotateLeft(hash, 1) ^ Boolean.valueOf(blocking).hashCode() & 0xffffffffL;
        hash = Long.rotateLeft(hash, 1) ^ Boolean.valueOf(allowCoreTimeout).hashCode() & 0xffffffffL;
        if (handoffExecutor != null) hash = Long.rotateLeft(hash, 1) ^ handoffExecutor.hashCode() & 0xffffffffL;
        hash = Long.rotateLeft(hash, 1) ^ queueLength.elementHash();
        if (coreThreads != null) hash = Long.rotateLeft(hash, 1) ^ coreThreads.elementHash();
        return hash;
    }

    protected void appendDifference(final Collection<AbstractModelUpdate<BoundedQueueThreadPoolExecutorElement>> target, final BoundedQueueThreadPoolExecutorElement other) {
    }

    protected Class<BoundedQueueThreadPoolExecutorElement> getElementClass() {
        return BoundedQueueThreadPoolExecutorElement.class;
    }

    public void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeAttribute("name", getName());
        if (blocking) { streamWriter.writeAttribute("blocking", "true"); }
        if (allowCoreTimeout) { streamWriter.writeAttribute("allow-core-timeout", "true"); }
        if (coreThreads != null) {
            writeScaledCountElement(streamWriter, coreThreads, "core-threads");
        }
        writeScaledCountElement(streamWriter, queueLength, "queue-length");
        writeScaledCountElement(streamWriter, getMaxThreads(), "max-threads");
        final TimeSpec keepaliveTime = getKeepaliveTime();
        if (keepaliveTime != null) writeTimeSpecElement(streamWriter, keepaliveTime, "keepalive-time");
        final String threadFactory = getThreadFactory();
        if (threadFactory != null) {
            streamWriter.writeEmptyElement("thread-factory");
            streamWriter.writeAttribute("name", threadFactory);
        }
        final PropertiesElement properties = getProperties();
        if (properties != null) {
            streamWriter.writeStartElement("properties");
            properties.writeContent(streamWriter);
        }
        if (handoffExecutor != null) {
            streamWriter.writeEmptyElement("handoff-executor");
            streamWriter.writeAttribute("name", handoffExecutor);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12418.java