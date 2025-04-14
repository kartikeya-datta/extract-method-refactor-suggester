error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6033.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6033.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6033.java
text:
```scala
m@@oduleDependencyProcessor.setGlobalModules(GlobalModulesDefinition.createModuleList(context, newValue));

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

package org.jboss.as.ee.subsystem;

import org.jboss.as.controller.AbstractWriteAttributeHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.ee.component.deployers.DefaultEarSubDeploymentsIsolationProcessor;
import org.jboss.as.ee.structure.DescriptorPropertyReplacementProcessor;
import org.jboss.as.ee.structure.GlobalModuleDependencyProcessor;
import org.jboss.dmr.ModelNode;

/**
 * Handles the "write-attribute" operation for the EE subsystem.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class EeWriteAttributeHandler extends AbstractWriteAttributeHandler<Void> {

    private final DefaultEarSubDeploymentsIsolationProcessor isolationProcessor;
    private final GlobalModuleDependencyProcessor moduleDependencyProcessor;
    private final DescriptorPropertyReplacementProcessor specDescriptorPropertyReplacementProcessor;
    private final DescriptorPropertyReplacementProcessor jbossDescriptorPropertyReplacementProcessor;

    public EeWriteAttributeHandler(final DefaultEarSubDeploymentsIsolationProcessor isolationProcessor,
                                   final GlobalModuleDependencyProcessor moduleDependencyProcessor,
                                   final DescriptorPropertyReplacementProcessor specDescriptorPropertyReplacementProcessor,
                                   final DescriptorPropertyReplacementProcessor jbossDescriptorPropertyReplacementProcessor) {
        super(EeSubsystemRootResource.ATTRIBUTES);
        this.isolationProcessor = isolationProcessor;
        this.moduleDependencyProcessor = moduleDependencyProcessor;
        this.specDescriptorPropertyReplacementProcessor = specDescriptorPropertyReplacementProcessor;
        this.jbossDescriptorPropertyReplacementProcessor = jbossDescriptorPropertyReplacementProcessor;
    }

    public void registerAttributes(final ManagementResourceRegistration registry) {
        for (AttributeDefinition ad : EeSubsystemRootResource.ATTRIBUTES) {
            registry.registerReadWriteAttribute(ad, null, this);
        }
    }

    @Override
    protected boolean applyUpdateToRuntime(OperationContext context, ModelNode operation, String attributeName,
                                           ModelNode newValue, ModelNode currentValue, HandbackHolder<Void> handbackHolder) throws OperationFailedException {

        applyUpdateToDeploymentUnitProcessor(context, newValue, attributeName);

        return false;
    }

    @Override
    protected void revertUpdateToRuntime(OperationContext context, ModelNode operation, String attributeName,
                                         ModelNode valueToRestore, ModelNode valueToRevert, Void handback) throws OperationFailedException {
        applyUpdateToDeploymentUnitProcessor(context, valueToRestore, attributeName);
    }

    private void applyUpdateToDeploymentUnitProcessor(final OperationContext context, ModelNode newValue, String attributeName) throws OperationFailedException {
        if (GlobalModulesDefinition.INSTANCE.getName().equals(attributeName)) {
            moduleDependencyProcessor.setGlobalModules(newValue);
        } else if (EeSubsystemRootResource.EAR_SUBDEPLOYMENTS_ISOLATED.getName().equals(attributeName)) {
            boolean isolate = newValue.asBoolean();
            isolationProcessor.setEarSubDeploymentsIsolated(isolate);
        } else if (EeSubsystemRootResource.SPEC_DESCRIPTOR_PROPERTY_REPLACEMENT.getName().equals(attributeName)) {
            boolean enabled = newValue.asBoolean();
            specDescriptorPropertyReplacementProcessor.setDescriptorPropertyReplacement(enabled);
        } else if (EeSubsystemRootResource.JBOSS_DESCRIPTOR_PROPERTY_REPLACEMENT.getName().equals(attributeName)) {
            boolean enabled = newValue.asBoolean();
            jbossDescriptorPropertyReplacementProcessor.setDescriptorPropertyReplacement(enabled);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6033.java