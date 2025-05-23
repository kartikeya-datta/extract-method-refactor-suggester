error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12402.java
text:
```scala
static final S@@impleAttributeDefinition WORKER_TASK_MAX_THREADS = createIntAttribute(CommonAttributes.WORKER_TASK_MAX_THREADS, Attribute.WORKER_TASK_MAX_THREADS, 8);

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.remoting;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.RestartParentWriteAttributeHandler;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.validation.IntRangeValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.msc.service.ServiceName;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class RemotingSubsystemRootResource extends SimpleResourceDefinition {

    //The defaults for these come from XnioWorker
    static final SimpleAttributeDefinition WORKER_READ_THREADS = createIntAttribute(CommonAttributes.WORKER_READ_THREADS, Attribute.WORKER_READ_THREADS, 1);
    static final SimpleAttributeDefinition WORKER_TASK_CORE_THREADS = createIntAttribute(CommonAttributes.WORKER_TASK_CORE_THREADS, Attribute.WORKER_TASK_CORE_THREADS, 4);
    static final SimpleAttributeDefinition WORKER_TASK_KEEPALIVE = createIntAttribute(CommonAttributes.WORKER_TASK_KEEPALIVE, Attribute.WORKER_TASK_KEEPALIVE, 60);
    static final SimpleAttributeDefinition WORKER_TASK_LIMIT = createIntAttribute(CommonAttributes.WORKER_TASK_LIMIT, Attribute.WORKER_TASK_LIMIT, 0x4000);
    static final SimpleAttributeDefinition WORKER_TASK_MAX_THREADS = createIntAttribute(CommonAttributes.WORKER_TASK_MAX_THREADS, Attribute.WORKER_TASK_MAX_THREADS, 16);
    static final SimpleAttributeDefinition WORKER_WRITE_THREADS = createIntAttribute(CommonAttributes.WORKER_WRITE_THREADS, Attribute.WORKER_WRITE_THREADS, 1);

    static final PathElement PATH = PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, RemotingExtension.SUBSYSTEM_NAME);

    static AttributeDefinition[] ATTRIBUTES = new AttributeDefinition[]{
            WORKER_READ_THREADS,
            WORKER_TASK_CORE_THREADS,
            WORKER_TASK_KEEPALIVE,
            WORKER_TASK_LIMIT,
            WORKER_TASK_MAX_THREADS,
            WORKER_WRITE_THREADS
    };

    private final ProcessType processType;

    public RemotingSubsystemRootResource(final ProcessType processType) {
        super(PATH,
                RemotingExtension.getResourceDescriptionResolver(RemotingExtension.SUBSYSTEM_NAME),
                processType.isServer() ? RemotingSubsystemAdd.SERVER : RemotingSubsystemAdd.DOMAIN,
                RemotingSubsystemRemove.INSTANCE);
        this.processType = processType;
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        for (final AttributeDefinition attribute : ATTRIBUTES) {
            registerReadWriteIntAttribute(resourceRegistration, attribute);
        }
    }

    private void registerReadWriteIntAttribute(ManagementResourceRegistration resourceRegistration, AttributeDefinition attr) {
        resourceRegistration.registerReadWriteAttribute(attr, null, new ThreadWriteAttributeHandler(attr, processType));
    }

    private static SimpleAttributeDefinition createIntAttribute(String name, Attribute attribute, int defaultValue) {
        return SimpleAttributeDefinitionBuilder.create(name, ModelType.INT, true)
                .setDefaultValue(new ModelNode().set(defaultValue))
                .setXmlName(attribute.getLocalName())
                .setValidator(new IntRangeValidator(1, true))
                .setAllowExpression(true)
                .build();
    }

    private static class ThreadWriteAttributeHandler extends RestartParentWriteAttributeHandler {
        private final ProcessType processType;

        ThreadWriteAttributeHandler(AttributeDefinition definition, ProcessType processType) {
            super(CommonAttributes.SUBSYSTEM, definition);
            this.processType = processType;
        }

        @Override
        protected void recreateParentService(OperationContext context, PathAddress parentAddress, ModelNode parentModel,
                                             ServiceVerificationHandler verificationHandler) throws OperationFailedException {
            RemotingSubsystemAdd addHandler = processType.isServer() ? RemotingSubsystemAdd.SERVER : RemotingSubsystemAdd.DOMAIN;
            addHandler.launchServices(context, parentModel, verificationHandler, null);
        }

        @Override
        protected ServiceName getParentServiceName(PathAddress parentAddress) {
            return RemotingServices.SUBSYSTEM_ENDPOINT;
        }

        @Override
        protected void removeServices(final OperationContext context, final ServiceName parentService, final ModelNode parentModel) throws OperationFailedException {
            super.removeServices(context, parentService, parentModel);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12402.java