error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2653.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2653.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2653.java
text:
```scala
public b@@oolean isStatisticsEnabled() {

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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;

import io.undertow.Version;
import org.jboss.as.controller.PathAddress;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.security.SecurityConstants;
import org.wildfly.extension.undertow.logging.UndertowLogger;
import org.wildfly.extension.undertow.security.jacc.HttpServletRequestPolicyContextHandler;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a> (c) 2013 Red Hat Inc.
 * @author Stuart Douglas
 */
public class UndertowService implements Service<UndertowService> {

    public static final ServiceName UNDERTOW = ServiceName.JBOSS.append("undertow");
    public static final ServiceName SERVLET_CONTAINER = UNDERTOW.append(Constants.SERVLET_CONTAINER);
    public static final ServiceName SERVER = UNDERTOW.append(Constants.SERVER);
    /**
     * service name under which default server is bound.
     */
    public static final ServiceName DEFAULT_SERVER = UNDERTOW.append("default-server");

    /**
     * service name under which default host of default server is bound.
     */
    public static final ServiceName DEFAULT_HOST = DEFAULT_SERVER.append("default-host");
    /**
     * The base name for listener/handler/filter services.
     */
    public static final ServiceName HANDLER = UNDERTOW.append(Constants.HANDLER);
    public static final ServiceName FILTER = UNDERTOW.append(Constants.FILTER);
    /**
     * The base name for web deployments.
     */
    static final ServiceName WEB_DEPLOYMENT_BASE = UNDERTOW.append("deployment");
    private final String defaultContainer;
    private final String defaultServer;
    private final String defaultVirtualHost;
    private final Set<Server> registeredServers = new CopyOnWriteArraySet<>();
    private final List<UndertowEventListener> listeners = Collections.synchronizedList(new LinkedList<UndertowEventListener>());
    private volatile String instanceId;//todo this should be final and no setter should be exposed, currently mod cluster "wants it", this needs to change
    private final boolean statistics;

    protected UndertowService(String defaultContainer, String defaultServer, String defaultVirtualHost, String instanceId, boolean statistics) {
        this.defaultContainer = defaultContainer;
        this.defaultServer = defaultServer;
        this.defaultVirtualHost = defaultVirtualHost;
        this.instanceId = instanceId;
        this.statistics = statistics;
    }

    public static ServiceName deploymentServiceName(final String serverName, final String virtualHost, final String contextPath) {
        return WEB_DEPLOYMENT_BASE.append(serverName).append(virtualHost).append("".equals(contextPath) ? "/" : contextPath);
    }

    public static ServiceName virtualHostName(final String server, final String virtualHost) {
        return SERVER.append(server).append(virtualHost);
    }

    public static ServiceName locationServiceName(final String server, final String virtualHost, final String locationName) {
        return virtualHostName(server, virtualHost).append(Constants.LOCATION, locationName);
    }

    public static ServiceName accessLogServiceName(final String server, final String virtualHost) {
        return virtualHostName(server, virtualHost).append(Constants.ACCESS_LOG);
    }

    public static ServiceName ssoServiceName(final String server, final String virtualHost) {
        return virtualHostName(server, virtualHost).append("single-sign-on");
    }

    public static ServiceName consoleRedirectServiceName(final String server, final String virtualHost) {
        return virtualHostName(server, virtualHost).append("console", "redirect");
    }

    public static ServiceName filterRefName(final String server, final String virtualHost, final String locationName, final String filterName) {
        return virtualHostName(server, virtualHost).append(Constants.LOCATION, locationName).append("filter-ref").append(filterName);
    }

    public static ServiceName filterRefName(final String server, final String virtualHost, final String filterName) {
        return SERVER.append(server).append(virtualHost).append("filter-ref").append(filterName);
    }

    public static ServiceName getFilterRefServiceName(final PathAddress address, String name) {
        final PathAddress oneUp = address.subAddress(0, address.size() - 1);
        final PathAddress twoUp = oneUp.subAddress(0, oneUp.size() - 1);
        final PathAddress threeUp = twoUp.subAddress(0, twoUp.size() - 1);
        ServiceName serviceName;
        if (address.getLastElement().getKey().equals(Constants.FILTER_REF)) {
            if (oneUp.getLastElement().getKey().equals(Constants.HOST)) { //adding reference
                String host = oneUp.getLastElement().getValue();
                String server = twoUp.getLastElement().getValue();
                serviceName = UndertowService.filterRefName(server, host, name);
            } else {
                String location = oneUp.getLastElement().getValue();
                String host = twoUp.getLastElement().getValue();
                String server = threeUp.getLastElement().getValue();
                serviceName = UndertowService.filterRefName(server, host, location, name);
            }
        } else if (address.getLastElement().getKey().equals(Constants.HOST)) {
            String host = address.getLastElement().getValue();
            String server = oneUp.getLastElement().getValue();
            serviceName = UndertowService.filterRefName(server, host, name);
        } else {
            String location = address.getLastElement().getValue();
            String host = oneUp.getLastElement().getValue();
            String server = twoUp.getLastElement().getValue();
            serviceName = UndertowService.filterRefName(server, host, location, name);
        }
        return serviceName;
    }

    public static ServiceName listenerName(String listenerName) {
        return UNDERTOW.append(Constants.LISTENER).append(listenerName);
    }

    @Override
    public void start(StartContext context) throws StartException {
        UndertowLogger.ROOT_LOGGER.serverStarting(Version.getVersionString());
        // Register the active request PolicyContextHandler
        try {
            PolicyContext.registerHandler(SecurityConstants.WEB_REQUEST_KEY,
                    new HttpServletRequestPolicyContextHandler(), true);
        } catch (PolicyContextException pce) {
            UndertowLogger.ROOT_LOGGER.failedToRegisterPolicyContextHandler(SecurityConstants.WEB_REQUEST_KEY, pce);
        }
    }

    @Override
    public void stop(StopContext context) {
        // Remove PolicyContextHandler
        Set handlerKeys = PolicyContext.getHandlerKeys();
        handlerKeys.remove(SecurityConstants.WEB_REQUEST_KEY);

        UndertowLogger.ROOT_LOGGER.serverStopping(Version.getVersionString());

        fireEvent(new EventInvoker() {
            @Override
            public void invoke(UndertowEventListener listener) {
                listener.onShutdown();
            }
        });
    }

    @Override
    public UndertowService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    protected void registerServer(final Server server) {
        registeredServers.add(server);
        fireEvent(new EventInvoker() {
            @Override
            public void invoke(UndertowEventListener listener) {
                listener.onServerStart(server);
            }
        });
    }

    protected void unregisterServer(final Server server) {
        registeredServers.remove(server);
        fireEvent(new EventInvoker() {
            @Override
            public void invoke(UndertowEventListener listener) {
                listener.onServerStop(server);
            }
        });
    }

    public String getDefaultContainer() {
        return defaultContainer;
    }

    public String getDefaultServer() {
        return defaultServer;
    }

    public String getDefaultVirtualHost() {
        return defaultVirtualHost;
    }

    public Set<Server> getServers() {
        return Collections.unmodifiableSet(registeredServers);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public boolean statisticsEnabled() {
        return statistics;
    }

    /**
     * Registers custom Event listener to server
     *
     * @param listener event listener to register
     */
    public void registerListener(UndertowEventListener listener) {
        this.listeners.add(listener);
    }

    public void unregisterListener(UndertowEventListener listener) {
        this.listeners.remove(listener);
    }

    protected void fireEvent(EventInvoker invoker) {
        synchronized (listeners) {
            for (UndertowEventListener listener : listeners) {
                invoker.invoke(listener);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2653.java