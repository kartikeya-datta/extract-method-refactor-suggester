error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8868.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8868.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8868.java
text:
```scala
static final S@@tring SUBSYSTEM_XML_FILE = "subsystem-infinispan-test.xml" ;

package org.jboss.as.clustering.infinispan.subsystem;

import static org.jboss.as.clustering.infinispan.subsystem.ModelKeys.DEFAULT_CACHE;
import static org.jboss.as.clustering.infinispan.subsystem.ModelKeys.JNDI_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;

import java.io.IOException;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.subsystem.test.AbstractSubsystemTest;
import org.jboss.dmr.ModelNode;

/**
* Base test case for testing management operations.
*
* @author Richard Achmatowicz (c) 2011 Red Hat Inc.
*/

public class OperationTestCaseBase extends AbstractSubsystemTest {

    static final String SUBSYSTEM_XML_FILE = "subsystem-infinispan-2_0.xml" ;

    public OperationTestCaseBase() {
        super(InfinispanExtension.SUBSYSTEM_NAME, new InfinispanExtension());
    }

    // cache container access
    protected static ModelNode getCacheContainerAddOperation(String containerName) {
        // create the address of the cache
        PathAddress containerAddr = getCacheContainerAddress(containerName);
        ModelNode addOp = Util.createAddOperation(containerAddr);
        // required attributes
        addOp.get(DEFAULT_CACHE).set("default");
        return addOp ;
    }

    protected static ModelNode getCacheContainerReadOperation(String containerName, String name) {
        // create the address of the subsystem
        PathAddress transportAddress = getCacheContainerAddress(containerName);
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(transportAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(name);
        return readOp ;
    }

    protected static ModelNode getCacheContainerAddAliasOperation(String containerName, String name, String value) {
        // create the address of the subsystem
        PathAddress cacheContainerAddress = getCacheContainerAddress(containerName);
        ModelNode addAliasOp = new ModelNode() ;
        addAliasOp.get(OP).set("add-alias");
        addAliasOp.get(OP_ADDR).set(cacheContainerAddress.toModelNode());
        // required attributes
        addAliasOp.get(NAME).set(name);
        return addAliasOp ;
    }

    protected static ModelNode getCacheContainerRemoveAliasOperation(String containerName, String name) {
        // create the address of the subsystem
        PathAddress cacheContainerAddress = getCacheContainerAddress(containerName);
        ModelNode removeAliasOp = new ModelNode() ;
        removeAliasOp.get(OP).set("remove-alias");
        removeAliasOp.get(OP_ADDR).set(cacheContainerAddress.toModelNode());
        // required attributes
        removeAliasOp.get(NAME).set(name);
        return removeAliasOp ;
    }

    protected static ModelNode getCacheContainerWriteOperation(String containerName, String name, String value) {
        // create the address of the subsystem
        PathAddress cacheAddress = getCacheContainerAddress(containerName);
        return Util.getWriteAttributeOperation(cacheAddress, name, new ModelNode().set(value));
    }

    protected static ModelNode getCacheContainerRemoveOperation(String containerName) {
        // create the address of the cache
        PathAddress containerAddr = getCacheContainerAddress(containerName);
        return Util.createRemoveOperation(containerAddr);
    }

    // cache access
    protected static ModelNode getCacheAddOperation(String containerName, String cacheType, String cacheName) {
        // create the address of the cache
        PathAddress cacheAddr = getCacheAddress(containerName, cacheType, cacheName);
        ModelNode addOp = Util.createAddOperation(cacheAddr);
        // required attributes
        addOp.get(JNDI_NAME).set("java:/fred/was/here");
        return addOp ;
    }

    protected static ModelNode getCacheReadOperation(String containerName, String cacheType, String cacheName, String name) {
        // create the address of the subsystem
        PathAddress cacheAddress = getCacheAddress(containerName, cacheType, cacheName);
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(cacheAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(name);
        return readOp ;
    }

    protected static ModelNode getCacheWriteOperation(String containerName, String cacheType, String cacheName, String name, String value) {
        PathAddress cacheAddress = getCacheAddress(containerName, cacheType, cacheName);
        return Util.getWriteAttributeOperation(cacheAddress, name, new ModelNode().set(value));
    }

    protected static ModelNode getCacheRemoveOperation(String containerName, String cacheType, String cacheName) {
        PathAddress cacheAddr = getCacheAddress(containerName, cacheType, cacheName);
        return Util.createRemoveOperation(cacheAddr) ;
    }

    // cache store access
    protected static ModelNode getCacheStoreReadOperation(String containerName, String cacheType, String cacheName, String name) {
        // create the address of the subsystem
        PathAddress cacheStoreAddress = getCacheStoreAddress(containerName, cacheType, cacheName);
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(cacheStoreAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(name);
        return readOp ;
    }

    protected static ModelNode getCacheStoreWriteOperation(String containerName, String cacheName, String cacheType, String name, String value) {
        PathAddress cacheStoreAddress = getCacheStoreAddress(containerName,  cacheType, cacheName);
        return Util.getWriteAttributeOperation(cacheStoreAddress, name, new ModelNode().set(value));
    }

    protected static ModelNode getMixedKeyedJDBCCacheStoreReadOperation(String containerName, String cacheType, String cacheName, String name) {
        // create the address of the subsystem
        PathAddress cacheAddress = getMixedKeyedJDBCCacheStoreAddress(containerName, cacheType, cacheName);
        ModelNode readOp = new ModelNode() ;
        readOp.get(OP).set(READ_ATTRIBUTE_OPERATION);
        readOp.get(OP_ADDR).set(cacheAddress.toModelNode());
        // required attributes
        readOp.get(NAME).set(name);
        return readOp ;
    }

    protected static ModelNode getMixedKeyedJDBCCacheStoreWriteOperation(String containerName, String cacheType, String cacheName, String name, String value) {
        PathAddress cacheStoreAddress = getMixedKeyedJDBCCacheStoreAddress(containerName, cacheType, cacheName);
        return Util.getWriteAttributeOperation(cacheStoreAddress, name, new ModelNode().set(value));
    }

    protected static ModelNode getMixedKeyedJDBCCacheStoreWriteOperation(String containerName, String cacheType, String cacheName, String name, ModelNode value) {
        PathAddress cacheStoreAddress = getMixedKeyedJDBCCacheStoreAddress(containerName, cacheType, cacheName);
        return Util.getWriteAttributeOperation(cacheStoreAddress, name, value);
    }

    //cache store property access
    protected static ModelNode getCacheStorePropertyAddOperation(String containerName, String cacheName, String cacheType, String propertyName, String value) {
        PathAddress cacheStorePropertyAddress = getCacheStorePropertyAddress(containerName,  cacheType, cacheName, propertyName);
        ModelNode addOp = Util.createAddOperation(cacheStorePropertyAddress);
        // required attributes
        addOp.get(VALUE).set(value);
        return addOp ;
    }

    protected static ModelNode getCacheStorePropertyWriteOperation(String containerName, String cacheName, String cacheType, String propertyName, String value) {
        PathAddress cacheStorePropertyAddress = getCacheStorePropertyAddress(containerName, cacheType, cacheName, propertyName);
        return Util.getWriteAttributeOperation(cacheStorePropertyAddress, "value", new ModelNode().set(value));
    }

    // address generation
    protected static PathAddress getCacheStorePropertyAddress(String containerName, String cacheType, String cacheName, String propertyName) {
        return getCacheStoreAddress(containerName, cacheType, cacheName).append(ModelKeys.PROPERTY, propertyName);
    }

    protected static PathAddress getMixedKeyedJDBCCacheStoreAddress(String containerName, String cacheType, String cacheName) {
        return getCacheAddress(containerName, cacheType, cacheName).append(ModelKeys.MIXED_KEYED_JDBC_STORE, ModelKeys.MIXED_KEYED_JDBC_STORE_NAME);
    }

    protected static PathAddress getBinaryKeyedJDBCCacheStoreAddress(String containerName, String cacheType, String cacheName) {
        return getCacheAddress(containerName, cacheType, cacheName).append(ModelKeys.BINARY_KEYED_JDBC_STORE, ModelKeys.BINARY_KEYED_JDBC_STORE_NAME);
    }

    protected static PathAddress getStringKeyedJDBCCacheStoreAddress(String containerName, String cacheType, String cacheName) {
        return getCacheAddress(containerName, cacheType, cacheName).append(ModelKeys.STRING_KEYED_JDBC_STORE, ModelKeys.STRING_KEYED_JDBC_STORE_NAME);
    }

    protected static PathAddress getRemoteCacheStoreAddress(String containerName, String cacheType, String cacheName) {
        return getCacheAddress(containerName, cacheType, cacheName).append(ModelKeys.REMOTE_STORE, ModelKeys.REMOTE_STORE_NAME);
    }

    protected static PathAddress getFileCacheStoreAddress(String containerName, String cacheType, String cacheName) {
        return getCacheAddress(containerName, cacheType, cacheName).append(ModelKeys.FILE_STORE, ModelKeys.FILE_STORE_NAME);
    }

    protected static PathAddress getCacheStoreAddress(String containerName, String cacheType, String cacheName) {
        return getCacheAddress(containerName, cacheType, cacheName).append(ModelKeys.STORE, ModelKeys.STORE_NAME);
    }

    protected static PathAddress getCacheContainerAddress(String containerName) {
        return PathAddress.pathAddress(InfinispanExtension.SUBSYSTEM_PATH).append(ModelKeys.CACHE_CONTAINER, containerName);
    }

    protected static PathAddress getCacheAddress(String containerName, String cacheType, String cacheName) {
        return getCacheContainerAddress(containerName).append(cacheType, cacheName);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8868.java