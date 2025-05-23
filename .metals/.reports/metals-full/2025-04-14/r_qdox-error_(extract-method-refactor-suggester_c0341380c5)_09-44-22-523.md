error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6020.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6020.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6020.java
text:
```scala
final S@@tring cluster = remoteSite.getClusterName();

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
package org.jboss.as.clustering.jgroups;

import static org.jboss.as.clustering.jgroups.JGroupsLogger.ROOT_LOGGER;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.management.MBeanServer;

import org.jboss.as.clustering.concurrent.ManagedExecutorService;
import org.jboss.as.clustering.concurrent.ManagedScheduledExecutorService;
import org.jboss.as.network.SocketBinding;
import org.jboss.as.server.ServerEnvironment;
import org.jgroups.Channel;
import org.jgroups.ChannelListener;
import org.jgroups.Global;
import org.jgroups.JChannel;
import org.jgroups.conf.ProtocolStackConfigurator;
import org.jgroups.jmx.JmxConfigurator;
import org.jgroups.protocols.TP;
import org.jgroups.protocols.relay.RELAY2;
import org.jgroups.protocols.relay.config.RelayConfig;
import org.jgroups.stack.Configurator;
import org.jgroups.stack.Protocol;
import org.jgroups.util.SocketFactory;

/**
 * @author Paul Ferraro
 *
 */
public class JChannelFactory implements ChannelFactory, ChannelListener, ProtocolStackConfigurator {

    private final ProtocolStackConfiguration configuration;
    private final Map<Channel, String> channels = Collections.synchronizedMap(new WeakHashMap<Channel, String>());

    public JChannelFactory(ProtocolStackConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ServerEnvironment getServerEnvironment() {
        return this.configuration.getEnvironment();
    }

    @Override
    public ProtocolStackConfiguration getProtocolStackConfiguration() {
        return this.configuration;
    }

    @Override
    public Channel createChannel(final String id) throws Exception {
        JChannel channel = new MuxChannel(this);

        // We need to synchronize on shared transport,
        // so we don't attempt to init a shared transport multiple times
        TP transport = channel.getProtocolStack().getTransport();
        if (transport.isSingleton()) {
            synchronized (transport) {
                this.init(transport);
            }
        } else {
            this.init(transport);
        }

        // Relay protocol is added to stack programmatically, not via ProtocolStackConfigurator
        final RelayConfiguration relayConfig = this.configuration.getRelay();
        if (relayConfig != null) {
            final String localSite = relayConfig.getSiteName();
            final List<RemoteSiteConfiguration> remoteSites = this.configuration.getRelay().getRemoteSites();
            final List<String> sites = new ArrayList<String>(remoteSites.size() + 1);
            sites.add(localSite);
            // Collect bridges, eliminating duplicates
            final Map<String, RelayConfig.BridgeConfig> bridges = new HashMap<String, RelayConfig.BridgeConfig>();
            for (final RemoteSiteConfiguration remoteSite: remoteSites) {
                final String siteName = remoteSite.getName();
                sites.add(siteName);
                final String cluster = remoteSite.getCluster();
                final String clusterName = (cluster != null) ? cluster : siteName;
                final RelayConfig.BridgeConfig bridge = new RelayConfig.BridgeConfig(clusterName) {
                    @Override
                    public JChannel createChannel() throws Exception {
                        return (JChannel) remoteSite.getChannelFactory().createChannel(id + "/" + clusterName);
                    }
                };
                bridges.put(clusterName, bridge);
            }
            // Acquire consistent order of sites
            Collections.sort(sites);
            final RELAY2 relay = new RELAY2().site(localSite);
            for (short i = 0; i < sites.size(); ++i) {
                final String site = sites.get(i);
                RelayConfig.SiteConfig siteConfig = new RelayConfig.SiteConfig(site, i);
                relay.addSite(site, siteConfig);
                if (site.equals(localSite)) {
                    for (RelayConfig.BridgeConfig bridge: bridges.values()) {
                        siteConfig.addBridge(bridge);
                    }
                }
            }
            Configurator.resolveAndAssignFields(relay, relayConfig.getProperties());
            Configurator.resolveAndInvokePropertyMethods(relay, relayConfig.getProperties());
            channel.getProtocolStack().addProtocol(relay);
            relay.init();
        }

        channel.setName(this.configuration.getEnvironment().getNodeName() + "/" + id);

        TransportConfiguration.Topology topology = this.configuration.getTransport().getTopology();
        if (topology != null) {
            channel.setAddressGenerator(new TopologyAddressGenerator(channel, topology.getSite(), topology.getRack(), topology.getMachine()));
        }

        MBeanServer server = this.configuration.getMBeanServer();
        if (server != null) {
            try {
                this.channels.put(channel, id);
                JmxConfigurator.registerChannel(channel, server, id);
            } catch (Exception e) {
                ROOT_LOGGER.warn(e.getMessage(), e);
            }
            channel.addChannelListener(this);
        }

        return channel;
    }

    private void init(TP transport) {
        TransportConfiguration transportConfig = this.configuration.getTransport();
        SocketBinding binding = transportConfig.getSocketBinding();
        if (binding != null) {
            SocketFactory factory = transport.getSocketFactory();
            if (!(factory instanceof ManagedSocketFactory)) {
                transport.setSocketFactory(new ManagedSocketFactory(factory, binding.getSocketBindings()));
            }
        }
        ThreadFactory threadFactory = transportConfig.getThreadFactory();
        if (threadFactory != null) {
            if (!(transport.getThreadFactory() instanceof ThreadFactoryAdapter)) {
                transport.setThreadFactory(new ThreadFactoryAdapter(threadFactory));
            }
        }
        ExecutorService defaultExecutor = transportConfig.getDefaultExecutor();
        if (defaultExecutor != null) {
            if (!(transport.getDefaultThreadPool() instanceof ManagedExecutorService)) {
                transport.setDefaultThreadPool(new ManagedExecutorService(defaultExecutor));
            }
        }
        ExecutorService oobExecutor = transportConfig.getOOBExecutor();
        if (oobExecutor != null) {
            if (!(transport.getOOBThreadPool() instanceof ManagedExecutorService)) {
                transport.setOOBThreadPool(new ManagedExecutorService(oobExecutor));
            }
        }
        ScheduledExecutorService timerExecutor = transportConfig.getTimerExecutor();
        if (timerExecutor != null) {
            if (!(transport.getTimer() instanceof TimerSchedulerAdapter)) {
                this.setValue(transport, "timer", new TimerSchedulerAdapter(new ManagedScheduledExecutorService(timerExecutor)));
            }
        }
    }

    /**
     * {@inheritDoc}
     * @see org.jgroups.conf.ProtocolStackConfigurator#getProtocolStackString()
     */
    @Override
    public String getProtocolStackString() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.jgroups.conf.ProtocolStackConfigurator#getProtocolStack()
     */
    @Override
    public List<org.jgroups.conf.ProtocolConfiguration> getProtocolStack() {
        List<org.jgroups.conf.ProtocolConfiguration> configs = new ArrayList<org.jgroups.conf.ProtocolConfiguration>(this.configuration.getProtocols().size() + 1);
        TransportConfiguration transport = this.configuration.getTransport();
        org.jgroups.conf.ProtocolConfiguration config = this.createProtocol(transport);
        Map<String, String> properties = config.getProperties();

        if (transport.isShared()) {
            properties.put(Global.SINGLETON_NAME, this.configuration.getName());
        }

        SocketBinding binding = transport.getSocketBinding();
        if (binding != null) {
            this.configureBindAddress(transport, config, binding);
            this.configureServerSocket(transport, config, "bind_port", binding);
            this.configureMulticastSocket(transport, config, "mcast_addr", "mcast_port", binding);
        }

        SocketBinding diagnosticsSocketBinding = transport.getDiagnosticsSocketBinding();
        boolean diagnostics = (diagnosticsSocketBinding != null);
        properties.put("enable_diagnostics", String.valueOf(diagnostics));
        if (diagnostics) {
            this.configureMulticastSocket(transport, config, "diagnostics_addr", "diagnostics_port", diagnosticsSocketBinding);
        }

        configs.add(config);

        boolean supportsMulticast = transport.hasProperty("mcast_addr");

        for (ProtocolConfiguration protocol: this.configuration.getProtocols()) {
            config = this.createProtocol(protocol);
            binding = protocol.getSocketBinding();
            if (binding != null) {
                this.configureBindAddress(protocol, config, binding);
                this.configureServerSocket(protocol, config, "bind_port", binding);
                this.configureServerSocket(protocol, config, "start_port", binding);
                this.configureMulticastSocket(protocol, config, "mcast_addr", "mcast_port", binding);
            } else if (transport.getSocketBinding() != null) {
                // If no socket-binding was specified, use bind address of transport
                this.configureBindAddress(protocol, config, transport.getSocketBinding());
            }
            if (!supportsMulticast) {
                this.setProperty(protocol, config, "use_mcast_xmit", String.valueOf(false));
            }
            configs.add(config);
        }
/*
        RelayConfiguration relay = this.configuration.getRelay();
        if (relay != null) {
            config = this.createProtocol(relay);
            this.setProperty(relay, config, "site", relay.getSiteName());
            configs.add(config);
        }
*/
        return configs;
    }

    private void configureBindAddress(ProtocolConfiguration protocol, org.jgroups.conf.ProtocolConfiguration config, SocketBinding binding) {
        this.setPropertyNoOverride(protocol, config, "bind_addr", binding.getSocketAddress().getAddress().getHostAddress());
    }

    private void configureServerSocket(ProtocolConfiguration protocol, org.jgroups.conf.ProtocolConfiguration config, String property, SocketBinding binding) {
        this.setPropertyNoOverride(protocol, config, property, String.valueOf(binding.getSocketAddress().getPort()));
    }

    private void configureMulticastSocket(ProtocolConfiguration protocol, org.jgroups.conf.ProtocolConfiguration config, String addressProperty, String portProperty, SocketBinding binding) {
        try {
            InetSocketAddress mcastSocketAddress = binding.getMulticastSocketAddress();
            this.setPropertyNoOverride(protocol, config, addressProperty, mcastSocketAddress.getAddress().getHostAddress());
            this.setPropertyNoOverride(protocol, config, portProperty, String.valueOf(mcastSocketAddress.getPort()));
        } catch (IllegalStateException e) {
            ROOT_LOGGER.couldNotSetAddressAndPortNoMulticastSocket(e, config.getProtocolName(), addressProperty, config.getProtocolName(), portProperty, binding.getName());
        }
    }

    private void setPropertyNoOverride(ProtocolConfiguration protocol, org.jgroups.conf.ProtocolConfiguration config, String name, String value) {
        boolean overridden = false ;
        String propertyValue = null ;
        // check if the property has been overridden by the user and log a message
        try {
            if (overridden = config.getOriginalProperties().containsKey(name))
               propertyValue = config.getOriginalProperties().get(name);
        }
        catch(Exception e) {
            ROOT_LOGGER.unableToAccessProtocolPropertyValue(e, name, protocol.getName());
        }

        // log a warning if property tries to override
        if (overridden) {
            ROOT_LOGGER.unableToOverrideSocketBindingValue(name, protocol.getName(), value, propertyValue);
        }
        setProperty(protocol, config, name, value);
    }

    private void setProperty(ProtocolConfiguration protocol, org.jgroups.conf.ProtocolConfiguration config, String name, String value) {
        if (protocol.hasProperty(name)) {
            config.getProperties().put(name, value);
        }
    }

    private org.jgroups.conf.ProtocolConfiguration createProtocol(final ProtocolConfiguration protocolConfig) {
        String protocol = protocolConfig.getName();
        final Map<String, String> properties = new HashMap<String, String>(this.configuration.getDefaults().getProperties(protocol));
        properties.putAll(protocolConfig.getProperties());
        return new org.jgroups.conf.ProtocolConfiguration(protocol, properties) {
            @Override
            public Map<String, String> getOriginalProperties() {
                return properties;
            }
        };
    }

    private void setValue(Protocol protocol, String property, Object value) {
        ROOT_LOGGER.setProtocolPropertyValue(protocol.getName(), property, value);
        try {
            protocol.setValue(property, value);
        } catch (IllegalArgumentException e) {
            ROOT_LOGGER.nonExistentProtocolPropertyValue(e, protocol.getName(), property, value);
        }
    }

    @Override
    public void channelConnected(Channel channel) {
        // no-op
    }

    @Override
    public void channelDisconnected(Channel channel) {
        // no-op
    }

    @Override
    public void channelClosed(Channel channel) {
        MBeanServer server = this.configuration.getMBeanServer();
        if (server != null) {
            try {
                JmxConfigurator.unregisterChannel((JChannel) channel, server, this.channels.remove(channel));
            } catch (Exception e) {
                ROOT_LOGGER.warn(e.getMessage(), e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6020.java