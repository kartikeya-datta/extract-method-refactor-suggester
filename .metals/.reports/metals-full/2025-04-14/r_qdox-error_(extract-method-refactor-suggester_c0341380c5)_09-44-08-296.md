error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3600.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3600.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3600.java
text:
```scala
p@@athHandler.addPath("/", rootConsoleRedirectHandler);

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
package org.jboss.as.domain.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.SSLContext;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.security.handlers.SinglePortConfidentialityHandler;
import io.undertow.security.impl.BasicAuthenticationMechanism;
import io.undertow.security.impl.CachedAuthenticatedSessionMechanism;
import io.undertow.security.impl.ClientCertAuthenticationMechanism;
import io.undertow.security.impl.DigestAlgorithm;
import io.undertow.security.impl.DigestAuthenticationMechanism;
import io.undertow.security.impl.DigestQop;
import io.undertow.security.impl.SimpleNonceManager;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpOpenListener;
import io.undertow.server.handlers.CanonicalPathHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.cache.CacheHandler;
import io.undertow.server.handlers.cache.DirectBufferCache;
import io.undertow.server.handlers.error.SimpleErrorPageHandler;
import org.jboss.as.controller.ControlledProcessStateService;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.domain.http.server.security.AuthenticationMechanismWrapper;
import org.jboss.as.domain.http.server.security.ConnectionAuthenticationCacheHandler;
import org.jboss.as.domain.http.server.security.DmrFailureReadinessHandler;
import org.jboss.as.domain.http.server.security.LogoutHandler;
import org.jboss.as.domain.http.server.security.RealmIdentityManager;
import org.jboss.as.domain.http.server.security.RedirectReadinessHandler;
import org.jboss.as.domain.management.AuthMechanism;
import org.jboss.as.domain.management.SecurityRealm;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.xnio.BufferAllocator;
import org.xnio.ByteBufferSlicePool;
import org.xnio.ChannelListener;
import org.xnio.ChannelListeners;
import org.xnio.IoUtils;
import org.xnio.OptionMap;
import org.xnio.OptionMap.Builder;
import org.xnio.Options;
import org.xnio.StreamConnection;
import org.xnio.Xnio;
import org.xnio.XnioWorker;
import org.xnio.channels.AcceptingChannel;
import org.xnio.channels.SslConnection;
import org.xnio.ssl.JsseXnioSsl;
import org.xnio.ssl.XnioSsl;

import static org.jboss.as.domain.http.server.HttpServerLogger.ROOT_LOGGER;
import static org.jboss.as.domain.management.RealmConfigurationConstants.DIGEST_PLAIN_TEXT;
import static org.xnio.Options.SSL_CLIENT_AUTH_MODE;
import static org.xnio.SslClientAuthMode.REQUESTED;
import static org.xnio.SslClientAuthMode.REQUIRED;

/**
 * The general HTTP server for handling management API requests.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class ManagementHttpServer {

    private final HttpOpenListener openListener;
    private final InetSocketAddress httpAddress;
    private final InetSocketAddress secureAddress;
    private volatile XnioWorker worker;
    private volatile AcceptingChannel<StreamConnection> normalServer;
    private volatile AcceptingChannel<SslConnection> secureServer;
    private final SecurityRealm securityRealm;

    private ManagementHttpServer(HttpOpenListener openListener, InetSocketAddress httpAddress, InetSocketAddress secureAddress, SecurityRealm securityRealm) {
        this.openListener = openListener;
        this.httpAddress = httpAddress;
        this.secureAddress = secureAddress;
        this.securityRealm = securityRealm;
    }


    public void start() {
        final Xnio xnio;
        try {
            //Do what org.jboss.as.remoting.XnioUtil does
            xnio = Xnio.getInstance(null, Module.getModuleFromCallerModuleLoader(ModuleIdentifier.fromString("org.jboss.xnio.nio")).getClassLoader());
        } catch (Exception e) {
            throw new IllegalStateException(e.getLocalizedMessage());
        }
        try {
            //TODO make this configurable
            worker = xnio.createWorker(OptionMap.builder()
                    .set(Options.WORKER_IO_THREADS, 4)
                    .set(Options.CONNECTION_HIGH_WATER, 1000000)
                    .set(Options.CONNECTION_LOW_WATER, 1000000)
                    .set(Options.WORKER_TASK_CORE_THREADS, 10)
                    .set(Options.WORKER_TASK_MAX_THREADS, 12)
                    .set(Options.TCP_NODELAY, true)
                    .set(Options.CORK, true)
                    .getMap());

            Builder serverOptionsBuilder = OptionMap.builder()
                    .set(Options.TCP_NODELAY, true)
                    .set(Options.REUSE_ADDRESSES, true);
            ChannelListener acceptListener = ChannelListeners.openListenerAdapter(openListener);
            if (httpAddress != null) {
                normalServer = worker.createStreamConnectionServer(httpAddress, acceptListener, serverOptionsBuilder.getMap());
                normalServer.resumeAccepts();
            }
            if (secureAddress != null) {
                SSLContext sslContext = securityRealm.getSSLContext();
                Set<AuthMechanism> supportedMechanisms = securityRealm.getSupportedAuthenticationMechanisms();
                if (supportedMechanisms.contains(AuthMechanism.CLIENT_CERT)) {
                    if (supportedMechanisms.contains(AuthMechanism.DIGEST)
 supportedMechanisms.contains(AuthMechanism.PLAIN)) {
                        // Username / Password auth is possible so don't mandate a client certificate.
                        serverOptionsBuilder.set(SSL_CLIENT_AUTH_MODE, REQUESTED);
                    } else {
                        serverOptionsBuilder.set(SSL_CLIENT_AUTH_MODE, REQUIRED);
                    }
                }
                OptionMap secureOptions = serverOptionsBuilder.getMap();
                XnioSsl xnioSsl = new JsseXnioSsl(worker.getXnio(), secureOptions, sslContext);
                secureServer = xnioSsl.createSslConnectionServer(worker, secureAddress, acceptListener, secureOptions);
                secureServer.resumeAccepts();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        IoUtils.safeClose(normalServer);
        IoUtils.safeClose(secureServer);
        worker.shutdown();
    }

    public static ManagementHttpServer create(InetSocketAddress bindAddress, InetSocketAddress secureBindAddress, int backlog,
                                              ModelControllerClient modelControllerClient, ExecutorService executorService, SecurityRealm securityRealm, ControlledProcessStateService controlledProcessStateService,
                                              ConsoleMode consoleMode, String consoleSlot)
            throws IOException {

        HttpOpenListener openListener = new HttpOpenListener(new ByteBufferSlicePool(BufferAllocator.DIRECT_BYTE_BUFFER_ALLOCATOR, 4096, 10 * 4096), 4096);
        int securePort = secureBindAddress != null ? secureBindAddress.getPort() : -1;
        setupOpenListener(openListener, modelControllerClient, consoleMode, consoleSlot, controlledProcessStateService, securePort, securityRealm);
        ManagementHttpServer server = new ManagementHttpServer(openListener, bindAddress, secureBindAddress, securityRealm);

        return server;
    }

    private static void setupOpenListener(HttpOpenListener listener, ModelControllerClient modelControllerClient, ConsoleMode consoleMode, String consoleSlot, ControlledProcessStateService controlledProcessStateService, int securePort, SecurityRealm securityRealm) {
        CanonicalPathHandler canonicalPathHandler = new CanonicalPathHandler();
        listener.setRootHandler(canonicalPathHandler);

        PathHandler pathHandler = new PathHandler();
        HttpHandler current = pathHandler;
        if (securePort > 0) {
            current = new SinglePortConfidentialityHandler(current, securePort);
        }
        //caching handler, used for static resources
        current = new CacheHandler(new DirectBufferCache(1024,1024 * 10, 1024 * 1000, BufferAllocator.BYTE_BUFFER_ALLOCATOR), current);
        current = new SimpleErrorPageHandler(current);
        canonicalPathHandler.setNext(current);

        ResourceHandlerDefinition consoleHandler = null;
        try {
            consoleHandler = consoleMode.createConsoleHandler(consoleSlot);
        } catch (ModuleLoadException e) {
            ROOT_LOGGER.consoleModuleNotFound(consoleSlot == null ? "main" : consoleSlot);
        }

        try {
            pathHandler.addPath(ErrorContextHandler.ERROR_CONTEXT, ErrorContextHandler.createErrorContext(consoleSlot));
        } catch (ModuleLoadException e) {
            ROOT_LOGGER.error(consoleSlot == null ? "main" : consoleSlot);
        }

        ManagementRootConsoleRedirectHandler rootConsoleRedirectHandler = new ManagementRootConsoleRedirectHandler(consoleHandler);
        DomainApiCheckHandler domainApiHandler = new DomainApiCheckHandler(modelControllerClient, controlledProcessStateService);
        pathHandler.setDefaultHandler(rootConsoleRedirectHandler);
        if (consoleHandler != null) {
            HttpHandler readinessHandler = new RedirectReadinessHandler(securityRealm, consoleHandler.getHandler(),
                    ErrorContextHandler.ERROR_CONTEXT);
            pathHandler.addPath(consoleHandler.getContext(), readinessHandler);
        }

        HttpHandler readinessHandler = new DmrFailureReadinessHandler(securityRealm, secureDomainAccess(domainApiHandler, securityRealm), ErrorContextHandler.ERROR_CONTEXT);
        pathHandler.addPath(DomainApiCheckHandler.PATH, readinessHandler);

        if (securityRealm != null) {
            pathHandler.addPath(LogoutHandler.PATH, new LogoutHandler(securityRealm.getName()));
        }
    }

    private static HttpHandler secureDomainAccess(final HttpHandler domainHandler, final SecurityRealm securityRealm) {
        if (securityRealm != null) {
            Set<AuthMechanism> mechanisms = securityRealm.getSupportedAuthenticationMechanisms();
            List<AuthenticationMechanism> undertowMechanisms = new ArrayList<AuthenticationMechanism>(mechanisms.size());
            undertowMechanisms.add(wrap(new CachedAuthenticatedSessionMechanism(), null));
            for (AuthMechanism current : mechanisms) {
                switch (current) {
                    case CLIENT_CERT:
                        undertowMechanisms.add(wrap(new ClientCertAuthenticationMechanism(), current));
                        break;
                    case DIGEST:
                        Map<String, String> mechConfig = securityRealm.getMechanismConfig(AuthMechanism.DIGEST);
                        boolean plainTextDigest = true;
                        if (mechConfig.containsKey(DIGEST_PLAIN_TEXT)) {
                            plainTextDigest = Boolean.parseBoolean(mechConfig.get(DIGEST_PLAIN_TEXT));
                        }
                        List<DigestAlgorithm> digestAlgorithms = Collections.singletonList(DigestAlgorithm.MD5);
                        List<DigestQop> digestQops = Collections.emptyList();
                        undertowMechanisms.add(wrap(new DigestAuthenticationMechanism(digestAlgorithms, digestQops,
                                securityRealm.getName(), "/management", new SimpleNonceManager(), plainTextDigest), current));
                        break;
                    case PLAIN:
                        undertowMechanisms.add(wrap(new BasicAuthenticationMechanism(securityRealm.getName()), current));
                        break;
                }
            }

            if (undertowMechanisms.size() > 1) {
                // If the only mechanism is the cached mechanism then no need to add these.
                HttpHandler current = domainHandler;
                current = new AuthenticationCallHandler(current);
                // Currently the security handlers are being added after a PATH handler so we know authentication is required by
                // this point.
                current = new AuthenticationConstraintHandler(current);
                current = new AuthenticationMechanismsHandler(current, undertowMechanisms);
                current = new ConnectionAuthenticationCacheHandler(current);

                return new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE, new RealmIdentityManager(securityRealm),
                        current);
            }
            // TODO - If there were no mechanisms to begin with requests should be represented as an anonymous user.
            // If there were mechanisms but none suitable for HTTP reject all requests.
        }

        return domainHandler;
    }

    private static AuthenticationMechanism wrap(final AuthenticationMechanism toWrap, final AuthMechanism mechanism) {
        return new AuthenticationMechanismWrapper(toWrap, mechanism);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3600.java