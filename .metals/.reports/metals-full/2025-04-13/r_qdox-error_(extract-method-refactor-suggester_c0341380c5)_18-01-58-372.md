error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3070.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3070.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3070.java
text:
```scala
S@@tring channelName = JcaDistributedWorkManagerDefinition.DWmParameters.TRANSPORT_JGROPUS_CLUSTER.getAttribute().resolveModelAttribute(context, model).asString();

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
package org.jboss.as.connector.subsystems.jca;

import org.jboss.as.clustering.jgroups.ChannelFactory;
import org.jboss.as.connector.services.workmanager.DistributedWorkManagerService;
import org.jboss.as.connector.services.workmanager.NamedDistributedWorkManager;
import org.jboss.as.connector.util.ConnectorServices;
import org.jboss.as.connector.util.Injection;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PropertiesAttributeDefinition;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.threads.ThreadsServices;
import org.jboss.as.txn.service.TxnServices;
import org.jboss.dmr.ModelNode;
import org.jboss.jca.core.workmanager.policy.Always;
import org.jboss.jca.core.workmanager.policy.Never;
import org.jboss.jca.core.workmanager.policy.WaterMark;
import org.jboss.jca.core.workmanager.selector.FirstAvailable;
import org.jboss.jca.core.workmanager.selector.MaxFreeThreads;
import org.jboss.jca.core.workmanager.selector.PingTime;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.tm.JBossXATerminator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.jboss.as.connector.logging.ConnectorLogger.ROOT_LOGGER;
import static org.jboss.as.connector.subsystems.jca.Constants.WORKMANAGER_LONG_RUNNING;
import static org.jboss.as.connector.subsystems.jca.Constants.WORKMANAGER_SHORT_RUNNING;

/**
 * @author <a href="jesper.pedersen@jboss.org">Jesper Pedersen</a>
 * @author <a href="stefano.maestri@redhat.com">Stefano Maestri</a>
 */
public class DistributedWorkManagerAdd extends AbstractAddStepHandler {

    public static final DistributedWorkManagerAdd INSTANCE = new DistributedWorkManagerAdd();

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        for (JcaDistributedWorkManagerDefinition.DWmParameters parameter : JcaDistributedWorkManagerDefinition.DWmParameters.values()) {
            parameter.getAttribute().validateAndSet(operation, model);
        }
    }

    @Override
    protected void performRuntime(final OperationContext context, final ModelNode operation, final ModelNode model,
                                  final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers) throws OperationFailedException {

        String name = JcaDistributedWorkManagerDefinition.DWmParameters.NAME.getAttribute().resolveModelAttribute(context, model).asString();

        String policy = JcaDistributedWorkManagerDefinition.DWmParameters.POLICY.getAttribute().resolveModelAttribute(context, model).asString();
        String selector = JcaDistributedWorkManagerDefinition.DWmParameters.SELECTOR.getAttribute().resolveModelAttribute(context, model).asString();

        ServiceTarget serviceTarget = context.getServiceTarget();
        NamedDistributedWorkManager namedDistributedWorkManager = new NamedDistributedWorkManager(name);

        if (policy != null && !policy.trim().isEmpty()) {
            switch (JcaDistributedWorkManagerDefinition.PolicyValue.valueOf(policy)) {
                case NEVER: {
                    namedDistributedWorkManager.setPolicy(new Never());
                    break;
                }
                case ALWAYS: {
                    namedDistributedWorkManager.setPolicy(new Always());
                    break;
                }
                case WATERMARK: {
                    namedDistributedWorkManager.setPolicy(new WaterMark());
                    break;
                }
                default:
                    throw ROOT_LOGGER.unsupportedPolicy(policy);

            }
            Injection injector = new Injection();
            for (Map.Entry<String, String> entry : ((PropertiesAttributeDefinition) JcaDistributedWorkManagerDefinition.DWmParameters.POLICY_OPTIONS.getAttribute()).unwrap(context, model).entrySet()) {
                try {
                    injector.inject(namedDistributedWorkManager.getPolicy(), entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    ROOT_LOGGER.unsupportedPolicyOption(entry.getKey());
                }
            }
        } else {
            namedDistributedWorkManager.setPolicy(new WaterMark());
        }

        if (selector != null && !selector.trim().isEmpty()) {
            switch (JcaDistributedWorkManagerDefinition.SelectorValue.valueOf(selector)) {
                case FIRST_AVAILABLE: {
                    namedDistributedWorkManager.setSelector(new FirstAvailable());
                    break;
                }
                case MAX_FREE_THREADS: {
                    namedDistributedWorkManager.setSelector(new MaxFreeThreads());
                    break;
                }
                case PING_TIME: {
                    namedDistributedWorkManager.setSelector(new PingTime());
                    break;
                }
                default:
                    throw ROOT_LOGGER.unsupportedSelector(selector);
            }
            Injection injector = new Injection();
            for (Map.Entry<String, String> entry : ((PropertiesAttributeDefinition) JcaDistributedWorkManagerDefinition.DWmParameters.SELECTOR_OPTIONS.getAttribute()).unwrap(context, model).entrySet()) {
                try {
                    injector.inject(namedDistributedWorkManager.getSelector(), entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    ROOT_LOGGER.unsupportedSelectorOption(entry.getKey());
                }
            }
        } else {
            namedDistributedWorkManager.setSelector(new PingTime());
        }

        String jgroupsStack = model.hasDefined(JcaDistributedWorkManagerDefinition.DWmParameters.TRANSPORT_JGROPUS_STACK.getAttribute().getName()) ?
                JcaDistributedWorkManagerDefinition.DWmParameters.TRANSPORT_JGROPUS_STACK.getAttribute().resolveModelAttribute(context, model).asString() :
                "udp";
        String channelName = JcaDistributedWorkManagerDefinition.DWmParameters.TRANSPORT_JGROPUS_CHANNEL.getAttribute().resolveModelAttribute(context, model).asString();
        Long requestTimeout = JcaDistributedWorkManagerDefinition.DWmParameters.TRANSPORT_REQUEST_TIMEOUT.getAttribute().resolveModelAttribute(context, model).asLong();

        DistributedWorkManagerService wmService = new DistributedWorkManagerService(namedDistributedWorkManager, channelName, requestTimeout);
        ServiceBuilder builder = serviceTarget
                .addService(ConnectorServices.WORKMANAGER_SERVICE.append(name), wmService);
        builder.addDependency(ServiceName.JBOSS.append("jgroups").append("stack").append(jgroupsStack), ChannelFactory.class, wmService.getJGroupsChannelFactoryInjector());


        builder.addDependency(ServiceBuilder.DependencyType.OPTIONAL, ThreadsServices.EXECUTOR.append(WORKMANAGER_LONG_RUNNING).append(name), Executor.class, wmService.getExecutorLongInjector());
        builder.addDependency(ThreadsServices.EXECUTOR.append(WORKMANAGER_SHORT_RUNNING).append(name), Executor.class, wmService.getExecutorShortInjector());

        builder.addDependency(TxnServices.JBOSS_TXN_XA_TERMINATOR, JBossXATerminator.class, wmService.getXaTerminatorInjector())
                .addListener(verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3070.java