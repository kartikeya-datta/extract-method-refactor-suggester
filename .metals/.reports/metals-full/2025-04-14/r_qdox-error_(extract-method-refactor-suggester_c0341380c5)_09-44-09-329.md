error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5693.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5693.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5693.java
text:
```scala
S@@erverService.addService(serviceTarget, configuration, processState, bootstrapListener, runningModeControl, vaultReader, configuration.getAuditLogger(), configuration.getAuthorizer());

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

import static org.jboss.as.server.ServerLogger.AS_ROOT_LOGGER;
import static org.jboss.as.server.ServerLogger.CONFIG_LOGGER;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.TreeSet;

import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.RunningModeControl;
import org.jboss.as.repository.ContentRepository;
import org.jboss.as.server.deployment.DeploymentMountProvider;
import org.jboss.as.server.mgmt.domain.RemoteFileRepositoryService;
import org.jboss.as.server.moduleservice.ExternalModuleService;
import org.jboss.as.server.moduleservice.ModuleIndexService;
import org.jboss.as.server.moduleservice.ServiceModuleLoader;
import org.jboss.as.server.services.security.AbstractVaultReader;
import org.jboss.as.version.ProductConfig;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.service.ValueService;
import org.jboss.msc.value.ImmediateValue;
import org.jboss.msc.value.Value;
import org.jboss.threads.AsyncFuture;

/**
 * The root service for an Application Server process.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class ApplicationServerService implements Service<AsyncFuture<ServiceContainer>> {

    private final List<ServiceActivator> extraServices;
    private final Bootstrap.Configuration configuration;
    private final RunningModeControl runningModeControl;
    private final ControlledProcessState processState;
    private final boolean standalone;
    private volatile FutureServiceContainer futureContainer;
    private volatile long startTime;

    ApplicationServerService(final List<ServiceActivator> extraServices, final Bootstrap.Configuration configuration,
                             final ControlledProcessState processState) {
        this.extraServices = extraServices;
        this.configuration = configuration;
        runningModeControl = configuration.getRunningModeControl();
        startTime = configuration.getStartTime();
        standalone = configuration.getServerEnvironment().isStandalone();
        this.processState = processState;
    }

    @Override
    public synchronized void start(final StartContext context) throws StartException {

        processState.setStarting();

        final Bootstrap.Configuration configuration = this.configuration;
        final ServerEnvironment serverEnvironment = configuration.getServerEnvironment();

        final ProductConfig config = serverEnvironment.getProductConfig();
        final String prettyVersion = config.getPrettyVersionString();
        AS_ROOT_LOGGER.serverStarting(prettyVersion);
        if (CONFIG_LOGGER.isDebugEnabled()) {
            final Properties properties = System.getProperties();
            final StringBuilder b = new StringBuilder(8192);
            b.append(ServerMessages.MESSAGES.configuredSystemPropertiesLabel());
            for (String property : new TreeSet<String>(properties.stringPropertyNames())) {
                b.append("\n\t").append(property).append(" = ").append(properties.getProperty(property, "<undefined>"));
            }
            CONFIG_LOGGER.debug(b);
            CONFIG_LOGGER.debugf(ServerMessages.MESSAGES.vmArgumentsLabel(getVMArguments()));
            if (CONFIG_LOGGER.isTraceEnabled()) {
                b.setLength(0);
                final Map<String,String> env = System.getenv();
                b.append(ServerMessages.MESSAGES.configuredSystemEnvironmentLabel());
                for (String key : new TreeSet<String>(env.keySet())) {
                    b.append("\n\t").append(key).append(" = ").append(env.get(key));
                }
                CONFIG_LOGGER.trace(b);
            }
        }
        final ServiceTarget serviceTarget = context.getChildTarget();
        final ServiceController<?> myController = context.getController();
        final ServiceContainer container = myController.getServiceContainer();
        futureContainer = new FutureServiceContainer();

        long startTime = this.startTime;
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        } else {
            this.startTime = -1;
        }

        CurrentServiceContainer.setServiceContainer(context.getController().getServiceContainer());

        final BootstrapListener bootstrapListener = new BootstrapListener(container, startTime, serviceTarget, futureContainer, prettyVersion);
        bootstrapListener.getStabilityMonitor().addController(myController);
        // Install either a local or remote content repository
        if(standalone) {
            ContentRepository.Factory.addService(serviceTarget, serverEnvironment.getServerContentDir());
        } else {
            RemoteFileRepositoryService.addService(serviceTarget, serverEnvironment.getServerContentDir());
        }
        DeploymentMountProvider.Factory.addService(serviceTarget);
        ServiceModuleLoader.addService(serviceTarget, configuration);
        ExternalModuleService.addService(serviceTarget);
        ModuleIndexService.addService(serviceTarget);
        final AbstractVaultReader vaultReader = service(AbstractVaultReader.class);
        AS_ROOT_LOGGER.debugf("Using VaultReader %s", vaultReader);
        ServerService.addService(serviceTarget, configuration, processState, bootstrapListener, runningModeControl, vaultReader, configuration.getAuditLogger());
        final ServiceActivatorContext serviceActivatorContext = new ServiceActivatorContext() {
            @Override
            public ServiceTarget getServiceTarget() {
                return serviceTarget;
            }

            @Override
            public ServiceRegistry getServiceRegistry() {
                return container;
            }
        };

        for(ServiceActivator activator : extraServices) {
            activator.activate(serviceActivatorContext);
        }

        // TODO: decide the fate of these

        // Add server environment
        ServerEnvironmentService.addService(serverEnvironment, serviceTarget);

        //Add server path manager service
        ServerPathManagerService serverPathManagerService = new ServerPathManagerService();
        ServerPathManagerService.addService(serviceTarget, serverPathManagerService, serverEnvironment);

        // Add product config service
        final Value<ProductConfig> productConfigValue = new ImmediateValue<ProductConfig>(config);
        serviceTarget.addService(Services.JBOSS_PRODUCT_CONFIG_SERVICE, new ValueService<ProductConfig>(productConfigValue))
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();

        // BES 2011/06/11 -- moved this to AbstractControllerService.start()
//        processState.setRunning();

        if (AS_ROOT_LOGGER.isDebugEnabled()) {
            final long nanos = context.getElapsedTime();
            AS_ROOT_LOGGER.debugf(prettyVersion + " root service started in %d.%06d ms",
                    Long.valueOf(nanos / 1000000L), Long.valueOf(nanos % 1000000L));
        }
    }

    @Override
    public synchronized void stop(final StopContext context) {
        processState.setStopping();
        CurrentServiceContainer.setServiceContainer(null);
        String prettyVersion = configuration.getServerEnvironment().getProductConfig().getPrettyVersionString();
        AS_ROOT_LOGGER.serverStopped(prettyVersion, Integer.valueOf((int) (context.getElapsedTime() / 1000000L)));
    }

    @Override
    public AsyncFuture<ServiceContainer> getValue() throws IllegalStateException, IllegalArgumentException {
        return futureContainer;
    }

    private String getVMArguments() {
      final StringBuilder result = new StringBuilder(1024);
      final RuntimeMXBean rmBean = ManagementFactory.getRuntimeMXBean();
      final List<String> inputArguments = rmBean.getInputArguments();
      for (String arg : inputArguments) {
          result.append(arg).append(" ");
      }
      return result.toString();
   }

    private static <S> S service(final Class<S> service) {
        final ServiceLoader<S> serviceLoader = ServiceLoader.load(service);
        final Iterator<S> it = serviceLoader.iterator();
        if (it.hasNext())
            return it.next();
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5693.java