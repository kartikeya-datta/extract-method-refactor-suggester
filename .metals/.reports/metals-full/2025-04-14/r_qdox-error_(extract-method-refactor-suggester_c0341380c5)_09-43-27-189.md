error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7344.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7344.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7344.java
text:
```scala
protected v@@oid initModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {

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

package org.jboss.as.controller.test;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ResourceBuilder;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.TestModelControllerService;
import org.jboss.as.controller.descriptions.NonResolvingResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.persistence.AbstractConfigurationPersister;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ConfigurationPersister;
import org.jboss.as.controller.persistence.ModelMarshallingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.staxmapper.XMLElementWriter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

/**
 * @author Emanuel Muckenhuber
 */
public abstract class AbstractControllerTestBase {


    protected abstract void initModel(final Resource rootResource, final ManagementResourceRegistration registration);

    private ServiceContainer container;
    private ModelController controller;
    protected final ProcessType processType;

    protected AbstractControllerTestBase(ProcessType processType) {
        this.processType = processType;
    }

    protected AbstractControllerTestBase() {
        this(ProcessType.EMBEDDED_SERVER);
    }

    public ModelController getController() {
        return controller;
    }

    public ServiceContainer getContainer() {
        return container;
    }

    protected ModelNode createOperation(String operationName, String... address) {
        ModelNode operation = new ModelNode();
        operation.get(OP).set(operationName);
        if (address.length > 0) {
            for (String addr : address) {
                operation.get(OP_ADDR).add(addr);
            }
        } else {
            operation.get(OP_ADDR).setEmptyList();
        }

        return operation;
    }

    public ModelNode executeForResult(ModelNode operation) throws OperationFailedException {
        return executeCheckNoFailure(operation).get(RESULT);
    }

    public void executeForFailure(ModelNode operation) throws OperationFailedException {
        try {
            executeForResult(operation);
            Assert.fail("Should have given error");
        } catch (OperationFailedException expected) {
            // ignore
        }
    }

    public ModelNode executeCheckNoFailure(ModelNode operation) throws OperationFailedException {
        ModelNode rsp = getController().execute(operation, null, null, null);
        if (FAILED.equals(rsp.get(OUTCOME).asString())) {
            throw new OperationFailedException(rsp.get(FAILURE_DESCRIPTION));
        }
        return rsp;
    }

    @Before
    public void setupController() throws InterruptedException {
        container = ServiceContainer.Factory.create("test");
        ServiceTarget target = container.subTarget();
        ModelControllerService svc = createModelControllerService(processType);
        ServiceBuilder<ModelController> builder = target.addService(ServiceName.of("ModelController"), svc);
        builder.install();
        svc.awaitStartup(30, TimeUnit.SECONDS);
        controller = svc.getValue();
        ModelNode setup = Util.getEmptyOperation("setup", new ModelNode());
        controller.execute(setup, null, null, null);
    }

    @After
    public void shutdownServiceContainer() {
        if (container != null) {
            container.shutdown();
            try {
                container.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                container = null;
            }
        }
    }

    protected ModelControllerService createModelControllerService(ProcessType processType) {
        return new ModelControllerService(processType);
    }

    protected void addBootOperations(List<ModelNode> bootOperations) {

    }

    public class ModelControllerService extends TestModelControllerService {

        public ModelControllerService(final ProcessType processType) {
            super(processType, new EmptyConfigurationPersister(), new ControlledProcessState(true),
                    ResourceBuilder.Factory.create(PathElement.pathElement("root"), new NonResolvingResourceDescriptionResolver()).build()
            );
        }

        public ModelControllerService(final ProcessType processType, ResourceDefinition resourceDefinition){
            super(processType, new EmptyConfigurationPersister(), new ControlledProcessState(true), resourceDefinition);
        }

        @Override
        protected boolean boot(List<ModelNode> bootOperations, boolean rollbackOnRuntimeFailure)
                throws ConfigurationPersistenceException {
            addBootOperations(bootOperations);
            return super.boot(bootOperations, rollbackOnRuntimeFailure);
        }

        protected void initModel(Resource rootResource, ManagementResourceRegistration rootRegistration, Resource modelControllerResource) {
            try {
                AbstractControllerTestBase.this.initModel(rootResource, rootRegistration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class EmptyConfigurationPersister extends AbstractConfigurationPersister {

        public EmptyConfigurationPersister() {
            super(null);
        }

        public EmptyConfigurationPersister(XMLElementWriter<ModelMarshallingContext> rootDeparser) {
            super(rootDeparser);
        }

        @Override
        public PersistenceResource store(final ModelNode model, Set<PathAddress> affectedAddresses) {
            return NullPersistenceResource.INSTANCE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<ModelNode> load() {
            return new ArrayList<ModelNode>();
        }

        private static class NullPersistenceResource implements ConfigurationPersister.PersistenceResource {

            private static final NullPersistenceResource INSTANCE = new NullPersistenceResource();

            @Override
            public void commit() {
            }

            @Override
            public void rollback() {
            }
        }
    }

    static void createModel(final OperationContext context, final ModelNode node) {
        createModel(context, PathAddress.EMPTY_ADDRESS, node);
    }

    static void createModel(final OperationContext context, final PathAddress base, final ModelNode node) {
        if (!node.isDefined()) {
            return;
        }
        final ManagementResourceRegistration registration = context.getResourceRegistrationForUpdate();
        final Set<String> children = registration.getChildNames(base);
        final ModelNode current = new ModelNode();
        final Resource resource = base.size() == 0 ? context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS) : context.createResource(base);
        if (node.getType() == ModelType.OBJECT) {
            for (final String key : node.keys()) {
                if (!children.contains(key)) {
                    current.get(key).set(node.get(key));
                }
            }
            resource.getModel().set(current);
        } else {
            resource.getModel().set(node);
            return;
        }
        if (children != null && !children.isEmpty()) {
            for (final String childType : children) {
                if (node.hasDefined(childType)) {
                    for (final String key : node.get(childType).keys()) {
                        createModel(context, base.append(PathElement.pathElement(childType, key)), node.get(childType, key));
                    }
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7344.java