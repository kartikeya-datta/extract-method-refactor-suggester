error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6779.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6779.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6779.java
text:
```scala
d@@isableSubstitutionOp.get(NAME).set("ejb-annotation-property-replacement");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.test.integration.common.jms;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

import java.io.IOException;

/**
 * A default implementation of JMSOperations used with hornetq
 * @author jpai, refactored by <a href="jmartisk@redhat.com">Jan Martiska</a>
 */
public class DefaultHornetQProviderJMSOperations implements JMSOperations {

    private final ManagementClient client;

    private static final Logger logger = Logger.getLogger(DefaultHornetQProviderJMSOperations.class);

    public DefaultHornetQProviderJMSOperations(ManagementClient client) {
        this.client = client;
    }

    @Override
    public void createJmsQueue(String queueName, String jndiName) {
        createJmsDestination("jms-queue", queueName, jndiName);
    }

    @Override
    public void createJmsTopic(String topicName, String jndiName) {
        createJmsDestination("jms-topic", topicName, jndiName);
    }

    @Override
    public void removeJmsQueue(String queueName) {
        removeJmsDestination("jms-queue", queueName);
    }

    @Override
    public void removeJmsTopic(String topicName) {
        removeJmsDestination("jms-topic", topicName);
    }

    @Override
     public void close() {
        // no-op
        // DO NOT close the management client. Whoever passed it into the constructor should close it
    }

    private ModelControllerClient getModelControllerClient() {
        return client.getControllerClient();
    }

    private void createJmsDestination(final String destinationType, final String destinationName, final String jndiName) {
        final ModelNode createJmsQueueOperation = new ModelNode();
        createJmsQueueOperation.get(ClientConstants.OP).set(ClientConstants.ADD);
        createJmsQueueOperation.get(ClientConstants.OP_ADDR).add("subsystem", "messaging");
        createJmsQueueOperation.get(ClientConstants.OP_ADDR).add("hornetq-server", "default");
        createJmsQueueOperation.get(ClientConstants.OP_ADDR).add(destinationType, destinationName);
        createJmsQueueOperation.get("entries").add(jndiName);
        try {
            this.applyUpdate(createJmsQueueOperation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeJmsDestination(final String destinationType, final String destinationName) {
        final ModelNode removeJmsQueue = new ModelNode();
        removeJmsQueue.get(ClientConstants.OP).set("remove");
        removeJmsQueue.get(ClientConstants.OP_ADDR).add("subsystem", "messaging");
        removeJmsQueue.get(ClientConstants.OP_ADDR).add("hornetq-server", "default");
        removeJmsQueue.get(ClientConstants.OP_ADDR).add(destinationType, destinationName);
        try {
            this.applyUpdate(removeJmsQueue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyUpdate(final ModelNode update) throws IOException, JMSOperationsException {
        ModelNode result = this.getModelControllerClient().execute(update);
        if (result.hasDefined(ClientConstants.OUTCOME) && ClientConstants.SUCCESS.equals(result.get(ClientConstants.OUTCOME).asString())) {
            logger.info("Operation successful for update = " + update.toString());
        } else if (result.hasDefined(ClientConstants.FAILURE_DESCRIPTION)) {
            final String failureDesc = result.get(ClientConstants.FAILURE_DESCRIPTION).toString();
            throw new JMSOperationsException(failureDesc);
        } else {
            throw new JMSOperationsException("Operation not successful; outcome = " + result.get(ClientConstants.OUTCOME));
        }
    }

    @Override
    public void setSystemProperties(String destination, String resourceAdapter) {
        final ModelNode enableSubstitutionOp = new ModelNode();
        enableSubstitutionOp.get(OP_ADDR).set(SUBSYSTEM, "ee");
        enableSubstitutionOp.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        enableSubstitutionOp.get(NAME).set("ejb-annotation-property-replacement");
        enableSubstitutionOp.get(VALUE).set(true);

        final ModelNode setDestinationOp = new ModelNode();
        setDestinationOp.get(ClientConstants.OP).set(ClientConstants.ADD);
        setDestinationOp.get(ClientConstants.OP_ADDR).add("system-property", "destination");
        setDestinationOp.get("value").set(destination);
        final ModelNode setResourceAdapterOp = new ModelNode();
        setResourceAdapterOp.get(ClientConstants.OP).set(ClientConstants.ADD);
        setResourceAdapterOp.get(ClientConstants.OP_ADDR).add("system-property", "resource.adapter");
        setResourceAdapterOp.get("value").set(resourceAdapter);

        try {
            applyUpdate(enableSubstitutionOp);
            applyUpdate(setDestinationOp);
            applyUpdate(setResourceAdapterOp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSystemProperties() {
        final ModelNode removeDestinationOp = new ModelNode();
        removeDestinationOp.get(ClientConstants.OP).set("remove");
        removeDestinationOp.get(ClientConstants.OP_ADDR).add("system-property", "destination");
        final ModelNode removeResourceAdapterOp = new ModelNode();
        removeResourceAdapterOp.get(ClientConstants.OP).set("remove");
        removeResourceAdapterOp.get(ClientConstants.OP_ADDR).add("system-property", "resource.adapter");

        final ModelNode disableSubstitutionOp = new ModelNode();
        disableSubstitutionOp.get(OP_ADDR).set(SUBSYSTEM, "ee");
        disableSubstitutionOp.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        disableSubstitutionOp.get(NAME).set("ejb-annotationr-property-replacement");
        disableSubstitutionOp.get(VALUE).set(false);

        try {
            applyUpdate(removeDestinationOp);
            applyUpdate(removeResourceAdapterOp);
            applyUpdate(disableSubstitutionOp);
        } catch (Exception e) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6779.java