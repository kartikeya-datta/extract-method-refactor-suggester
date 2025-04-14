error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1151.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1151.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1151.java
text:
```scala
public v@@oid setup(final ManagementClient managementClient, String containerId) throws Exception {

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
package org.jboss.as.test.integration.security.common;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.security.Constants;
import org.jboss.as.test.integration.security.common.config.realm.Authentication;
import org.jboss.as.test.integration.security.common.config.realm.RealmKeystore;
import org.jboss.as.test.integration.security.common.config.realm.SecurityRealm;
import org.jboss.as.test.integration.security.common.config.realm.ServerIdentity;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * {@link ServerSetupTask} instance for security realms setup.
 * 
 * @see SecurityRealm
 * @author Josef Cacek
 */
public abstract class AbstractSecurityRealmsServerSetupTask implements ServerSetupTask {

    private static final Logger LOGGER = Logger.getLogger(AbstractSecurityRealmsServerSetupTask.class);
    private static final String KEYSTORE_PATH = "keystore-path";

    protected ManagementClient managementClient;

    private SecurityRealm[] securityRealms;

    // Public methods --------------------------------------------------------

    /**
     * Adds security realms retrieved from {@link #getSecurityRealms()}.
     * 
     * @param managementClient
     * @param containerId
     * @throws Exception
     * @see org.jboss.as.arquillian.api.ServerSetupTask#setup(org.jboss.as.arquillian.container.ManagementClient,
     *      java.lang.String)
     */
    public final void setup(final ManagementClient managementClient, String containerId) throws Exception {
        this.managementClient = managementClient;
        securityRealms = getSecurityRealms();

        if (securityRealms == null || securityRealms.length == 0) {
            LOGGER.warn("Empty security realm configuration.");
            return;
        }

        final List<ModelNode> updates = new LinkedList<ModelNode>();
        for (final SecurityRealm securityRealm : securityRealms) {
            final String securityRealmName = securityRealm.getName();
            LOGGER.info("Adding security realm " + securityRealmName);
            final ModelNode compositeOp = new ModelNode();
            compositeOp.get(OP).set(COMPOSITE);
            compositeOp.get(OP_ADDR).setEmptyList();
            ModelNode steps = compositeOp.get(STEPS);

            // /core-service=management/security-realm=foo:add
            final PathAddress realmAddr = PathAddress.pathAddress().append(CORE_SERVICE, MANAGEMENT)
                    .append(SECURITY_REALM, securityRealmName);
            ModelNode op = Util.createAddOperation(realmAddr);
            steps.add(op);

            final ServerIdentity serverIdentity = securityRealm.getServerIdentity();
            if (serverIdentity != null) {
                // /core-service=management/security-realm=foo/server-identity=secret:add(value="Q29ubmVjdGlvblBhc3N3b3JkMSE=")
                if (StringUtils.isNotEmpty(serverIdentity.getSecret())) {
                    final ModelNode secretModuleNode = Util.createAddOperation(realmAddr.append(SERVER_IDENTITY, SECRET));
                    secretModuleNode.get(Constants.VALUE).set(serverIdentity.getSecret());
                    secretModuleNode.get(OPERATION_HEADERS, ALLOW_RESOURCE_SERVICE_RESTART).set(true);
                    steps.add(secretModuleNode);
                }
                // /core-service=management/security-realm=JBossTest/server-identity=ssl:add(keystore-path=server.keystore, keystore-password=123456)
                final RealmKeystore ssl = serverIdentity.getSsl();
                if (ssl != null) {
                    final ModelNode sslModuleNode = Util.createAddOperation(realmAddr.append(SERVER_IDENTITY, SSL));
                    sslModuleNode.get(KEYSTORE_PATH).set(ssl.getKeystorePath());
                    sslModuleNode.get(Constants.KEYSTORE_PASSWORD).set(ssl.getKeystorePassword());
                    sslModuleNode.get(OPERATION_HEADERS, ALLOW_RESOURCE_SERVICE_RESTART).set(true);
                    steps.add(sslModuleNode);
                }
            }
            final Authentication authentication = securityRealm.getAuthentication();
            if (authentication != null) {
                final RealmKeystore truststore = authentication.getTruststore();
                if (truststore != null) {
                    final ModelNode sslModuleNode = Util.createAddOperation(realmAddr.append(AUTHENTICATION, TRUSTSTORE));
                    sslModuleNode.get(KEYSTORE_PATH).set(truststore.getKeystorePath());
                    sslModuleNode.get(Constants.KEYSTORE_PASSWORD).set(truststore.getKeystorePassword());
                    sslModuleNode.get(OPERATION_HEADERS, ALLOW_RESOURCE_SERVICE_RESTART).set(true);
                    steps.add(sslModuleNode);
                }
            }

            updates.add(compositeOp);
        }
        Utils.applyUpdates(updates, managementClient.getControllerClient());
    }

    /**
     * Removes the security realms from the AS configuration.
     * 
     * @param managementClient
     * @param containerId
     */
    public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
        if (securityRealms == null || securityRealms.length == 0) {
            LOGGER.warn("Empty security realms configuration.");
            return;
        }

        final List<ModelNode> updates = new ArrayList<ModelNode>();
        for (final SecurityRealm securityRealm : securityRealms) {
            final String realmName = securityRealm.getName();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Removing security realm " + realmName);
            }
            final ModelNode op = Util.createRemoveOperation(PathAddress.pathAddress().append(CORE_SERVICE, MANAGEMENT)
                    .append(SECURITY_REALM, realmName));
            op.get(OPERATION_HEADERS, ROLLBACK_ON_RUNTIME_FAILURE).set(false);
            op.get(OPERATION_HEADERS, ALLOW_RESOURCE_SERVICE_RESTART).set(true);
            updates.add(op);
        }
        Utils.applyUpdates(updates, managementClient.getControllerClient());
        this.managementClient = null;
        this.securityRealms = null;
    }

    // Protected methods -----------------------------------------------------

    /**
     * Returns configuration for creating security realms.
     * 
     * @return array of SecurityRealm instances
     */
    protected abstract SecurityRealm[] getSecurityRealms() throws Exception;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1151.java