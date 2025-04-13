error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8359.java
text:
```scala
.@@addDependency(NetworkInterfaceService.JBOSS_NETWORK_INTERFACE.append(localDomainControllerElement.getInterfaceName()), NetworkInterfaceBinding.class, serverManagerCommunicationService.getInterfaceInjector())

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

package org.jboss.as.domain.controller;

import org.jboss.as.model.Host;
import org.jboss.as.model.LocalDomainControllerElement;
import org.jboss.as.model.socket.ServerInterfaceElement;
import org.jboss.as.server.manager.DomainControllerConfig;
import org.jboss.as.server.manager.ServerManagerProtocolCommand;
import org.jboss.as.services.net.NetworkInterfaceBinding;
import org.jboss.as.services.net.NetworkInterfaceService;
import org.jboss.logging.Logger;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceActivatorContextImpl;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.service.StartException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Domain controller instance. 
 *
 * @author John Bailey
 */
public class DomainController {
    static final String DOMAIN_CONTROLLER_PROCESS_NAME = "domain-controller";
    private static final Logger log = Logger.getLogger("org.jboss.as.domain.controller");
    private final DomainControllerEnvironment environment;
    private final ProcessMessageHandler messageHandler = new ProcessMessageHandler(this);
    private ProcessCommunicationHandler communicationHandler;
    private final ServiceContainer serviceContainer = ServiceContainer.Factory.create();

    /**
     * Create an instance with an environment.
     *
     * @param environment The DomainController environment
     */
    public DomainController(DomainControllerEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Start the domain controller
     */
    void start() {
        launchCommunicationHandler();
        sendMessage(ServerManagerProtocolCommand.SERVER_AVAILABLE);
    }


    /**
     * Start the domain controller with configuration.  This will launch required service for the domain controller.
     *
     * @param domainControllerConfig The domain controller configuration
     */
    synchronized void start(final DomainControllerConfig domainControllerConfig) {
        log.info("Starting Domain Controller");

        final LocalDomainControllerElement localDomainControllerElement = domainControllerConfig.getDomainControllerElement();
        final Host hostConfig = domainControllerConfig.getHost();
        final BatchBuilder batchBuilder = serviceContainer.batchBuilder();

        final ServiceActivatorContext serviceActivatorContext = new ServiceActivatorContextImpl(batchBuilder);

        // Activate Interfaces
        final Map<String, ServerInterfaceElement> interfaces = new HashMap<String, ServerInterfaceElement>();
        final Set<ServerInterfaceElement> hostInterfaces = hostConfig.getInterfaces();
        if(hostInterfaces != null) {
            for(ServerInterfaceElement interfaceElement : hostInterfaces) {
                interfaces.put(interfaceElement.getName(), interfaceElement);
            }
        }
        final Map<String, ServerInterfaceElement> dcInterfaces = localDomainControllerElement.getInterfaces();
        if(dcInterfaces != null)
        for(Map.Entry<String, ServerInterfaceElement> interfaceElement : dcInterfaces.entrySet()) {
            interfaces.put(interfaceElement.getKey(), interfaceElement.getValue());
        }
        for(ServerInterfaceElement interfaceElement : interfaces.values()) {
            interfaceElement.activate(serviceActivatorContext);
        }

        //  Add the server manager communication service
        final ServerManagerCommunicationService serverManagerCommunicationService = new ServerManagerCommunicationService(this, localDomainControllerElement);
        batchBuilder.addService(ServerManagerCommunicationService.SERVICE_NAME, serverManagerCommunicationService)
            .addListener(new DomainControllerStartupListener())
            .addDependency(NetworkInterfaceService.JBOSS_NETWORK_INTERFACE.append(localDomainControllerElement.getAdminInterface()), NetworkInterfaceBinding.class, serverManagerCommunicationService.getInterfaceInjector())
            .setInitialMode(ServiceController.Mode.IMMEDIATE);

        try {
            batchBuilder.install();
        } catch (ServiceRegistryException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stop the domain controller
     */
    synchronized void stop() {
        log.info("Stopping Domain Controller");
        serviceContainer.shutdown();
    }

    private void launchCommunicationHandler() {
        communicationHandler = new ProcessCommunicationHandler(environment.getProcessManagerAddress(), environment.getProcessManagerPort(), messageHandler);
        Thread t = new Thread(communicationHandler.getController(), "DomainController Process");
        t.start();
    }

    private void sendMessage(ServerManagerProtocolCommand command) {
        try {
            byte[] bytes = command.createCommandBytes(null);
            communicationHandler.sendMessage(bytes);
        } catch (IOException e) {
            log.error("Failed to send message to Server Manager [" + command + "]", e);
        }
    }

    private class DomainControllerStartupListener extends AbstractServiceListener<Void> {
        @Override
        public void serviceStarted(ServiceController<? extends Void> serviceController) {
            DomainController.this.sendMessage(ServerManagerProtocolCommand.SERVER_STARTED);
        }

        @Override
        public void serviceFailed(ServiceController<? extends Void> serviceController, StartException reason) {
            log.error("Failed to start server manger communication service", reason);
            DomainController.this.sendMessage(ServerManagerProtocolCommand.SERVER_START_FAILED);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8359.java