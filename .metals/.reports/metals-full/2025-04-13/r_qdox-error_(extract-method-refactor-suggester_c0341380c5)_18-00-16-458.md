error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14799.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14799.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14799.java
text:
```scala
a@@ddDeploymentProcessor(batchBuilder, new NestedJarInlineProcessor(), NestedJarInlineProcessor.PRIORITY);

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

package org.jboss.as.service;

import org.jboss.as.deployment.chain.DeploymentChain;
import org.jboss.as.deployment.chain.DeploymentChainImpl;
import org.jboss.as.deployment.chain.DeploymentChainProcessorInjector;
import org.jboss.as.deployment.chain.DeploymentChainProvider;
import org.jboss.as.deployment.chain.DeploymentChainProviderInjector;
import org.jboss.as.deployment.chain.DeploymentChainProviderService;
import org.jboss.as.deployment.chain.DeploymentChainService;
import org.jboss.as.deployment.chain.JarDeploymentActivator;
import org.jboss.as.deployment.module.DeploymentModuleLoader;
import org.jboss.as.deployment.module.DeploymentModuleLoaderProcessor;
import org.jboss.as.deployment.module.DeploymentModuleLoaderService;
import org.jboss.as.deployment.module.ManifestAttachmentProcessor;
import org.jboss.as.deployment.module.ModuleConfigProcessor;
import org.jboss.as.deployment.module.ModuleDependencyProcessor;
import org.jboss.as.deployment.module.ModuleDeploymentProcessor;
import org.jboss.as.deployment.module.NestedJarInlineProcessor;
import org.jboss.as.deployment.naming.ModuleContextProcessor;
import org.jboss.as.deployment.processor.ServiceActivatorDependencyProcessor;
import org.jboss.as.deployment.processor.ServiceActivatorProcessor;
import org.jboss.as.deployment.unit.DeploymentUnitProcessor;
import org.jboss.as.deployment.unit.DeploymentUnitProcessorService;
import org.jboss.as.model.AbstractSubsystemAdd;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.BatchServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.InjectedValue;
import org.jboss.msc.value.Value;
import org.jboss.msc.value.Values;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class SarSubsystemAdd extends AbstractSubsystemAdd<SarSubsystemElement> {

    private static final long serialVersionUID = 7065406809630792436L;

    public static final ServiceName SAR_DEPLOYMENT_CHAIN_SERVICE_NAME = DeploymentChain.SERVICE_NAME.append("sar");
    public static final long SAR_DEPLOYMENT_CHAIN_PRIORITY = JarDeploymentActivator.JAR_DEPLOYMENT_CHAIN_PRIORITY - 1000000L;

    public SarSubsystemAdd() {
        super(SarExtension.NAMESPACE);
    }

    protected <P> void applyUpdate(final UpdateContext updateContext, final UpdateResultHandler<? super Void, P> resultHandler, final P param) {
        final BatchBuilder batchBuilder = updateContext.getBatchBuilder();
        batchBuilder.addServiceValueIfNotExist(DeploymentChainProviderService.SERVICE_NAME, new DeploymentChainProviderService());

        final Value<DeploymentChain> deploymentChainValue = Values.immediateValue((DeploymentChain)new DeploymentChainImpl(SAR_DEPLOYMENT_CHAIN_SERVICE_NAME.toString()))   ;
        final DeploymentChainService deploymentChainService = new DeploymentChainService(deploymentChainValue);
        batchBuilder.addService(SAR_DEPLOYMENT_CHAIN_SERVICE_NAME, deploymentChainService)
            .addDependency(DeploymentChainProviderService.SERVICE_NAME, DeploymentChainProvider.class, new DeploymentChainProviderInjector<DeploymentChain>(deploymentChainValue, new SarDeploymentChainSelector(), SAR_DEPLOYMENT_CHAIN_PRIORITY));

        addDeploymentProcessor(batchBuilder, new ManifestAttachmentProcessor(), ManifestAttachmentProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ServiceActivatorDependencyProcessor(), ServiceActivatorDependencyProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ModuleDependencyProcessor(), ModuleDependencyProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new NestedJarInlineProcessor(), ModuleDependencyProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ModuleConfigProcessor(), ModuleConfigProcessor.PRIORITY);
        final InjectedValue<DeploymentModuleLoader> deploymentModuleLoaderValue = new InjectedValue<DeploymentModuleLoader>();
        addDeploymentProcessor(batchBuilder, new DeploymentModuleLoaderProcessor(deploymentModuleLoaderValue), DeploymentModuleLoaderProcessor.PRIORITY)
            .addDependency(DeploymentModuleLoaderService.SERVICE_NAME, DeploymentModuleLoader.class, deploymentModuleLoaderValue);
        addDeploymentProcessor(batchBuilder, new ModuleDeploymentProcessor(), ModuleDeploymentProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ServiceDeploymentParsingProcessor(), ServiceDeploymentParsingProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ModuleContextProcessor(), ModuleContextProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ServiceActivatorProcessor(), ServiceActivatorProcessor.PRIORITY);
        addDeploymentProcessor(batchBuilder, new ParsedServiceDeploymentProcessor(), ParsedServiceDeploymentProcessor.PRIORITY);
    }

    protected SarSubsystemElement createSubsystemElement() {
        return new SarSubsystemElement();
    }

    private static <T extends DeploymentUnitProcessor> BatchServiceBuilder<T> addDeploymentProcessor(final BatchBuilder batchBuilder, final T deploymentUnitProcessor, final long priority) {
        final DeploymentUnitProcessorService<T> deploymentUnitProcessorService = new DeploymentUnitProcessorService<T>(deploymentUnitProcessor);
        return batchBuilder.addService(SAR_DEPLOYMENT_CHAIN_SERVICE_NAME.append(deploymentUnitProcessor.getClass().getName()), deploymentUnitProcessorService)
            .addDependency(SAR_DEPLOYMENT_CHAIN_SERVICE_NAME, DeploymentChain.class, new DeploymentChainProcessorInjector<T>(deploymentUnitProcessorService, priority));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14799.java