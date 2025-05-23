error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4556.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4556.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4556.java
text:
```scala
final H@@ost service = new Host(name, aliases == null ? new LinkedList<String>() : aliases, defaultWebModule);

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

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.msc.service.ServiceBuilder.DependencyType.OPTIONAL;
import static org.jboss.msc.service.ServiceBuilder.DependencyType.REQUIRED;

import java.util.LinkedList;
import java.util.List;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.server.mgmt.UndertowHttpManagementService;
import org.jboss.as.server.mgmt.domain.HttpManagement;
import org.jboss.as.web.host.CommonWebServer;
import org.jboss.as.web.host.WebHost;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.wildfly.extension.undertow.filters.FilterService;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a> (c) 2013 Red Hat Inc.
 */
class HostAdd extends AbstractAddStepHandler {

    static final HostAdd INSTANCE = new HostAdd();

    private HostAdd() {
        super(HostDefinition.ALIAS, HostDefinition.DEFAULT_WEB_MODULE);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        final PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));
        final PathAddress serverAddress = address.subAddress(0, address.size() - 1);
        final PathAddress subsystemAddress = serverAddress.subAddress(0, address.size() - 1);
        final ModelNode subsystemModel = Resource.Tools.readModel(context.readResourceFromRoot(subsystemAddress));
        final ModelNode fullModel = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));


        final String name = address.getLastElement().getValue();
        final List<String> aliases = HostDefinition.ALIAS.unwrap(context, model);
        final String defaultWebModule = HostDefinition.DEFAULT_WEB_MODULE.resolveModelAttribute(context, model).asString();
        final Resource resource = context.readResource(PathAddress.EMPTY_ADDRESS);
        final Resource accessLog = resource.getChild(UndertowExtension.PATH_ACCESS_LOG);
        final String defaultServerName = UndertowRootDefinition.DEFAULT_SERVER.resolveModelAttribute(context, subsystemModel).asString();
        final String serverName = serverAddress.getLastElement().getValue();

        boolean installCommonHost = defaultServerName.equals(serverName);


        final ServiceName virtualHostServiceName = UndertowService.virtualHostName(serverName, name);
        final ServiceName accessLogServiceName = UndertowService.accessLogServiceName(serverName, name);
        Host service = new Host(name, aliases == null ? new LinkedList<String>() : aliases, defaultWebModule);
        final ServiceBuilder<Host> builder = context.getServiceTarget().addService(virtualHostServiceName, service)
                .addDependency(UndertowService.SERVER.append(serverName), Server.class, service.getServerInjection())
                .addDependency(UndertowService.UNDERTOW, UndertowService.class, service.getUndertowService())
                .addDependency(accessLog != null ? REQUIRED : OPTIONAL, accessLogServiceName, AccessLogService.class, service.getAccessLogService());

        builder.addListener(verificationHandler);
        builder.setInitialMode(Mode.ON_DEMAND);

        configureFilterRef(fullModel, builder, service);

        final ServiceController<WebHost> commonController = null;
        if (installCommonHost) {
            addCommonHost(context, verificationHandler, name, aliases, serverName, virtualHostServiceName);
        }

        final ServiceController<Host> serviceController = builder.install();

        // Setup the web console redirect
        final ServiceName consoleRedirectName = UndertowService.consoleRedirectServiceName(serverName, name);
        final ServiceController<ConsoleRedirectService> consoleServiceServiceController;
        // A standalone server is the only process type with a console redirect
        if (context.getProcessType() == ProcessType.STANDALONE_SERVER) {
            final ConsoleRedirectService redirectService = new ConsoleRedirectService();
            final ServiceBuilder<ConsoleRedirectService> redirectBuilder = context.getServiceTarget().addService(consoleRedirectName, redirectService)
                    .addDependency(UndertowHttpManagementService.SERVICE_NAME, HttpManagement.class, redirectService.getHttpManagementInjector())
                    .addDependency(virtualHostServiceName, Host.class, redirectService.getHostInjector())
                    .setInitialMode(Mode.PASSIVE);
            consoleServiceServiceController = redirectBuilder.install();
        } else {
            // Other process types don't have a console, not depending on the UndertowHttpManagementService should
            // result in a null dependency in the service and redirect accordingly
            final ConsoleRedirectService redirectService = new ConsoleRedirectService();
            final ServiceBuilder<ConsoleRedirectService> redirectBuilder = context.getServiceTarget().addService(consoleRedirectName, redirectService)
                    .addDependency(virtualHostServiceName, Host.class, redirectService.getHostInjector())
                    .setInitialMode(Mode.PASSIVE);
            consoleServiceServiceController = redirectBuilder.install();
        }

        if (newControllers != null) {
            newControllers.add(serviceController);
            newControllers.add(consoleServiceServiceController);
            if (installCommonHost) {
                newControllers.add(commonController);
            }
        }
    }

    private ServiceController<WebHost> addCommonHost(OperationContext context, ServiceVerificationHandler verificationHandler, String hostName, List<String> aliases,
                                                     String serverName, ServiceName virtualHostServiceName) {
        WebHostService service = new WebHostService();
        final ServiceBuilder<WebHost> builder = context.getServiceTarget().addService(WebHost.SERVICE_NAME.append(hostName), service)
                .addDependency(UndertowService.SERVER.append(serverName), Server.class, service.getServer())
                .addDependency(CommonWebServer.SERVICE_NAME)
                .addDependency(virtualHostServiceName, Host.class, service.getHost());

        if (aliases != null) {
            for (String alias : aliases) {
                builder.addAliases(WebHost.SERVICE_NAME.append(alias));
            }
        }

        builder.addListener(verificationHandler);
        builder.setInitialMode(Mode.PASSIVE);
        return builder.install();
    }

    private static void configureFilterRef(final ModelNode model, ServiceBuilder<Host> builder, Host service) {
        if (model.hasDefined(Constants.FILTER_REF)) {
            for (Property property : model.get(Constants.FILTER_REF).asPropertyList()) {
                String name = property.getName();
                LocationAdd.addDep(builder, UndertowService.FILTER.append(name), FilterService.class, service.getInjectedFilters());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4556.java