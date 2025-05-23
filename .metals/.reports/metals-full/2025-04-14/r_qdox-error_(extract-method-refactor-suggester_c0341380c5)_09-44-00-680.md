error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2115.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2115.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2115.java
text:
```scala
c@@onnector.get("in-vm").set(new ModelNode());

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

package org.jboss.as.test.smoke.jms;

import java.io.IOException;

import org.hornetq.jms.bridge.QualityOfServiceMode;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.as.test.jms.auxiliary.CreateQueueSetupTask;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.logging.Logger;

/**
 * Setup task to create/remove a JMS bridge.
 *
 * @author Jeff Mesnil (c) 2012 Red Hat Inc.
 */
public class CreateJMSBridgeSetupTask extends CreateQueueSetupTask {

    public static final String CF_NAME = "myAwesomeCF";
    public static final String CF_JNDI_NAME = "/myAwesomeCF";
    public static final String JMS_BRIDGE_NAME = "myAwesomeJMSBridge";

    private static final Logger logger = Logger.getLogger(CreateJMSBridgeSetupTask.class);

    private ManagementClient managementClient;

    @Override
    public void setup(ManagementClient managementClient, String containerId) throws Exception {
        super.setup(managementClient, containerId);
        this.managementClient = managementClient;

        createConnectionFactory(CF_NAME, CF_JNDI_NAME);
        createJmsBridge(JMS_BRIDGE_NAME);
    }


    @Override
    public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
        removeJmsBridge(JMS_BRIDGE_NAME);
        removeConnectionFactory(CF_NAME);
        super.tearDown(managementClient, containerId);
    }


    private void createConnectionFactory(String cfName, String cfJndiName) {
        final ModelNode createConnectionFactoryOp = new ModelNode();
        createConnectionFactoryOp.get(ClientConstants.OP).set(ClientConstants.ADD);
        createConnectionFactoryOp.get(ClientConstants.OP_ADDR).add("subsystem", "messaging");
        createConnectionFactoryOp.get(ClientConstants.OP_ADDR).add("hornetq-server", "default");
        createConnectionFactoryOp.get(ClientConstants.OP_ADDR).add("connection-factory", cfName);
        ModelNode connector = createConnectionFactoryOp.get("connector");
        connector.get("in-vm").set(ModelType.UNDEFINED);

        createConnectionFactoryOp.get("factory-type").set("XA_GENERIC");
        createConnectionFactoryOp.get("entries").add(cfJndiName);
        System.err.println("Create operation =====> " + createConnectionFactoryOp);
        try {
            this.applyUpdate(createConnectionFactoryOp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeConnectionFactory(String cfName) {
        final ModelNode removeConnectionFactoryOp = new ModelNode();
        removeConnectionFactoryOp.get(ClientConstants.OP).set("remove");
        removeConnectionFactoryOp.get(ClientConstants.OP_ADDR).add("subsystem", "messaging");
        removeConnectionFactoryOp.get(ClientConstants.OP_ADDR).add("hornetq-server", "default");
        removeConnectionFactoryOp.get(ClientConstants.OP_ADDR).add("connection-factory", cfName);
        try {
            this.applyUpdate(removeConnectionFactoryOp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void createJmsBridge(String bridgeName) {
        final ModelNode createJmsBridgeOp = new ModelNode();
        createJmsBridgeOp.get(ClientConstants.OP).set(ClientConstants.ADD);
        createJmsBridgeOp.get(ClientConstants.OP_ADDR).add("subsystem", "messaging");
        createJmsBridgeOp.get(ClientConstants.OP_ADDR).add("jms-bridge", bridgeName);
        createJmsBridgeOp.get("source-connection-factory").set(CF_JNDI_NAME);
        createJmsBridgeOp.get("source-destination").set(QUEUE1_JNDI_NAME);
        createJmsBridgeOp.get("target-connection-factory").set(CF_JNDI_NAME);
        createJmsBridgeOp.get("target-destination").set(QUEUE2_JNDI_NAME);
        createJmsBridgeOp.get("quality-of-service").set(QualityOfServiceMode.ONCE_AND_ONLY_ONCE.toString());
        createJmsBridgeOp.get("failure-retry-interval").set(500);
        createJmsBridgeOp.get("max-retries").set(2);
        createJmsBridgeOp.get("max-batch-size").set(1024);
        createJmsBridgeOp.get("max-batch-time").set(100);
        createJmsBridgeOp.get("add-messageID-in-header").set("true");
        try {
            this.applyUpdate(createJmsBridgeOp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeJmsBridge(String bridgeName) {
        final ModelNode removeJmsBridgeOp = new ModelNode();
        removeJmsBridgeOp.get(ClientConstants.OP).set("remove");
        removeJmsBridgeOp.get(ClientConstants.OP_ADDR).add("subsystem", "messaging");
        removeJmsBridgeOp.get(ClientConstants.OP_ADDR).add("jms-bridge", bridgeName);
        try {
            this.applyUpdate(removeJmsBridgeOp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyUpdate(final ModelNode update) throws IOException {
        ModelNode result = managementClient.getControllerClient().execute(update);
        if (result.hasDefined(ClientConstants.OUTCOME)
                && ClientConstants.SUCCESS.equals(result.get(ClientConstants.OUTCOME).asString())) {
            logger.info("Operation successful for update = " + update.toString());
        } else if (result.hasDefined(ClientConstants.FAILURE_DESCRIPTION)) {
            final String failureDesc = result.get(ClientConstants.FAILURE_DESCRIPTION).toString();
            throw new RuntimeException(failureDesc);
        } else {
            throw new RuntimeException("Operation not successful; outcome = " + result.get(ClientConstants.OUTCOME));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2115.java