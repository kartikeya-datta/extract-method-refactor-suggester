error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14273.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14273.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14273.java
text:
```scala
M@@odelDescriptionConstants.AUTHORIZATION), AccessAuthorizationResourceDefinition.createResource(null));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.as.core.model.test.access;

import static org.jboss.as.controller.PathAddress.pathAddress;
import static org.jboss.as.controller.PathElement.pathElement;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ACCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.AUTHORIZATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.BASE_ROLE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CORE_SERVICE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOSTS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST_SCOPED_ROLE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.access.constraint.ApplicationTypeConfig;
import org.jboss.as.controller.access.constraint.SensitivityClassification;
import org.jboss.as.controller.access.management.ApplicationTypeAccessConstraintDefinition;
import org.jboss.as.controller.access.management.SensitiveTargetAccessConstraintDefinition;
import org.jboss.as.controller.access.rbac.StandardRole;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.core.model.test.AbstractCoreModelTest;
import org.jboss.as.core.model.test.KernelServices;
import org.jboss.as.core.model.test.ModelInitializer;
import org.jboss.as.core.model.test.TestModelType;
import org.jboss.as.domain.management.access.AccessAuthorizationResourceDefinition;
import org.jboss.dmr.ModelNode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ladislav Thon <lthon@redhat.com>
 */
public class HostScopedRolesTestCase extends AbstractCoreModelTest {
    private static final String FOO = "foo";

    private static final String MONITOR = "Monitor"; // StandardRole.MONITOR
    private static final String OPERATOR = "Operator"; // StandardRole.OPERATOR

    private static final String SOME_HOST = "some-host";
    private static final String ANOTHER_HOST = "another-host";

    private KernelServices kernelServices;

    @Before
    public void setUp() throws Exception {
        // must initialize the classes, otherwise the kernel won't boot correctly
        new SensitiveTargetAccessConstraintDefinition(new SensitivityClassification("play", "security-realm", true, true, true));
        new ApplicationTypeAccessConstraintDefinition(new ApplicationTypeConfig("play", "deployment", false));

        kernelServices = createKernelServicesBuilder(TestModelType.DOMAIN)
                .setXmlResource("domain-all.xml")
                .validateDescription()
                .setModelInitializer(new ModelInitializer() {
                    @Override
                    public void populateModel(Resource rootResource) {
                        Resource management = Resource.Factory.create();
                        rootResource.registerChild(PathElement.pathElement(ModelDescriptionConstants.CORE_SERVICE,
                                ModelDescriptionConstants.MANAGEMENT), management);
                        management.registerChild(PathElement.pathElement(ModelDescriptionConstants.ACCESS,
                                ModelDescriptionConstants.AUTHORIZATION), AccessAuthorizationResourceDefinition.RESOURCE);
                    }
                }, null)
                .build();
    }

    @Test
    public void testReadHostScopedRole() {
        assertTrue(kernelServices.isSuccessfulBoot());

        // see domain-all.xml

        ModelNode operation = Util.createOperation(READ_RESOURCE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, "c")
        ));
        operation.get(RECURSIVE).set(true);
        ModelNode result = execute(operation);
        checkOutcome(result);
        result = result.get(RESULT);

        assertEquals("Deployer", result.get(BASE_ROLE).asString());
        assertEquals(2, result.get(HOSTS).asList().size());
        assertEquals("master", result.get(HOSTS).get(0).asString());
        assertEquals("slave", result.get(HOSTS).get(1).asString());

        operation = Util.createOperation(READ_RESOURCE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, "d")
        ));
        operation.get(RECURSIVE).set(true);
        result = execute(operation);
        checkOutcome(result);
        result = result.get(RESULT);

        assertEquals("Administrator", result.get(BASE_ROLE).asString());
        assertFalse(result.get(HOSTS).isDefined());
    }

    @Test
    public void testAddHostScopedRoleWithHosts() {
        assertTrue(kernelServices.isSuccessfulBoot());

        ModelNode operation = Util.createOperation(ADD, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(BASE_ROLE).set(MONITOR);
        operation.get(HOSTS).add(SOME_HOST);
        operation.get(HOSTS).add(ANOTHER_HOST);
        ModelNode result = execute(operation);
        checkOutcome(result);

        operation = Util.createOperation(READ_RESOURCE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(RECURSIVE).set(true);
        result = execute(operation);
        checkOutcome(result);
        result = result.get(RESULT);

        assertEquals(MONITOR, result.get(BASE_ROLE).asString());
        assertEquals(2, result.get(HOSTS).asList().size());
        assertEquals(SOME_HOST, result.get(HOSTS).get(0).asString());
        assertEquals(ANOTHER_HOST, result.get(HOSTS).get(1).asString());
    }

    @Test
    public void testAddHostScopedRoleWithoutHosts() {
        assertTrue(kernelServices.isSuccessfulBoot());

        ModelNode operation = Util.createOperation(ADD, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(BASE_ROLE).set(MONITOR);
        ModelNode result = execute(operation);
        checkOutcome(result);

        operation = Util.createOperation(READ_RESOURCE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(RECURSIVE).set(true);
        result = execute(operation);
        checkOutcome(result);
        result = result.get(RESULT);

        assertEquals(MONITOR, result.get(BASE_ROLE).asString());
        assertFalse(result.hasDefined(HOSTS));
    }

    @Test
    public void testModifyBaseRoleOfHostScopedRole() {
        testAddHostScopedRoleWithHosts();

        ModelNode operation = Util.createOperation(WRITE_ATTRIBUTE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(NAME).set(BASE_ROLE);
        operation.get(VALUE).set(OPERATOR);
        ModelNode result = execute(operation);
        checkOutcome(result);

        operation = Util.createOperation(READ_RESOURCE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(RECURSIVE).set(true);
        result = execute(operation);
        checkOutcome(result);
        result = result.get(RESULT);

        assertEquals(OPERATOR, result.get(BASE_ROLE).asString());
        assertEquals(2, result.get(HOSTS).asList().size());
        assertEquals(SOME_HOST, result.get(HOSTS).get(0).asString());
        assertEquals(ANOTHER_HOST, result.get(HOSTS).get(1).asString());
    }

    @Test
    public void testModifyHostsOfHostScopedRole() {
        testAddHostScopedRoleWithHosts();

        ModelNode operation = Util.createOperation(WRITE_ATTRIBUTE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(NAME).set(HOSTS);
        operation.get(VALUE).add(SOME_HOST);
        ModelNode result = execute(operation);
        checkOutcome(result);

        operation = Util.createOperation(READ_RESOURCE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(RECURSIVE).set(true);
        result = execute(operation);
        checkOutcome(result);
        result = result.get(RESULT);

        assertEquals(MONITOR, result.get(BASE_ROLE).asString());
        assertEquals(1, result.get(HOSTS).asList().size());
        assertEquals(SOME_HOST, result.get(HOSTS).get(0).asString());
    }

    @Test
    public void testRemoveHostsOfHostScopedRole() {
        testAddHostScopedRoleWithHosts();

        ModelNode operation = Util.createOperation(WRITE_ATTRIBUTE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(NAME).set(HOSTS); // no operation.get(VALUE).set(...), meaning "undefined"
        ModelNode result = execute(operation);
        checkOutcome(result);

        operation = Util.createOperation(READ_RESOURCE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        operation.get(RECURSIVE).set(true);
        result = execute(operation);
        checkOutcome(result);
        result = result.get(RESULT);

        assertEquals(MONITOR, result.get(BASE_ROLE).asString());
        assertFalse(result.hasDefined(HOSTS));
    }

    @Test
    public void testRemoveHostScopedRole() {
        testAddHostScopedRoleWithHosts();

        ModelNode operation = Util.createOperation(REMOVE, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE, FOO)
        ));
        ModelNode result = execute(operation);
        checkOutcome(result);

        operation = Util.createOperation(READ_RESOURCE_OPERATION, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(HOST_SCOPED_ROLE)
        ));
        operation.get(RECURSIVE).set(true);
        result = execute(operation);
        checkOutcome(result);
        result = result.get(RESULT);

        assertEquals(2, result.asList().size()); // see domain-all.xml
    }

    // test utils

    private ModelNode execute(ModelNode operation) {
        operation.get(OPERATION_HEADERS, "roles").add(StandardRole.SUPERUSER.name());
        return kernelServices.executeOperation(operation);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14273.java