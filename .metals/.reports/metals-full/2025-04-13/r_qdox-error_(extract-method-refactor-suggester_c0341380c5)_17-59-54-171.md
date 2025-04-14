error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4305.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4305.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4305.java
text:
```scala
M@@anagementRemotingServices.installRemotingEndpoint(serviceTarget, endpointName, WildFlySecurityManager.getPropertyPrivileged(ServerEnvironment.NODE_NAME, null), endpointType, options, null, null);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.server;

import org.jboss.as.controller.ControlledProcessStateService;
import org.jboss.as.controller.ExpressionResolver;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.protocol.ProtocolChannelClient;
import org.jboss.as.remoting.EndpointConfigFactory;
import org.jboss.as.remoting.EndpointService;
import org.jboss.as.remoting.RemotingServices;
import org.jboss.as.remoting.management.ManagementRemotingServices;
import org.jboss.as.server.mgmt.domain.HostControllerConnectionService;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.remoting3.Endpoint;
import org.jboss.remoting3.RemotingOptions;
import org.wildfly.security.manager.WildFlySecurityManager;
import org.xnio.OptionMap;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Service activator for the communication services of a managed server in a domain.
 *
 * @author Emanuel Muckenhuber
 */
public class DomainServerCommunicationServices  implements ServiceActivator, Serializable {

    private static final OptionMap DEFAULTS = OptionMap.create(RemotingOptions.RECEIVE_WINDOW_SIZE, ProtocolChannelClient.Configuration.WINDOW_SIZE);

    private static final long serialVersionUID = 1593964083902839384L;

    // Shared operation ID for connection, this will get updated for start and reload
    private static volatile int initialOperationID;

    private final ModelNode endpointConfig;
    private final InetSocketAddress managementSocket;
    private final String serverName;
    private final String serverProcessName;
    private final byte[] authKey;

    private final boolean managementSubsystemEndpoint;

    DomainServerCommunicationServices(ModelNode endpointConfig, InetSocketAddress managementSocket, String serverName, String serverProcessName, byte[] authKey, boolean managementSubsystemEndpoint) {
        this.endpointConfig = endpointConfig;
        this.managementSocket = managementSocket;
        this.serverName = serverName;
        this.serverProcessName = serverProcessName;
        this.authKey = authKey;
        this.managementSubsystemEndpoint = managementSubsystemEndpoint;
    }

    static void updateOperationID(final int operationID) {
        initialOperationID = operationID;
    }

    @Override
    public void activate(final ServiceActivatorContext serviceActivatorContext) throws ServiceRegistryException {
        final ServiceTarget serviceTarget = serviceActivatorContext.getServiceTarget();
        final ServiceName endpointName = managementSubsystemEndpoint ? RemotingServices.SUBSYSTEM_ENDPOINT : ManagementRemotingServices.MANAGEMENT_ENDPOINT;
        final EndpointService.EndpointType endpointType = managementSubsystemEndpoint ? EndpointService.EndpointType.SUBSYSTEM : EndpointService.EndpointType.MANAGEMENT;
        try {
            // TODO see if we can figure out a way to work in the vault resolver instead of having to use ExpressionResolver.DEFAULT
            @SuppressWarnings("deprecation")
            final OptionMap options = EndpointConfigFactory.create(ExpressionResolver.DEFAULT, endpointConfig, DEFAULTS);
            ManagementRemotingServices.installRemotingManagementEndpoint(serviceTarget, endpointName, WildFlySecurityManager.getPropertyPrivileged(ServerEnvironment.NODE_NAME, null), endpointType, options, null, null);

            // Install the communication services
            final int port = managementSocket.getPort();
            final String host = managementSocket.getAddress().getHostAddress();
            HostControllerConnectionService service = new HostControllerConnectionService(host, port, serverName, serverProcessName, authKey, initialOperationID, managementSubsystemEndpoint);
            serviceTarget.addService(HostControllerConnectionService.SERVICE_NAME, service)
                    .addDependency(endpointName, Endpoint.class, service.getEndpointInjector())
                    .addDependency(ControlledProcessStateService.SERVICE_NAME, ControlledProcessStateService.class, service.getProcessStateServiceInjectedValue())
                    .setInitialMode(ServiceController.Mode.ACTIVE)
                    .install();

        } catch (OperationFailedException e) {
            throw new ServiceRegistryException(e);
        }
    }

    /**
     * Create a new service activator for the domain server communication services.
     *
     * @param endpointConfig the endpoint configuration
     * @param managementSocket the management socket address
     * @param serverName the server name
     * @param serverProcessName the server process name
     * @param authKey the authentication key
     * @param managementSubsystemEndpoint whether to use the mgmt subsystem endpoint or not
     * @return the service activator
     */
    public static ServiceActivator create(final ModelNode endpointConfig, final InetSocketAddress managementSocket, final String serverName, final String serverProcessName,
                                          final byte[] authKey, final boolean managementSubsystemEndpoint) {

        return new DomainServerCommunicationServices(endpointConfig, managementSocket, serverName, serverProcessName, authKey, managementSubsystemEndpoint);
    }

    public interface OperationIDUpdater {

        /**
         * Update the operation ID when connecting to the HC.
         *
         * @param operationID the new operation ID
         */
        void updateOperationID(int operationID);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4305.java