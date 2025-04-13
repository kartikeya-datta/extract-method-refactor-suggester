error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3009.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3009.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3009.java
text:
```scala
i@@f(resource == null || registration.isRemote() || registration.isRuntimeOnly() || resource.isProxy() || resource.isRuntime()) {

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

package org.jboss.as.controller.operations.common;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import org.jboss.as.controller.descriptions.common.CommonDescriptions;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;

import java.util.Locale;
import java.util.Set;

/**
 * A generic handler recursively creating add operations for a managed resource using it's
 * attributes as the request-parameters.
 *
 * @author Emanuel Muckenhuber
 */
public class GenericSubsystemDescribeHandler implements OperationStepHandler, DescriptionProvider {

    public static final GenericSubsystemDescribeHandler INSTANCE = new GenericSubsystemDescribeHandler();

    protected GenericSubsystemDescribeHandler() {
        //
    }

    @Override
    public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {
        final ModelNode address = PathAddress.pathAddress(PathAddress.pathAddress(operation.require(OP_ADDR)).getLastElement()).toModelNode();
        final Resource resource = context.readResource(PathAddress.EMPTY_ADDRESS);
        final ModelNode result = context.getResult();
        describe(resource, address, result, context.getResourceRegistration());
        context.completeStep();
    }

    protected void describe(final Resource resource, final ModelNode address, ModelNode result, final ImmutableManagementResourceRegistration registration) {
        if(registration.isRemote() || registration.isRuntimeOnly() || resource.isProxy() || resource.isRuntime()) {
            return;
        }
        final Set<PathElement> children = registration.getChildAddresses(PathAddress.EMPTY_ADDRESS);
        result.add(createAddOperation(address, resource.getModel(), children));
        for(final PathElement element : children) {
            if(element.isMultiTarget()) {
                final String childType = element.getKey();
                for(final Resource.ResourceEntry entry : resource.getChildren(childType)) {
                    final ImmutableManagementResourceRegistration childRegistration = registration.getSubModel(PathAddress.pathAddress(PathElement.pathElement(childType, entry.getName())));
                    final ModelNode childAddress = address.clone();
                    childAddress.add(childType, entry.getName());
                    describe(entry, childAddress, result, childRegistration);
                }
            } else {
                final Resource child = resource.getChild(element);
                final ImmutableManagementResourceRegistration childRegistration = registration.getSubModel(PathAddress.pathAddress(element));
                final ModelNode childAddress = address.clone();
                childAddress.add(element.getKey(), element.getValue());
                describe(child, childAddress, result, childRegistration);
            }
        }
    }

    protected ModelNode createAddOperation(final ModelNode address, final ModelNode subModel, final Set<PathElement> children) {
        final ModelNode operation = subModel.clone();
        operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.ADD);
        operation.get(ModelDescriptionConstants.OP_ADDR).set(address);
        if(children != null && ! children.isEmpty()) {
            for(final PathElement path : children) {
                if(subModel.hasDefined(path.getKey())) {
                    subModel.remove(path.getKey());
                }
            }
        }
        return operation;
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        // This is a private operation, so we should not be getting requests for descriptions
        return CommonDescriptions.getSubsystemDescribeOperation(locale);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3009.java