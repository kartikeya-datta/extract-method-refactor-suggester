error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3856.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3856.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3856.java
text:
```scala
l@@og.debug("transforming subsystem: " + subsystemName + ", to model version: " + transformer.getMajorManagementVersion() + "." + transformer.getMinorManagementVersion());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.controller.transform;

import org.jboss.as.controller.ControllerLogger;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

/**
 * @author Emanuel Muckenhuber
 * @author Tomaz Cerar
 */
public class TransformersImpl implements Transformers {
    private static final Logger log = Logger.getLogger(TransformersImpl.class);
    private final TransformationTarget target;

    TransformersImpl(TransformationTarget target) {
        assert target != null;
        this.target = target;
    }

    @Override
    public TransformationTarget getTarget() {
        return target;
    }

    @Override
    public ModelNode transformOperation(final TransformationContext context, final ModelNode operation) {
        if (!target.isTransformationNeeded()) {
            return operation;
        }

        final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        final String operationName = operation.require(OP).asString();

        final OperationTransformer transformer = target.resolveTransformer(address, operationName);
        if (transformer == null) {
            ControllerLogger.ROOT_LOGGER.tracef("operation %s does not need transformation", operation);
            return operation;
        }
        return transformer.transformOperation(context, address, operation);
    }

    @Override
    public Resource transformResource(final TransformationContext context, Resource resource) {
        if (!target.isTransformationNeeded()) {
            return resource;
        }
        return resolveRecursive(resource, context.getResourceRegistration(PathAddress.EMPTY_ADDRESS), PathAddress.EMPTY_ADDRESS);
    }


    private Resource resolveRecursive(final Resource resource, final ImmutableManagementResourceRegistration registration, final PathAddress address) {
        boolean isSubsystem = address.size() > 0
                && !ModelDescriptionConstants.EXTENSION.equals(address.getElement(0).getKey())
                && ModelDescriptionConstants.SUBSYSTEM.equals(address.getLastElement().getKey());
        if (isSubsystem) {
            String subsystemName = address.getLastElement().getValue();
            SubsystemTransformer transformer = target.getSubsystemTransformer(subsystemName);
            if (transformer != null) {
                log.info("transforming subsystem: " + subsystemName + ", to model version: " + transformer.getMajorManagementVersion() + "." + transformer.getMinorManagementVersion());
                ResourceDefinition rd = TransformerRegistry.loadSubsystemDefinition(subsystemName, transformer.getMajorManagementVersion(), transformer.getMinorManagementVersion());
                ManagementResourceRegistration targetDefinition = ManagementResourceRegistration.Factory.create(rd);
                ModelNode fullSubsystemModel = Resource.Tools.readModel(resource);
                ModelNode transformed = transformer.transformModel(null, fullSubsystemModel);
                return TransformerRegistry.modelToResource(targetDefinition, transformed);
            }

            return resource;
        }
        for (PathElement element : registration.getChildAddresses(PathAddress.EMPTY_ADDRESS)) {
            if (element.isMultiTarget()) {
                final String childType = element.getKey();
                for (final Resource.ResourceEntry entry : resource.getChildren(childType)) {
                    final ImmutableManagementResourceRegistration childRegistration = registration.getSubModel(PathAddress.pathAddress(PathElement.pathElement(childType, entry.getName())));
                    Resource res = resolveRecursive(entry, childRegistration, address.append(entry.getPathElement()));
                    if (!res.equals(entry)) {
                        resource.removeChild(entry.getPathElement());
                        resource.registerChild(entry.getPathElement(), res);
                    }
                }
            } else {
                final Resource child = resource.getChild(element);
                final ImmutableManagementResourceRegistration childRegistration = registration.getSubModel(PathAddress.pathAddress(element));
                if (child != null) {
                    Resource res = resolveRecursive(child, childRegistration, address.append(element));
                    if (!res.equals(child)) {
                        resource.removeChild(element);
                        resource.registerChild(element, res);
                    }
                }
            }
        }
        return resource;
    }


    static class DelegateTransformContext implements TransformationContext {

        private final OperationContext context;

        DelegateTransformContext(OperationContext context) {
            this.context = context;
        }

        @Override
        public ProcessType getProcessType() {
            return context.getProcessType();
        }

        @Override
        public RunningMode getRunningMode() {
            return context.getRunningMode();
        }

        @Override
        public ImmutableManagementResourceRegistration getResourceRegistration(final PathAddress address) {
            return context.getResourceRegistration().getSubModel(address);
        }

        @Override
        public ImmutableManagementResourceRegistration getResourceRegistrationFromRoot(final PathAddress address) {
            return context.getRootResourceRegistration().getSubModel(address);
        }

        @Override
        public Resource readResource(PathAddress address) {
            return context.readResource(address);
        }

        @Override
        public Resource readResourceFromRoot(PathAddress address) {
            return context.readResourceFromRoot(address);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3856.java