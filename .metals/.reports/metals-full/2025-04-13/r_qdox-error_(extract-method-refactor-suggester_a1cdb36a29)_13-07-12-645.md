error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13914.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13914.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13914.java
text:
```scala
static final S@@tring SUBSYSTEM_XML_FILE = "subsystem-jgroups-2_0.xml" ;

package org.jboss.as.clustering.jgroups.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.COMPOSITE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.STEPS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.io.IOException;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.subsystem.test.AbstractSubsystemTest;
import org.jboss.dmr.ModelNode;

/**
* Base test case for testing management operations.
*
* @author Richard Achmatowicz (c) 2011 Red Hat Inc.
*/
public class OperationTestCaseBase extends AbstractSubsystemTest {

    static final String SUBSYSTEM_XML_FILE = "subsystem-jgroups-test.xml" ;

    public OperationTestCaseBase() {
        super(JGroupsExtension.SUBSYSTEM_NAME, new JGroupsExtension());
    }

    protected static ModelNode getCompositeOperation(ModelNode[] operations) {
        // create the address of the cache
        ModelNode compositeOp = new ModelNode() ;
        compositeOp.get(OP).set(COMPOSITE);
        compositeOp.get(OP_ADDR).setEmptyList();
        // the operations to be performed
        for (ModelNode operation : operations) {
            compositeOp.get(STEPS).add(operation);
        }
        return compositeOp ;
    }

    protected static ModelNode getSubsystemAddOperation(String defaultStack) {
        // create the address of the subsystem
        PathAddress subsystemAddress =  PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME));
        ModelNode addOp = new ModelNode() ;
        addOp.get(OP).set(ADD);
        addOp.get(OP_ADDR).set(subsystemAddress.toModelNode());
        // required attributes
        addOp.get(ModelKeys.DEFAULT_STACK).set(defaultStack);
        return addOp ;
    }

    protected static ModelNode getSubsystemReadOperation(String name) {
        // create the address of the subsystem
        PathAddress subsystemAddress =  PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME));
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(subsystemAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(name);
        return readOp ;
    }

    protected static ModelNode getSubsystemWriteOperation(String name, String value) {
        // create the address of the subsystem
        PathAddress subsystemAddress =  PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME));
        ModelNode writeOp = new ModelNode() ;
        writeOp.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        writeOp.get(OP_ADDR).set(subsystemAddress.toModelNode());
        // required attributes
        writeOp.get(NAME).set(name);
        writeOp.get(VALUE).set(value);
        return writeOp ;
    }

    protected static ModelNode getSubsystemRemoveOperation() {
        // create the address of the subsystem
        PathAddress subsystemAddress =  PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME));
        ModelNode removeOp = new ModelNode() ;
        removeOp.get(OP).set(REMOVE);
        removeOp.get(OP_ADDR).set(subsystemAddress.toModelNode());
        return removeOp ;
    }

    protected static ModelNode getProtocolStackAddOperation(String stackName) {
        // create the address of the cache
        PathAddress stackAddr = getProtocolStackAddress(stackName);
        ModelNode addOp = new ModelNode() ;
        addOp.get(OP).set(ADD);
        addOp.get(OP_ADDR).set(stackAddr.toModelNode());
        // required attributes
        // addOp.get(DEFAULT_CACHE).set("default");
        return addOp ;
    }

    protected static ModelNode getProtocolStackAddOperationWithParameters(String stackName) {
         ModelNode addOp = getProtocolStackAddOperation(stackName);
         // add optional TRANSPORT attribute
         ModelNode transport = addOp.get(ModelKeys.TRANSPORT);
         transport.get(ModelKeys.TYPE).set("UDP");

         // add optional PROTOCOLS attribute
         ModelNode protocolsList = new ModelNode();
         ModelNode mping = new ModelNode() ;
         mping.get(ModelKeys.TYPE).set("MPING");
         protocolsList.add(mping);
         ModelNode flush = new ModelNode() ;
         flush.get(ModelKeys.TYPE).set("pbcast.FLUSH");
         protocolsList.add(flush);
         addOp.get(ModelKeys.PROTOCOLS).set(protocolsList);
         return addOp ;
     }

    protected static ModelNode getProtocolStackRemoveOperation(String stackName) {
        // create the address of the cache
        PathAddress stackAddr = getProtocolStackAddress(stackName);
        ModelNode removeOp = new ModelNode() ;
        removeOp.get(OP).set(REMOVE);
        removeOp.get(OP_ADDR).set(stackAddr.toModelNode());
        return removeOp ;
    }

    protected static ModelNode getTransportAddOperation(String stackName, String protocolType) {
        // create the address of the cache
        PathAddress transportAddr = getTransportAddress(stackName);
        ModelNode addOp = new ModelNode() ;
        addOp.get(OP).set(ADD);
        addOp.get(OP_ADDR).set(transportAddr.toModelNode());
        // required attributes
        addOp.get(ModelKeys.TYPE).set(protocolType);
        return addOp ;
    }

    protected static ModelNode getTransportAddOperationWithProperties(String stackName, String protocolType) {
        ModelNode addOp = getTransportAddOperation(stackName, protocolType);
        // add optional PROPERTIES attribute
        ModelNode propertyList = new ModelNode();
        ModelNode propA = new ModelNode();
        propA.add("A","a");
        propertyList.add(propA);
        ModelNode propB = new ModelNode();
        propB.add("B","b");
        propertyList.add(propB);
        addOp.get(ModelKeys.PROPERTIES).set(propertyList);
        return addOp ;
    }

    protected static ModelNode getTransportRemoveOperation(String stackName, String protocolType) {
        // create the address of the cache
        PathAddress transportAddr = getTransportAddress(stackName);
        ModelNode removeOp = new ModelNode() ;
        removeOp.get(OP).set(REMOVE);
        removeOp.get(OP_ADDR).set(transportAddr.toModelNode());
        return removeOp ;
    }

    protected static ModelNode getTransportReadOperation(String stackName, String name) {
        // create the address of the subsystem
        PathAddress transportAddress = getTransportAddress(stackName);
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(transportAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(name);
        return readOp ;
    }

    protected static ModelNode getTransportWriteOperation(String stackName, String name, String value) {
        // create the address of the subsystem
        PathAddress transportAddress = getTransportAddress(stackName);
        ModelNode writeOp = new ModelNode() ;
        writeOp.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        writeOp.get(OP_ADDR).set(transportAddress.toModelNode());
        // required attributes
        writeOp.get(NAME).set(name);
        writeOp.get(VALUE).set(value);
        return writeOp ;
    }

    protected static ModelNode getTransportPropertyAddOperation(String stackName, String propertyName, String propertyValue) {
        // create the address of the subsystem
        PathAddress transportPropertyAddress = getTransportPropertyAddress(stackName, propertyName);
        ModelNode addOp = new ModelNode() ;
        addOp.get(OP).set(ADD);
        addOp.get(OP_ADDR).set(transportPropertyAddress.toModelNode());
        // required attributes
        addOp.get(NAME).set(ModelKeys.VALUE);
        addOp.get(VALUE).set(propertyValue);
        return addOp ;
    }

    protected static ModelNode getTransportPropertyReadOperation(String stackName, String propertyName) {
        // create the address of the subsystem
        PathAddress transportPropertyAddress = getTransportPropertyAddress(stackName, propertyName);
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(transportPropertyAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(ModelKeys.VALUE);
        return readOp ;
    }

    protected static ModelNode getTransportPropertyWriteOperation(String stackName, String propertyName, String propertyValue) {
        // create the address of the subsystem
        PathAddress transportPropertyAddress = getTransportPropertyAddress(stackName, propertyName);
        ModelNode writeOp = new ModelNode() ;
        writeOp.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        writeOp.get(OP_ADDR).set(transportPropertyAddress.toModelNode());
        // required attributes
        writeOp.get(NAME).set(ModelKeys.VALUE);
        writeOp.get(VALUE).set(propertyValue);
        return writeOp ;
    }

    protected static ModelNode getProtocolAddOperation(String stackName, String protocolType) {
        // create the address of the cache
        PathAddress stackAddr = getProtocolStackAddress(stackName);
        ModelNode addOp = new ModelNode() ;
        addOp.get(OP).set("add-protocol");
        addOp.get(OP_ADDR).set(stackAddr.toModelNode());
        // required attributes
        addOp.get(ModelKeys.TYPE).set(protocolType);
        return addOp ;
    }

    protected static ModelNode getProtocolAddOperationWithProperties(String stackName, String protocolType) {
        ModelNode addOp = getProtocolAddOperation(stackName, protocolType);
        // add optional PROPERTIES attribute
        ModelNode propertyList = new ModelNode();
        ModelNode propA = new ModelNode();
        propA.add("A","a");
        propertyList.add(propA);
        ModelNode propB = new ModelNode();
        propB.add("B","b");
        propertyList.add(propB);
        addOp.get(ModelKeys.PROPERTIES).set(propertyList);
        return addOp ;
    }

    protected static ModelNode getProtocolReadOperation(String stackName, String protocolName, String name) {
        // create the address of the subsystem
        PathAddress protocolAddress = getProtocolAddress(stackName, protocolName);
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(protocolAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(name);
        return readOp ;
    }

    protected static ModelNode getProtocolWriteOperation(String stackName, String protocolName, String name, String value) {
        // create the address of the subsystem
        PathAddress protocolAddress = getProtocolAddress(stackName, protocolName);
        ModelNode writeOp = new ModelNode() ;
        writeOp.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        writeOp.get(OP_ADDR).set(protocolAddress.toModelNode());
        // required attributes
        writeOp.get(NAME).set(name);
        writeOp.get(VALUE).set(value);
        return writeOp ;
    }

    protected static ModelNode getProtocolPropertyAddOperation(String stackName, String protocolName, String propertyName, String propertyValue) {
        // create the address of the subsystem
        PathAddress protocolPropertyAddress = getProtocolPropertyAddress(stackName, protocolName, propertyName);
        ModelNode addOp = new ModelNode() ;
        addOp.get(OP).set(ADD);
        addOp.get(OP_ADDR).set(protocolPropertyAddress.toModelNode());
        // required attributes
        addOp.get(NAME).set(ModelKeys.VALUE);
        addOp.get(VALUE).set(propertyValue);
        return addOp ;
    }

    protected static ModelNode getProtocolPropertyReadOperation(String stackName, String protocolName, String propertyName) {
        // create the address of the subsystem
        PathAddress protocolPropertyAddress = getProtocolPropertyAddress(stackName, protocolName, propertyName);
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(protocolPropertyAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(ModelKeys.VALUE);
        return readOp ;
    }

    protected static ModelNode getProtocolPropertyWriteOperation(String stackName, String protocolName, String propertyName, String propertyValue) {
        // create the address of the subsystem
        PathAddress protocolPropertyAddress = getProtocolPropertyAddress(stackName, protocolName, propertyName);
        ModelNode writeOp = new ModelNode() ;
        writeOp.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        writeOp.get(OP_ADDR).set(protocolPropertyAddress.toModelNode());
        // required attributes
        writeOp.get(NAME).set(ModelKeys.VALUE);
        writeOp.get(VALUE).set(propertyValue);
        return writeOp ;
    }

    protected static ModelNode getProtocolRemoveOperation(String stackName, String protocolType) {
        // create the address of the cache
        PathAddress stackAddr = getProtocolStackAddress(stackName);
        ModelNode removeOp = new ModelNode() ;
        removeOp.get(OP).set("remove-protocol");
        removeOp.get(OP_ADDR).set(stackAddr.toModelNode());
        // required attributes
        removeOp.get(ModelKeys.TYPE).set(protocolType);
        return removeOp ;
    }

    protected static PathAddress getProtocolStackAddress(String stackName) {
        // create the address of the stack
        PathAddress stackAddr = PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME),
                PathElement.pathElement("stack",stackName));
        return stackAddr ;
    }

    protected static PathAddress getTransportAddress(String stackName) {
        // create the address of the cache
        PathAddress protocolAddr = PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME),
                PathElement.pathElement("stack",stackName),
                PathElement.pathElement("transport", "TRANSPORT"));
        return protocolAddr ;
    }

    protected static PathAddress getTransportPropertyAddress(String stackName, String propertyName) {
        // create the address of the cache
        PathAddress protocolAddr = PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME),
                PathElement.pathElement("stack",stackName),
                PathElement.pathElement("transport", "TRANSPORT"),
                PathElement.pathElement("property", propertyName));
        return protocolAddr ;
    }

    protected static PathAddress getProtocolAddress(String stackName, String protocolType) {
        // create the address of the cache
        PathAddress protocolAddr = PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME),
                PathElement.pathElement("stack",stackName),
                PathElement.pathElement("protocol", protocolType));
        return protocolAddr ;
    }

    protected static PathAddress getProtocolPropertyAddress(String stackName, String protocolType, String propertyName) {
        // create the address of the cache
        PathAddress protocolAddr = PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME),
                PathElement.pathElement("stack",stackName),
                PathElement.pathElement("protocol", protocolType),
                PathElement.pathElement("property", propertyName));
        return protocolAddr ;
    }

    protected String getSubsystemXml() throws IOException {
        return readResource(SUBSYSTEM_XML_FILE) ;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13914.java