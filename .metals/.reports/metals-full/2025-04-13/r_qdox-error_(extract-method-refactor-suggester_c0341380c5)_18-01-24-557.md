error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3698.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3698.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3698.java
text:
```scala
static final A@@ttributeDefinition[] ATTRIBUTES = {IDLE_TIMEOUT, IDLE_TIMEOUT_UNIT, MAX_SIZE, CACHE_CONTAINER, BEAN_CACHE, CLIENT_MAPPINGS_CACHE, PASSIVATE_EVENTS_ON_REPLICATE };

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

package org.jboss.as.ejb3.subsystem;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ReadResourceNameOperationStepHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.ejb3.cache.impl.backing.clustering.ClusteredBackingCacheEntryStoreConfig;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author Paul Ferraro
 */
public class ClusterPassivationStoreResourceDefinition extends PassivationStoreResourceDefinition {

    static final SimpleAttributeDefinition MAX_SIZE = MAX_SIZE_BUILDER.setDefaultValue(new ModelNode(ClusteredBackingCacheEntryStoreConfig.DEFAULT_MAX_SIZE)).build();
    static final SimpleAttributeDefinition CACHE_CONTAINER =
            new SimpleAttributeDefinitionBuilder(EJB3SubsystemModel.CACHE_CONTAINER, ModelType.STRING, true)
                    .setXmlName(EJB3SubsystemXMLAttribute.CACHE_CONTAINER.getLocalName())
                    .setDefaultValue(new ModelNode(ClusteredBackingCacheEntryStoreConfig.DEFAULT_CACHE_CONTAINER))
                    .setAllowExpression(true)
                    .setFlags(AttributeAccess.Flag.RESTART_NONE)
                    .build();
    static final SimpleAttributeDefinition BEAN_CACHE =
            new SimpleAttributeDefinitionBuilder(EJB3SubsystemModel.BEAN_CACHE, ModelType.STRING, true)
                    .setXmlName(EJB3SubsystemXMLAttribute.BEAN_CACHE.getLocalName())
                    .setAllowExpression(true)
                    .setFlags(AttributeAccess.Flag.RESTART_NONE)
                    .build();
    static final SimpleAttributeDefinition CLIENT_MAPPINGS_CACHE =
            new SimpleAttributeDefinitionBuilder(EJB3SubsystemModel.CLIENT_MAPPINGS_CACHE, ModelType.STRING, true)
                    .setXmlName(EJB3SubsystemXMLAttribute.CLIENT_MAPPINGS_CACHE.getLocalName())
                    .setDefaultValue(new ModelNode(ClusteredBackingCacheEntryStoreConfig.DEFAULT_CLIENT_MAPPING_CACHE))
                    .setAllowExpression(true)
                    .setFlags(AttributeAccess.Flag.RESTART_NONE)
                    .build();
    static final SimpleAttributeDefinition PASSIVATE_EVENTS_ON_REPLICATE =
            new SimpleAttributeDefinitionBuilder(EJB3SubsystemModel.PASSIVATE_EVENTS_ON_REPLICATE, ModelType.BOOLEAN, true)
                    .setXmlName(EJB3SubsystemXMLAttribute.PASSIVATE_EVENTS_ON_REPLICATE.getLocalName())
                    .setDefaultValue(new ModelNode(ClusteredBackingCacheEntryStoreConfig.DEFAULT_PASSIVATE_EVENTS_ON_REPLICATE))
                    .setAllowExpression(true)
                    .setFlags(AttributeAccess.Flag.RESTART_NONE)
                    .build();

    static final AttributeDefinition[] ATTRIBUTES = new AttributeDefinition[] {IDLE_TIMEOUT, IDLE_TIMEOUT_UNIT, MAX_SIZE, CACHE_CONTAINER, BEAN_CACHE, CLIENT_MAPPINGS_CACHE, PASSIVATE_EVENTS_ON_REPLICATE };

    static final ClusterPassivationStoreAdd ADD_HANDLER = new ClusterPassivationStoreAdd(ATTRIBUTES);
    static final ClusterPassivationStoreRemove REMOVE_HANDLER = new ClusterPassivationStoreRemove(ADD_HANDLER);
    static final ClusterPassivationStoreWriteHandler WRITE_HANDLER = new ClusterPassivationStoreWriteHandler(ATTRIBUTES);

    static final ClusterPassivationStoreResourceDefinition INSTANCE = new ClusterPassivationStoreResourceDefinition();

    private ClusterPassivationStoreResourceDefinition() {
        super(EJB3SubsystemModel.CLUSTER_PASSIVATION_STORE, ADD_HANDLER, REMOVE_HANDLER, OperationEntry.Flag.RESTART_NONE, OperationEntry.Flag.RESTART_RESOURCE_SERVICES, WRITE_HANDLER, ATTRIBUTES);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3698.java