error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8614.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8614.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8614.java
text:
```scala
t@@his.autoDeployExploded = autoDeployExploded == null ? false : autoDeployExploded.booleanValue();

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

package org.jboss.as.server.deployment.scanner;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jboss.as.server.ServerController;
import org.jboss.as.server.Services;
import org.jboss.as.server.deployment.api.ServerDeploymentRepository;
import org.jboss.as.server.deployment.scanner.api.DeploymentScanner;
import org.jboss.as.server.services.path.AbsolutePathService;
import org.jboss.as.server.services.path.RelativePathService;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * Service responsible creating a {@code DeploymentScanner}
 *
 * @author Emanuel Muckenhuber
 */
public class DeploymentScannerService implements Service<DeploymentScanner> {

    private static final int DEFAULT_INTERVAL = 5000;
    private long interval;
    private TimeUnit unit = TimeUnit.MILLISECONDS;
    private boolean enabled;
    private boolean autoDeployZipped;
    private boolean autoDeployExploded;
    private Long deploymentTimeout;

    /** The created scanner. */
    private DeploymentScanner scanner;

    private final InjectedValue<String> pathValue = new InjectedValue<String>();
    private final InjectedValue<ServerController> serverControllerValue = new InjectedValue<ServerController>();
    private final InjectedValue<ServerDeploymentRepository> deploymentRepositoryValue = new InjectedValue<ServerDeploymentRepository>();
    private final InjectedValue<ScheduledExecutorService> scheduledExecutorValue = new InjectedValue<ScheduledExecutorService>();

    public static ServiceName getServiceName(String repositoryName) {
        return DeploymentScanner.BASE_SERVICE_NAME.append(repositoryName);
    }

    /**
     * Add the deployment scanner service to a batch.
     *
     * @param serviceTarget the service target
     * @param name the repository name
     * @param relativeTo the relative to
     * @param path the path
     * @param scanInterval the scan interval
     * @param scanEnabled scan enabled
     * @param deploymentTimeout the deployment timeout
     * @return
     */
    public static void addService(final ServiceTarget serviceTarget, final String name, final String relativeTo, final String path,
            final Integer scanInterval, TimeUnit unit, final Boolean autoDeployZip, final Boolean autoDeployExploded, final Boolean scanEnabled, final Long deploymentTimeout) {
        final DeploymentScannerService service = new DeploymentScannerService(scanInterval, unit, autoDeployZip, autoDeployExploded, scanEnabled, deploymentTimeout);
        final ServiceName serviceName = getServiceName(name);
        final ServiceName pathService = serviceName.append("path");

        if(relativeTo != null) {
            RelativePathService.addService(pathService, path, relativeTo, serviceTarget);
        } else {
            AbsolutePathService.addService(pathService, path, serviceTarget);
        }

        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        serviceTarget.addService(serviceName, service)
            .addDependency(pathService, String.class, service.pathValue)
            .addDependency(Services.JBOSS_SERVER_CONTROLLER, ServerController.class, service.serverControllerValue)
            .addDependency(ServerDeploymentRepository.SERVICE_NAME, ServerDeploymentRepository.class, service.deploymentRepositoryValue)
            .addInjection(service.scheduledExecutorValue, scheduledExecutorService)
            .setInitialMode(Mode.ACTIVE)
            .install();
    }

    DeploymentScannerService(final Integer interval, final TimeUnit unit, final Boolean autoDeployZipped,
            final Boolean autoDeployExploded, final Boolean enabled, final Long deploymentTimeout) {
        this.interval = interval == null ? DEFAULT_INTERVAL : interval.longValue();
        this.unit = unit;
        this.autoDeployZipped = autoDeployZipped == null ? true : autoDeployZipped.booleanValue();
        this.autoDeployExploded = autoDeployExploded == null ? true : autoDeployExploded.booleanValue();
        this.enabled = enabled == null ? true : enabled.booleanValue();
        this.deploymentTimeout = deploymentTimeout;
    }


    /** {@inheritDoc} */
    @Override
    public synchronized void start(StartContext context) throws StartException {
        try {
            final String pathName = pathValue.getValue();

            final FileSystemDeploymentService scanner = new FileSystemDeploymentService(new File(pathName), serverControllerValue.getValue(), scheduledExecutorValue.getValue(), deploymentRepositoryValue.getValue());
            scanner.setScanInterval(unit.toMillis(interval));
            scanner.setAutoDeployExplodedContent(autoDeployExploded);
            scanner.setAutoDeployZippedContent(autoDeployZipped);
            if(deploymentTimeout != null) {
                scanner.setDeploymentTimeout(deploymentTimeout);
            }

            if(enabled) {
                scanner.startScanner();
            }
            this.scanner = scanner;
        } catch (Exception e) {
            throw new StartException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void stop(StopContext context) {
        final DeploymentScanner scanner = this.scanner;
        this.scanner = null;
        scanner.stopScanner();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized DeploymentScanner getValue() throws IllegalStateException {
        final DeploymentScanner scanner = this.scanner;
        if(scanner == null) {
            throw new IllegalStateException();
        }
        return scanner;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8614.java