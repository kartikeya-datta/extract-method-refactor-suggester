error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3588.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3588.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3588.java
text:
```scala
b@@uilder.serialization().addAdvancedExternalizer(externalizerConfig.getId(), externalizerConfig.getAdvancedExternalizer());

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
package org.jboss.as.clustering.infinispan;

import java.util.Map;

import org.infinispan.config.AdvancedExternalizerConfig;
import org.infinispan.config.FluentGlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.ShutdownHookBehavior;
import org.infinispan.executors.ExecutorFactory;
import org.infinispan.executors.ScheduledExecutorFactory;
import org.infinispan.marshall.AdvancedExternalizer;
import org.infinispan.marshall.Marshaller;
import org.infinispan.remoting.transport.Transport;
import org.infinispan.util.Util;

/**
 * Workaround for ISPN-1701
 *
 * @author Paul Ferraro
 */
@SuppressWarnings("deprecation")
public class LegacyGlobalConfigurationAdapter {
    public static org.infinispan.config.GlobalConfiguration adapt(GlobalConfiguration config) {

        // Handle the case that null is passed in
        if (config == null)  return null;

        FluentGlobalConfiguration legacy = new org.infinispan.config.GlobalConfiguration(config.classLoader()).fluent();

        legacy.transport()
                .clusterName(config.transport().clusterName())
                .machineId(config.transport().machineId())
                .rackId(config.transport().rackId())
                .siteId(config.transport().siteId())
                .strictPeerToPeer(config.transport().strictPeerToPeer())
                .distributedSyncTimeout(config.transport().distributedSyncTimeout())
                .nodeName(config.transport().nodeName())
                .withProperties(config.transport().properties())
        ;
        if (config.transport().transport() != null) {
            legacy.transport().transportClass(config.transport().transport().getClass());
        }

        legacy.globalJmxStatistics()
                .jmxDomain(config.globalJmxStatistics().domain())
                .mBeanServerLookup(config.globalJmxStatistics().mbeanServerLookup())
                .allowDuplicateDomains(config.globalJmxStatistics().allowDuplicateDomains())
                .cacheManagerName(config.globalJmxStatistics().cacheManagerName())
                .withProperties(config.globalJmxStatistics().properties())
        ;
        if (!config.globalJmxStatistics().enabled()) {
            legacy.globalJmxStatistics().disable();
        }

        legacy.serialization()
                .marshallerClass(config.serialization().marshallerClass())
                .version(config.serialization().version())
        ;

        for (Map.Entry<Integer, AdvancedExternalizer<?>> entry : config.serialization().advancedExternalizers().entrySet()) {
            legacy.serialization().addAdvancedExternalizer(entry.getKey(), entry.getValue());
        }

        legacy.asyncTransportExecutor()
                .factory(config.asyncTransportExecutor().factory().getClass())
                .withProperties(config.asyncTransportExecutor().properties())
        ;
        legacy.asyncListenerExecutor()
                .factory(config.asyncListenerExecutor().factory().getClass())
                .withProperties(config.asyncListenerExecutor().properties())
        ;
        legacy.evictionScheduledExecutor()
                .factory(config.evictionScheduledExecutor().factory().getClass())
                .withProperties(config.asyncListenerExecutor().properties())
        ;
        legacy.replicationQueueScheduledExecutor()
                .factory(config.replicationQueueScheduledExecutor().factory().getClass())
                .withProperties(config.replicationQueueScheduledExecutor().properties())
        ;

        legacy.shutdown().hookBehavior(org.infinispan.config.GlobalConfiguration.ShutdownHookBehavior.valueOf(config.shutdown().hookBehavior().name()));

        return legacy.build();
    }

    @SuppressWarnings("unchecked")
    public static GlobalConfiguration adapt(org.infinispan.config.GlobalConfiguration legacy) {

        // Handle the case that null is passed in
        if (legacy == null) return null;

        GlobalConfigurationBuilder builder = new GlobalConfigurationBuilder();

        if (legacy.getTransportClass() != null) {
            builder.transport()
                    .clusterName(legacy.getClusterName())
                    .machineId(legacy.getMachineId())
                    .rackId(legacy.getRackId())
                    .siteId(legacy.getSiteId())
                    .strictPeerToPeer(legacy.isStrictPeerToPeer())
                    .distributedSyncTimeout(legacy.getDistributedSyncTimeout())
                    .transport(Util.<Transport> getInstance(legacy.getTransportClass(), legacy.getClassLoader()))
                    .nodeName(legacy.getTransportNodeName())
                    .withProperties(legacy.getTransportProperties())
            ;
        }

        builder.globalJmxStatistics()
                .enabled(legacy.isExposeGlobalJmxStatistics())
                .jmxDomain(legacy.getJmxDomain())
                .mBeanServerLookup(legacy.getMBeanServerLookupInstance())
                .allowDuplicateDomains(legacy.isAllowDuplicateDomains())
                .cacheManagerName(legacy.getCacheManagerName())
                .withProperties(legacy.getMBeanServerProperties())
        ;

        builder.serialization()
                .marshallerClass(Util.<Marshaller> loadClass(legacy.getMarshallerClass(), legacy.getClassLoader()))
                .version(legacy.getMarshallVersion())
        ;

        for (AdvancedExternalizerConfig externalizerConfig : legacy.getExternalizers()) {
            builder.serialization().addAdvancedExternalizer(externalizerConfig.getAdvancedExternalizer());
        }

        builder.asyncTransportExecutor()
                .factory(Util.<ExecutorFactory> getInstance(legacy.getAsyncTransportExecutorFactoryClass(), legacy.getClassLoader()))
                .withProperties(legacy.getAsyncTransportExecutorProperties())
        ;
        builder.asyncListenerExecutor()
                .factory(Util.<ExecutorFactory> getInstance(legacy.getAsyncListenerExecutorFactoryClass(), legacy.getClassLoader()))
                .withProperties(legacy.getAsyncListenerExecutorProperties())
        ;
        builder.evictionScheduledExecutor()
                .factory(Util.<ScheduledExecutorFactory> getInstance(legacy.getEvictionScheduledExecutorFactoryClass(), legacy.getClassLoader()))
                .withProperties(legacy.getAsyncListenerExecutorProperties())
        ;
        builder.replicationQueueScheduledExecutor()
                .factory(Util.<ScheduledExecutorFactory> getInstance(legacy.getReplicationQueueScheduledExecutorFactoryClass(), legacy.getClassLoader()))
                .withProperties(legacy.getReplicationQueueScheduledExecutorProperties())
        ;

        builder.shutdown().hookBehavior(ShutdownHookBehavior.valueOf(legacy.getShutdownHookBehavior().name()));

        return builder.build();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3588.java