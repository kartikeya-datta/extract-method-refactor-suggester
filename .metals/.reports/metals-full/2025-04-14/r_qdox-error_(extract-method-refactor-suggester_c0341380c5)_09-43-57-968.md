error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4437.java
text:
```scala
S@@ecurityActions.getSystemEnvironment(), null, null, ServerEnvironment.LaunchType.DOMAIN, RunningMode.NORMAL, productConfig);

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

package org.jboss.as.server;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.extension.ExtensionRegistry;
import org.jboss.as.controller.persistence.AbstractConfigurationPersister;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ExtensibleConfigurationPersister;
import org.jboss.as.server.parsing.StandaloneXml;
import org.jboss.as.version.ProductConfig;
import org.jboss.dmr.ModelNode;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.threads.AsyncFuture;

/**
 * This is the task used by the Host Controller and passed to a Server instance
 * in order to bootstrap it from a remote source process.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author Mike M. Clark
 * @author Emanuel Muckenhuber
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public final class ServerStartTask implements ServerTask, Serializable, ObjectInputValidation {

    private static final long serialVersionUID = -1037124182656400874L;

    private final String serverName;
    private final int portOffset;
    private final String hostControllerName;
    private final String home;
    private final List<ServiceActivator> startServices;
    private final List<ModelNode> updates;
    private final Properties properties = new Properties();

    public ServerStartTask(final String hostControllerName, final String serverName, final int portOffset,
                           final List<ServiceActivator> startServices, final List<ModelNode> updates, final Map<String, String> launchProperties) {
        assert serverName != null && serverName.length() > 0  : "Server name \"" + serverName + "\" is invalid; cannot be null or blank";
        assert hostControllerName != null && hostControllerName.length() > 0 : "Host Controller name \"" + hostControllerName + "\" is invalid; cannot be null or blank";

        this.serverName = serverName;
        this.portOffset = portOffset;
        this.startServices = startServices;
        this.updates = updates;

        this.hostControllerName = hostControllerName;

        this.home = SecurityActions.getSystemProperty("jboss.home.dir");
        String serverBaseDir = SecurityActions.getSystemProperty("jboss.domain.servers.dir") + File.separatorChar + serverName;
        properties.setProperty(ServerEnvironment.SERVER_NAME, serverName);
        properties.setProperty(ServerEnvironment.HOME_DIR, home);
        properties.setProperty(ServerEnvironment.SERVER_BASE_DIR, serverBaseDir);
        properties.setProperty(ServerEnvironment.CONTROLLER_TEMP_DIR, SecurityActions.getSystemProperty("jboss.domain.temp.dir"));
        properties.setProperty(ServerEnvironment.DOMAIN_BASE_DIR, SecurityActions.getSystemProperty(ServerEnvironment.DOMAIN_BASE_DIR));
        properties.setProperty(ServerEnvironment.DOMAIN_CONFIG_DIR, SecurityActions.getSystemProperty(ServerEnvironment.DOMAIN_CONFIG_DIR));

        // Provide any other properties that standalone Main.determineEnvironment() would read
        // from system properties and pass in to ServerEnvironment
        setPropertyIfFound(launchProperties, ServerEnvironment.JAVA_EXT_DIRS, properties);
        setPropertyIfFound(launchProperties, ServerEnvironment.QUALIFIED_HOST_NAME, properties);
        setPropertyIfFound(launchProperties, ServerEnvironment.HOST_NAME, properties);
        setPropertyIfFound(launchProperties, ServerEnvironment.NODE_NAME, properties);
        @SuppressWarnings("deprecation")
        String deprecated = ServerEnvironment.MODULES_DIR;
        setPropertyIfFound(launchProperties, deprecated, properties);
        setPropertyIfFound(launchProperties, ServerEnvironment.BUNDLES_DIR, properties);
        setPropertyIfFound(launchProperties, ServerEnvironment.SERVER_DATA_DIR, properties);
        setPropertyIfFound(launchProperties, ServerEnvironment.SERVER_CONTENT_DIR, properties);
        setPropertyIfFound(launchProperties, ServerEnvironment.SERVER_LOG_DIR, properties);
        setPropertyIfFound(launchProperties, ServerEnvironment.SERVER_TEMP_DIR, properties);
    }

    @Override
    public AsyncFuture<ServiceContainer> run(final List<ServiceActivator> runServices) {
        final Bootstrap bootstrap = Bootstrap.Factory.newInstance();
        final ProductConfig productConfig = new ProductConfig(Module.getBootModuleLoader(), home);
        // Create server environment on the server, so that the system properties are getting initialized on the right side
        final ServerEnvironment providedEnvironment = new ServerEnvironment(hostControllerName, properties,
                SecurityActions.getSystemEnvironment(), null, ServerEnvironment.LaunchType.DOMAIN, RunningMode.NORMAL, productConfig);
        final Bootstrap.Configuration configuration = new Bootstrap.Configuration(providedEnvironment);
        final ExtensionRegistry extensionRegistry = configuration.getExtensionRegistry();
        final Bootstrap.ConfigurationPersisterFactory configurationPersisterFactory = new Bootstrap.ConfigurationPersisterFactory() {
            @Override
            public ExtensibleConfigurationPersister createConfigurationPersister(ServerEnvironment serverEnvironment, ExecutorService executorService) {
                ExtensibleConfigurationPersister persister = new AbstractConfigurationPersister(new StandaloneXml(configuration.getModuleLoader(), executorService, extensionRegistry)) {

                    private final PersistenceResource pr = new PersistenceResource() {

                        @Override
                        public void commit() {
                        }

                        @Override
                        public void rollback() {
                        }
                    };

                    @Override
                    public PersistenceResource store(final ModelNode model, Set<PathAddress> affectedAddresses) throws ConfigurationPersistenceException {
                        return pr;
                    }

                    @Override
                    public List<ModelNode> load() throws ConfigurationPersistenceException {
                        return updates;
                    }
                };
                extensionRegistry.setWriterRegistry(persister);
                return persister;
            }
        };
        configuration.setConfigurationPersisterFactory(configurationPersisterFactory);
        return bootstrap.bootstrap(configuration, startServices);
    }

    @Override
    public void validateObject() throws InvalidObjectException {
        if (serverName == null) {
            throw ServerMessages.MESSAGES.invalidObject("serverName");
        }
        if(hostControllerName == null) {
            throw ServerMessages.MESSAGES.invalidObject("hostControllerName");
        }
        if (portOffset < 0) {
            throw ServerMessages.MESSAGES.invalidPortOffset();
        }
        if (updates == null) {
            throw ServerMessages.MESSAGES.invalidObject("updates");
        }
        if (startServices == null) {
            throw ServerMessages.MESSAGES.invalidObject("startServices");
        }
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        ois.registerValidation(this, 100);
    }

    static void setPropertyIfFound(final Map<String, String> launchProperties, final String key, final Properties properties) {
        if (launchProperties.containsKey(key)) {
            properties.setProperty(key, launchProperties.get(key));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4437.java