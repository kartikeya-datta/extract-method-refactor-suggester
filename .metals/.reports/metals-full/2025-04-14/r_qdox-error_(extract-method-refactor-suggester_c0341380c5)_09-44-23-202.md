error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10505.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10505.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10505.java
text:
```scala
i@@f (HornetQDefaultConfiguration.getDefaultClusterPassword().equals(server.getConfiguration().getClusterPassword())) {

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

package org.jboss.as.messaging;

import static org.jboss.as.messaging.MessagingLogger.ROOT_LOGGER;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServer;

import org.hornetq.api.config.HornetQDefaultConfiguration;
import org.hornetq.api.core.BroadcastGroupConfiguration;
import org.hornetq.api.core.DiscoveryGroupConfiguration;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.journal.impl.AIOSequentialFileFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.JournalType;
import org.hornetq.core.server.impl.HornetQServerImpl;
import org.jboss.as.clustering.jgroups.ChannelFactory;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.services.path.AbsolutePathService;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.network.OutboundSocketBinding;
import org.jboss.as.network.SocketBinding;
import org.jboss.as.security.plugins.SecurityDomainContext;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.inject.MapInjector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jgroups.JChannel;

/**
 * Service configuring and starting the {@code HornetQService}.
 *
 * @author scott.stark@jboss.org
 * @author Emanuel Muckenhuber
 */
class HornetQService implements Service<HornetQServer> {

    /** */
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String LOGGING_FACTORY = "org.jboss.as.messaging.HornetQLoggerFactory";

    /**
     * The name of the SocketBinding reference to use for HOST/PORT
     * configuration
     */
    private static final String SOCKET_REF = RemoteTransportDefinition.SOCKET_BINDING.getName();

    private Configuration configuration;

    private HornetQServer server;
    private Map<String, SocketBinding> socketBindings = new HashMap<String, SocketBinding>();
    private Map<String, OutboundSocketBinding> outboundSocketBindings = new HashMap<String, OutboundSocketBinding>();
    private Map<String, SocketBinding> groupBindings = new HashMap<String, SocketBinding>();
    private final InjectedValue<PathManager> pathManager = new InjectedValue<PathManager>();
    private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();
    private final InjectedValue<SecurityDomainContext> securityDomainContextValue = new InjectedValue<SecurityDomainContext>();
    private final PathConfig pathConfig;
    // mapping between the {broacast|discovery}-groups and the *names* of the JGroups channel they use
    private final Map<String, String> jgroupsChannels = new HashMap<String, String>();
    // mapping between the {broacast|discovery}-groups and the JGroups channel factory for the *stack* they use
    private Map<String, ChannelFactory> jgroupFactories = new HashMap<String, ChannelFactory>();

    // broadcast-group and discovery-groups configured with JGroups must share the same channel
    private final Map<String, JChannel> channels = new HashMap<String, JChannel>();

    public HornetQService(Configuration configuration, PathConfig pathConfig) {
        this.configuration = configuration;
        this.pathConfig = pathConfig;
    }

    Injector<PathManager> getPathManagerInjector(){
        return pathManager;
    }

    Injector<SocketBinding> getSocketBindingInjector(String name) {
        return new MapInjector<String, SocketBinding>(socketBindings, name);
    }

    Injector<ChannelFactory> getJGroupsInjector(String name) {
        return new MapInjector<String, ChannelFactory>(jgroupFactories, name);
    }

    Injector<OutboundSocketBinding> getOutboundSocketBindingInjector(String name) {
        return new MapInjector<String, OutboundSocketBinding>(outboundSocketBindings, name);
    }

    Injector<SocketBinding> getGroupBindingInjector(String name) {
        return new MapInjector<String, SocketBinding>(groupBindings, name);
    }

    InjectedValue<MBeanServer> getMBeanServer() {
        return mbeanServer;
    }

    Map<String, JChannel> getChannels() {
        return channels;
    }

    public synchronized void start(final StartContext context) throws StartException {
        ClassLoader origTCCL = SecurityActions.getContextClassLoader();
        // Validate whether the AIO native layer can be used
        JournalType jtype = configuration.getJournalType();
        if (jtype == JournalType.ASYNCIO) {
            boolean supportsAIO = AIOSequentialFileFactory.isSupported();

            if (supportsAIO == false) {
                ROOT_LOGGER.aioWarning();
                configuration.setJournalType(JournalType.NIO);
            }
        }

        // Disable file deployment
        configuration.setFileDeploymentEnabled(false);
        // Setup paths
        PathManager pathManager = this.pathManager.getValue();
        configuration.setBindingsDirectory(pathConfig.resolveBindingsPath(pathManager));
        configuration.setLargeMessagesDirectory(pathConfig.resolveLargeMessagePath(pathManager));
        configuration.setJournalDirectory(pathConfig.resolveJournalPath(pathManager));
        configuration.setPagingDirectory(pathConfig.resolvePagingPath(pathManager));

        try {
            // Update the acceptor/connector port/host values from the
            // Map the socket bindings onto the connectors/acceptors
            Collection<TransportConfiguration> acceptors = configuration.getAcceptorConfigurations();
            Collection<TransportConfiguration> connectors = configuration.getConnectorConfigurations().values();
            Collection<BroadcastGroupConfiguration> broadcastGroups = configuration.getBroadcastGroupConfigurations();
            Map<String, DiscoveryGroupConfiguration> discoveryGroups = configuration.getDiscoveryGroupConfigurations();
            if (connectors != null) {
                for (TransportConfiguration tc : connectors) {
                    // If there is a socket binding set the HOST/PORT values
                    Object socketRef = tc.getParams().remove(SOCKET_REF);
                    if (socketRef != null) {
                        String name = socketRef.toString();
                        String host;
                        int port;
                        OutboundSocketBinding binding = outboundSocketBindings.get(name);
                        if (binding == null) {
                            final SocketBinding socketBinding = socketBindings.get(name);
                            if (socketBinding == null) {
                                throw MESSAGES.failedToFindConnectorSocketBinding(tc.getName());
                            }
                            InetSocketAddress sa = socketBinding.getSocketAddress();
                            port = sa.getPort();
                            // resolve the host name of the address only if a loopback adress has been set
                            if (sa.getAddress().isLoopbackAddress()) {
                                host = sa.getAddress().getHostName();
                            } else {
                                host = sa.getAddress().getHostAddress();
                            }
                        } else {
                            port = binding.getDestinationPort();
                            if (binding.getDestinationAddress().isLoopbackAddress()) {
                                host = binding.getDestinationAddress().getHostName();
                            } else {
                                host = binding.getDestinationAddress().getHostAddress();
                            }
                        }
                        tc.getParams().put(HOST, host);
                        tc.getParams().put(PORT, String.valueOf(port));
                    }
                }
            }
            if (acceptors != null) {
                for (TransportConfiguration tc : acceptors) {
                    // If there is a socket binding set the HOST/PORT values
                    Object socketRef = tc.getParams().remove(SOCKET_REF);
                    if (socketRef != null) {
                        String name = socketRef.toString();
                        SocketBinding binding = socketBindings.get(name);
                        if (binding == null) {
                            throw MESSAGES.failedToFindConnectorSocketBinding(tc.getName());
                        }
                        InetSocketAddress socketAddress = binding.getSocketAddress();
                        tc.getParams().put(HOST, socketAddress.getAddress().getHostAddress());
                        tc.getParams().put(PORT, "" + socketAddress.getPort());
                    }
                }
            }


            if(broadcastGroups != null) {
                final List<BroadcastGroupConfiguration> newConfigs = new ArrayList<BroadcastGroupConfiguration>();
                for(final BroadcastGroupConfiguration config : broadcastGroups) {
                    final String name = config.getName();
                    final String key = "broadcast" + name;
                    if (jgroupFactories.containsKey(key)) {
                        ChannelFactory channelFactory = jgroupFactories.get(key);
                        String channelName = jgroupsChannels.get(key);
                        JChannel channel = (JChannel) channelFactory.createChannel(channelName);
                        channels.put(channelName, channel);
                        newConfigs.add(BroadcastGroupAdd.createBroadcastGroupConfiguration(name, config, channel, channelName));
                    } else {
                        final SocketBinding binding = groupBindings.get(key);
                        if (binding == null) {
                            throw MESSAGES.failedToFindBroadcastSocketBinding(name);
                        }
                       newConfigs.add(BroadcastGroupAdd.createBroadcastGroupConfiguration(name, config, binding));
                    }
                }
                configuration.getBroadcastGroupConfigurations().clear();
                configuration.getBroadcastGroupConfigurations().addAll(newConfigs);
            }
            if(discoveryGroups != null) {
                configuration.setDiscoveryGroupConfigurations(new HashMap<String, DiscoveryGroupConfiguration>());
                for(final Map.Entry<String, DiscoveryGroupConfiguration> entry : discoveryGroups.entrySet()) {
                    final String name = entry.getKey();
                    final String key = "discovery" + name;
                    DiscoveryGroupConfiguration config = null;
                    if (jgroupFactories.containsKey(key)) {
                        ChannelFactory channelFactory = jgroupFactories.get(key);
                        String channelName = jgroupsChannels.get(key);
                        JChannel channel = channels.get(channelName);
                        if (channel == null) {
                            channel = (JChannel) channelFactory.createChannel(key);
                            channels.put(channelName, channel);
                        }
                        config = DiscoveryGroupAdd.createDiscoveryGroupConfiguration(name, entry.getValue(), channel, channelName);
                    } else {
                        final SocketBinding binding = groupBindings.get(key);
                        if (binding == null) {
                            throw MESSAGES.failedToFindDiscoverySocketBinding(name);
                        }
                        config = DiscoveryGroupAdd.createDiscoveryGroupConfiguration(name, entry.getValue(), binding);
                    }
                    configuration.getDiscoveryGroupConfigurations().put(name, config);
                }
            }

            // security
            HornetQSecurityManagerAS7 hornetQSecurityManagerAS7 = new HornetQSecurityManagerAS7(securityDomainContextValue.getValue());

            // Now start the server
            server = new HornetQServerImpl(configuration, mbeanServer.getOptionalValue(), hornetQSecurityManagerAS7);
            if (HornetQDefaultConfiguration.DEFAULT_CLUSTER_PASSWORD.equals(server.getConfiguration().getClusterPassword())) {
                server.getConfiguration().setClusterPassword(java.util.UUID.randomUUID().toString());
            }

            // FIXME started by the JMSService
            // HornetQ expects the TCCL to be set to something that can find the
            // log factory class.
            // ClassLoader loader = getClass().getClassLoader();
            // SecurityActions.setContextClassLoader(loader);
            // server.start();
        } catch (Exception e) {
            throw MESSAGES.failedToStartService(e);
        } finally {
            SecurityActions.setContextClassLoader(origTCCL);
        }
    }

    public synchronized void stop(final StopContext context) {
        try {
            if (server != null) {
                // FIXME stopped by the JMSService
                // server.stop();
            }
            pathConfig.closeCallbacks(pathManager.getValue());
        } catch (Exception e) {
            throw MESSAGES.failedToShutdownServer(e, "HornetQ");
        }
    }

    public synchronized HornetQServer getValue() throws IllegalStateException {
        final HornetQServer server = this.server;
        if (server == null) {
            throw new IllegalStateException();
        }
        return server;
    }

    public Injector<SecurityDomainContext> getSecurityDomainContextInjector() {
        return securityDomainContextValue;
    }

    public Map<String, String> getJGroupsChannels() {
        return jgroupsChannels;
    }

    /**
     * Returns true if a {@link ServiceController} for this service has been {@link org.jboss.msc.service.ServiceBuilder#install() installed}
     * in MSC under the
     * {@link MessagingServices#getHornetQServiceName(PathAddress) service name appropriate to the given operation}.
     *
     * @param context the operation context
     * @param operation the operation
     *
     * @return {@code true} if a {@link ServiceController} is installed
     */
    static boolean isHornetQServiceInstalled(final OperationContext context, final ModelNode operation) {
        if (context.isNormalServer()) {
            final ServiceName hqServiceName = MessagingServices.getHornetQServiceName(PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR)));
            final ServiceController<?> controller = context.getServiceRegistry(false).getService(hqServiceName);
            return controller != null;
        }
        return false;
    }

    static class PathConfig {
        private final String bindingsPath;
        private final String bindingsRelativeToPath;
        private final String journalPath;
        private final String journalRelativeToPath;
        private final String largeMessagePath;
        private final String largeMessageRelativeToPath;
        private final String pagingPath;
        private final String pagingRelativeToPath;
        private final List<PathManager.Callback.Handle> callbackHandles = new ArrayList<PathManager.Callback.Handle>();

        public PathConfig(String bindingsPath, String bindingsRelativeToPath, String journalPath, String journalRelativeToPath,
                String largeMessagePath, String largeMessageRelativeToPath, String pagingPath, String pagingRelativeToPath) {
            this.bindingsPath = bindingsPath;
            this.bindingsRelativeToPath = bindingsRelativeToPath;
            this.journalPath = journalPath;
            this.journalRelativeToPath = journalRelativeToPath;
            this.largeMessagePath = largeMessagePath;
            this.largeMessageRelativeToPath = largeMessageRelativeToPath;
            this.pagingPath = pagingPath;
            this.pagingRelativeToPath = pagingRelativeToPath;
        }

        String resolveBindingsPath(PathManager pathManager) {
            return resolve(pathManager, bindingsPath, bindingsRelativeToPath);
        }

        String resolveJournalPath(PathManager pathManager) {
            return resolve(pathManager, journalPath, journalRelativeToPath);
        }

        String resolveLargeMessagePath(PathManager pathManager) {
            return resolve(pathManager, largeMessagePath, largeMessageRelativeToPath);
        }

        String resolvePagingPath(PathManager pathManager) {
            return resolve(pathManager, pagingPath, pagingRelativeToPath);
        }

        String resolve(PathManager pathManager, String path, String relativeToPath) {
            // discard the relativeToPath if the path is absolute and must not be resolved according
            // to the default relativeToPath value
            String relativeTo = AbsolutePathService.isAbsoluteUnixOrWindowsPath(path) ? null : relativeToPath;
            return pathManager.resolveRelativePathEntry(path, relativeTo);
        }

        synchronized void registerCallbacks(PathManager pathManager) {
            if (bindingsRelativeToPath != null) {
                callbackHandles.add(pathManager.registerCallback(bindingsRelativeToPath, PathManager.ReloadServerCallback.create(), PathManager.Event.UPDATED, PathManager.Event.REMOVED));
            }
            if (journalRelativeToPath != null) {
                callbackHandles.add(pathManager.registerCallback(journalRelativeToPath, PathManager.ReloadServerCallback.create(), PathManager.Event.UPDATED, PathManager.Event.REMOVED));
            }
            if (largeMessageRelativeToPath != null) {
                callbackHandles.add(pathManager.registerCallback(largeMessageRelativeToPath, PathManager.ReloadServerCallback.create(), PathManager.Event.UPDATED, PathManager.Event.REMOVED));
            }
            if (pagingRelativeToPath != null) {
                callbackHandles.add(pathManager.registerCallback(pagingRelativeToPath, PathManager.ReloadServerCallback.create(), PathManager.Event.UPDATED, PathManager.Event.REMOVED));
            }
        }

        synchronized void closeCallbacks(PathManager pathManager) {
            for (PathManager.Callback.Handle callbackHandle : callbackHandles) {
                callbackHandle.remove();
            }
            callbackHandles.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10505.java