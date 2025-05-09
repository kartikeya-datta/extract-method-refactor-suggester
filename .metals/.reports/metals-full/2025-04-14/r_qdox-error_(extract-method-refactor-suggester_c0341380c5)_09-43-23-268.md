error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11175.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11175.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11175.java
text:
```scala
A@@ssert.assertTrue(stepResult.getType() == ModelType.EXPRESSION || stepResult.asInt() >= 0);

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

package org.jboss.as.test.smoke.mgmt;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ANY_ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ANY_IPV4_ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ANY_IPV6_ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.COMPOSITE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INCLUDE_RUNTIME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PORT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE_DEPTH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.STEPS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ContainerResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Basic management operation unit test.
 *
 * @author Emanuel Muckenhuber
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BasicOperationsUnitTestCase {

    @ContainerResource
    private ManagementClient managementClient;

    @Test
    public void testSocketBindingsWildcards() throws IOException {

        final ModelNode address = new ModelNode();
        address.add("socket-binding-group", "*");
        address.add("socket-binding", "*");
        address.protect();

        final ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_RESOURCE_OPERATION);
        operation.get(OP_ADDR).set(address);

        final ModelNode result = managementClient.getControllerClient().execute(operation);
        Assert.assertTrue(result.hasDefined(RESULT));
        Assert.assertEquals(SUCCESS, result.get(OUTCOME).asString());
        final Collection<ModelNode> steps = getSteps(result.get(RESULT));
        Assert.assertFalse(steps.isEmpty());
        for(final ModelNode step : steps) {
            Assert.assertTrue(step.hasDefined(OP_ADDR));
            Assert.assertTrue(step.hasDefined(RESULT));
            Assert.assertEquals(SUCCESS, step.get(OUTCOME).asString());
        }
    }

    @Test
    public void testReadResourceRecursiveDepth() throws IOException {
        final ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_RESOURCE_OPERATION);
        operation.get(OP_ADDR).setEmptyList();
        operation.get(RECURSIVE_DEPTH).set(1);

        final ModelNode result = managementClient.getControllerClient().execute(operation);
        Assert.assertEquals(SUCCESS, result.get(OUTCOME).asString());
        Assert.assertTrue(result.hasDefined(RESULT));

        final ModelNode web = result.get(RESULT, SUBSYSTEM, "io");
        Assert.assertTrue(web.hasDefined("worker"));
    }

    @Test
    public void testReadAttributeWildcards() throws IOException {

        final ModelNode address = new ModelNode();
        address.add("socket-binding-group", "*");
        address.add("socket-binding", "*");
        address.protect();

        final ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
        operation.get(OP_ADDR).set(address);
        operation.get(NAME).set(PORT);

        final ModelNode result = managementClient.getControllerClient().execute(operation);
        Assert.assertTrue(result.hasDefined(RESULT));
        Assert.assertEquals(SUCCESS, result.get(OUTCOME).asString());
        final Collection<ModelNode> steps = getSteps(result.get(RESULT));
        Assert.assertFalse(steps.isEmpty());
        for(final ModelNode step : steps) {
            Assert.assertTrue(step.hasDefined(OP_ADDR));
            Assert.assertTrue(step.hasDefined(RESULT));
            final ModelNode stepResult = step.get(RESULT);
            Assert.assertTrue(stepResult.getType() == ModelType.EXPRESSION || stepResult.asInt() > 0);
        }
    }

    @Test
    public void testSocketBindingDescriptions() throws IOException {

        final ModelNode address = new ModelNode();
        address.add("socket-binding-group", "*");
        address.add("socket-binding", "*");
        address.protect();

        final ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_RESOURCE_DESCRIPTION_OPERATION);
        operation.get(OP_ADDR).set(address);

        final ModelNode result = managementClient.getControllerClient().execute(operation);
        Assert.assertTrue(result.hasDefined(RESULT));
        Assert.assertEquals(SUCCESS, result.get(OUTCOME).asString());
        final Collection<ModelNode> steps = result.get(RESULT).asList();
        Assert.assertFalse(steps.isEmpty());
        Assert.assertEquals("should only contain a single type", 1, steps.size());
        for(final ModelNode step : steps) {
            Assert.assertTrue(step.hasDefined(OP_ADDR));
            Assert.assertTrue(step.hasDefined(RESULT));
            Assert.assertEquals(SUCCESS, step.get(OUTCOME).asString());
            final ModelNode stepResult = step.get(RESULT);
            Assert.assertTrue(stepResult.hasDefined(DESCRIPTION));
            Assert.assertTrue(stepResult.hasDefined(ATTRIBUTES));
            Assert.assertTrue(stepResult.get(ModelDescriptionConstants.ATTRIBUTES).hasDefined(ModelDescriptionConstants.NAME));
            Assert.assertTrue(stepResult.get(ModelDescriptionConstants.ATTRIBUTES).hasDefined(ModelDescriptionConstants.INTERFACE));
            Assert.assertTrue(stepResult.get(ModelDescriptionConstants.ATTRIBUTES).hasDefined(ModelDescriptionConstants.PORT));
        }
    }

    @Test
    public void testRecursiveReadIncludingRuntime() throws IOException {
        final ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_RESOURCE_OPERATION);
        operation.get(OP_ADDR).setEmptyList();
        operation.get(RECURSIVE).set(true);
        operation.get(INCLUDE_RUNTIME).set(true);

        final ModelNode result = managementClient.getControllerClient().execute(operation);
        Assert.assertEquals(result.get(FAILURE_DESCRIPTION).isDefined() ? result.get(FAILURE_DESCRIPTION).asString() : "", SUCCESS, result.get(OUTCOME).asString());
        Assert.assertTrue(result.hasDefined(RESULT));
    }

    @Test
    public void testHttpSocketBinding() throws IOException {
        final ModelNode address = new ModelNode();
        address.add("socket-binding-group", "*");
        address.add("socket-binding", "http");
        address.protect();

        final ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_RESOURCE_OPERATION);
        operation.get(OP_ADDR).set(address);

        final ModelNode result = managementClient.getControllerClient().execute(operation);
        Assert.assertTrue(result.hasDefined(RESULT));
        Assert.assertEquals(SUCCESS, result.get(OUTCOME).asString());
        final List<ModelNode> steps = getSteps(result.get(RESULT));
        Assert.assertEquals(1, steps.size());
        final ModelNode httpBinding = steps.get(0);
        Assert.assertEquals(8080, httpBinding.get(RESULT, "port").resolve().asInt());

    }

    @Test
    public void testSimpleReadAttribute() throws IOException {
        final ModelNode address = new ModelNode();
        address.add("subsystem", "deployment-scanner");
        address.add("scanner", "default");

        final ModelNode operation = createReadAttributeOperation(address, "path");
        final ModelNode result = managementClient.getControllerClient().execute(operation);
        assertSuccessful(result);

        Assert.assertEquals("deployments", result.get(RESULT).asString());
    }

    @Test
    @Ignore //TODO UNDERTOW reneable when we expose metrics from undertow
    public void testMetricReadAttribute() throws IOException {
        final ModelNode address = new ModelNode();
        address.add("subsystem", "undertow");
        address.add("connector", "http");

        final ModelNode operation = createReadAttributeOperation(address, "bytesReceived");
        final ModelNode result = managementClient.getControllerClient().execute(operation);
        assertSuccessful(result);
        Assert.assertTrue(result.asInt() >= 0);
    }

    @Test
    public void testReadAttributeChild() throws IOException {
        final ModelNode address = new ModelNode();
        address.add("subsystem", "deployment-scanner");

        final ModelNode operation = createReadAttributeOperation(address, "scanner");
        final ModelNode result = managementClient.getControllerClient().execute(operation);
        Assert.assertEquals(FAILED, result.get(OUTCOME).asString());
    }

    @Test
    public void testInterfaceAdd() throws IOException {

        final ModelNode base = new ModelNode();
        base.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        base.get(OP_ADDR).add(INTERFACE, "test");
        base.protect();

        final ModelNode add = base.clone();
        add.get(OP).set(ADD);
        add.get(ANY_ADDRESS).set(true);
        // Add interface
        execute(add);

        final ModelNode any = base.clone();
        any.get(NAME).set(ANY_ADDRESS);
        any.get(VALUE).set(false);

        final ModelNode any6 = base.clone();
        any6.get(NAME).set(ANY_IPV6_ADDRESS) ;
        any6.get(VALUE).set(false);

        final ModelNode any4 = base.clone();
        any4.get(NAME).set(ANY_IPV4_ADDRESS);
        any4.get(VALUE).set(true);

        final ModelNode composite = new ModelNode();
        composite.get(OP).set(COMPOSITE);
        composite.get(OP_ADDR).setEmptyList();

        composite.get(STEPS).add(any);
        composite.get(STEPS).add(any6);
        composite.get(STEPS).add(any4);

        execute(composite);

        // Remove interface
        final ModelNode remove = base.clone();
        remove.get(OP).set(REMOVE);
        execute(remove);
    }

    protected ModelNode execute(final ModelNode operation) throws IOException {
        final ModelNode result = managementClient.getControllerClient().execute(operation);
        Assert.assertEquals(result.toString(), SUCCESS, result.get(OUTCOME).asString());
        return result;
    }

    static void assertSuccessful(final ModelNode result) {
        Assert.assertEquals(SUCCESS, result.get(OUTCOME).asString());
        Assert.assertTrue(result.hasDefined(RESULT));
    }

    static ModelNode createReadAttributeOperation(final ModelNode address, final String attributeName) {
        final ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
        operation.get(OP_ADDR).set(address);
        operation.get(NAME).set(attributeName);
        return operation;
    }

    protected static List<ModelNode> getSteps(final ModelNode result) {
        Assert.assertTrue(result.isDefined());
        return result.asList();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11175.java