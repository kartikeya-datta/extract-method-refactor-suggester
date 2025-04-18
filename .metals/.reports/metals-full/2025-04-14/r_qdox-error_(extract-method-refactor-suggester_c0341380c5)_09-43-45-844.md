error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5916.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5916.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5916.java
text:
```scala
t@@hrow new StartException("Non HTTP connectors do not support SSL");

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.concurrent.Executor;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11AprProtocol;
import org.apache.coyote.http11.Http11Protocol;
import org.jboss.as.server.services.net.ManagedBinding;
import org.jboss.as.server.services.net.SocketBinding;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * Service creating and starting a web connector.
 *
 * @author Emanuel Muckenhuber
 */
class WebConnectorService implements Service<Connector> {

    private String protocol = "HTTP/1.1";
    private String scheme = "http";

    private Boolean enableLookups = null;
    private String proxyName = null;
    private Integer proxyPort = null;
    private Integer redirectPort = null;
    private Boolean secure = null;
    private Integer maxPostSize = null;
    private Integer maxSavePostSize = null;
    private Integer maxConnections = null;
    private ModelNode ssl;
    private ModelNode virtualServers;

    private Connector connector;

    private final InjectedValue<Executor> executor = new InjectedValue<Executor>();
    private final InjectedValue<SocketBinding> binding = new InjectedValue<SocketBinding>();
    private final InjectedValue<WebServer> server = new InjectedValue<WebServer>();

    public WebConnectorService(String protocol, String scheme) {
        if(protocol != null) this.protocol = protocol;
        if(scheme != null) this.scheme = scheme;
    }

    /**
     * Start, register and bind the web connector.
     *
     * @param context the start context
     * @throws StartException if the connector cannot be started
     */
    public synchronized void start(StartContext context) throws StartException {
        final SocketBinding binding = this.binding.getValue();
        final InetSocketAddress address = binding.getSocketAddress();
        final Executor executor = this.executor.getOptionalValue();
        try {
            // Create connector
            final Connector connector = new Connector(protocol);
            connector.setPort(address.getPort());
            connector.setScheme(scheme);
            if(enableLookups != null) connector.setEnableLookups(enableLookups);
            if(maxPostSize != null) connector.setMaxPostSize(maxPostSize);
            if(maxSavePostSize != null) connector.setMaxSavePostSize(maxSavePostSize);
            if(proxyName != null) connector.setProxyName(proxyName);
            if(proxyPort != null) connector.setProxyPort(proxyPort);
            if(redirectPort != null) connector.setRedirectPort(redirectPort);
            if(secure != null) connector.setSecure(secure);
            if (executor != null) {
                Method m = connector.getProtocolHandler().getClass().getMethod("setExecutor", Executor.class);
                m.invoke(connector.getProtocolHandler(), executor);
            }
            if (address != null && address.getAddress() != null)  {
                Method m = connector.getProtocolHandler().getClass().getMethod("setAddress", InetAddress.class);
                m.invoke(connector.getProtocolHandler(), address.getAddress());
            }
            if (maxConnections != null) {
                try {
                    Method m = connector.getProtocolHandler().getClass().getMethod("setPollerSize", Integer.TYPE);
                    m.invoke(connector.getProtocolHandler(), maxConnections);
                } catch (NoSuchMethodException e) {
                 // Not all connectors will have this
                }
                try {
                    Method m = connector.getProtocolHandler().getClass().getMethod("setSendfileSize", Integer.TYPE);
                    m.invoke(connector.getProtocolHandler(), maxConnections);
                } catch (NoSuchMethodException e) {
                 // Not all connectors will have this
                }
            }
            if (virtualServers != null) {
                HashSet<String> virtualServersList = new HashSet<String>();
                for (final ModelNode virtualServer : virtualServers.asList()) {
                    virtualServersList.add(virtualServer.asString());
                }
                connector.setAllowedHosts(virtualServersList);
            }
            if (ssl != null) {
                boolean nativeSSL = false;
                if (connector.getProtocolHandler() instanceof Http11AprProtocol) {
                    nativeSSL = true;
                } else if (!(connector.getProtocolHandler() instanceof Http11Protocol)) {
                    throw new StartException("Non HTTP connectors dor not support SSL");
                }
                // Enable SSL
                try {
                    Method m = connector.getProtocolHandler().getClass().getMethod("setSSLEnabled", Boolean.TYPE);
                    m.invoke(connector.getProtocolHandler(), true);
                } catch (NoSuchMethodException e) {
                    // No SSL support
                    throw new StartException(e);
                }
                if (nativeSSL) {
                    // OpenSSL configuration
                    try {
                        if (ssl.hasDefined(Constants.PASSWORD)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLPassword", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.PASSWORD).asString());
                        }
                        if (ssl.hasDefined(Constants.CERTIFICATE_KEY_FILE)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLCertificateKeyFile", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.CERTIFICATE_KEY_FILE).asString());
                        }
                        if (ssl.hasDefined(Constants.CIPHER_SUITE)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLCipherSuite", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.CIPHER_SUITE).asString());
                        }
                        if (ssl.hasDefined(Constants.PROTOCOL)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLProtocol", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.PROTOCOL).asString());
                        }
                        if (ssl.hasDefined(Constants.VERIFY_CLIENT)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLVerifyClient", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.VERIFY_CLIENT).asString());
                        }
                        if (ssl.hasDefined(Constants.VERIFY_DEPTH)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLVerifyDepth", Integer.TYPE);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.VERIFY_DEPTH).asInt());
                        }
                        if (ssl.hasDefined(Constants.CERTIFICATE_FILE)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLCertificateFile", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.CERTIFICATE_FILE).asString());
                        }
                        if (ssl.hasDefined(Constants.CA_CERTIFICATE_FILE)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLCACertificateFile", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.CA_CERTIFICATE_FILE).asString());
                        }
                        if (ssl.hasDefined(Constants.CA_REVOCATION_URL)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setSSLCARevocationFile", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.CA_REVOCATION_URL).asString());
                        }
                   } catch (NoSuchMethodException e) {
                        throw new StartException(e);
                    }
                } else {
                    // JSSE configuration
                    try {
                        if (ssl.hasDefined(Constants.KEY_ALIAS)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setKeyAlias", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.KEY_ALIAS).asString());
                        }
                        if (ssl.hasDefined(Constants.PASSWORD)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setKeypass", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.PASSWORD).asString());
                        }
                        if (ssl.hasDefined(Constants.CERTIFICATE_KEY_FILE)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setKeystore", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.CERTIFICATE_KEY_FILE).asString());
                        }
                        if (ssl.hasDefined(Constants.CIPHER_SUITE)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setCiphers", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.CIPHER_SUITE).asString());
                        }
                        if (ssl.hasDefined(Constants.PROTOCOL)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setProtocols", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.PROTOCOL).asString());
                        }
                        if (ssl.hasDefined(Constants.VERIFY_CLIENT)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setClientauth", String.class);
                            m.invoke(connector.getProtocolHandler(), ssl.get(Constants.VERIFY_CLIENT).asString());
                        }
                        if (ssl.hasDefined(Constants.SESSION_CACHE_SIZE)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setAttribute", String.class, Object.class);
                            m.invoke(connector.getProtocolHandler(), "sessionCacheSize", ssl.get(Constants.SESSION_CACHE_SIZE).asString());
                        }
                        if (ssl.hasDefined(Constants.SESSION_TIMEOUT)) {
                            Method m = connector.getProtocolHandler().getClass().getMethod("setAttribute", String.class, Object.class);
                            m.invoke(connector.getProtocolHandler(), "sessionCacheTimeout", ssl.get(Constants.SESSION_TIMEOUT).asString());
                        }
                    } catch (NoSuchMethodException e) {
                        throw new StartException(e);
                    }
                }
            }
            getWebServer().addConnector(connector);
            this.connector = connector;
        } catch (Exception e) {
            throw new StartException(e);
        }
        // Register the binding after the connector is started
        binding.getSocketBindings().getNamedRegistry().registerBinding(new ConnectorBinding(binding));
    }

    /** {@inheritDoc} */
    public synchronized void stop(StopContext context) {
        final SocketBinding binding = this.binding.getValue();
        binding.getSocketBindings().getNamedRegistry().unregisterBinding(binding.getName());
        final Connector connector = this.connector;
        getWebServer().removeConnector(connector);
        this.connector = null;
    }

    /** {@inheritDoc} */
    public synchronized Connector getValue() throws IllegalStateException {
        final Connector connector = this.connector;
        if (connector == null) {
            throw new IllegalStateException();
        }
        return connector;
    }

    void setSsl(final ModelNode ssl) {
        this.ssl = ssl;
    }

    void setVirtualServers(ModelNode virtualServers) {
        this.virtualServers = virtualServers;
    }

    protected boolean isEnableLookups() {
        return enableLookups;
    }

    protected void setEnableLookups(boolean enableLookups) {
        this.enableLookups = enableLookups;
    }

    protected String getProxyName() {
        return proxyName;
    }

    protected void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    protected int getProxyPort() {
        return proxyPort;
    }

    protected void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    protected int getRedirectPort() {
        return redirectPort;
    }

    protected void setRedirectPort(int redirectPort) {
        this.redirectPort = redirectPort;
    }

    protected boolean isSecure() {
        return secure;
    }

    protected void setSecure(boolean secure) {
        this.secure = secure;
    }

    protected int getMaxPostSize() {
        return maxPostSize;
    }

    protected void setMaxPostSize(int maxPostSize) {
        this.maxPostSize = maxPostSize;
    }

    protected int getMaxSavePostSize() {
        return maxSavePostSize;
    }

    protected void setMaxSavePostSize(int maxSavePostSize) {
        this.maxSavePostSize = maxSavePostSize;
    }

    protected int getMaxConnections() {
        return maxConnections;
    }

    protected void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    InjectedValue<Executor> getExecutor() {
        return executor;
    }

    InjectedValue<SocketBinding> getBinding() {
        return binding;
    }

    InjectedValue<WebServer> getServer() {
        return server;
    }

    private WebServer getWebServer() {
        return server.getValue();
    }

    static class ConnectorBinding implements ManagedBinding {

        private final SocketBinding binding;

        private ConnectorBinding(final SocketBinding binding) {
            this.binding = binding;
        }

        @Override
        public String getSocketBindingName() {
            return binding.getName();
        }

        @Override
        public InetSocketAddress getBindAddress() {
            return binding.getSocketAddress();
        }

        @Override
        public void close() throws IOException {
            // TODO should this do something?
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5916.java