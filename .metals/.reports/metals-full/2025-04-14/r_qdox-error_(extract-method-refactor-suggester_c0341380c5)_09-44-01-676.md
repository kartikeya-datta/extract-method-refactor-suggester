error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9446.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9446.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9446.java
text:
```scala
static final S@@tring[] USERS = { MONITOR_USER, OPERATOR_USER, MAINTAINER_USER, DEPLOYER_USER,

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

package org.jboss.as.test.integration.domain.rbac;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.BASE_ROLE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.BYTES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILD_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.COMPOSITE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_RESOURCES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUPS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.STEPS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.UPLOAD_DEPLOYMENT_BYTES;
import static org.jboss.as.test.integration.management.util.ModelUtil.createOpNode;

import java.io.IOException;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.domain.DomainClient;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.test.integration.management.rbac.Outcome;
import org.jboss.as.test.integration.management.rbac.RbacUtil;
import org.jboss.dmr.ModelNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract superclass of access control provider test cases covering server group scoped roles.
 *
 * @author Brian Stansberry (c) 2013 Red Hat Inc.
 */
public abstract class AbstractServerGroupScopedRolesTestCase extends AbstractRbacTestCase {

    public static final String MONITOR_USER = "MainGroupMonitor";
    public static final String OPERATOR_USER = "MainGroupOperator";
    public static final String MAINTAINER_USER = "MainGroupMaintainer";
    public static final String DEPLOYER_USER = "MainGroupDeployer";
    public static final String ADMINISTRATOR_USER = "MainGroupAdministrator";
    public static final String AUDITOR_USER = "MainGroupAuditor";
    public static final String SUPERUSER_USER = "MainGroupSuperUser";

    private static final String[] USERS = { MONITOR_USER, OPERATOR_USER, MAINTAINER_USER, DEPLOYER_USER,
            ADMINISTRATOR_USER, AUDITOR_USER, SUPERUSER_USER };
    private static final String[] BASES = { RbacUtil.MONITOR_USER, RbacUtil.OPERATOR_USER, RbacUtil.MAINTAINER_USER,
            RbacUtil.DEPLOYER_USER, RbacUtil.ADMINISTRATOR_USER, RbacUtil.AUDITOR_USER,
            RbacUtil.SUPERUSER_USER };

    private static final String SCOPED_ROLE = "core-service=management/access=authorization/server-group-scoped-role=";

    private static final ModelNode WFLY_2089_OP;
    private static final ModelNode WFLY_1916_OP;
    static {
        WFLY_2089_OP = Util.createEmptyOperation(COMPOSITE, PathAddress.EMPTY_ADDRESS);

        ModelNode steps = WFLY_2089_OP.get(STEPS);
        ModelNode step1 = Util.createEmptyOperation(READ_CHILDREN_RESOURCES_OPERATION, PathAddress.EMPTY_ADDRESS);
        step1.get(CHILD_TYPE).set(DEPLOYMENT);
        steps.add(step1);
        ModelNode step2 = Util.createEmptyOperation(READ_CHILDREN_RESOURCES_OPERATION, PathAddress.EMPTY_ADDRESS);
        step2.get(CHILD_TYPE).set(SERVER_GROUP);
        steps.add(step2);
        ModelNode step3 = Util.createEmptyOperation(READ_RESOURCE_OPERATION,
                PathAddress.pathAddress(PathElement.pathElement(SERVER_GROUP), PathElement.pathElement(DEPLOYMENT)));
        steps.add(step3);

        WFLY_2089_OP.protect();

        WFLY_1916_OP = Util.createEmptyOperation(UPLOAD_DEPLOYMENT_BYTES, PathAddress.EMPTY_ADDRESS);
        WFLY_1916_OP.get(BYTES).set(new byte[64]);

        WFLY_1916_OP.protect();
    }
    protected static void setupRoles(DomainClient domainClient) throws IOException {
        for (int i = 0; i < USERS.length; i++) {
            ModelNode op = createOpNode(SCOPED_ROLE + USERS[i], ADD);
            op.get(BASE_ROLE).set(BASES[i]);
            op.get(SERVER_GROUPS).add(SERVER_GROUP_A);
            RbacUtil.executeOperation(domainClient, op, Outcome.SUCCESS);
        }
    }

    protected static void tearDownRoles(DomainClient domainClient) throws IOException {
        for (String role : USERS) {
            ModelNode op = createOpNode(SCOPED_ROLE + role, REMOVE);
            RbacUtil.executeOperation(domainClient, op, Outcome.SUCCESS);
        }
    }

    @After
    public void tearDown() throws IOException {
        AssertionError assertionError = null;
        String[] toRemove = {DEPLOYMENT_2, TEST_PATH, getPrefixedAddress(SERVER_GROUP, SERVER_GROUP_A, SMALL_JVM),
                getPrefixedAddress(SERVER_GROUP, SERVER_GROUP_B, SMALL_JVM),
                getPrefixedAddress(HOST, MASTER, SMALL_JVM),
                getPrefixedAddress(HOST, MASTER, SCOPED_ROLE_SERVER)};
        for (String address : toRemove) {
            try {
                removeResource(address);
            } catch (AssertionError e) {
                if (assertionError == null) {
                    assertionError = e;
                }
            }
        }

        if (assertionError != null) {
            throw assertionError;
        }
    }

    protected abstract boolean isAllowLocalAuth();

    protected abstract void configureRoles(ModelNode op, String[] roles);

    @Test
    public void testMonitor() throws Exception {
        ModelControllerClient client = getClientForUser(MONITOR_USER, isAllowLocalAuth(), masterClientConfig);
        checkStandardReads(client, null, null, MONITOR_USER);
        checkRootRead(client, null, null, Outcome.SUCCESS, MONITOR_USER);
        checkRootRead(client, MASTER, null, Outcome.SUCCESS, MONITOR_USER);
        checkRootRead(client, MASTER, MASTER_A, Outcome.SUCCESS, MONITOR_USER);
        checkRootRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, MONITOR_USER);
        readResource(client, AUTHORIZATION, null, null, Outcome.HIDDEN, MONITOR_USER);
        readResource(client, AUTHORIZATION, MASTER, MASTER_A, Outcome.HIDDEN, MONITOR_USER);
        readResource(client, AUTHORIZATION, SLAVE, SLAVE_B, Outcome.HIDDEN, MONITOR_USER);
        checkSecurityDomainRead(client, null, null, Outcome.HIDDEN, MONITOR_USER);
        checkSecurityDomainRead(client, MASTER, MASTER_A, Outcome.HIDDEN, MONITOR_USER);
        checkSecurityDomainRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, MONITOR_USER);
        checkSensitiveAttribute(client, null, null, false, MONITOR_USER);
        checkSensitiveAttribute(client, MASTER, MASTER_A, false, MONITOR_USER);
        runGC(client, MASTER, null, Outcome.UNAUTHORIZED, MONITOR_USER);
        runGC(client, MASTER, MASTER_A, Outcome.UNAUTHORIZED, MONITOR_USER);
        runGC(client, SLAVE, null, Outcome.UNAUTHORIZED, MONITOR_USER);
        runGC(client, SLAVE, SLAVE_B, Outcome.HIDDEN, MONITOR_USER);
        addDeployment2(client, Outcome.UNAUTHORIZED, MONITOR_USER);
        addPath(client, Outcome.UNAUTHORIZED, MONITOR_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_A, Outcome.UNAUTHORIZED, MONITOR_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_B, Outcome.HIDDEN, MONITOR_USER);
        addJvm(client, HOST, MASTER, Outcome.UNAUTHORIZED, MONITOR_USER);
        addJvm(client, HOST, SLAVE, Outcome.UNAUTHORIZED, MONITOR_USER);

        testWFLY2089(client, MONITOR_USER);

        testWFLY1916(client, Outcome.UNAUTHORIZED, MONITOR_USER);

        testWLFY2299(client, Outcome.UNAUTHORIZED, MONITOR_USER);
    }

    @Test
    public void testOperator() throws Exception {
        ModelControllerClient client = getClientForUser(OPERATOR_USER, isAllowLocalAuth(), masterClientConfig);
        checkStandardReads(client, null, null, OPERATOR_USER);
        checkRootRead(client, null, null, Outcome.SUCCESS, OPERATOR_USER);
        checkRootRead(client, MASTER, null, Outcome.SUCCESS, OPERATOR_USER);
        checkRootRead(client, MASTER, MASTER_A, Outcome.SUCCESS, OPERATOR_USER);
        checkRootRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, OPERATOR_USER);
        readResource(client, AUTHORIZATION, null, null, Outcome.HIDDEN, OPERATOR_USER);
        readResource(client, AUTHORIZATION, MASTER, MASTER_A, Outcome.HIDDEN, OPERATOR_USER);
        readResource(client, AUTHORIZATION, SLAVE, SLAVE_B, Outcome.HIDDEN, OPERATOR_USER);
        checkSecurityDomainRead(client, null, null, Outcome.HIDDEN, OPERATOR_USER);
        checkSecurityDomainRead(client, MASTER, MASTER_A, Outcome.HIDDEN, OPERATOR_USER);
        checkSecurityDomainRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, OPERATOR_USER);
        checkSensitiveAttribute(client, null, null, false, OPERATOR_USER);
        checkSensitiveAttribute(client, MASTER, MASTER_A, false, OPERATOR_USER);
        runGC(client, MASTER, null, Outcome.SUCCESS, OPERATOR_USER);
        runGC(client, MASTER, MASTER_A, Outcome.SUCCESS, OPERATOR_USER);
        runGC(client, SLAVE, null, Outcome.UNAUTHORIZED, OPERATOR_USER);
        runGC(client, SLAVE, SLAVE_B, Outcome.HIDDEN, OPERATOR_USER);
        addDeployment2(client, Outcome.UNAUTHORIZED, OPERATOR_USER);
        addPath(client, Outcome.UNAUTHORIZED, OPERATOR_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_A, Outcome.UNAUTHORIZED, OPERATOR_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_B, Outcome.HIDDEN, OPERATOR_USER);
        addJvm(client, HOST, MASTER, Outcome.UNAUTHORIZED, OPERATOR_USER);
        addJvm(client, HOST, SLAVE, Outcome.UNAUTHORIZED, OPERATOR_USER);

        testWFLY2089(client, OPERATOR_USER);

        testWFLY1916(client, Outcome.SUCCESS, OPERATOR_USER);

        testWLFY2299(client, Outcome.UNAUTHORIZED, OPERATOR_USER);
    }

    @Test
    public void testMaintainer() throws Exception {
        ModelControllerClient client = getClientForUser(MAINTAINER_USER, isAllowLocalAuth(), masterClientConfig);
        checkStandardReads(client, null, null, MAINTAINER_USER);
        checkRootRead(client, null, null, Outcome.SUCCESS, MAINTAINER_USER);
        checkRootRead(client, MASTER, null, Outcome.SUCCESS, MAINTAINER_USER);
        checkRootRead(client, MASTER, MASTER_A, Outcome.SUCCESS, MAINTAINER_USER);
        checkRootRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, MAINTAINER_USER);
        readResource(client, AUTHORIZATION, null, null, Outcome.HIDDEN, MAINTAINER_USER);
        readResource(client, AUTHORIZATION, MASTER, MASTER_A, Outcome.HIDDEN, MAINTAINER_USER);
        readResource(client, AUTHORIZATION, SLAVE, SLAVE_B, Outcome.HIDDEN, MAINTAINER_USER);
        checkSecurityDomainRead(client, null, null, Outcome.HIDDEN, MAINTAINER_USER);
        checkSecurityDomainRead(client, MASTER, MASTER_A, Outcome.HIDDEN, MAINTAINER_USER);
        checkSecurityDomainRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, MAINTAINER_USER);
        checkSensitiveAttribute(client, null, null, false, MAINTAINER_USER);
        checkSensitiveAttribute(client, MASTER, MASTER_A, false, MAINTAINER_USER);
        runGC(client, MASTER, null, Outcome.SUCCESS, MAINTAINER_USER);
        runGC(client, MASTER, MASTER_A, Outcome.SUCCESS, MAINTAINER_USER);
        runGC(client, SLAVE, null, Outcome.UNAUTHORIZED, MAINTAINER_USER);
        runGC(client, MASTER, SLAVE_B, Outcome.HIDDEN, MAINTAINER_USER);
        addDeployment2(client, Outcome.SUCCESS, MAINTAINER_USER);
        addPath(client, Outcome.UNAUTHORIZED, MAINTAINER_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_A, Outcome.SUCCESS, MAINTAINER_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_B, Outcome.HIDDEN, MAINTAINER_USER);
        addJvm(client, HOST, MASTER, Outcome.SUCCESS, MAINTAINER_USER);
        addJvm(client, HOST, SLAVE, Outcome.UNAUTHORIZED, MAINTAINER_USER);

        testWFLY2089(client, MAINTAINER_USER);

        testWFLY1916(client, Outcome.SUCCESS, MAINTAINER_USER);

        testWLFY2299(client, Outcome.SUCCESS, MAINTAINER_USER);
    }

    @Test
    public void testDeployer() throws Exception {
        ModelControllerClient client = getClientForUser(DEPLOYER_USER, isAllowLocalAuth(), masterClientConfig);
        checkStandardReads(client, null, null, DEPLOYER_USER);
        checkRootRead(client, null, null, Outcome.SUCCESS, DEPLOYER_USER);
        checkRootRead(client, MASTER, null, Outcome.SUCCESS, DEPLOYER_USER);
        checkRootRead(client, MASTER, MASTER_A, Outcome.SUCCESS, DEPLOYER_USER);
        checkRootRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, DEPLOYER_USER);
        readResource(client, AUTHORIZATION, null, null, Outcome.HIDDEN, DEPLOYER_USER);
        readResource(client, AUTHORIZATION, MASTER, MASTER_A, Outcome.HIDDEN, DEPLOYER_USER);
        readResource(client, AUTHORIZATION, SLAVE, SLAVE_B, Outcome.HIDDEN, DEPLOYER_USER);
        checkSecurityDomainRead(client, null, null, Outcome.HIDDEN, DEPLOYER_USER);
        checkSecurityDomainRead(client, MASTER, MASTER_A, Outcome.HIDDEN, DEPLOYER_USER);
        checkSecurityDomainRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, DEPLOYER_USER);
        checkSensitiveAttribute(client, null, null, false, DEPLOYER_USER);
        checkSensitiveAttribute(client, MASTER, MASTER_A, false, DEPLOYER_USER);
        runGC(client, MASTER, null, Outcome.UNAUTHORIZED, DEPLOYER_USER);
        runGC(client, MASTER, MASTER_A, Outcome.UNAUTHORIZED, DEPLOYER_USER);
        runGC(client, SLAVE, null, Outcome.UNAUTHORIZED, DEPLOYER_USER);
        runGC(client, SLAVE, SLAVE_B, Outcome.HIDDEN, DEPLOYER_USER);
        addDeployment2(client, Outcome.SUCCESS, DEPLOYER_USER);
        addPath(client, Outcome.UNAUTHORIZED, DEPLOYER_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_A, Outcome.UNAUTHORIZED, DEPLOYER_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_B, Outcome.HIDDEN, DEPLOYER_USER);
        addJvm(client, HOST, MASTER, Outcome.UNAUTHORIZED, DEPLOYER_USER);
        addJvm(client, HOST, SLAVE, Outcome.UNAUTHORIZED, DEPLOYER_USER);

        testWFLY2089(client, DEPLOYER_USER);

        testWFLY1916(client, Outcome.SUCCESS, DEPLOYER_USER);

        testWLFY2299(client, Outcome.UNAUTHORIZED, DEPLOYER_USER);
    }

    @Test
    public void testAdministrator() throws Exception {
        ModelControllerClient client = getClientForUser(ADMINISTRATOR_USER, isAllowLocalAuth(), masterClientConfig);
        checkStandardReads(client, null, null, ADMINISTRATOR_USER);
        checkRootRead(client, null, null, Outcome.SUCCESS, ADMINISTRATOR_USER);
        checkRootRead(client, MASTER, null, Outcome.SUCCESS, ADMINISTRATOR_USER);
        checkRootRead(client, MASTER, MASTER_A, Outcome.SUCCESS, ADMINISTRATOR_USER);
        checkRootRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, ADMINISTRATOR_USER);
        readResource(client, AUTHORIZATION, null, null, Outcome.HIDDEN, ADMINISTRATOR_USER);
        readResource(client, AUTHORIZATION, MASTER, MASTER_A, Outcome.SUCCESS, ADMINISTRATOR_USER);
        readResource(client, AUTHORIZATION, SLAVE, SLAVE_B, Outcome.HIDDEN, ADMINISTRATOR_USER);
        checkSecurityDomainRead(client, null, null, Outcome.SUCCESS, ADMINISTRATOR_USER);
        checkSecurityDomainRead(client, MASTER, MASTER_A, Outcome.SUCCESS, ADMINISTRATOR_USER);
        checkSecurityDomainRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, ADMINISTRATOR_USER);
        checkSensitiveAttribute(client, null, null, true, ADMINISTRATOR_USER);
        checkSensitiveAttribute(client, MASTER, MASTER_A, true, ADMINISTRATOR_USER);
        runGC(client, MASTER, null, Outcome.SUCCESS, ADMINISTRATOR_USER);
        runGC(client, MASTER, MASTER_A, Outcome.SUCCESS, ADMINISTRATOR_USER);
        runGC(client, SLAVE, null, Outcome.UNAUTHORIZED, ADMINISTRATOR_USER);
        runGC(client, SLAVE, SLAVE_B, Outcome.HIDDEN, ADMINISTRATOR_USER);
        addDeployment2(client, Outcome.SUCCESS, ADMINISTRATOR_USER);
        addPath(client, Outcome.UNAUTHORIZED, ADMINISTRATOR_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_A, Outcome.SUCCESS, ADMINISTRATOR_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_B, Outcome.HIDDEN, ADMINISTRATOR_USER);
        addJvm(client, HOST, MASTER, Outcome.SUCCESS, ADMINISTRATOR_USER);
        addJvm(client, HOST, SLAVE, Outcome.UNAUTHORIZED, ADMINISTRATOR_USER);

        testWFLY2089(client, ADMINISTRATOR_USER);

        testWFLY1916(client, Outcome.SUCCESS, ADMINISTRATOR_USER);

        testWLFY2299(client, Outcome.SUCCESS, ADMINISTRATOR_USER);
    }

    @Test
    public void testAuditor() throws Exception {
        ModelControllerClient client = getClientForUser(AUDITOR_USER, isAllowLocalAuth(), masterClientConfig);
        checkStandardReads(client, null, null, AUDITOR_USER);
        checkRootRead(client, null, null, Outcome.SUCCESS, AUDITOR_USER);
        checkRootRead(client, MASTER, null, Outcome.SUCCESS, AUDITOR_USER);
        checkRootRead(client, MASTER, MASTER_A, Outcome.SUCCESS, AUDITOR_USER);
        checkRootRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, AUDITOR_USER);
        readResource(client, AUTHORIZATION, null, null, Outcome.HIDDEN, AUDITOR_USER);
        readResource(client, AUTHORIZATION, MASTER, MASTER_A, Outcome.SUCCESS, AUDITOR_USER);
        readResource(client, AUTHORIZATION, SLAVE, SLAVE_B, Outcome.HIDDEN, AUDITOR_USER);
        checkSecurityDomainRead(client, null, null, Outcome.SUCCESS, AUDITOR_USER);
        checkSecurityDomainRead(client, MASTER, MASTER_A, Outcome.SUCCESS, AUDITOR_USER);
        checkSecurityDomainRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, AUDITOR_USER);
        checkSensitiveAttribute(client, null, null, true, AUDITOR_USER);
        checkSensitiveAttribute(client, MASTER, MASTER_A, true, AUDITOR_USER);
        runGC(client, MASTER, null, Outcome.UNAUTHORIZED, AUDITOR_USER);
        runGC(client, MASTER, MASTER_A, Outcome.UNAUTHORIZED, AUDITOR_USER);
        runGC(client, SLAVE, null, Outcome.UNAUTHORIZED, AUDITOR_USER);
        runGC(client, SLAVE, SLAVE_B, Outcome.HIDDEN, AUDITOR_USER);
        addDeployment2(client, Outcome.UNAUTHORIZED, AUDITOR_USER);
        addPath(client, Outcome.UNAUTHORIZED, AUDITOR_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_A, Outcome.UNAUTHORIZED, AUDITOR_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_B, Outcome.HIDDEN, AUDITOR_USER);
        addJvm(client, HOST, MASTER, Outcome.UNAUTHORIZED, AUDITOR_USER);
        addJvm(client, HOST, SLAVE, Outcome.UNAUTHORIZED, AUDITOR_USER);

        testWFLY2089(client, AUDITOR_USER);

        testWFLY1916(client, Outcome.UNAUTHORIZED, AUDITOR_USER);

        testWLFY2299(client, Outcome.UNAUTHORIZED, AUDITOR_USER);
    }

    @Test
    public void testSuperUser() throws Exception {
        ModelControllerClient client = getClientForUser(SUPERUSER_USER, isAllowLocalAuth(), masterClientConfig);
        checkStandardReads(client, null, null, SUPERUSER_USER);
        checkRootRead(client, null, null, Outcome.SUCCESS, SUPERUSER_USER);
        checkRootRead(client, MASTER, null, Outcome.SUCCESS, SUPERUSER_USER);
        checkRootRead(client, MASTER, MASTER_A, Outcome.SUCCESS, SUPERUSER_USER);
        checkRootRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, SUPERUSER_USER);
        readResource(client, AUTHORIZATION, null, null, Outcome.HIDDEN, SUPERUSER_USER);
        readResource(client, AUTHORIZATION, MASTER, MASTER_A, Outcome.SUCCESS, SUPERUSER_USER);
        readResource(client, AUTHORIZATION, SLAVE, SLAVE_B, Outcome.HIDDEN, SUPERUSER_USER);
        checkSecurityDomainRead(client, null, null, Outcome.SUCCESS, SUPERUSER_USER);
        checkSecurityDomainRead(client, MASTER, MASTER_A, Outcome.SUCCESS, SUPERUSER_USER);
        checkSecurityDomainRead(client, MASTER, SLAVE_B, Outcome.HIDDEN, SUPERUSER_USER);
        checkSensitiveAttribute(client, null, null, true, SUPERUSER_USER);
        checkSensitiveAttribute(client, MASTER, MASTER_A, true, SUPERUSER_USER);
        runGC(client, MASTER, null, Outcome.SUCCESS, SUPERUSER_USER);
        runGC(client, MASTER, MASTER_A, Outcome.SUCCESS, SUPERUSER_USER);
        runGC(client, SLAVE, null, Outcome.UNAUTHORIZED, SUPERUSER_USER);
        runGC(client, SLAVE, SLAVE_B, Outcome.HIDDEN, SUPERUSER_USER);
        addDeployment2(client, Outcome.SUCCESS, SUPERUSER_USER);
        addPath(client, Outcome.UNAUTHORIZED, SUPERUSER_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_A, Outcome.SUCCESS, SUPERUSER_USER);
        addJvm(client, SERVER_GROUP, SERVER_GROUP_B, Outcome.HIDDEN, SUPERUSER_USER);
        addJvm(client, HOST, MASTER, Outcome.SUCCESS, SUPERUSER_USER);
        addJvm(client, HOST, SLAVE, Outcome.UNAUTHORIZED, SUPERUSER_USER);

        testWFLY2089(client, SUPERUSER_USER);

        testWFLY1916(client, Outcome.SUCCESS, SUPERUSER_USER);

        testWLFY2299(client, Outcome.SUCCESS, SUPERUSER_USER);
    }


    private void testWFLY2089(ModelControllerClient client, String... roles) throws Exception {
        ModelNode op = WFLY_2089_OP.clone();
        configureRoles(op, roles);
        RbacUtil.executeOperation(client, op, Outcome.SUCCESS);

    }

    private void testWFLY1916(ModelControllerClient client, Outcome expected, String... roles) throws IOException {
        ModelNode op = WFLY_1916_OP.clone();
        configureRoles(op, roles);
        RbacUtil.executeOperation(client, op, expected);
    }

    private void testWLFY2299(ModelControllerClient client, Outcome expected, String... roles) throws IOException {

        addServerConfig(client, MASTER, SERVER_GROUP_B, Outcome.HIDDEN, roles);
        addServerConfig(client, MASTER, SERVER_GROUP_A, expected, roles);

        ModelNode metadata = getServerConfigAccessControl(client, roles);

        ModelNode add = metadata.get("default", "operations", "add", "execute");
        Assert.assertTrue(add.isDefined());
        Assert.assertEquals(expected == Outcome.SUCCESS, add.asBoolean());

        ModelNode writeConfig = metadata.get("default", "write");
        Assert.assertTrue(writeConfig.isDefined());
        Assert.assertEquals(expected == Outcome.SUCCESS, writeConfig.asBoolean());

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9446.java