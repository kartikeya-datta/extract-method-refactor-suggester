error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14259.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14259.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14259.java
text:
```scala
final S@@tringConfigurationPersister persister = new StringConfigurationPersister(Collections.<ModelNode>emptyList(), new StandaloneXml(null, null));

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

package org.jboss.as.server.test;

import junit.framework.Assert;
import org.jboss.as.controller.AbstractControllerService;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.InterfaceDescription;
import org.jboss.as.controller.parsing.StandaloneXml;
import org.jboss.as.controller.persistence.AbstractConfigurationPersister;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ModelMarshallingContext;

import org.jboss.as.controller.registry.ManagementResourceRegistration;

import org.jboss.as.controller.registry.Resource;

import org.jboss.as.server.ServerControllerModelUtil;
import org.jboss.as.server.ServerEnvironment;

import org.jboss.as.server.Services;
import org.jboss.as.server.controller.descriptions.ServerDescriptionProviders;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.staxmapper.XMLElementWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Basic server controller unit test.
 *
 * @author Emanuel Muckenhuber
 */
public class ServerControllerUnitTestCase {

    private final ServiceContainer container = ServiceContainer.Factory.create();
    private ModelController controller;

    @Before
    public void beforeClass() throws Exception {
        final ServiceTarget target = container.subTarget();
        final StringConfigurationPersister persister = new StringConfigurationPersister(Collections.<ModelNode>emptyList(), new StandaloneXml(null));
        final ControlledProcessState processState = new ControlledProcessState(true);
        final ModelControllerService svc = new ModelControllerService(OperationContext.Type.MANAGEMENT, processState, persister);
        final ServiceBuilder<ModelController> builder = target.addService(Services.JBOSS_SERVER_CONTROLLER, svc);
        builder.install();

        svc.latch.await();
        this.controller = svc.getValue();

    }

    @After
    public void afterClass() {
        container.shutdown();
    }

    @Test
    public void testInterfacesAlternatives() throws IOException {
        final ModelControllerClient client = controller.createClient(Executors.newCachedThreadPool());
        final ModelNode base = new ModelNode();
        base.get(ModelDescriptionConstants.OP).set("add");
        base.get(ModelDescriptionConstants.OP_ADDR).add("interface", "alternative");
        {
            final ModelNode operation = base.clone();
            operation.get("any-address").set(true);
            populateCritieria(operation, false);
            executeForFailure(client, operation);
        }
        {
            final ModelNode operation = base.clone();
            operation.get("any-ipv4-address").set(true);
            populateCritieria(operation, false);
            executeForFailure(client, operation);
        }
        {
            final ModelNode operation = base.clone();
            operation.get("any-ipv6-address").set(true);
            populateCritieria(operation, false);
            executeForFailure(client, operation);
        }
        {
            final ModelNode operation = base.clone();
            operation.get("any-ipv6-address").set(true);
            operation.get("any-ipv4-address").set(true);
            executeForFailure(client, operation);
        }
        {
            final ModelNode operation = base.clone();
            operation.get("any-address").set(true);
            operation.get("any-ipv4-address").set(true);
            executeForFailure(client, operation);
        }
        {
            final ModelNode operation = base.clone();
            // operation.get("any-address").set(true);
            populateCritieria(operation, false);
            executeForResult(client, operation);
        }
    }

    @Test
    public void testUpdateInterface() throws IOException {
        final ModelControllerClient client = controller.createClient(Executors.newCachedThreadPool());
        final ModelNode address = new ModelNode();
        address.add("interface", "other");
        {
            final ModelNode operation = new ModelNode();
            operation.get(ModelDescriptionConstants.OP).set("add");
            operation.get(ModelDescriptionConstants.OP_ADDR).set(address);
            operation.get("any-address").set(true);

            executeForResult(client, operation);
            final ModelNode resource = readResource(client, operation.get(ModelDescriptionConstants.OP_ADDR));
            Assert.assertTrue(resource.get("any-address").asBoolean());
        }
        {
            final ModelNode composite = new ModelNode();
            composite.get(ModelDescriptionConstants.OP).set("composite");
            composite.get(ModelDescriptionConstants.OP_ADDR).setEmptyList();
            final ModelNode one = composite.get(ModelDescriptionConstants.STEPS).add();
            one.get(ModelDescriptionConstants.OP).set("write-attribute");
            one.get(ModelDescriptionConstants.OP_ADDR).set(address);
            one.get(ModelDescriptionConstants.NAME).set("any-address");
            one.get(ModelDescriptionConstants.VALUE);

            final ModelNode two = composite.get(ModelDescriptionConstants.STEPS).add();
            two.get(ModelDescriptionConstants.OP).set("write-attribute");
            two.get(ModelDescriptionConstants.OP_ADDR).set(address);
            two.get(ModelDescriptionConstants.NAME).set("inet-address");
            two.get(ModelDescriptionConstants.VALUE).set("127.0.0.1");

            executeForResult(client, composite);
            final ModelNode resource = readResource(client, address);
            Assert.assertFalse(resource.hasDefined("any-address"));
            Assert.assertEquals("127.0.0.1", resource.get("inet-address").asString());
        }
    }

    @Test
    public void testComplexInterface() throws IOException {

        final ModelNode operation = new ModelNode();
        operation.get(ModelDescriptionConstants.OP).set("add");
        operation.get(ModelDescriptionConstants.OP_ADDR).add("interface", "complex");
        // This won't be resolvable with the runtime layer enabled
        populateCritieria(operation, false);
        populateCritieria(operation.get("not"), true);
        populateCritieria(operation.get("any"), true);

        final ModelControllerClient client = controller.createClient(Executors.newCachedThreadPool());

        executeForResult(client, operation);
    }

    protected void populateCritieria(final ModelNode model, final boolean nested) {
        for(final AttributeDefinition def : InterfaceDescription.NESTED_ATTRIBUTES) {
            if(def.getType() == ModelType.BOOLEAN) {
                model.get(def.getName()).set(true);
            } else {
                if(nested) {
                    model.get(def.getName()).add("127.0.0.1");
                } else {
                    model.get(def.getName()).set("127.0.0.1");
                }
            }
        }
    }

    private static class ModelControllerService extends AbstractControllerService {

        final CountDownLatch latch = new CountDownLatch(1);
        final StringConfigurationPersister persister;
        final ControlledProcessState processState;
        volatile ManagementResourceRegistration rootRegistration;
        volatile Exception error;

        ModelControllerService(final OperationContext.Type type, final ControlledProcessState processState, final StringConfigurationPersister persister) {
            super(type, persister, processState, ServerDescriptionProviders.ROOT_PROVIDER, null);
            this.persister = persister;
            this.processState = processState;
        }

        @Override
        protected void initModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
            this.rootRegistration = rootRegistration;
            Properties properties = new Properties();
            properties.put("jboss.home.dir", ".");
            final ServerEnvironment environment = new ServerEnvironment(properties, new HashMap<String, String>(), null, ServerEnvironment.LaunchType.DOMAIN);
            ServerControllerModelUtil.initOperations(rootRegistration, null, persister, environment, processState);
        }

        @Override
        protected void boot(List<ModelNode> bootOperations) throws ConfigurationPersistenceException {
            try {
                super.boot(persister.bootOperations);
            } catch (Exception e) {
                error = e;
            } catch (Throwable t) {
                error = new Exception(t);
            } finally {
                latch.countDown();
            }
        }

        @Override
        public void start(StartContext context) throws StartException {
            super.start(context);
        }
    }

    static class StringConfigurationPersister extends AbstractConfigurationPersister {

        private final List<ModelNode> bootOperations;
        volatile String marshalled;

        public StringConfigurationPersister(List<ModelNode> bootOperations, XMLElementWriter<ModelMarshallingContext> rootDeparser) {
            super(rootDeparser);
            this.bootOperations = bootOperations;
        }

        @Override
        public PersistenceResource store(ModelNode model, Set<PathAddress> affectedAddresses)
                throws ConfigurationPersistenceException {
            return new StringPersistenceResource(model, this);
        }

        @Override
        public List<ModelNode> load() throws ConfigurationPersistenceException {
            return bootOperations;
        }

        private class StringPersistenceResource implements PersistenceResource {

            private byte[] bytes;
            private final AbstractConfigurationPersister persister;

            StringPersistenceResource(final ModelNode model, final AbstractConfigurationPersister persister) throws ConfigurationPersistenceException {
                this.persister = persister;
                ByteArrayOutputStream output = new ByteArrayOutputStream(1024 * 8);
                try {
                    try {
                        persister.marshallAsXml(model, output);
                    } finally {
                        try {
                            output.close();
                        } catch (Exception ignore) {
                        }
                        bytes = output.toByteArray();
                    }
                } catch (Exception e) {
                    throw new ConfigurationPersistenceException("Failed to marshal configuration", e);
                }
            }

            @Override
            public void commit() {
                StringConfigurationPersister.this.marshalled = new String(bytes);
            }

            @Override
            public void rollback() {
                marshalled = null;
            }
        }
    }

    static ModelNode readResource(final ModelControllerClient client, final ModelNode address) {
        final ModelNode operation = new ModelNode();
        operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.READ_RESOURCE_OPERATION);
        operation.get(ModelDescriptionConstants.OP_ADDR).set(address);
        return executeForResult(client, operation);
    }

    static void executeForFailure(final ModelControllerClient client, final ModelNode operation) {
        try {
            final ModelNode result = client.execute(operation);
            if (! result.hasDefined("outcome") && ! ModelDescriptionConstants.FAILED.equals(result.get("outcome").asString())) {
                Assert.fail("Operation outcome is " + result.get("outcome").asString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static ModelNode executeForResult(final ModelControllerClient client, final ModelNode operation) {
        try {
            final ModelNode result = client.execute(operation);
            if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
                return result.get("result");
            } else {
                Assert.fail("Operation outcome is " + result.get("outcome").asString());
                throw new RuntimeException(); // not reached
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14259.java