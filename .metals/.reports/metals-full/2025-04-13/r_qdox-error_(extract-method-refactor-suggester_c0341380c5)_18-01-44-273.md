error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/211.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/211.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/211.java
text:
```scala
p@@athElement("mail-session", "default")

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

package org.jboss.as.test.integration.mgmt.access;

import static org.jboss.as.controller.PathAddress.pathAddress;
import static org.jboss.as.controller.PathElement.pathElement;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ACCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.APPLICATION_CLASSIFICATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.AUTHORIZATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CLASSIFICATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONFIGURED_APPLICATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONSTRAINT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CORE_SERVICE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.UNDEFINE_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.test.integration.management.rbac.Outcome;
import org.jboss.as.test.integration.management.rbac.RbacUtil;
import org.jboss.as.test.integration.management.rbac.UserRolesMappingServerSetupTask;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Ladislav Thon <lthon@redhat.com>
 */
@RunWith(Arquillian.class)
@ServerSetup(UserRolesMappingServerSetupTask.StandardUsersSetup.class)
public class ApplicationTypeTestCase extends AbstractRbacTestCase {
    @Deployment(testable = false)
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(ApplicationTypeTestCase.class);
    }

    @Test
    public void testMonitor() throws Exception {
        test(false, false, getClientForUser(RbacUtil.MONITOR_USER));
    }

    @Test
    public void testOperator() throws Exception {
        test(false, false, getClientForUser(RbacUtil.OPERATOR_USER));
    }

    @Test
    public void testMaintainer() throws Exception {
        test(true, true, getClientForUser(RbacUtil.MAINTAINER_USER));
    }

    @Test
    public void testDeployer() throws Exception {
        test(true, false, getClientForUser(RbacUtil.DEPLOYER_USER));
    }

    @Test
    public void testAdministrator() throws Exception {
        test(true, true, getClientForUser(RbacUtil.ADMINISTRATOR_USER));
    }

    @Test
    public void testAuditor() throws Exception {
        test(false, false, getClientForUser(RbacUtil.AUDITOR_USER));
    }

    @Test
    public void testSuperUser() throws Exception {
        test(true, true, getClientForUser(RbacUtil.SUPERUSER_USER));
    }

    private void test(boolean canWriteWhenApplicationSet, boolean canWriteWhenApplicationNotSet, ModelControllerClient client) throws IOException {
        testDataSource(canWriteWhenApplicationNotSet, client);
        testMailSession(canWriteWhenApplicationNotSet, client);

        setDataSourceAsApplicationType(true, getManagementClient().getControllerClient());
        setMailSessionAsApplicationType(true, getManagementClient().getControllerClient());
        testDataSource(canWriteWhenApplicationSet, client);
        testMailSession(canWriteWhenApplicationSet, client);

        setDataSourceAsApplicationType(false, getManagementClient().getControllerClient());
        setMailSessionAsApplicationType(false, getManagementClient().getControllerClient());
        testDataSource(canWriteWhenApplicationNotSet, client);
        testMailSession(canWriteWhenApplicationNotSet, client);

        setDataSourceAsApplicationType(null, getManagementClient().getControllerClient());
        setMailSessionAsApplicationType(null, getManagementClient().getControllerClient());
        testDataSource(canWriteWhenApplicationNotSet, client);
        testMailSession(canWriteWhenApplicationNotSet, client);
    }

    private void testDataSource(boolean canWrite, ModelControllerClient client) throws IOException {
        ModelNode operation = Util.createOperation(WRITE_ATTRIBUTE_OPERATION, pathAddress(
                pathElement(SUBSYSTEM, "datasources"),
                pathElement("data-source", "ExampleDS")
        ));
        operation.get(NAME).set("jndi-name");
        operation.get(VALUE).set("java:jboss/datasources/ExampleDS_XXX");
        try {
            RbacUtil.executeOperation(client, operation, canWrite ? Outcome.SUCCESS : Outcome.UNAUTHORIZED);
        } finally {
            operation.get(VALUE).set("java:jboss/datasources/ExampleDS");
            RbacUtil.executeOperation(getManagementClient().getControllerClient(), operation, Outcome.SUCCESS);
        }
    }

    private void testMailSession(boolean canWrite, ModelControllerClient client) throws IOException {
        ModelNode operation = Util.createOperation(WRITE_ATTRIBUTE_OPERATION, pathAddress(
                pathElement(SUBSYSTEM, "mail"),
                pathElement("mail-session", "java:jboss/mail/Default")
        ));
        operation.get(NAME).set("jndi-name");
        operation.get(VALUE).set("java:jboss/mail/Default_XXX");
        try {
            RbacUtil.executeOperation(client, operation, canWrite ? Outcome.SUCCESS : Outcome.UNAUTHORIZED);
        } finally {
            operation.get(VALUE).set("java:jboss/mail/Default");
            RbacUtil.executeOperation(getManagementClient().getControllerClient(), operation, Outcome.SUCCESS);
        }
    }

    // test utils

    private static void setDataSourceAsApplicationType(Boolean isApplication, ModelControllerClient client) throws IOException {
        setApplicationType("datasources", "data-source", isApplication, client);
    }

    private static void setMailSessionAsApplicationType(Boolean isApplication, ModelControllerClient client) throws IOException {
        setApplicationType("mail", "mail-session", isApplication, client);
    }

    private static void setApplicationType(String type, String classification, Boolean isApplication, ModelControllerClient client) throws IOException {
        String operationName = isApplication != null ? WRITE_ATTRIBUTE_OPERATION : UNDEFINE_ATTRIBUTE_OPERATION;
        ModelNode operation = Util.createOperation(operationName, pathAddress(
                pathElement(CORE_SERVICE, MANAGEMENT),
                pathElement(ACCESS, AUTHORIZATION),
                pathElement(CONSTRAINT, APPLICATION_CLASSIFICATION),
                pathElement(TYPE, type),
                pathElement(CLASSIFICATION, classification)
        ));
        operation.get(NAME).set(CONFIGURED_APPLICATION);
        if (isApplication != null) {
            operation.get(VALUE).set(isApplication);
        }
        RbacUtil.executeOperation(client, operation, Outcome.SUCCESS);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/211.java