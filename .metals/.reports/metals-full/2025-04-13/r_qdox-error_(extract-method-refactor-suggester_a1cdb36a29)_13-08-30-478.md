error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7622.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7622.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7622.java
text:
```scala
L@@oggers.getLogger(MonitorModule.class).trace("failed to load sigar", e);

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.monitor;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Scopes;
import org.elasticsearch.common.inject.assistedinject.FactoryProvider;
import org.elasticsearch.common.inject.multibindings.MapBinder;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.monitor.dump.DumpContributorFactory;
import org.elasticsearch.monitor.dump.DumpMonitorService;
import org.elasticsearch.monitor.dump.cluster.ClusterDumpContributor;
import org.elasticsearch.monitor.dump.heap.HeapDumpContributor;
import org.elasticsearch.monitor.dump.summary.SummaryDumpContributor;
import org.elasticsearch.monitor.dump.thread.ThreadDumpContributor;
import org.elasticsearch.monitor.jvm.JvmMonitorService;
import org.elasticsearch.monitor.jvm.JvmService;
import org.elasticsearch.monitor.network.JmxNetworkProbe;
import org.elasticsearch.monitor.network.NetworkProbe;
import org.elasticsearch.monitor.network.NetworkService;
import org.elasticsearch.monitor.network.SigarNetworkProbe;
import org.elasticsearch.monitor.os.JmxOsProbe;
import org.elasticsearch.monitor.os.OsProbe;
import org.elasticsearch.monitor.os.OsService;
import org.elasticsearch.monitor.os.SigarOsProbe;
import org.elasticsearch.monitor.process.JmxProcessProbe;
import org.elasticsearch.monitor.process.ProcessProbe;
import org.elasticsearch.monitor.process.ProcessService;
import org.elasticsearch.monitor.process.SigarProcessProbe;
import org.elasticsearch.monitor.sigar.SigarService;

import java.util.Map;

import static org.elasticsearch.monitor.dump.cluster.ClusterDumpContributor.*;
import static org.elasticsearch.monitor.dump.heap.HeapDumpContributor.*;
import static org.elasticsearch.monitor.dump.summary.SummaryDumpContributor.*;
import static org.elasticsearch.monitor.dump.thread.ThreadDumpContributor.*;

/**
 * @author kimchy (Shay Banon)
 */
public class MonitorModule extends AbstractModule {

    public static final class MonitorSettings {
        public static final String MEMORY_MANAGER_TYPE = "monitor.memory.type";
    }

    private final Settings settings;

    public MonitorModule(Settings settings) {
        this.settings = settings;
    }

    @Override protected void configure() {
        boolean sigarLoaded = false;
        try {
            settings.getClassLoader().loadClass("org.hyperic.sigar.Sigar");
            SigarService sigarService = new SigarService(settings);
            if (sigarService.sigarAvailable()) {
                bind(SigarService.class).toInstance(sigarService);
                bind(ProcessProbe.class).to(SigarProcessProbe.class).asEagerSingleton();
                bind(OsProbe.class).to(SigarOsProbe.class).asEagerSingleton();
                bind(NetworkProbe.class).to(SigarNetworkProbe.class).asEagerSingleton();
                sigarLoaded = true;
            }
        } catch (Throwable e) {
            // no sigar
            Loggers.getLogger(MonitorModule.class).debug("failed to load sigar", e);
        }
        if (!sigarLoaded) {
            // bind non sigar implementations
            bind(ProcessProbe.class).to(JmxProcessProbe.class).asEagerSingleton();
            bind(OsProbe.class).to(JmxOsProbe.class).asEagerSingleton();
            bind(NetworkProbe.class).to(JmxNetworkProbe.class).asEagerSingleton();
        }
        // bind other services
        bind(ProcessService.class).asEagerSingleton();
        bind(OsService.class).asEagerSingleton();
        bind(NetworkService.class).asEagerSingleton();
        bind(JvmService.class).asEagerSingleton();

        bind(JvmMonitorService.class).asEagerSingleton();

        MapBinder<String, DumpContributorFactory> tokenFilterBinder
                = MapBinder.newMapBinder(binder(), String.class, DumpContributorFactory.class);

        Map<String, Settings> dumpContSettings = settings.getGroups("monitor.dump");
        for (Map.Entry<String, Settings> entry : dumpContSettings.entrySet()) {
            String dumpContributorName = entry.getKey();
            Settings dumpContributorSettings = entry.getValue();

            Class<? extends DumpContributorFactory> type = dumpContributorSettings.getAsClass("type", null, "org.elasticsearch.monitor.dump." + dumpContributorName + ".", "DumpContributor");
            if (type == null) {
                throw new IllegalArgumentException("Dump Contributor [" + dumpContributorName + "] must have a type associated with it");
            }
            tokenFilterBinder.addBinding(dumpContributorName).toProvider(FactoryProvider.newFactory(DumpContributorFactory.class, type)).in(Scopes.SINGLETON);
        }
        // add default
        if (!dumpContSettings.containsKey(SUMMARY)) {
            tokenFilterBinder.addBinding(SUMMARY).toProvider(FactoryProvider.newFactory(DumpContributorFactory.class, SummaryDumpContributor.class)).in(Scopes.SINGLETON);
        }
        if (!dumpContSettings.containsKey(THREAD_DUMP)) {
            tokenFilterBinder.addBinding(THREAD_DUMP).toProvider(FactoryProvider.newFactory(DumpContributorFactory.class, ThreadDumpContributor.class)).in(Scopes.SINGLETON);
        }
        if (!dumpContSettings.containsKey(HEAP_DUMP)) {
            tokenFilterBinder.addBinding(HEAP_DUMP).toProvider(FactoryProvider.newFactory(DumpContributorFactory.class, HeapDumpContributor.class)).in(Scopes.SINGLETON);
        }
        if (!dumpContSettings.containsKey(CLUSTER)) {
            tokenFilterBinder.addBinding(CLUSTER).toProvider(FactoryProvider.newFactory(DumpContributorFactory.class, ClusterDumpContributor.class)).in(Scopes.SINGLETON);
        }


        bind(DumpMonitorService.class).asEagerSingleton();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7622.java