error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7579.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7579.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7579.java
text:
```scala
b@@atchBuilder.addService(JAR_DEPLOYMENT_CHAIN_SERVICE_NAME.append(deploymentUnitProcessor.getClass().getName()), deploymentUnitProcessorService)

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

package org.jboss.as.deployment.chain;

import org.jboss.as.deployment.managedbean.ManagedBeanAnnotationProcessor;
import org.jboss.as.deployment.managedbean.ManagedBeanDependencyProcessor;
import org.jboss.as.deployment.managedbean.ManagedBeanDeploymentProcessor;
import org.jboss.as.deployment.module.DeploymentModuleLoaderProcessor;
import org.jboss.as.deployment.module.ModuleConfigProcessor;
import org.jboss.as.deployment.module.ModuleDependencyProcessor;
import org.jboss.as.deployment.module.ModuleDeploymentProcessor;
import org.jboss.as.deployment.naming.ModuleContextProcessor;
import org.jboss.as.deployment.processor.AnnotationIndexProcessor;
import org.jboss.as.deployment.service.ParsedServiceDeploymentProcessor;
import org.jboss.as.deployment.service.ServiceDeploymentParsingProcessor;
import org.jboss.as.deployment.unit.DeploymentUnitProcessor;
import org.jboss.as.deployment.unit.DeploymentUnitProcessorService;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.Value;
import org.jboss.msc.value.Values;

/**
 * Service activator which installs the various service required for jar deployments.
 * 
 * @author John E. Bailey
 */
public class JarDeploymentActivator implements ServiceActivator {
    public static final long JAR_DEPLOYMENT_CHAIN_PRIORITY = 1000000L;
    public static final ServiceName JAR_DEPLOYMENT_CHAIN_SERVICE_NAME = DeploymentChain.SERVICE_NAME.append("jar");

    /**
     * Activate the services required for service deployments.
     * 
     * @param context The service activator context
     */
    public void activate(final ServiceActivatorContext context) {
        final BatchBuilder batchBuilder = context.getBatchBuilder();
        batchBuilder.addServiceValueIfNotExist(DeploymentChainProviderService.SERVICE_NAME, new DeploymentChainProviderService());

        final Value<DeploymentChain> deploymentChainValue = Values.immediateValue((DeploymentChain)new DeploymentChainImpl(JAR_DEPLOYMENT_CHAIN_SERVICE_NAME.toString()))   ;
        final DeploymentChainService deploymentChainService = new DeploymentChainService(deploymentChainValue);
        batchBuilder.addService(JAR_DEPLOYMENT_CHAIN_SERVICE_NAME, deploymentChainService)
            .addDependency(DeploymentChainProviderService.SERVICE_NAME, DeploymentChainProvider.class, new DeploymentChainProviderInjector<DeploymentChain>(deploymentChainValue, new JarDeploymentChainSelector(), JAR_DEPLOYMENT_CHAIN_PRIORITY));

        addDeploymentProcessor(batchBuilder, new AnnotationIndexProcessor(), AnnotationIndexProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ManagedBeanDependencyProcessor(), ManagedBeanDependencyProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ModuleDependencyProcessor(), ModuleDependencyProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ModuleConfigProcessor(), ModuleConfigProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new DeploymentModuleLoaderProcessor(), DeploymentModuleLoaderProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ModuleDeploymentProcessor(), ModuleDeploymentProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ManagedBeanAnnotationProcessor(), ManagedBeanAnnotationProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ServiceDeploymentParsingProcessor(), ServiceDeploymentParsingProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ModuleContextProcessor(), ModuleContextProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ParsedServiceDeploymentProcessor(), ParsedServiceDeploymentProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ManagedBeanDeploymentProcessor(), ManagedBeanDeploymentProcessor.PRIORITY);
    }

    private <T extends DeploymentUnitProcessor> void addDeploymentProcessor(final BatchBuilder batchBuilder, final T deploymentUnitProcessor, final long priority) {
        final DeploymentUnitProcessorService<T> deploymentUnitProcessorService = new DeploymentUnitProcessorService<T>(deploymentUnitProcessor);
        batchBuilder.addService(DeploymentUnitProcessor.DEPLOYMENT_PROCESSOR_SERVICE_NAME.append(deploymentUnitProcessor.getClass().getName()), deploymentUnitProcessorService)
            .addDependency(JAR_DEPLOYMENT_CHAIN_SERVICE_NAME, DeploymentChain.class, new DeploymentChainProcessorInjector<T>(deploymentUnitProcessorService, priority));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7579.java