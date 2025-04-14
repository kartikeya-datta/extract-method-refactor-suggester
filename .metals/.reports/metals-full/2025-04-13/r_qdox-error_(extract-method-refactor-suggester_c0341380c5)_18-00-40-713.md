error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8098.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8098.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8098.java
text:
```scala
public static v@@oid addDeploymentProcessor(final String subsystemName, Phase phase, int priority, DeploymentUnitProcessor processor) {

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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.server.deployment.DeployerChainsService;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.server.deployment.RegisteredDeploymentUnitProcessor;
import org.jboss.as.server.deployment.Services;
import org.jboss.dmr.ModelNode;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADDRESS;

/**
 * @author John Bailey
 */
public class DeployerChainAddHandler implements OperationStepHandler, DescriptionProvider {
    static final String NAME = "add-deployer-chains";
    public static final DeployerChainAddHandler INSTANCE = new DeployerChainAddHandler();

    static void addDeploymentProcessor(final String subsystemName, Phase phase, int priority, DeploymentUnitProcessor processor) {
        final EnumMap<Phase, Set<RegisteredDeploymentUnitProcessor>> deployerMap = INSTANCE.deployerMap;
        deployerMap.get(phase).add(new RegisteredDeploymentUnitProcessor(priority, processor, subsystemName));
    }

    static ModelNode OPERATION = new ModelNode();
    static {
        OPERATION.get(ModelDescriptionConstants.OP).set(NAME);
        OPERATION.get(ADDRESS).setEmptyList();
    }

    // This map is concurrently read by multiple threads but will only
    // be written by a single thread, the boot thread
    private final EnumMap<Phase, Set<RegisteredDeploymentUnitProcessor>> deployerMap;

    private DeployerChainAddHandler() {
        final EnumMap<Phase, Set<RegisteredDeploymentUnitProcessor>> map = new EnumMap<Phase, Set<RegisteredDeploymentUnitProcessor>>(Phase.class);
        for (Phase phase : Phase.values()) {
            map.put(phase, new ConcurrentSkipListSet<RegisteredDeploymentUnitProcessor>());
        }
        this.deployerMap = map;
    }

    /** This is only public so AbstractSubsystemTest can use it; otherwise it would be package-protected. */
    public void clearDeployerMap() {
        for (Set<RegisteredDeploymentUnitProcessor> set : deployerMap.values()) {
            set.clear();
        }
    }

    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
        if(context.isNormalServer()) {

            // Our real work needs to run after all RUNTIME steps that add DUPs have run. ServerService adds
            // this operation at the end of the boot op list, so our MODEL stage step is last, and
            // this RUNTIME step we are about to add should therefore be last as well *at the time
            // we register it*. However, other RUNTIME steps can themselves add new RUNTIME steps that
            // will then come after this one. So we do the same -- add a RUNTIME step that adds another
            // RUNTIME step that does the real work.
            // Theoretically this kind of "predecessor runtime step adds another runtime step, so we have to
            // add one to come later" business could go on forever. But any operation that does that with
            // a DUP-add step is just broken and should just find another way.

            context.addStep(new OperationStepHandler() {
                public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {

                    context.addStep(new FinalRuntimeStepHandler(), OperationContext.Stage.RUNTIME);

                    context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
                }
            }, OperationContext.Stage.RUNTIME);
        }
        context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        //Since this instance should have EntryType.PRIVATE, there is no need for a description
        return new ModelNode();
    }

    private class FinalRuntimeStepHandler implements OperationStepHandler {

        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {

            final EnumMap<Phase, List<RegisteredDeploymentUnitProcessor>> finalDeployers = new EnumMap<Phase, List<RegisteredDeploymentUnitProcessor>>(Phase.class);
            final List<RegisteredDeploymentUnitProcessor> processorList = new ArrayList<RegisteredDeploymentUnitProcessor>(256);
            for (Phase phase : Phase.values()) {
                processorList.clear();
                final Set<RegisteredDeploymentUnitProcessor> processorSet = deployerMap.get(phase);
                for (RegisteredDeploymentUnitProcessor processor : processorSet) {
                    processorList.add(processor);
                }
                finalDeployers.put(phase, new ArrayList<RegisteredDeploymentUnitProcessor>(processorList));
            }
            final ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
            DeployerChainsService.addService(context.getServiceTarget(), finalDeployers, verificationHandler);

            context.addStep(verificationHandler, OperationContext.Stage.VERIFY);

            context.completeStep(new OperationContext.RollbackHandler() {
                @Override
                public void handleRollback(OperationContext context, ModelNode operation) {
                    context.removeService(Services.JBOSS_DEPLOYMENT_CHAINS);
                }
            });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8098.java