error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10621.java
text:
```scala
r@@eturn true;

/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.wildfly.mod_cluster.undertow;

import java.net.InetAddress;

import org.jboss.modcluster.container.Connector;
import org.wildfly.extension.undertow.ListenerService;
import org.wildfly.extension.undertow.AjpListenerService;
import org.wildfly.extension.undertow.HttpListenerService;
import org.wildfly.mod_cluster.undertow.metric.BytesReceivedStreamSourceConduit;
import org.wildfly.mod_cluster.undertow.metric.BytesSentStreamSinkConduit;
import org.wildfly.mod_cluster.undertow.metric.RequestCountHttpHandler;
import org.wildfly.mod_cluster.undertow.metric.RunningRequestsHttpHandler;

/**
 * Adapts {@link org.wildfly.extension.undertow.ListenerService} to a {@link Connector}.
 *
 * @author Radoslav Husar
 * @version Aug 2013
 * @since 8.0
 */
public class UndertowConnector implements Connector {

    private final ListenerService<?> listener;

    public UndertowConnector(ListenerService<?> listener) {
        this.listener = listener;
    }

    @Override
    public boolean isReverse() {
        return false;
    }

    @Override
    public Type getType() {
        if (this.listener instanceof AjpListenerService) {
            return Type.AJP;
        } else if (this.listener instanceof HttpListenerService) {
            if (this.listener.isSecure()) {
                return Type.HTTPS;
            } else {
                return Type.HTTP;
            }
        }
        return null;
    }

    @Override
    public InetAddress getAddress() {
        return this.listener.getBinding().getValue().getAddress();
    }

    @Override
    public void setAddress(InetAddress address) {
        // Do nothing
    }

    @Override
    public int getPort() {
        return this.listener.getBinding().getValue().getAbsolutePort();
    }

    @Override
    public boolean isAvailable() {
        return !this.listener.getWorker().getValue().isShutdown();
    }

    /**
     * @return int value "-1" to indicate this connector does not maintain corresponding maximum number of threads; capacity
     *         needs to be defined instead
     */
    @Override
    public int getMaxThreads() {
        return -1;
    }

    /**
     * @return int number of <em>running requests</em> on all connectors as opposed to busy threads
     */
    @Override
    public int getBusyThreads() {
        return RunningRequestsHttpHandler.getRunningRequestCount();
    }

    /**
     * @return long number of bytes sent on all connectors
     */
    @Override
    public long getBytesSent() {
        return BytesSentStreamSinkConduit.getBytesSent();
    }

    /**
     * @return long number of bytes received on all listeners without HTTP request size itself
     */
    @Override
    public long getBytesReceived() {
        return BytesReceivedStreamSourceConduit.getBytesReceived();
    }

    /**
     * @return long number of requests on all listeners as opposed to only this 'connector'
     */
    @Override
    public long getRequestCount() {
        return RequestCountHttpHandler.getRequestCount();
    }

    @Override
    public String toString() {
        return this.listener.getName();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UndertowConnector)) return false;

        UndertowConnector connector = (UndertowConnector) object;
        return this.listener.getName().equals(connector.listener.getName());
    }

    @Override
    public int hashCode() {
        return this.listener.getName().hashCode();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10621.java