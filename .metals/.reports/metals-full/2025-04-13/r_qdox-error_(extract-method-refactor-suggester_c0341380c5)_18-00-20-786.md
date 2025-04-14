error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/108.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/108.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/108.java
text:
```scala
r@@eturn new HttpOpenListener(getBufferPool().getValue(), OptionMap.builder().addAll(commonOptions).addAll(listenerOptions).set(UndertowOptions.ENABLE_CONNECTOR_STATISTICS, getUndertowService().isStatisticsEnabled()).getMap());

/*
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

package org.wildfly.extension.undertow;

import java.io.IOException;
import java.net.InetSocketAddress;

import io.undertow.UndertowOptions;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.ListenerRegistry;
import io.undertow.server.OpenListener;
import io.undertow.server.handlers.ChannelUpgradeHandler;
import io.undertow.server.handlers.ProxyPeerAddressHandler;
import io.undertow.server.handlers.SSLHeaderHandler;
import io.undertow.server.protocol.http.HttpOpenListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.ValueService;
import org.jboss.msc.value.ImmediateValue;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.extension.undertow.logging.UndertowLogger;
import org.xnio.ChannelListener;
import org.xnio.IoUtils;
import org.xnio.OptionMap;
import org.xnio.StreamConnection;
import org.xnio.XnioWorker;
import org.xnio.channels.AcceptingChannel;

/**
 * @author Stuart Douglas
 * @author Tomaz Cerar
 */
public class HttpListenerService extends ListenerService<HttpListenerService> {
    private volatile AcceptingChannel<StreamConnection> server;

    private final ChannelUpgradeHandler httpUpgradeHandler = new ChannelUpgradeHandler();
    protected final InjectedValue<ListenerRegistry> httpListenerRegistry = new InjectedValue<>();
    static final ServiceName HTTP_UPGRADE_REGISTRY = ServiceName.JBOSS.append("http-upgrade-registry");
    static final String PROTOCOL = "http";

    private final String serverName;

    public HttpListenerService(String name, final String serverName, OptionMap listenerOptions, OptionMap socketOptions, boolean certificateForwarding, boolean proxyAddressForwarding) {
        super(name, listenerOptions, socketOptions);
        this.serverName = serverName;
        addWrapperHandler(new HandlerWrapper() {
            @Override
            public HttpHandler wrap(final HttpHandler handler) {
                httpUpgradeHandler.setNonUpgradeHandler(handler);
                return httpUpgradeHandler;
            }
        });
        if (certificateForwarding) {
            addWrapperHandler(new HandlerWrapper() {
                @Override
                public HttpHandler wrap(HttpHandler handler) {
                    return new SSLHeaderHandler(handler);
                }
            });
        }
        if (proxyAddressForwarding) {
            addWrapperHandler(new HandlerWrapper() {
                @Override
                public HttpHandler wrap(HttpHandler handler) {
                    return new ProxyPeerAddressHandler(handler);
                }
            });
        }
    }

    @Override
    protected OpenListener createOpenListener() {
        return new HttpOpenListener(getBufferPool().getValue(), OptionMap.builder().addAll(commonOptions).addAll(listenerOptions).set(UndertowOptions.ENABLE_CONNECTOR_STATISTICS, getUndertowService().isStatisticsEnabled()).getMap(), getBufferSize());
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    protected void preStart(final StartContext context) {
        //adds the HTTP upgrade service
        //TODO: have a bit more of a think about how we handle this
        context.getChildTarget().addService(HTTP_UPGRADE_REGISTRY.append(getName()), new ValueService<Object>(new ImmediateValue<Object>(httpUpgradeHandler)))
                .install();
        ListenerRegistry.Listener listener = new ListenerRegistry.Listener(getProtocol(), getName(), serverName, getBinding().getValue().getSocketAddress());
        listener.setContextInformation("socket-binding", getBinding().getValue());
        httpListenerRegistry.getValue().addListener(listener);
    }

    protected void startListening(XnioWorker worker, InetSocketAddress socketAddress, ChannelListener<AcceptingChannel<StreamConnection>> acceptListener)
            throws IOException {
        server = worker.createStreamConnectionServer(socketAddress, acceptListener, OptionMap.builder().addAll(commonOptions).addAll(socketOptions).getMap());
        server.resumeAccepts();
        UndertowLogger.ROOT_LOGGER.listenerStarted("HTTP", getName(), socketAddress);
    }

    @Override
    protected void stopListening() {
        server.suspendAccepts();
        UndertowLogger.ROOT_LOGGER.listenerSuspend("HTTP", getName());
        IoUtils.safeClose(server);
        server = null;
        UndertowLogger.ROOT_LOGGER.listenerStopped("HTTP", getName(), getBinding().getValue().getSocketAddress());
        httpListenerRegistry.getValue().removeListener(getName());
    }

    @Override
    public HttpListenerService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    public InjectedValue<ListenerRegistry> getHttpListenerRegistry() {
        return httpListenerRegistry;
    }

    @Override
    protected String getProtocol() {
        return PROTOCOL;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/108.java