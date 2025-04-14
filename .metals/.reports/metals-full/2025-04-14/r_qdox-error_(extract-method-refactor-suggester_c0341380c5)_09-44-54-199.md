error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3237.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3237.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3237.java
text:
```scala
private volatile W@@ebAppController pclwa;

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.webservices.service;

import static org.jboss.as.webservices.WSLogger.ROOT_LOGGER;

import org.jboss.as.web.VirtualHost;
import org.jboss.as.web.WebSubsystemServices;
import org.jboss.as.webservices.util.WSServices;
import org.jboss.as.webservices.util.WebAppController;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceBuilder.DependencyType;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.wsf.spi.classloading.ClassLoaderProvider;
import org.jboss.wsf.spi.management.ServerConfig;

/**
 * PortComponentLink service, retrieves a WebAppController instance to start/stop port component link servlet on demand; that is
 * required to allow remote port-component-link resolution as per JSR-109 requirements.
 *
 * @author alessio.soldano@jboss.com
 * @since 02-Dec-2011
 */
public final class PortComponentLinkService implements Service<WebAppController> {

    private final ServiceName name;
    private final InjectedValue<VirtualHost> hostInjector = new InjectedValue<VirtualHost>();
    private WebAppController pclwa;
    private final InjectedValue<ServerConfig> serverConfigInjectorValue = new InjectedValue<ServerConfig>();
    private static final String DEFAULT_HOST_NAME = "default-host";

    private PortComponentLinkService() {
        this.name = WSServices.PORT_COMPONENT_LINK_SERVICE;
    }

    @Override
    public WebAppController getValue() {
        return pclwa;
    }

    public ServiceName getName() {
        return name;
    }

    public InjectedValue<VirtualHost> getHostInjector() {
        return hostInjector;
    }

    @Override
    public void start(final StartContext ctx) throws StartException {
        ROOT_LOGGER.starting(name);
        String serverTempDir = serverConfigInjectorValue.getValue().getServerTempDir().getAbsolutePath();
        ClassLoader cl = ClassLoaderProvider.getDefaultProvider().getServerJAXRPCIntegrationClassLoader();
        pclwa = new WebAppController(hostInjector.getValue().getHost(), "org.jboss.ws.core.server.PortComponentLinkServlet",
                cl, "/jbossws", "/pclink", serverTempDir);
    }

    @Override
    public void stop(final StopContext ctx) {
        ROOT_LOGGER.stopping(name);
        pclwa = null;
    }

    public Injector<ServerConfig> getServerConfigInjector() {
        return serverConfigInjectorValue;
    }

    public static ServiceBuilder<WebAppController> createServiceBuilder(final ServiceTarget serviceTarget,
            final String hostName) {
        final PortComponentLinkService service = new PortComponentLinkService();
        final ServiceBuilder<WebAppController> builder = serviceTarget.addService(service.getName(), service);
        builder.addDependency(DependencyType.REQUIRED, WSServices.REGISTRY_SERVICE);
        builder.addDependency(DependencyType.REQUIRED, WSServices.CONFIG_SERVICE, ServerConfig.class,
                service.getServerConfigInjector());
        builder.addDependency(WebSubsystemServices.JBOSS_WEB_HOST.append(hostName), VirtualHost.class,
                service.getHostInjector());
        return builder;
    }

    public static ServiceController<WebAppController> install(final ServiceTarget serviceTarget, final ServiceListener<Object>... listeners) {
        return install(serviceTarget, DEFAULT_HOST_NAME, listeners);
    }

    public static ServiceController<WebAppController> install(final ServiceTarget serviceTarget, final String hostName, final ServiceListener<Object>... listeners) {
        ServiceBuilder<WebAppController> builder = createServiceBuilder(serviceTarget, hostName);
        builder.addListener(listeners);
        builder.setInitialMode(Mode.ON_DEMAND);
        return builder.install();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3237.java